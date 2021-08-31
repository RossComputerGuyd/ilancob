package com.uwo.tools.aibum.local;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.uwo.tools.aibum.R;
import com.uwo.tools.aibum.local.basic.BasicActivity;
import com.uwo.tools.aibum.utils.CameraSurfacePreview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SRain on 2015/12/15.
 * <p/>
 * 自定义相机工具类
 */
public class CameraActivity extends BasicActivity implements View.OnClickListener, Camera.PictureCallback {

    public static void actionStart(final Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, CameraActivity.class);
        activity.startActivity(intent);
    }

    private CameraSurfacePreview mCameraSurPreview = null;
    private Button mCaptureButton = null;
    private String TAG = "Dennis";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }

    @Override
    protected void initView() {
        // Create our Preview view and set it as the content of our activity.
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        mCameraSurPreview = new CameraSurfacePreview(this);
        preview.addView(mCameraSurPreview);

        // Add a listener to the Capture button
        mCaptureButton = (Button) findViewById(R.id.button_capture);
        mCaptureButton.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        mCaptureButton.setEnabled(false);
        // get an image from the camera
        mCameraSurPreview.takePicture(this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        //save the picture to sdcard
        // 文件的保存路径/storage/emulated/0/Pictures/IMAGE_20151215_093643.jpg
        File pictureFile = getOutputMediaFile();
        Log.d("pictureFile", pictureFile.getPath() + pictureFile.isFile());
        if (pictureFile == null) {
            Log.d(TAG + "pictureFile", "Error creating media file, check storage permissions: ");
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        // Restart the preview and re-enable the shutter button so that we can take another picture
        camera.startPreview();
        //See if need to enable or not
        mCaptureButton.setEnabled(true);
    }

    /**
     * 拍照后的文件保存位置
     * /storage/emulated/0/Pictures/IMAGE_20151215_093643.jpg
     *
     * @return
     */
    private File getOutputMediaFile() {
        //get the mobile Pictures directory
        File picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        //get the current time
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(picDir.getPath() + File.separator + "IMAGE_" + timeStamp + ".jpg");
    }
}
