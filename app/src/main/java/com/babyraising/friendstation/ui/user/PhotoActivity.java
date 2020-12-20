package com.babyraising.friendstation.ui.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.AlbumAdapter;
import com.babyraising.friendstation.adapter.DialogFirstShowAdapter;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.AlbumDetailBean;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.decoration.FirstShowSpaceItemDecoration;
import com.babyraising.friendstation.decoration.PhotoSpaceItemDecoration;
import com.babyraising.friendstation.request.UpdateAlbumRequest;
import com.babyraising.friendstation.response.AlbumResponse;
import com.babyraising.friendstation.response.UmsUserAllInfoResponse;
import com.babyraising.friendstation.response.UploadPicResponse;
import com.babyraising.friendstation.util.DisplayUtils;
import com.babyraising.friendstation.util.FileUtil;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;
import com.nanchen.compresshelper.CompressHelper;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_photo)
public class PhotoActivity extends BaseActivity {

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @ViewInject(R.id.layout_take_photo)
    private LinearLayout takePhotoLayout;

    @ViewInject(R.id.layout_take_photo)
    private LinearLayout photoLayout;

    @ViewInject(R.id.content)
    private EditText content;

    @Event(R.id.layout_camera)
    private void cameraClick(View view) {
        takePhoto();
        photoLayout.setVisibility(View.GONE);
    }

    @Event(R.id.layout_photo)
    private void selectPhoto(View view) {
        choosePhoto();
        photoLayout.setVisibility(View.GONE);
    }

    @ViewInject(R.id.photo_list)
    private RecyclerView photoRecyleViewList;

    @Event(R.id.layout_cancel)
    private void layoutAllPhoto(View view) {
        photoLayout.setVisibility(View.GONE);
    }

    public static final int RC_TAKE_PHOTO = 1;
    public static final int RC_CHOOSE_PHOTO = 2;
    public static final int SIGN_CODE = 101;

    private String mTempPhotoPath;
    private String newHeadIconUrl;
    private Uri imageUri;

    private List<AlbumDetailBean> photoList = new ArrayList<>();
    private AlbumAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getPhotoList();
    }

    private void initView() {
        adapter = new AlbumAdapter(this, photoList);
        GridLayoutManager manager = new GridLayoutManager(this, 3);


        WindowManager wm1 = this.getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth() - DisplayUtils.dp2px(this, 30);
        int itemWidth = DisplayUtils.dp2px(this, 69); //每个item的宽度

        photoRecyleViewList.setLayoutManager(manager);
        photoRecyleViewList.setAdapter(adapter);
        photoRecyleViewList.addItemDecoration(new PhotoSpaceItemDecoration((width1 - itemWidth * 3) / 6));
    }

    private void getPhotoList() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_ALBUM_PAGE);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                AlbumResponse response = gson.fromJson(result, AlbumResponse.class);
                System.out.println("albumList:" + result);
                switch (response.getCode()) {
                    case 200:
                        photoList.clear();
                        for (int i = 0; i < response.getData().getRecords().size(); i++) {
                            photoList.add(response.getData().getRecords().get(i));
                        }
                        photoList.add(new AlbumDetailBean());
                        adapter.notifyDataSetChanged();
                        break;
                    default:

                        break;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("错误处理:" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void addNewPhoto() {
        if (takePhotoLayout.getVisibility() == View.GONE) {
            takePhotoLayout.setVisibility(View.VISIBLE);
        }
    }

    public void deletePhoto(int position) {
        adapter.notifyDataSetChanged();
        deleteAlbum(photoList.get(position).getId());
        photoList.remove(position);
    }

    private void takePhoto() {
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "photoTest" + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File photoFile = new File(fileDir, "photo.jpeg");
        mTempPhotoPath = photoFile.getAbsolutePath();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            /*7.0以上要通过FileProvider将File转化为Uri*/
            imageUri = FileProvider.getUriForFile(this, "", photoFile);
        } else {
            /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
            imageUri = Uri.fromFile(photoFile);
        }
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentToTakePhoto, RC_TAKE_PHOTO);
    }

    private void uploadPic(String localPic) {

        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();

        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_UPLOAD);
        params.addHeader("Authorization", bean.getAccessToken());
        File oldFile = new File(localPic);
        File newFile = new CompressHelper.Builder(this)
                .setMaxWidth(360)  // 默认最大宽度为720
                .setMaxHeight(480) // 默认最大高度为960
                .setQuality(80)    // 默认压缩质量为80
                .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build()
                .compressToFile(oldFile);
        params.setAsJsonContent(true);
        List<KeyValue> list = new ArrayList<>();
        list.add(new KeyValue("file", newFile));
        MultipartBody body = new MultipartBody(list, "UTF-8");
        params.setRequestBody(body);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                switch (response.getCode()) {
                    case 200:
                        T.s("上传成功");
//                        AlbumDetailBean bean = new AlbumDetailBean();
//                        bean.setUrl(response.getData());
//                        photoList.add(photoList.size() - 1, bean);
//                        adapter.notifyDataSetChanged();
                        updateAlbum(response.getData());
                        break;
                    default:
                        T.s(response.getMsg());
                        break;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("错误处理:" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void deleteAlbum(int id) {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        Gson gson = new Gson();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_DELETE_ALBUM + "/" + id);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                System.out.println("deleteAlbum:" + result);
                switch (response.getCode()) {
                    case 200:
                        getPhotoList();
                        break;
                    default:
                        T.s(response.getMsg());
                        break;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("错误处理:" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void updateAlbum(String newUrl) {
        UpdateAlbumRequest request = new UpdateAlbumRequest();
        request.setSort(photoList.size());
        request.setUrl(newUrl);
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        Gson gson = new Gson();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_SAVE_ALBUM);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                System.out.println("updateAlbum:" + result);
                switch (response.getCode()) {
                    case 200:
                        getPhotoList();

                        break;
                    default:
                        T.s(response.getMsg());
                        break;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("错误处理:" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_CHOOSE_PHOTO:
                Uri uri = data.getData();
                String filePath = FileUtil.getFilePathByUri(this, uri);
                if (!TextUtils.isEmpty(filePath)) {
                    uploadPic(filePath);
                } else {
                    T.s("选择照片出错");
                }
                break;
            case RC_TAKE_PHOTO:
                if (!TextUtils.isEmpty(mTempPhotoPath)) {
//
                    uploadPic(mTempPhotoPath);
                } else {
                    T.s("选择照片出错");
                }
                break;

            case SIGN_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
//                        editUser("", "", "", "", "", "", "", data.getStringExtra("sign"));
//                        cardSign.setText(data.getStringExtra("sign"));
//                        userBean.setSIGNNAME(data.getStringExtra("sign"));
                    }
                }
                break;
        }
    }
}