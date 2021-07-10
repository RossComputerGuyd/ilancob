package com.tools.bitmap.header.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.NetworkImageView;
import com.tools.bitmap.header.HeaderMainActivity;

/**
 * 代码直接从项目中取出的, 没有优化, 各种公有变量..没功夫弄了
 * 
 * 
 * 封装头像剪切控件，可以通过getBitmap方法获取到圆里的图片做为剪切结果 可以通过setOffsetY方法设置圆的位置在y轴上向下的偏移
 * 可以通过setMoveAble方法设置背景图是否可以移动，当只做显示时可以调用此方法 可以通过setImageBimtmap方法设置要剪切的对象
 * 
 * 可以设置头像编辑圆框的大小和y轴方向的偏移量.
 * 
 * 当调用setHeadView()传入一个null时, 头像编辑界面上的圆框不显示.
 * 
 * 加入了本地缓存, 可以直接给一个url.
 * @author 
 */
public class RingView extends ImageView {
	// 三个属性根据自己需求进行改变
//	private static final int CIRCLE_RADIUS = 133;
//	private static final int RING_WIDTH = 2;
	public int circle_radius;
	public int ring_width;
	public int offset_y;

	private final Paint paint;

	private int width;
	private int height;

	private Matrix matrix;
	private Matrix savedMatrix;

	private PointF start;
	private PointF mid;
	float oldDist = 1f;

	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	private static final String TAG = "myclipview";
	int mode = NONE;

	private boolean isMoveAble = true;
	private boolean isShow = true;

	// private Bitmap mBm;
	// private boolean isInitBm = false;

	public RingView(Context context) {
		this(context, null);
	}

	public RingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		ring_width = dip2px(context, 2);

		this.setScaleType(ScaleType.MATRIX);
		isMatrix = true;

		matrix = new Matrix();
		savedMatrix = new Matrix();
		start = new PointF();
		mid = new PointF();

		// matrix.postTranslate(0, offset_y);
		// this.setImageMatrix(matrix);

