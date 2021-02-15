package com.babyraising.friendstation.ui.user;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslCertificate;
import android.os.Build;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.babyraising.friendstation.ui.main.LookPhotoActivity;
import com.babyraising.friendstation.util.DisplayUtils;
import com.babyraising.friendstation.util.FileUtil;
import com.babyraising.friendstation.util.PhotoUtil;
import com.babyraising.friendstation.util.SizeUtil;
import com.babyraising.friendstation.util.T;
import com.babyraising.friendstation.util.TypeUtil;
import com.google.gson.Gson;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.loader.ImageLoader;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.nanchen.compresshelper.CompressHelper;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.GlideEngine;
import io.valuesfeng.picker.engine.LoadEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;
import loader.PicassoImageLoader;
import pub.devrel.easypermissions.EasyPermissions;

@ContentView(R.layout.activity_photo)
public class PhotoActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    private boolean editStatus = true;

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @ViewInject(R.id.layout_take_photo)
    private LinearLayout takePhotoLayout;

    @ViewInject(R.id.layout_main)
    private RelativeLayout mainLayout;

    @ViewInject(R.id.layout_take_photo)
    private LinearLayout photoLayout;

    @ViewInject(R.id.content)
    private EditText content;

    @ViewInject(R.id.edit)
    private TextView edit;

    @ViewInject(R.id.layout_none)
    private LinearLayout noneLayout;

    private int currentUserId = 0;

    @Event(R.id.edit)
    private void editClick(View view) {
        adapter.updateEditStatus(editStatus);
        editStatus = !editStatus;
        if (editStatus) {
            edit.setText("编辑");
            if (photoList.size() == 0) {
                photoRecyleViewList.setVisibility(View.GONE);
                noneLayout.setVisibility(View.VISIBLE);
            }

            if (photoList.size() == 1) {
                if (TextUtils.isEmpty(photoList.get(0).getUrl())) {
                    photoRecyleViewList.setVisibility(View.GONE);
                    noneLayout.setVisibility(View.VISIBLE);
                }
            }
        } else {
            edit.setText("编辑完成");
            photoRecyleViewList.setVisibility(View.VISIBLE);
            noneLayout.setVisibility(View.GONE);
        }
    }

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
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        int mode = intent.getIntExtra("mode", 0);
        currentUserId = intent.getIntExtra("currentUserId", 0);
        if (mode == 1) {
            edit.setVisibility(View.GONE);
            getPhotoListForUser();
        } else {
            getPhotoList();
        }
    }

    private void initView() {
        adapter = new AlbumAdapter(this, photoList);
        GridLayoutManager manager = new GridLayoutManager(this, 4);


        WindowManager wm1 = this.getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth() - DisplayUtils.dp2px(this, 60);
        int itemWidth = DisplayUtils.dp2px(this, 60); //每个item的宽度

        photoRecyleViewList.setLayoutManager(manager);
        photoRecyleViewList.setAdapter(adapter);
        photoRecyleViewList.addItemDecoration(new PhotoSpaceItemDecoration((width1 - itemWidth * 4) / 8));
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
                        if (editStatus) {
                            if (photoList.size() == 0) {
                                photoRecyleViewList.setVisibility(View.GONE);
                                noneLayout.setVisibility(View.VISIBLE);
                            }

                            if (photoList.size() == 1) {
                                if (TextUtils.isEmpty(photoList.get(0).getUrl())) {
                                    photoRecyleViewList.setVisibility(View.GONE);
                                    noneLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            photoRecyleViewList.setVisibility(View.VISIBLE);
                            noneLayout.setVisibility(View.GONE);
                        }
                        photoList.add(new AlbumDetailBean());
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        photoRecyleViewList.setVisibility(View.GONE);
                        noneLayout.setVisibility(View.VISIBLE);
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
        if (!checkPermission()) {
            return;
        }
//        if (takePhotoLayout.getVisibility() == View.GONE) {
//            takePhotoLayout.setVisibility(View.VISIBLE);
//        }
        choosePhoto();
    }

    public void goToLookPhoto(int position) {
        Intent intent = new Intent(this, LookPhotoActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("list", new Gson().toJson(photoList));
        startActivity(intent);
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

    private Uri getImageStreamFromExternal() {
        File externalPubPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
        );

        Uri uri = null;
        if (externalPubPath.exists()) {
            uri = Uri.fromFile(externalPubPath);
        }

        return uri;
    }

    private void uploadPic(String localPic) {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();

        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_UPLOAD);
        params.addHeader("Authorization", bean.getAccessToken());
        File oldFile = new File(localPic);
        if (oldFile.getTotalSpace() == 0) {
            System.out.println("取消拍照");
            return;
        }

        File newFile = null;
        try {
            newFile = new CompressHelper.Builder(this)
                    .setMaxWidth(1080)  // 默认最大宽度为720
                    .setMaxHeight(1920) // 默认最大高度为960
                    .setQuality(60)    // 默认压缩质量为80
                    .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .build()
                    .compressToFile(oldFile);
        } catch (Exception e) {
//            String filePath = PhotoUtil.newAmendRotatePhoto2(localPic, this);
//            T.s("空白后处理的地址:" + filePath);
//            try {
//                oldFile = new File(filePath);
//                newFile = new CompressHelper.Builder(this)
//                        .setMaxWidth(1080)  // 默认最大宽度为720
//                        .setMaxHeight(1920) // 默认最大高度为960
//                        .setQuality(60)    // 默认压缩质量为80
//                        .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
//                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
//                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
//                        .build()
//                        .compressToFile(oldFile);
//
//            } catch (Exception e1) {
//                T.s("空白后处理报错:" + e1.toString());
//                newFile = oldFile;
//            }
            newFile = oldFile;
        }
//
//        if (localPic.contains(".jpeg")) {
//            final BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            Bitmap bitmap = BitmapFactory.decodeFile(localPic, options);
//            String tmpFile = Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/cachePng.png";
//            System.out.println("tmpFile:" + tmpFile);
//            try {
//                FileOutputStream out = new FileOutputStream(tmpFile);
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // 100-best quality
//                out.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            newFile = new File(tmpFile);
//            System.out.println("tmpFileSize:" + newFile.getTotalSpace());
//        }

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

    private void getPhotoListForUser() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_ALBUM_PAGEBYID);
        params.addQueryStringParameter("userId", currentUserId);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                AlbumResponse response = gson.fromJson(result, AlbumResponse.class);
                System.out.println("albumListUserId:" + result);
                switch (response.getCode()) {
                    case 200:
                        photoList.clear();
                        for (int i = 0; i < response.getData().getRecords().size(); i++) {
                            photoList.add(response.getData().getRecords().get(i));
                        }
                        if (editStatus) {
                            if (photoList.size() == 0) {
                                photoRecyleViewList.setVisibility(View.GONE);
                                noneLayout.setVisibility(View.VISIBLE);
                            }

                            if (photoList.size() == 1) {
                                if (TextUtils.isEmpty(photoList.get(0).getUrl())) {
                                    photoRecyleViewList.setVisibility(View.GONE);
                                    noneLayout.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            photoRecyleViewList.setVisibility(View.VISIBLE);
                            noneLayout.setVisibility(View.GONE);
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
                        ((FriendStationApplication) getApplication()).isUpdateDoTask(PhotoActivity.this, mainLayout, 7);
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
//        Intent intent = new Intent();
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        if (Build.VERSION.SDK_INT < 19) {
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//        } else {
//            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//        }
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
//        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
//        YUtils.startForPickGalleryPhotoVideo(this, new BaseCallBack() {
//            @Override
//            public void onSuccess(String path) {
//                System.out.println("choose-photo-path:" + path);
//                if (!TextUtils.isEmpty(path)) {
//                    uploadPic(path);
//                } else {
//                    T.s("选择照片出错");
//                }
//            }
//
//            @Override
//            public void onFailure(int errCode) {
//
//            }
//        });

//        if (TypeUtil.isHuawei()) {
//            ImagePicker imagePicker = ImagePicker.getInstance();
//            imagePicker.setImageLoader(new PicassoImageLoader());   //设置图片加载器
//            imagePicker.setShowCamera(false);  //显示拍照按钮
//            imagePicker.setCrop(false);        //允许裁剪（单选才有效）
//            imagePicker.setSaveRectangle(true); //是否按矩形区域保存
//            imagePicker.setMultiMode(false); //是否按矩形区域保存
//            imagePicker.setSelectLimit(1);    //选中数量限制
//            imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
//            imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//            imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//            imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
//            imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
//            Intent intent = new Intent(this, ImageGridActivity.class);
//            startActivityForResult(intent, RC_CHOOSE_PHOTO);
//        } else {
//            Picker.from(this)
//                    .count(1)
//                    .enableCamera(false)
//                    .setEngine(new GlideEngine())
//                    .forResult(RC_CHOOSE_PHOTO);
//        }

    }

    private String getRealPathFromURI(Uri uri) {
        try {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            if (i != PackageManager.PERMISSION_GRANTED) {
//                showWaringDialog();

//                EasyPermissions.requestPermissions(
//                        new PermissionRequest.Builder(this, RC_CAMERA_AND_LOCATION, perms)
//                                .setRationale(R.string.camera_and_location_rationale)
//                                .setPositiveButtonText(R.string.rationale_ask_ok)
//                                .setNegativeButtonText(R.string.rationale_ask_cancel)
//                                .setTheme(R.style.my_fancy_style)
//                                .build());

                EasyPermissions.requestPermissions(this, "您需要允许以下权限，才可以正常使用该功能",
                        Constant.REQUEST_PERMISSION_CODE, permissions);
                return false;
            }
        }

        return true;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (requestCode == Constant.REQUEST_PERMISSION_CODE) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("提示！")
                    .setMessage("如拒绝权限将无法正常使用该功能！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 一般情况下如果用户不授权的话，功能是无法运行的，做退出处理

                        }
                    }).show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_CHOOSE_PHOTO:
                if (data == null) {
                    return;
                }
//                if (TypeUtil.isHuawei()) {
//                    ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//                    for (int i = 0; i < images.size(); i++) {
//                        double oldSize = SizeUtil.getFileOrFilesSize(images.get(i).path, 2);
//                        String filePath = PhotoUtil.newAmendRotatePhoto3(images.get(i).path, this, oldSize);
//                        if (!TextUtils.isEmpty(filePath)) {
//                            uploadPic(filePath);
//                        }
//                    }
//
//                } else {
//                    List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
//                    for (Uri u : mSelected) {
//                        String oldFilePath = FileUtil.getFilePathByUri(this, u);
//                        System.out.println("oldFilePath:" + oldFilePath);
//                        double oldSize = SizeUtil.getFileOrFilesSize(oldFilePath, 2);
//                        System.out.println("oldSize:" + oldSize);
//                        String filePath = PhotoUtil.amendRotatePhoto(oldFilePath, this);
//                        if (!TextUtils.isEmpty(filePath)) {
//                            uploadPic(filePath);
//                        }
//                    }
//                }
                Uri uri = data.getData();
                String oldFilePath = FileUtil.getFilePathByUri(this, uri);
                double oldSize = SizeUtil.getFileOrFilesSize(oldFilePath, 2);
                if (oldSize <= 15) {
                    T.s("太模糊了,请上传高清图");
                    return;
                }
                System.out.println("oldSize:" + oldSize);
                String filePath = PhotoUtil.newAmendRotatePhoto3(oldFilePath, this, oldSize);
                if (!TextUtils.isEmpty(filePath)) {
                    uploadPic(filePath);
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
