package com.uwo.tools.aibum.utils;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by SRain on 2015/12/15.
 * <p/>
 * 自定义相机工具类
 * <p/>
 * open()：通过open方法获取Camera实例。
 * setPreviewDisplay(SurfaceHolder)：设置预览拍照
 * startPreview()：开始预览
 * stopPreview()：停止预览
 * release():释放Camera实例
 * <p/>
 * Camera.PictureCallback接口：该回调接口包含了一个onPictureTaken(byte[]data, Camera camera)方法。在这个方法中可以保存图像数据。
 * <p/>
 * SurfaceView类：用于控制预览界面
 * SurfaceHolder.Callback接口：用于处理预览的事件，需实现如下三个方法：
 * <p/>
 * 通过Camera方式 会比通过Intent方式获得更为丰富的功能。通常创建一个定制化的Camera需要如下步骤：
 * (1)通过Camera.open()来获取Camera实例。
 * (2)创建Preview类，需要继承SurfaceView类并实现SurfaceHolder.Callback接口。
 * (3)为相机设置Preview
 * (4)构建一个Preview的Layout来 预览相机；
 * (5)为拍照建立Listener以获取拍照后的回调;
 * (6)拍照并保存文件;
 * (7)释放Camera。
 */
public class CameraSurfacePreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraSurfacePreview(Context context) {
        super(context);
        mCamera = getCameraInstance();
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    /**
     * 预览界面创建时调用，每次界面改变后都会重新创建，需要获取相机资源并设置SurfaceHolder。
     *
     * @param holder
     */
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("Dennis", "surfaceCreated() is called");
        try {
            mCamera.setPreviewDisplay(holder);
            //默认情况下，拍照预览窗口的角度是横屏显示的。如果你的activity是竖屏状态，那么需要设置预览窗口的角度
//            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d("Dennis", "Error setting camera preview: " + e.getMessage());
        }
    }

    /**
     * 预览界面发生变化时调用，每次界面发生变化之后需要重新启动预览。
     *
     * @param holder
     * @param format
     * @param w
     * @param h
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mHolder.getSurface() == null) {
            return;
        }

        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            Log.e("stopPreview", e.toString());
        }

        try {
            mCamera.setPreviewDisplay(mHolder);
//            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (Exception e) {
        }
    }

    /**
     * 预览销毁时调用，停止预览，释放相应资源。
     *
     * @param holder
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 这个是拍照要执行的方法，包含了三个回调参数。
     * Shutter是快门按下时的回调，
     * raw是获取拍照原始数据的回调，
     * jpeg是获取经过压缩成jpg格式的图像数据。
     *
     * @param imageCallback
     */
    public void takePicture(Camera.PictureCallback imageCallback) {
        /**
         * imageCallback，他就是我们的拍照数据捕获类PictureCallback，
         * 它的任务很简单，就是在拍照完成后，处理图片，这里我们是将图片保存下来。
         * onPictureTaken方法中可以通过data参数获得拍照的照片数据。
         * 我们将这些数据存储在公共目录的picture目录里面，就是存放相册图片的地方，
         * 获得这个存放地址的方法为getOutputMediaFile，具体的代码参见上面的activity的代码。
         */
        mCamera.takePicture(null, null, imageCallback);
    }

    /**
     * Camera不能同时被两个activity使用，否则会引起程序崩溃，
     * 所以需要在不需要camera的时候释放资源，
     * 一般我们是在onPause中释放。
     */
    public void release() {
        Log.i("cameratest", "onPause");
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }


    /**
     * A safeway to get an instance of the Camera object.
     * 获得一个Camera对象不是通过new的方式，而是通过Camera.open()返回一个Camera对象，
     * 这一过程必须要有异常处理，为此我们将整个过程封装在getCameraInstance函数中，
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            Log.e("open", e.toString());
        }
        return c;
    }
}
