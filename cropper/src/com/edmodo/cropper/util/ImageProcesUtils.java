package com.edmodo.cropper.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.Calendar;

/**
 * Created by root on 15-12-7.
 */
public class ImageProcesUtils {

    /**
     * 使用Uri截图
     * <p/>
     * http://blog.csdn.net/floodingfire/article/details/8144617
     * https://github.com/ryanhoo/PhotoCropper​
     *
     * @param context
     * @param uri
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    public void cropImageUri(Context context, Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true"); // 才能出剪辑的小方框，不然没有剪辑功能，只能选取图片
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    public void crop(Context context) {
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        innerIntent.putExtra("crop", "true");// 才能出剪辑的小方框，不然没有剪辑功能，只能选取图片
        innerIntent.putExtra("aspectX", 1); // 出现放大和缩小
        innerIntent.setType("image/*"); // 查看类型 详细的类型在 com.google.android.mms.ContentType

//===============================
//          innerIntent.putExtra("aspectX", 1);//裁剪框比例
//          innerIntent.putExtra("aspectY", 1);
//          innerIntent.putExtra("outputX", 120);//输出图片大小
//          innerIntent.putExtra("outputY", 120);
//================================

        File tempFile = new File("/sdcard/ll1x/" + Calendar.getInstance().getTimeInMillis() + ".jpg"); // 以时间秒为文件名
        File temp = new File("/sdcard/ll1x/");//自已项目 文件夹
        if (!temp.exists()) {
            temp.mkdir();
        }
        innerIntent.putExtra("output", Uri.fromFile(tempFile));  // 专入目标文件
        innerIntent.putExtra("outputFormat", "JPEG"); //输入文件格式

        Intent wrapperIntent = Intent.createChooser(innerIntent, "先择图片"); //开始 并设置标题
        ((Activity) context).startActivityForResult(wrapperIntent, 1); // 设返回 码为 1  onActivityResult 中的 requestCode 对应
    }
}
