package com.uwo.tools.aibum.local;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uwo.tools.aibum.R;
import com.uwo.tools.aibum.cropper.MyCropperActivity;
import com.uwo.tools.aibum.cropper2.SimpleCropperActivity;
import com.uwo.tools.aibum.imagescan.ScanMainActivity;
import com.uwo.tools.aibum.local.basic.BasicActivity;
import com.uwo.tools.aibum.local.datetime.DateDialogActivity;
import com.uwo.tools.aibum.local.datetime.DateTimePickerActivity;
import com.uwo.tools.aibum.local.datetime.SlideActivity;
import com.uwo.tools.aibum.local.datetime.aige.AigeMainActivity;
import com.uwo.tools.aibum.local.datetime.card.CalendarCardActivity;
import com.uwo.tools.aibum.local.datetime.circle.CircleMainActivity;
import com.uwo.tools.aibum.local.datetime.countdownview.CuntdownMainActivity;
import com.uwo.tools.aibum.local.datetime.google.MainGoogleActivity;
import com.uwo.tools.aibum.local.datetime.square.SampleTimesSquareActivity;
import com.uwo.tools.aibum.local.dialog.DialogMainActivity;
import com.uwo.tools.aibum.local.player.InteractivePlayerActivity;
import com.uwo.tools.aibum.local.progressbar.VelocimeterActivity;
import com.uwo.tools.aibum.local.progressbar.avloading.AVLoadMainActivity;
import com.uwo.tools.aibum.local.progressbar.circleprogress.CircleProgressActivity;
import com.uwo.tools.aibum.local.progressbar.loadindicators.LoaderMainActivity;
import com.uwo.tools.aibum.local.progressbar.shape.ShapeMainActivity;
import com.uwo.tools.aibum.local.progressbar.squareprobar.SquprobarMainActivity;
import com.uwo.tools.aibum.local.progressbar.waveswipe.WaveSwipeMainActivity;
import com.uwo.tools.aibum.local.progressbar.waveview.WaveMainActivity;
import com.uwo.tools.aibum.local.progressbar.waveview.WaveMainActivity2;
import com.uwo.tools.aibum.local.skin.SinkMainActivity;
import com.uwo.tools.aibum.local.utils.ActionUtils;
import com.uwo.tools.aibum.local.utils.DialogUtils;
import com.uwo.tools.aibum.local.utils.ImageUriUtils;
import com.uwo.tools.aibum.local.utils.StaticCode;

import java.io.File;

/**
 * Created by SRain on 2015/12/11.
 * <p/>
 * 读取本地相册(整理)
 */
public class LocalActivity extends BasicActivity implements View.OnClickListener {

