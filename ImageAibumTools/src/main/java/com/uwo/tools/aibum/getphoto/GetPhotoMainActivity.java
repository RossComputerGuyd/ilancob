package com.uwo.tools.aibum.getphoto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.uwo.tools.aibum.R;
import com.uwo.tools.aibum.utils.ActionUtils;
import com.uwo.tools.aibum.utils.ImageUriUtils;

/**
 * 通过Action实现
 * 拍照
 * 图片查找
 * 图片剪切
 */
public class GetPhotoMainActivity extends Activity {
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getphoto_main);
        image = (ImageView) findViewById(R.id.image);
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
                                ActionUtils.openCameraImage(GetPhotoMainActivity.this);
                                break;
                            case 1:
                                ActionUtils.openLocalImage(GetPhotoMainActivity.this);
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
            case ActionUtils.OPEN_CAMERA_IMAGE_URI:
                // uri传入与否影响图片获取方式,以下二选一
                // 方式一,自定义Uri(ImageBitmapUtils.imageUriFromCamera),用于保存拍照后图片地址
                if (ImageUriUtils.imageUriFromCamera != null) {
                    // 可以直接显示图片,或者进行其他处理(如压缩或裁剪等)
//                    iv.setImageURI(ImageBitmapUtils.imageUriFromCamera);
                    // 对图片进行裁剪
                    ActionUtils.cropUriImage(this, ImageUriUtils.imageUriFromCamera);
                    String path = ImageUriUtils.getPathForUri(this, ImageUriUtils.imageUriFromCamera);
                    break;
                }
                break;
            // 手机相册获取图片
            case ActionUtils.GET_IMAGE_FROM_PHONE:
                if (data != null && data.getData() != null) {
                    // 可以直接显示图片,或者进行其他处理(如压缩或裁剪等)
                    image.setImageURI(data.getData());
                    // 对图片进行裁剪
                    ActionUtils.cropUriImage(this, data.getData());
                }
                break;
            // 裁剪图片后结果
            case ActionUtils.CROP_IMAGE:
                if (ImageUriUtils.cropImageUri != null) {
                    // 可以直接显示图片,或者进行其他处理(如压缩等)
                    image.setImageURI(ImageUriUtils.cropImageUri);
                }
                break;
            default:
                break;
        }
    }
}
