package com.babyraising.friendstation.ui.user;

import android.content.ContentValues;
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
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.AvatarBean;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.ScoreRecordBean;
import com.babyraising.friendstation.request.SetPasswordRequest;
import com.babyraising.friendstation.request.SetusernameAndIconRequest;
import com.babyraising.friendstation.response.AvatarResponse;
import com.babyraising.friendstation.response.ScoreRecordResponse;
import com.babyraising.friendstation.response.UmsUpdatePasswordResponse;
import com.babyraising.friendstation.response.UmsUpdateUsernameAndIconResponse;
import com.babyraising.friendstation.response.UploadPicResponse;
import com.babyraising.friendstation.util.FileUtil;
import com.babyraising.friendstation.util.NameUtils;
import com.babyraising.friendstation.util.NickNameUtil;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;
import com.nanchen.compresshelper.CompressHelper;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ContentView(R.layout.activity_build_user)
public class BuildUserActivity extends BaseActivity {

    private List<AvatarBean> randomList = new ArrayList<>();

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Event(R.id.refresh)
    private void refreshClick(View view) {
        username.setText(NickNameUtil.generateName());

        if (randomList.size() > 0) {
            int math = (int) (Math.random() * randomList.size());
            x.image().bind(head, randomList.get(math).getAvatarUrl());
            newHeadIconUrl = randomList.get(math).getAvatarUrl();
        }
    }

    @Event(R.id.head)
    private void headClick(View view) {
        if (photoLayout.getVisibility() == View.GONE) {
            photoLayout.setVisibility(View.VISIBLE);
        }
    }

    @Event(R.id.save)
    private void saveClick(View view) {
        if (TextUtils.isEmpty(username.getText().toString())) {
            T.s("昵称不能为空");
            return;
        }

        if (TextUtils.isEmpty(newHeadIconUrl)) {
            T.s("请先设置头像");
            return;
        }
        saveUsernameAndIcon(username.getText().toString());
    }

    @ViewInject(R.id.head)
    private ImageView head;

    @ViewInject(R.id.username)
    private EditText username;

    @ViewInject(R.id.layout_take_photo)
    private LinearLayout takePhotoLayout;

    @ViewInject(R.id.layout_take_photo)
    private LinearLayout photoLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        getRandomIcon();
    }

    private void initView() {
//        Editable etext = username.getText();
//        username.setSelection(etext.length());
    }

    private void getRandomIcon() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_CONFIGAVATAR);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                AvatarResponse response = gson.fromJson(result, AvatarResponse.class);
                System.out.println("RandomIcon:" + result);
                switch (response.getCode()) {
                    case 200:
                        randomList.clear();
                        for (int l = 0; l < response.getData().size(); l++) {
                            randomList.add(response.getData().get(l));
                        }
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

    private void saveUsernameAndIcon(String username) {
        SetusernameAndIconRequest request = new SetusernameAndIconRequest();
        request.setAvatar(newHeadIconUrl);
        request.setNickname(username);

        Gson gson = new Gson();

        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();

        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_UPDATE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        System.out.println("saveUsernameAndIcon:" + gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUpdateUsernameAndIconResponse response = gson.fromJson(result, UmsUpdateUsernameAndIconResponse.class);
                switch (response.getCode()) {
                    case 200:
                        T.s("保存成功");
                        startInfoDateActivity();
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

    private void startInfoDateActivity() {
        Intent intent = new Intent(this, BuildUserNameActivity.class);
        startActivity(intent);
    }

    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
//        Intent intent = new Intent();
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        if (Build.VERSION.SDK_INT < 19) {
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//        } else {
//            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//        }
//        startActivityForResult(intent, RC_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_CHOOSE_PHOTO:
                if (data != null) {
                    Uri uri = data.getData();
                    String filePath = FileUtil.getFilePathByUri(this, uri);
                    if (!TextUtils.isEmpty(filePath)) {
                        uploadPic(filePath);
                    } else {
                        T.s("选择照片出错");
                    }
                }
                break;
            case RC_TAKE_PHOTO:
                if (!TextUtils.isEmpty(mTempPhotoPath)) {
//                    ImageOptions options = new ImageOptions.Builder().
//                            setRadius(DensityUtil.dip2px(60)).setCrop(true).build();
                    x.image().bind(head, mTempPhotoPath);
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


    private void uploadPic(String localPic) {

        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();

        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_UPLOAD);
        params.addHeader("Authorization", bean.getAccessToken());
        File oldFile = new File(localPic);
        if (oldFile.getTotalSpace() == 0){
            System.out.println("取消拍照");
            return;
        }
        File newFile = null;
        try {
            newFile = new CompressHelper.Builder(this)
                    .setMaxWidth(360)  // 默认最大宽度为720
                    .setMaxHeight(480) // 默认最大高度为960
                    .setQuality(80)    // 默认压缩质量为80
                    .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .build()
                    .compressToFile(oldFile);
        } catch (Exception e) {
            newFile = oldFile;
        }

        if (newFile == null) {
            return;
        }
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
                System.out.println("result:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("上传成功");
                        ImageOptions options = new ImageOptions.Builder().
                                setRadius(DensityUtil.dip2px(8))
                                .setCrop(true).build();
                        x.image().bind(head, response.getData(), options);
                        newHeadIconUrl = response.getData();
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

    private void takePhoto() {
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "photoTest" + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File photoFile = new File(fileDir, "photo" + System.currentTimeMillis() + ".jpeg");
        mTempPhotoPath = photoFile.getAbsolutePath();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            /*7.0以上要通过FileProvider将File转化为Uri*/
//            imageUri = FileProvider.getUriForFile(this, "", photoFile);
//        } else {
//            /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
//            imageUri = Uri.fromFile(photoFile);
//        }
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < 24) {
            intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(intentToTakePhoto, RC_TAKE_PHOTO);
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, photoFile.getAbsolutePath());
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intentToTakePhoto, RC_TAKE_PHOTO);
        }
    }
}
