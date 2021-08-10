package com.tools.bitmap.mycropper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.edmodo.cropper.CropImageView;
import com.tools.bitmap.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by SRain on 2015/12/7.
 */
public class MyCropperActivity extends Activity implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private TextView back, title;
    private CropImageView cropImageView; //主图
    private SeekBar aspectRatioXSeek; // 按X轴缩放
    private SeekBar aspectRatioYSeek; // 按Y轴缩放
    private ToggleButton fixedAspectRatioToggle; // 是否开启按比例缩放
    private Spinner showGuidelinesSpin; // 选择
    private Button rotateButton; // 旋转按钮
    private TextView aspectRatioX; // 显示X缩放比例
    private TextView aspectRatioY; // 显示Y缩放比例
    private Button cropButton; // 截取按钮

    // Static final constants
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
    private static final int ROTATE_NINETY_DEGREES = 90;
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    private static final int ON_TOUCH = 1;

    // Instance variables
    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;

    private Bitmap croppedBtimap; // 处理后的图片

    // Saves the state upon rotating the screen/restarting the activity
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
        bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
    }

    // Restores the state upon rotating the screen/restarting the activity
    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
        mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper_rotate);
        init();
    }

    protected void init() {
        // Initialize components of the app
        cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        initData();
        aspectRatioXSeek = (SeekBar) findViewById(R.id.aspectRatioXSeek);
        aspectRatioYSeek = (SeekBar) findViewById(R.id.aspectRatioYSeek);
        fixedAspectRatioToggle = (ToggleButton) findViewById(R.id.fixedAspectRatioToggle);
        showGuidelinesSpin = (Spinner) findViewById(R.id.showGuidelinesSpin);
        // Set initial spinner value
        showGuidelinesSpin.setSelection(ON_TOUCH);

        // Sets the rotate button 旋转
        rotateButton = (Button) findViewById(R.id.Button_rotate);
        rotateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(ROTATE_NINETY_DEGREES);
            }
        });

        // Sets fixedAspectRatio
        fixedAspectRatioToggle.setOnCheckedChangeListener(this);

        // Sets initial aspect ratio to 10/10, for demonstration purposes
        cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);

        // Sets aspectRatioX&Y
        aspectRatioX = (TextView) findViewById(R.id.aspectRatioX);
        aspectRatioXSeek.setOnSeekBarChangeListener(this);
        aspectRatioXSeek.setEnabled(false);
        aspectRatioY = (TextView) findViewById(R.id.aspectRatioY);
        aspectRatioYSeek.setOnSeekBarChangeListener(this);
        aspectRatioYSeek.setEnabled(false);

        // Sets up the Spinner
        showGuidelinesSpin.setOnItemSelectedListener(this);

        cropButton = (Button) findViewById(R.id.Button_crop);
        cropButton.setOnClickListener(this);
    }


    private Bitmap bitmap = null;

    /**
     * 获取数据
     */
    private void initData() {
        try {
            String pathString = this.getIntent().getStringExtra("filepath");
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {
            Log.e("e", e.toString());
        }
        cropImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Button_crop:
                croppedBtimap = cropImageView.getCroppedImage();

                // 将Bitmap转换成二进制数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                croppedBtimap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bitmapByte = baos.toByteArray();

//                Intent intent = new Intent();
//                intent.putExtra("photoFlag", this.getIntent().getIntExtra("photoFlag", 0));
//                intent.putExtra("bitmap", bitmapByte);
//                intent.setClass(MyCropperActivity.this, ShowUploadActivity.class);
//                startActivityForResult(intent, StaticCode.SELECT_PHOTO_CODE);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        cropImageView.setFixedAspectRatio(isChecked);
        if (isChecked) {
            aspectRatioXSeek.setEnabled(true);
            aspectRatioYSeek.setEnabled(true);
        } else {
            aspectRatioXSeek.setEnabled(false);
            aspectRatioYSeek.setEnabled(false);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.aspectRatioXSeek:
                try {
                    mAspectRatioX = progress;
                    cropImageView.setAspectRatio(progress, mAspectRatioY);
                    aspectRatioX.setText(" " + progress);
                } catch (IllegalArgumentException e) {
                    Log.e("onProgressChanged", e.toString());
                }
                break;
            case R.id.aspectRatioYSeek:
                try {
                    mAspectRatioY = progress;
                    cropImageView.setAspectRatio(mAspectRatioX, progress);
                    aspectRatioY.setText(" " + progress);
                } catch (IllegalArgumentException e) {
                    Log.e("onProgressChanged", e.toString());
                }
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        cropImageView.setGuidelines(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        return;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (croppedBtimap != null) {
            croppedBtimap.recycle();
        }

        if (bitmap != null) {
            bitmap.recycle();
        }
    }
}
