package com.tools.bitmap.header;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.tools.bitmap.header.other.ImageCacheManager;
import com.tools.bitmap.header.other.ImageCacheManager.CacheType;
import com.tools.bitmap.header.other.RequestManager;
import com.tools.bitmap.header.view.HeadView;
import com.tools.bitmap.header.view.RingView;
import com.tools.bitmap.R;

/**
 * 大部分代码直接从项目中拷过来的, 没有处理.
 * 第一次分享自己东西, 不喜就喷吧.
 * 新的adt默认得用上actionbar和fragment... 所以东西有点多而且乱, 有兴趣的耐心看吧, 挺简单的.
 * 建议能用系统的就用系统的吧, 稳定点, 4.0后的也挺好看的. 我们的逗b产品就是要让自己做...
 * @author 
 *
 */
public class HeaderMainActivity extends ActionBarActivity {

	public static int WIN_WIDTH;
	public static int WIN_HEIGHT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_header_main);

//		getSupportActionBar().hide();
		initWindowHW();
		initHeadCache();

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initWindowHW() {
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metric = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metric);
		WIN_WIDTH = metric.widthPixels; // 屏幕宽度（像素）
		WIN_HEIGHT = metric.heightPixels; // 屏幕高度（像素）
	}
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements
			OnClickListener {

		private RelativeLayout mRlHead;
		private HeadView mHvHead;
		private RingView mRvClip;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_header_main, container, false);

			mHvHead = (HeadView) rootView.findViewById(R.id.hv_head);
			mHvHead.setImageUrl("http://testurl", ImageCacheManager.getInstance().getImageLoader());
			mHvHead.setErrorImageResId(R.drawable.home_ic_nohead_landing);
			mHvHead.setOnClickListener(this);
			mHvHead.setRegisted(true);

			mRlHead = (RelativeLayout) rootView.findViewById(R.id.rl_head);
			rootView.findViewById(R.id.btn_camera).setOnClickListener(this);
			rootView.findViewById(R.id.btn_pic).setOnClickListener(this);
			rootView.findViewById(R.id.btn_save).setOnClickListener(this);

			mRvClip = (RingView) rootView.findViewById(R.id.rv_clip);
			mRvClip.circle_radius = dp2Px(getActivity(), 140); // 半径设置,
																// 这里设置给的值只是显示用,
																// 项目中应自己考虑适配
			mRvClip.offset_y = dp2Px(getActivity(), 250); // y轴偏移设置 根据1280*
															// 720设置的
															// 建议在多个dimens中设置
			mRvClip.setMoveAble(true); // 设置头像可以移动, 我的项目中查看头像状态是不能编辑移动差价头像的.
			mRvClip.setHeadView(ImageCacheManager.getInstance().getBitmap(
					"http://testurl"));

			return rootView;
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {

			// 结果码不等于取消时候
			if (resultCode == Activity.RESULT_OK) {
				mRvClip.setSelected(true);// 设置头像背景为黑
				switch (requestCode) {
				case 2:
					Bitmap bm = getBitmapFromLocal(data.getData());
					if (bm != null) {
						mRvClip.setMoveAble(true);
						mRvClip.setImageBitmap(bm);
					} else {
						// mRvHead.setVisibility(View.GONE);
					}
					break;
				case 1:
					File file = new File(
							Environment.getExternalStorageDirectory(),
							"tempHead.jpg");
					if (file.exists()) {
						mRvClip.setMoveAble(true);
						mRvClip.setImageBitmap(GetBitmapForFile(file,
								mRvClip.getHeight()));
					} else {
					}
					break;

				/*
				 * case CAMERA2_REQUEST_CODE:
				 * mBtnCamera.setVisibility(View.VISIBLE); if(data != null){
				 * if(data.hasExtra("data")){ Bitmap thunbnail =
				 * data.getParcelableExtra("data"); //处理缩略图
				 * mRvHead.setMoveAble(true); mRvHead.setImageBitmap(thunbnail);
				 * } } break;
				 */

				}
				
				
			} else if (resultCode == Activity.RESULT_CANCELED) {
				mRvClip.setVisibility(View.GONE);
			}
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.hv_head:
				mRlHead.setVisibility(View.VISIBLE);
				mRvClip.setMoveAble(false);
				mRvClip.setHeadView(ImageCacheManager.getInstance().getBitmap("http://testurl"));
				break;

			case R.id.btn_camera:
				go2Camera();
				break;

			case R.id.btn_pic:
				go2Gallary();
				break;

			case R.id.btn_save:
				Bitmap bm = mRvClip.getBitmap(getActivity());
				// TODO 根据新的url保存新的头像， 并更新用户信息里的头像url
				mRvClip.setSelected(false);// 重置头像背景为透明
				ImageCacheManager.getInstance().putBitmap("http://testurl", bm);
				mHvHead.setHeadBitmap(bm);

				mRlHead.setVisibility(View.GONE);
				break;
			}
		}

		/**
		 * 根据uri获取到图片资源
		 * 
		 * @param uri
		 * @return
		 */
		private Bitmap getBitmapFromLocal(Uri uri) {
			Bitmap bitmap = null;
			Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
			if (cursor.moveToFirst()) {
				String filePath = cursor.getString(1);
				bitmap = GetBitmapForFile(new File(filePath), mRvClip.getHeight());
				cursor.close();
			}
			return bitmap;
		}

		/**
		 * 跳转到相机
		 */
		private void go2Camera() {
			Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			File file = new File(Environment.getExternalStorageDirectory(), "tempHead.jpg");
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
			startActivityForResult(intentFromCapture, 1);
			// }
		}

		/**
		 * 跳转到相册
		 */
		private void go2Gallary() {
			Intent intentFromGallery = new Intent();
			intentFromGallery.setType("image/*"); // 设置文件类型
			intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intentFromGallery, 2);
		}
	}

	/**
	 * Intialize the request manager and the image cache
	 */
	private void initHeadCache() {
		RequestManager.init(this);
		createImageCache();
	}

	/** 头像缓存 */
	private static int DISK_IMAGECACHE_SIZE = 1024 * 1024 * 1; // 1M
	private static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.JPEG;
	private static int DISK_IMAGECACHE_QUALITY = 100; // PNG is lossless so
														// quality is ignored
														// but must be provided

	/**
	 * Create the image cache. Uses Memory Cache by default. Change to Disk for
	 * a Disk based LRU implementation.
	 */
	private void createImageCache() {
		ImageCacheManager.getInstance().init(this, this.getPackageCodePath(),
				DISK_IMAGECACHE_SIZE, DISK_IMAGECACHE_COMPRESS_FORMAT,
				DISK_IMAGECACHE_QUALITY, CacheType.DISK);
	}

	public static int dp2Px(Context context, float dp) {
		return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
	}

	/**
	 * 通过文件获取图片
	 * 
	 * @param file
	 *            图片文件
	 * @param newWidth
	 *            要生成图片的宽度
	 * @param isRotate
	 *            是否要旋转
	 * @return
	 */
	public static Bitmap GetBitmapForFile(File file, int newHeight) {
		Bitmap bitmap;
		Options op = new Options();
		op.inJustDecodeBounds = true;
		bitmap = BitmapFactory.decodeFile(file.getPath());

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		int scale = width / WIN_WIDTH > height
				/ WIN_HEIGHT ? height
				/ WIN_HEIGHT : width
				/ WIN_WIDTH;
		op.inSampleSize = scale;
		op.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(file.getPath(), op);

		width = bitmap.getWidth();
		height = bitmap.getHeight();
		// 根据高度来缩放， 高度满屏, 是满要显示控件的屏
		float temp = ((float) newHeight) / ((float) height);

		Matrix matrix = new Matrix();
		matrix.postScale(temp, temp);

		int degrees = readPictureDegree(file.getAbsolutePath());
		matrix.postRotate(degrees);

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		bitmap.recycle();
		bitmap = null;

		return resizedBitmap;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		bitmap.recycle();
		return resizedBitmap;
	}
}
