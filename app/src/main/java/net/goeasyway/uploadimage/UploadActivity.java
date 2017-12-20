package net.goeasyway.uploadimage;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.goeasyway.uploadimage.model.UploadResult;
import net.goeasyway.uploadimage.persenter.UploadResultpersenterinter;
import net.goeasyway.uploadimage.retrofit.ApiService;
import net.goeasyway.uploadimage.retrofit.PhotoApiService;
import net.goeasyway.uploadimage.retrofit.PhotoRequestBody;
import net.goeasyway.uploadimage.retrofit.UploadCallback;
import net.goeasyway.uploadimage.utils.photoyasuo;
import net.goeasyway.uploadimage.view.UploadResultview;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadActivity extends AppCompatActivity implements UploadResultview{
    private ImageView mImage;
    private static final int SELECT_PIC = 1;
    private Bitmap mBitmap;
    private TextView textView;

    private String imagePath;
    private PhotoApiService apiService;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static Uri tempUri;
    private static final int CROP_SMALL_PICTURE = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        apiService = ApiService.getInstance();

        Button selectBtn = (Button) findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFromGallery();
            }
        });
        mImage= (ImageView) findViewById(R.id.iv_image);
        Button uploadBtn = (Button) findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(imagePath);
            }
        });

                textView = (TextView) findViewById(R.id.photoPath);
    }

    private void selectFromGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, SELECT_PIC);
//        Intent intent = new Intent(UploadActivity.this, Main2Activity.class);
//        startActivityForResult(intent, SELECT_PIC);
        showChoosePicDialog();
    }
    /**
     * 显示修改图片的对话框
     */
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
        builder.setTitle("添加图片");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_PICK);
                        openAlbumIntent.setType("image/*");
                        //用startActivityForResult方法，待会儿重写onActivityResult()方法，拿到图片做裁剪操作
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "temp_image.jpg"));
                        // 将拍照所得的相片保存到SD卡根目录
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == MainActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    cutImage(tempUri); // 对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    cutImage(data.getData()); // 对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }
    /**
     * 裁剪图片方法实现
     */
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri,  projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            imagePath = cursor.getString(0);
            textView.setText(imagePath);
        }
        startActivityForResult(intent, CROP_SMALL_PICTURE);

    }
    /**
     * 保存裁剪之后的图片数据
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            mBitmap = extras.getParcelable("data");
            //这里图片是方形的，可以用一个工具类处理成圆形（很多头像都是圆形，这种工具类网上很多不再详述）
            mImage.setImageBitmap(mBitmap);//显示图片
            //在这个地方可以写上上传该图片到服务器的代码，后期将单独写一篇这方面的博客，敬请期待...
        }
    }
 //   @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != RESULT_OK) {
//            return;
//        }
//        switch (requestCode) {
//            case SELECT_PIC:
//                if (data == null) {
//                    return;
//                }
//                Uri uri = data.getData();
//                String[] projection = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getContentResolver().query(uri,  projection, null, null, null);
//                if (cursor != null && cursor.moveToFirst()) {
//                    imagePath = cursor.getString(0);
//                    textView.setText(imagePath);
//                }
//
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
    private void uploadImage(String path) {
        //图片压缩处理
        File file =  photoyasuo.getimage(path);
       // File file = new File(path);
        if (file == null || !file.exists()) {
            return;
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        PhotoRequestBody photoRequestBody = new PhotoRequestBody(body, new UploadProgress());
        MultipartBody.Part part = MultipartBody.Part.createFormData("photo", file.getName(), photoRequestBody);
        UploadResultpersenterinter uploadResultpersenterinter = new UploadResultpersenterinter(this);
        uploadResultpersenterinter.relevance(part);
//        Call<UploadResult> call = apiService.uploadPhoto(part);
//        call.enqueue(new Callback<UploadResult>() {
//            @Override
//            public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {
//                Log.i("Upload", "上传成功！");
//                UploadResult result = response.body();
//                if (result.getCode() != 0) {
//                    textView.setText("上传成功！url: " + result.getUrl());
//                } else {
//                    textView.setText("上传失败！error: " + result.getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UploadResult> call, Throwable t) {
//                Log.i("Upload", "上传失败！error: " + t.toString());
//                textView.setText("上传失败！");
//            }
//        });
    }


    @Override
    public void showData(UploadResult uploadResult) {
        if (uploadResult.getCode() != 0) {
                    textView.setText("上传成功！url: " + uploadResult.getUrl());
                } else {
                    textView.setText("上传失败！error: " + uploadResult.getMessage());
                }
    }

    private class UploadProgress implements UploadCallback {

        @Override
        public void onProgress(final long progress, final long total) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("上传进度：" + progress * 100/total + "%");
                }
            });

            Log.i("Upload", "上传进度：" + progress * 100/total + "%");
        }
    }
}