		this.paint = new Paint();
		this.paint.setAntiAlias(true); // 消除锯齿
		this.paint.setStyle(Paint.Style.STROKE); // 绘制空心圆
	}

	private int bmWidth;
	private int bmHeight;

	@Override
	public void setImageBitmap(Bitmap bm) {
		if (null == bm) {
			return;
		}
		isShow = true;
		bmWidth = bm.getWidth();
		bmHeight = bm.getHeight();
		super.setImageBitmap(bm);
		matrix = new Matrix();
		matrix.postTranslate(HeaderMainActivity.WIN_WIDTH / 2 - bmWidth / 2,
				offset_y - bmHeight / 2);
		this.setImageMatrix(matrix);
	}

	public void setHeadView(Bitmap bm) {
		if (bm == null) {
			isShow = false;
			return;
		}
		isShow = true;
		bmWidth = bm.getWidth();
		bmHeight = bm.getHeight();
		super.setImageBitmap(toRoundBitmap(bm));
		matrix = new Matrix();
		matrix.postTranslate(HeaderMainActivity.WIN_WIDTH / 2 - bmWidth / 2,
				offset_y - bmHeight / 2);
		this.setImageMatrix(matrix);
	}

	private boolean isMatrix = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isMoveAble) {
			return true;
		}

		Log.i(TAG, "" + getScaleType() + ", isMatrix:" + isMatrix);
		if (!isMatrix) {
			matrix.setTranslate(width / 2 - bmWidth / 2, height / 2 - bmHeight
					/ 2);
			this.setScaleType(ScaleType.MATRIX);
			this.setImageMatrix(matrix);

			this.isMatrix = true;
		}
		// Handle touch events here...
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			// 設置初始點位置
			start.set(event.getX(), event.getY());
			Log.d(TAG, "mode=DRAG");
			mode = DRAG;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			Log.d(TAG, "oldDist=" + oldDist);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
				Log.d(TAG, "mode=ZOOM");
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			Log.d(TAG, "mode=NONE");
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				// ...
				matrix.set(savedMatrix);
				float x = event.getX() - start.x;
				float y = event.getY() - start.y;
				Log.i(TAG, "x:" + x + ", y:" + y);
				matrix.postTranslate(event.getX() - start.x, event.getY()
						- start.y);
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				Log.d(TAG, "newDist=" + newDist);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}

		this.setImageMatrix(matrix);
		return true;
	}

	public void initMatrixt(Matrix matrix) {
		// FIXME
		this.matrix.set(matrix);
	}

	public boolean isMoveAble() {
		return isMoveAble;
	}

	public void setMoveAble(boolean isMoveAble) {
		this.isMoveAble = isMoveAble;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setIsShow(boolean isShow) {
		this.isShow = isShow;
	}

	/**
	 * 设置剪切圆的中心Y值：圆向下的偏移量
	 * 
	 * @param offsetY
	 */
	public void setOffsetY(int offsetY) {
		this.offset_y = offsetY;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		

		this.paint.setARGB(160, 0, 0, 0);
		if (!isShow) {
			this.paint.setStyle(Paint.Style.FILL);
			canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
			this.paint.setStyle(Paint.Style.STROKE);
			return;
		}

		locations = new int[2];
		getLocationInWindow(locations);

		width = this.getWidth();
		height = this.getHeight();

		int centerX = width / 2;
		// centerY = height / 2 + dip2px(context, offset_y);
		int centerY = offset_y;
//		int innerCircle = dip2px(context, CIRCLE_RADIUS); // 设置内圆半径
//		int ringWidth = dip2px(context, RING_WIDTH); // 设置圆环宽度
		int innerCircle = circle_radius; // 设置内圆半径
		int ringWidth = ring_width; // 设置圆环宽度

		int newRingWidth = (getHeight() + locations[1]) / 2 - innerCircle
				- ringWidth;
		this.paint.setStrokeWidth(newRingWidth * 2);
		canvas.drawCircle(centerX, centerY,
				(getMeasuredHeight() + locations[1]) / 2, this.paint);

		// 绘制内圆
		this.paint.setARGB(155, 167, 190, 206);
		this.paint.setStrokeWidth(2);
		canvas.drawCircle(centerX, centerY, innerCircle, this.paint);

		// 绘制圆环
		this.paint.setARGB(255, 212, 225, 233);
		// this.paint.setARGB(255, 212 ,0, 0);
		this.paint.setStrokeWidth(ringWidth + dip2px(getContext(), 1));
		canvas.drawCircle(centerX, centerY, innerCircle + ringWidth / 2
				+ dip2px(getContext(), 1), this.paint);

	}

	/** Determine the space between the first two fingers */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	/** Calculate the mid point of the first two fingers */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/* 获取矩形区域内的截图 */
	public Bitmap getBitmap(Activity activity) {
		getBarHeight(activity);
		Bitmap screenShoot = takeScreenShot(activity);

//		int realRadius = dip2px(getContext(), CIRCLE_RADIUS);
		int realRadius = circle_radius;

		int x = width / 2 - realRadius;
		// int y = height / 2 - realRadius + locations[1] + dip2px(context,
		// offset_y);

		Bitmap finalBitmap = Bitmap.createBitmap(screenShoot, x,
				offset_y - realRadius + statusBarHeight, 
				2 * realRadius, 2 * realRadius);
		setImageDrawable(new ColorDrawable(android.R.color.transparent));
		return toRoundBitmap(finalBitmap);
	}

	int statusBarHeight = 0;
	int titleBarHeight = 0;
	private int[] locations;

	private void getBarHeight(Activity activity) {
		// 获取状态栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		statusBarHeight = frame.top;

		int contenttop = activity.getWindow()
				.findViewById(Window.ID_ANDROID_CONTENT).getTop();
		// statusBarHeight是上面所求的状态栏的高度
		titleBarHeight = contenttop - statusBarHeight;
	}

	// 获取Activity的截屏
	private Bitmap takeScreenShot(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bm = view.getDrawingCache();
		Bitmap bm2 = Bitmap.createBitmap(bm);
		view.setDrawingCacheEnabled(false);
		return bm2;
	}

	/**
	 * 转换图片成圆形
	 * 
	 * @param bitmap
	 *            传入Bitmap对象
	 * @return
	 */
	public Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}
		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);

		bitmap.recycle();
		return output;
	}

	/*
	 * 如果界面是跳转到其它应用,如相机,相册等 再回来此界面时, 为了能让图片继续能正确响应touch, 需要在回调方法里执行下面代码 Matrix
	 * matrix = mMcvHead.getImageMatrix(); matrix.setTranslate(0, 0);
	 * mMcvHead.initMatrixt(matrix); mMcvHead.setImageMatrix(matrix);
	 */

	/** The URL of the network image to load */
	private String mUrl;

	/**
	 * Resource ID of the image to be used as a placeholder until the network
	 * image is loaded.
	 */
	private int mDefaultImageId;

	/**
	 * Resource ID of the image to be used if the network response fails.
	 */
	private int mErrorImageId;

	/** Local copy of the ImageLoader. */
	private ImageLoader mImageLoader;

	/** Current ImageContainer. (either in-flight or finished) */
	private ImageContainer mImageContainer;

	/**
	 * Sets URL of the image that should be loaded into this view. Note that
	 * calling this will immediately either set the cached image (if available)
	 * or the default image specified by
	 * {@link NetworkImageView#setDefaultImageResId(int)} on the view.
	 * 
	 * NOTE: If applicable, {@link NetworkImageView#setDefaultImageResId(int)}
	 * and {@link NetworkImageView#setErrorImageResId(int)} should be called
	 * prior to calling this function.
	 * 
	 * @param url
	 *            The URL that should be loaded into this ImageView.
	 * @param imageLoader
	 *            ImageLoader that will be used to make the request.
	 */
	public void setImageUrl(String url, ImageLoader imageLoader) {
		mUrl = url;
		mImageLoader = imageLoader;
		// The URL has potentially changed. See if we need to load it.
		loadImageIfNecessary(false);
	}

	/**
	 * Sets the default image resource ID to be used for this view until the
	 * attempt to load it completes.
	 */
	public void setDefaultImageResId(int defaultImage) {
		mDefaultImageId = defaultImage;
	}

	/**
	 * Sets the error image resource ID to be used for this view in the event
	 * that the image requested fails to load.
	 */
	public void setErrorImageResId(int errorImage) {
		mErrorImageId = errorImage;
	}

	/**
	 * Loads the image for the view if it isn't already loaded.
	 * 
	 * @param isInLayoutPass
	 *            True if this was invoked from a layout pass, false otherwise.
	 */
	private void loadImageIfNecessary(final boolean isInLayoutPass) {
		int width = getWidth();
		int height = getHeight();

		boolean isFullyWrapContent = getLayoutParams() != null
				&& getLayoutParams().height == LayoutParams.WRAP_CONTENT
				&& getLayoutParams().width == LayoutParams.WRAP_CONTENT;
		// if the view's bounds aren't known yet, and this is not a
		// wrap-content/wrap-content
		// view, hold off on loading the image.
		if (width == 0 && height == 0 && !isFullyWrapContent) {
			return;
		}

		// if the URL to be loaded in this view is empty, cancel any old
		// requests and clear the
		// currently loaded image.
		if (TextUtils.isEmpty(mUrl)) {
			if (mImageContainer != null) {
				mImageContainer.cancelRequest();
				mImageContainer = null;
			}
			setImageBitmap(null);
			return;
		}

		// if there was an old request in this view, check if it needs to be
		// canceled.
		if (mImageContainer != null && mImageContainer.getRequestUrl() != null) {
			if (mImageContainer.getRequestUrl().equals(mUrl)) {
				// if the request is from the same URL, return.
				return;
			} else {
				// if there is a pre-existing request, cancel it if it's
				// fetching a different URL.
				mImageContainer.cancelRequest();
				setImageBitmap(null);
			}
		}

		// The pre-existing content of this view didn't match the current URL.
		// Load the new image
		// from the network.
		ImageContainer newContainer = mImageLoader.get(mUrl,
				new ImageListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (mErrorImageId != 0) {
							setImageBitmap(BitmapFactory.decodeResource(
									getResources(), mErrorImageId));
						}
					}

					@Override
					public void onResponse(final ImageContainer response,
							boolean isImmediate) {
						// If this was an immediate response that was delivered
						// inside of a layout
						// pass do not set the image immediately as it will
						// trigger a requestLayout
						// inside of a layout. Instead, defer setting the image
						// by posting back to
						// the main thread.
						if (isImmediate && isInLayoutPass) {
							post(new Runnable() {
								@Override
								public void run() {
									onResponse(response, false);
								}
							});
							return;
						}

						if (response.getBitmap() != null) {
							setImageBitmap(response.getBitmap());
						} else if (mDefaultImageId != 0) {
							setImageBitmap(BitmapFactory.decodeResource(
									getResources(), mDefaultImageId));
						}
					}
				});

		// update the ImageContainer to be the new bitmap container.
		mImageContainer = newContainer;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		loadImageIfNecessary(true);
	}

	@Override
	protected void onDetachedFromWindow() {
		if (mImageContainer != null) {
			// If the view was bound to an image request, cancel it and clear
			// out the image from the view.
			mImageContainer.cancelRequest();
			setImageBitmap(null);
			// also clear out the container so we can reload the image if
			// necessary.
			mImageContainer = null;
		}
		super.onDetachedFromWindow();
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		invalidate();
	}
}
