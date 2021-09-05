package com.uwo.tools.aibum.local.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

/**
 * Created by SRain on 2015/12/11.
 * <p/>
 * 根据Action实现功能
 * 1.拍照
 * 2.获取本地相册
 * 3.剪切图片
 */
public class ActionUtils {

    public static final int GET_IMAGE_FROM_PHONE = 5002; // 获取相册返回标识码
    public static final int CROP_IMAGE = 5003;  // 剪切返回标识码


    public static final int OPEN_CAMERA_IMAGE_URI = 5100; // 拍照直接返回uri
    public static final int OPEN_CAMERA_IMAGE_PATH = 5101; // 拍照传入返回文件路径
    public static final int OPEN_CAMERA_IMAGE_THUMB = 5102; // 拍照返回缩略图
    public static final int OPEN_CAMERA_IMAGE_NO_RESULT = 5103; // 拍照无返回值


    /**
     * 使用系统相机拍照
     * 设置输出参数，返回Uri
     *
     * @param activity
     */
    public static void openCameraImage(final Activity activity) {
        ImageUriUtils.imageUriFromCamera = ImageUriUtils.createImagePathUri(activity);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUriUtils.imageUriFromCamera);
        activity.startActivityForResult(intent, OPEN_CAMERA_IMAGE_URI);
    }

    /**
     * 使用系统相机拍照
     * 设置输出参数，返回指定的文件路径中
     *
     * @param activity
     * @param path     拍照后文件保存的路径
     */
    public static void openCameraImage(final Activity activity, String path) {
        File file = new File(path);
        if (!file.exists()) {
            file = new File(Environment.getExternalStorageDirectory(), path);
        }
        Uri uri = Uri.fromFile(file);
        ImageUriUtils.imageUriFromCamera = ImageUriUtils.createImagePathUri(activity);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, OPEN_CAMERA_IMAGE_PATH);
    }

    /**
     * 使用系统相机拍照
     * 不设置输出参数，系统会自动生成一个uri,但是只会返回一个缩略图
     * <p/>
     *
     * @param activity
     * @return 返回图片在onActivityResult中通过以下代码获取 Bitmap bitmap = (Bitmap) data.getExtras().get("data");
     */
    public static void openCameraImageThumb(final Activity activity) {
        ImageUriUtils.imageUriFromCamera = ImageUriUtils.createImagePathUri(activity);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, OPEN_CAMERA_IMAGE_THUMB);
    }

    /**
     * 不关心返回结果直接拍照
     */
    public static void openCameraImageNoResult(final Activity activity) {
        Intent intent = new Intent(); //调用照相机
        intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
        activity.startActivity(intent);
    }


    /**
     * 打开本地相册
     *
     * @param activity
     */
    public static void openLocalImage(final Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, GET_IMAGE_FROM_PHONE);
    }

    /**
     * 图片剪裁
     *
     * @param activity
     * @param srcUri
     */
    public static void cropUriImage(Activity activity, Uri srcUri) {
        ImageUriUtils.cropImageUri = ImageUriUtils.createImagePathUri(activity);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(srcUri, "image/*");
        intent.putExtra("crop", "true");

        ////////////////////////////////////////////////////////////////
        // 1.宽高和比例都不设置时,裁剪框可以自行调整(比例和大小都可以随意调整)
        ////////////////////////////////////////////////////////////////
        // 2.只设置裁剪框宽高比(aspect)后,裁剪框比例固定不可调整,只能调整大小
        ////////////////////////////////////////////////////////////////
        // 3.裁剪后生成图片宽高(output)的设置和裁剪框无关,只决定最终生成图片大小
        ////////////////////////////////////////////////////////////////
        // 4.裁剪框宽高比例(aspect)可以和裁剪后生成图片比例(output)不同,此时,
        //	会以裁剪框的宽为准,按照裁剪宽高比例生成一个图片,该图和框选部分可能不同,
        //  不同的情况可能是截取框选的一部分,也可能超出框选部分,向下延伸补足
        ////////////////////////////////////////////////////////////////

        // aspectX aspectY 是裁剪框宽高的比例
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪后生成图片的宽高
//		intent.putExtra("outputX", 300);
//		intent.putExtra("outputY", 100);

        // return-data为true时,会直接返回bitmap数据,但是大图裁剪时会出现问题,推荐下面为false时的方式
        // return-data为false时,不会返回bitmap,但需要指定一个MediaStore.EXTRA_OUTPUT保存图片uri
        Log.e("srcUri", srcUri.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUriUtils.cropImageUri);
        Log.e("cropImageUri", ImageUriUtils.cropImageUri.toString());
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, CROP_IMAGE);
    }
}
