package com.uwo.tools.aibum.getphoto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.uwo.tools.aibum.R;

import java.io.File;

/**
 * 通过Action实现
 * 拍照
 * 图片查找
 * 图片剪切
 */
public class GetPhotoMainActivity extends Activity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getphoto_main);

        iv = (ImageView) findViewById(R.id.iv);

        findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });
    }

    public void showImagePickDialog() {
        String title = "获取图片方式";
        String[] choices = new String[]{"拍照", "从手机中选择"};

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setItems(choices, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0:
                                ImageBitmapUtils.openCameraImage(GetPhotoMainActivity.this);
                                break;
                            case 1:
                                ImageBitmapUtils.openLocalImage(GetPhotoMainActivity.this);
                                break;
                        }
                    }
                })
                .setNegativeButton("返回", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }

        switch (requestCode) {
            // 拍照获取图片
            case ImageBitmapUtils.GET_IMAGE_BY_CAMERA:
                // uri传入与否影响图片获取方式,以下二选一
                // 方式一,自定义Uri(ImageBitmapUtils.imageUriFromCamera),用于保存拍照后图片地址
                if (ImageBitmapUtils.imageUriFromCamera != null) {

                    // 可以直接显示图片,或者进行其他处理(如压缩或裁剪等)
//                    iv.setImageURI(ImageBitmapUtils.imageUriFromCamera);
                    // 对图片进行裁剪
                    ImageBitmapUtils.cropImage(this, ImageBitmapUtils.imageUriFromCamera);
                    uriToFilepath(ImageBitmapUtils.imageUriFromCamera);
                    break;
                }
                break;
            // 手机相册获取图片
            case ImageBitmapUtils.GET_IMAGE_FROM_PHONE:
                if (data != null && data.getData() != null) {
                    // 可以直接显示图片,或者进行其他处理(如压缩或裁剪等)
                    iv.setImageURI(data.getData());

//                    Intent intent = new Intent(GetPhotoMainActivity.this, CropperMainActivity.class);
////                    intent.putExtra("uri", data.getData().toString());
//                    intent.setData(data.getData());
//                    Log.e("uri", data.getData().toString());
//                    startActivity(intent);

                    // 对图片进行裁剪
                    ImageBitmapUtils.cropImage(this, data.getData());
                }
                break;
            // 裁剪图片后结果
            case ImageBitmapUtils.CROP_IMAGE:
                if (ImageBitmapUtils.cropImageUri != null) {
                    // 可以直接显示图片,或者进行其他处理(如压缩等)
                    iv.setImageURI(ImageBitmapUtils.cropImageUri);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 根据URI获取文件路径
     *
     * @param uri
     * @return
     */
    public String uriToFilepath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, proj, null, null, null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String img_path = cursor.getString(index);
        File file = new File(img_path);
        Log.e("file", file.isFile() + file.getPath());
        return img_path;
    }
}
