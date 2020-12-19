package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.request.VerifyRequest;
import com.babyraising.friendstation.response.UploadPicResponse;
import com.babyraising.friendstation.response.VerifyResponse;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_person_auth)
public class PersonAuthActivity extends BaseActivity {
    private int status = 1;

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Event(R.id.retake)
    private void retakeClick(View view) {
        if (status == 1) {
            takePhoto();
        }
    }

    @ViewInject(R.id.layout_error)
    private RelativeLayout errorLayout;

    @ViewInject(R.id.retake)
    private Button retake;

    public static final int RC_TAKE_PHOTO = 1;
    public static final int RC_CHOOSE_PHOTO = 2;
    public static final int SIGN_CODE = 101;

    private String mTempPhotoPath;
    private String newHeadIconUrl;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_TAKE_PHOTO:
                if (!TextUtils.isEmpty(mTempPhotoPath)) {
                    uploadPic(mTempPhotoPath);
                } else {
                    T.s("选择照片出错");
                }
                break;

        }
    }

    private String encodeImage(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;

    }

    private void uploadAuth(String mTempPhotoPath) {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();

        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_UPDATE_VERIFY);
        params.addHeader("Authorization", bean.getAccessToken());
        VerifyRequest request = new VerifyRequest();
        request.setImg(mTempPhotoPath);

        Gson gson = new Gson();
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                VerifyResponse response = gson.fromJson(result, VerifyResponse.class);
                System.out.println("uploadAuth:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("认证成功");
                        finish();
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
//                        AlbumDetailBean bean = new AlbumDetailBean();
//                        bean.setUrl(response.getData());
//                        photoList.add(photoList.size() - 1, bean);
//                        adapter.notifyDataSetChanged();
                        uploadAuth(response.getData());
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

    /**
     * 获取指定文件大小
     *
     * @param f
     * @return
     * @throws Exception
     */
    private long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }
}