    private Button btnAlbum, btnScan, btnImageCropper, btnMap, btnDialog, btnDateTime, btnPlayer, btnProgress, btnSkin, btnGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);
    }

    /**
     * 初始化控件
     */
    protected void initView() {
        TextView back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("工具类综合整理");
        btnAlbum = (Button) findViewById(R.id.btnAlbum);
        btnAlbum.setOnClickListener(this);
        btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(this);
        btnImageCropper = (Button) findViewById(R.id.btnImageCropper);
        btnImageCropper.setOnClickListener(this);
        btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(this);
        btnDialog = (Button) findViewById(R.id.btnDialog);
        btnDialog.setOnClickListener(this);
        btnDateTime = (Button) findViewById(R.id.btnDateTime);
        btnDateTime.setOnClickListener(this);
        btnPlayer = (Button) findViewById(R.id.btnPlayer);
        btnPlayer.setOnClickListener(this);
        btnProgress = (Button) findViewById(R.id.btnProgress);
        btnProgress.setOnClickListener(this);
        btnSkin = (Button) findViewById(R.id.btnSkin);
        btnSkin.setOnClickListener(this);
        btnGuide = (Button) findViewById(R.id.btnGuide);
        btnGuide.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.btnAlbum:
                DialogUtils.createListDialog(this, this.getResources().getString(R.string.selectLocalAlbumType), this.getResources().getStringArray(R.array.selectAlbumType), new ItemClickListener(StaticCode.SELECT_BUTTON_STATE_ALBUM));
                break;
            case R.id.btnScan:
                DialogUtils.createListDialog(this, this.getResources().getString(R.string.selectSCanType), this.getResources().getStringArray(R.array.selectSCanType), new ItemClickListener(StaticCode.SELECT_BUTTON_STATE_SCAN));
                break;
            case R.id.btnImageCropper:
                DialogUtils.createListDialog(this, this.getResources().getString(R.string.selectImageCropperType), this.getResources().getStringArray(R.array.selectImageCropperType), new ItemClickListener(StaticCode.SELECT_BUTTON_STATE_CROPPER));
                break;
            case R.id.btnMap:
                break;
            case R.id.btnDialog:
                Intent intent = new Intent();
                intent.setClass(LocalActivity.this, DialogMainActivity.class);
                startActivity(intent);
                break;
            case R.id.btnDateTime:
                DialogUtils.createListDialog(this, this.getResources().getString(R.string.selectDateType), this.getResources().getStringArray(R.array.selectDateType), new ItemClickListener(StaticCode.SELECT_BUTTON_STATE_DATE));
                break;
            case R.id.btnPlayer:
                DialogUtils.createListDialog(this, this.getResources().getString(R.string.selectPlayerType), this.getResources().getStringArray(R.array.selectPlayerType), new ItemClickListener(StaticCode.SELECT_BUTTON_STATE_PLAYER));
                break;
            case R.id.btnProgress:
                DialogUtils.createListDialog(this, this.getResources().getString(R.string.selectProgressType), this.getResources().getStringArray(R.array.selectProgressType), new ItemClickListener(StaticCode.SELECT_BUTTON_STATE_PROGRESS));
                break;
            case R.id.btnSkin:
                DialogUtils.createListDialog(this, this.getResources().getString(R.string.selectSkinType), this.getResources().getStringArray(R.array.selectSkinType), new ItemClickListener(StaticCode.SELECT_BUTTON_STATE_SKIN));
                break;
            case R.id.btnGuide:
                DialogUtils.createListDialog(this, this.getResources().getString(R.string.selectGuideType), this.getResources().getStringArray(R.array.selectGuideType), new ItemClickListener(StaticCode.SELECT_BUTTON_STATE_GUIDE));
                break;
        }
    }

    public class ItemClickListener implements DialogInterface.OnClickListener {

        private int state;

        public ItemClickListener(int state) {
            this.state = state;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (state == StaticCode.SELECT_BUTTON_STATE_ALBUM) {
                switch (which) {
                    case 0:
                        ActionUtils.openLocalImage(LocalActivity.this);
                        break;
                    case 1:
                        Intent intent = new Intent(LocalActivity.this, ScanMainActivity.class);
                        startActivity(intent);
                        break;
                }
            } else if (state == StaticCode.SELECT_BUTTON_STATE_CROPPER) {
                switch (which) {
                    case 0:
                        SimpleCropperActivity.actionStart(LocalActivity.this);
                        break;
                    case 1:
                        MyCropperActivity.actionStart(LocalActivity.this);
                        break;
                }
            } else if (state == StaticCode.SELECT_BUTTON_STATE_SCAN) {
                switch (which) {
                    case 0:
                        ActionUtils.openCameraImage(LocalActivity.this);
                        break;
                    case 1:
                        String test = "srainTest.jpg"; // 文件名
                        ActionUtils.openCameraImage(LocalActivity.this, test);
                        break;
                    case 2:
                        ActionUtils.openCameraImageThumb(LocalActivity.this);
                        break;
                    case 3:
                        ActionUtils.openCameraImageNoResult(LocalActivity.this);
                        break;
                    case 4:
                        CameraActivity.actionStart(LocalActivity.this);
                        break;
                    case 5:
                        Camera1Activity.actionStart(LocalActivity.this);
                        break;
                }
            } else if (state == StaticCode.SELECT_BUTTON_STATE_DATE) {
                switch (which) {
                    case 0:
                        DateDialogActivity.actionStart(LocalActivity.this);
                        break;
                    case 1:
                        SlideActivity.actionStart(LocalActivity.this);
                        break;
                    case 2:
                        DateTimePickerActivity.actionStart(LocalActivity.this);
                        break;
                    case 3:
                        CalendarCardActivity.actionStart(LocalActivity.this);
                        break;
                    case 4:
                        SampleTimesSquareActivity.actionStart(LocalActivity.this);
                        break;
                    case 5:
                        MainGoogleActivity.actionStart(LocalActivity.this);
                        break;
                    case 6:
                        AigeMainActivity.actionStart(LocalActivity.this);
                        break;
                    case 7:
                        Toast.makeText(LocalActivity.this, "此demo在AsynchronousLoadImage中", Toast.LENGTH_SHORT).show();
                        break;
                    case 8:
                        CircleMainActivity.actionStart(LocalActivity.this);
                        break;
                    case 9:
                        CuntdownMainActivity.actionStart(LocalActivity.this);
                        break;
                }
            }

            if (state == StaticCode.SELECT_BUTTON_STATE_PLAYER) {
                switch (which) {
                    case 0:
                        InteractivePlayerActivity.actionStart(LocalActivity.this);
                        break;
                }
            }

            if (state == StaticCode.SELECT_BUTTON_STATE_PROGRESS) {
                switch (which) {
                    case 0:
                        VelocimeterActivity.actionStart(LocalActivity.this);
                        break;
                    case 1:
                        SquprobarMainActivity.actionStart(LocalActivity.this);
                        break;
                    case 2:
                        AVLoadMainActivity.actionStart(LocalActivity.this);
                        break;
                    case 3:
                        WaveMainActivity.actionStart(LocalActivity.this);
                        break;
                    case 4:
                        WaveMainActivity2.actionStart(LocalActivity.this);
                        break;

                    case 5:
                        WaveSwipeMainActivity.actionStart(LocalActivity.this);
                        break;

                    case 6:
                        LoaderMainActivity.actionStart(LocalActivity.this);
                        break;

                    case 7:
                        CircleProgressActivity.actionStart(LocalActivity.this);
                        break;

                    case 8:
                        ShapeMainActivity.actionStart(LocalActivity.this);
                        break;
                }
            }

            if (state == StaticCode.SELECT_BUTTON_STATE_SKIN) {
                switch (which) {
                    case 0:
                        SinkMainActivity.actionStart(LocalActivity.this);
                        break;
                }
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView image = (ImageView) findViewById(R.id.image);

        /**
         * 拍照返回值处理
         */
        switch (requestCode) {
            // 调用系统相机拍照，直接返回uri
            case ActionUtils.OPEN_CAMERA_IMAGE_URI:
                if (ImageUriUtils.imageUriFromCamera != null) {
                    // 可以直接显示图片,或者进行其他处理(如压缩或裁剪等)
                    image.setImageURI(ImageUriUtils.imageUriFromCamera);
                }
                break;

            // 调用系统相机拍照,传入返回路径
            case ActionUtils.OPEN_CAMERA_IMAGE_PATH:
                // 当传入文件路径的的情况下，data返回参数是null值，
                // 只要resultCode为RESULT_OK，则上述代码中/sdcard/srainTest.jpg的图片文件就是最新的照片文件。
                // MediaStore.EXTRA_OUTPUT的方法，经过手机实测除了我们设定的路径下有照片外，
                // 在手机存储卡上也会保存一份照片，默认目录为sdcard/dcim/camera下面，
                // 我曾经尝试着想如果每次返回可以取得sdcard/dcim/camera下面的路径就好了，
                // 但是目前看来没办法直接获得，可以借助MediaStroe每次去查询最后一条照片记录，应该也是可行的。
                String filePath = Environment.getExternalStorageDirectory() + "/srainTest.jpg";
                image.setImageURI(Uri.fromFile(new File(filePath)));
                break;

            // 调用系统相机拍照,返回缩略图
            case ActionUtils.OPEN_CAMERA_IMAGE_THUMB:
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                image.setImageBitmap(bitmap);
                break;
        }
    }
}
