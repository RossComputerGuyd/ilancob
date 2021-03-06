package com.uwo.tools.aibum.local;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.uwo.tools.aibum.R;
import com.uwo.tools.aibum.local.basic.BasicActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by SRain on 2015/12/15.
 * 前置摄像头拍照
 */
public class Camera1Activity extends BasicActivity {

    public static void actionStart(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, Camera1Activity.class);
        activity.startActivity(intent);
    }

    static final String TAG = "CAMERA ACTIVITY";

    Camera mCamera;
    //Preview surface
    SurfaceView surfaceView;
    //Preview surface handle for callback
    SurfaceHolder surfaceHolder;
    //Camera button
    Button btnCapture;
    //Note if preview windows is on.
    boolean previewing;

    int mCurrentCamIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera1);
    }

    @Override
    protected void initView() {
        btnCapture = (Button) findViewById(R.id.btn_capture);
        btnCapture.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View arg0) {
                if (previewing)
                    mCamera.takePicture(shutterCallback, rawPictureCallback, jpegPictureCallback);
            }
        });

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceViewCallback());
        //surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    ShutterCallback shutterCallback = new ShutterCallback() {
        @Override
        public void onShutter() {
        }
    };

    PictureCallback rawPictureCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {
        }
    };

    PictureCallback jpegPictureCallback = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] arg0, Camera arg1) {

            String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    .toString()
                    + File.separator
                    + "PicTest_" + System.currentTimeMillis() + ".jpg";
            File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }

            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(arg0);
                bos.flush();
                bos.close();
                scanFileToPhotoAlbum(file.getAbsolutePath());
                Toast.makeText(Camera1Activity.this, "[Test] Photo take and store in" + file.toString(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(Camera1Activity.this, "Picture Failed" + e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    };

    public void scanFileToPhotoAlbum(String path) {
        MediaScannerConnection.scanFile(Camera1Activity.this,
                new String[]{path}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("TAG", "Finished scanning " + path);
                    }
                });
    }

    private final class SurfaceViewCallback implements android.view.SurfaceHolder.Callback {
        public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
            if (previewing) {
                mCamera.stopPreview();
                previewing = false;
            }

            try {
                mCamera.setPreviewDisplay(arg0);
                mCamera.startPreview();
                previewing = true;
                setCameraDisplayOrientation(Camera1Activity.this, mCurrentCamIndex, mCamera);
            } catch (Exception e) {
            }
        }

        public void surfaceCreated(SurfaceHolder holder) {
//				mCamera = Camera.open();
            //change to front camera
            mCamera = openFrontFacingCameraGingerbread();
            // get Camera parameters
            Camera.Parameters params = mCamera.getParameters();

            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                // Autofocus mode is supported
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            previewing = false;
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private Camera openFrontFacingCameraGingerbread() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                    mCurrentCamIndex = camIdx;
                } catch (RuntimeException e) {
                    Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }
        return cam;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();

        //degrees  the angle that the picture will be rotated clockwise. Valid values are 0, 90, 180, and 270.
        //The starting position is 0 (landscape).
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
}
