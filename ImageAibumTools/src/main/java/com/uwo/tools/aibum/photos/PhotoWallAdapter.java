package com.uwo.tools.aibum.photos;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.uwo.tools.aibum.MyApplication;
import com.uwo.tools.aibum.R;

/**
 * GridView的适配器，负责异步从网络上加载(下载)图片展示在照片墙上。
 * 
 */
public class PhotoWallAdapter extends ArrayAdapter<String> implements
		OnScrollListener {

	/**
	 * 记录所有正在下载或等待下载的任务。(集合)
	 */
	private Set<BitmapWorkerTask> taskCollection;

	/**
	 * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
	 */
	private LruCache<String, Bitmap> mMemoryCache;

	/**
	 * GridView的实例
	 */
	private GridView mPhotoWall;

	/**
	 * 第一张可见图片的下标
	 */
	private int mFirstVisibleItem;

	/**
	 * 一屏有多少张图片可见
	 */
	private int mVisibleItemCount;

	/**
	 * 记录是否刚打开程序，用于解决进入程序不滚动屏幕，不会下载图片的问题。
	 */
	private boolean isFirstEnter = true;

	@SuppressLint("NewApi")
	public PhotoWallAdapter(Context context, int textViewResourceId,
			String[] objects, GridView photoWall) {
		super(context, textViewResourceId, objects);
		mPhotoWall = photoWall;
		taskCollection = new HashSet<BitmapWorkerTask>();
		// 获取应用程序最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		// 设置图片缓存大小为程序最大可用内存的1/8
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount();
			}
		};
		mPhotoWall.setOnScrollListener(this);
	}

	/**
	 * 该方法是继承ArrayAdapter的一个方法,复用convertView，减少对内存的占用
	 * 
	 * @author wangheng
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final String url = getItem(position);
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(
					R.layout.photo_layout, null);
		} else {
			view = convertView;
		}
		final ImageView photo = (ImageView) view.findViewById(R.id.photo);
		// 给ImageView设置一个Tag，保证异步加载图片时不会乱序
		photo.setTag(url);
		setImageView(url, photo);
		return view;
	}

	/**
	 * 给ImageView设置图片。首先从LruCache中取出图片的缓存，设置到ImageView上。如果LruCache中没有该图片的缓存，
	 * 就给ImageView设置一张默认图片。
	 * 
	 * @param imageUrl
	 *            图片的URL地址，用于作为LruCache的键。
	 * @param imageView
	 *            用于显示图片的控件。
	 */
	private void setImageView(String imageUrl, ImageView imageView) {
		Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(R.drawable.empty_photo);
		}
	}

	/**
	 * 将一张图片存储到LruCache中。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @param bitmap
	 *            LruCache的键，这里传入从网络上下载的Bitmap对象。
	 */
	@SuppressLint("NewApi")
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemoryCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	/**
	 * 从LruCache中获取一张图片，如果不存在就返回null。
	 * 
	 * @param key
	 *            LruCache的键，这里传入图片的URL地址。
	 * @return 对应传入键的Bitmap对象，或者null。
	 */
	@SuppressLint("NewApi")
	public Bitmap getBitmapFromMemoryCache(String key) {
		return mMemoryCache.get(key);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 仅当GridView静止时才去下载图片，GridView滑动时取消所有正在下载的任务
		if (scrollState == SCROLL_STATE_IDLE) {
			loadBitmaps(mFirstVisibleItem, mVisibleItemCount);
		} else {
			cancelAllTasks();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mFirstVisibleItem = firstVisibleItem;
		mVisibleItemCount = visibleItemCount;
		// 下载的任务应该由onScrollStateChanged里调用，但首次进入程序时onScrollStateChanged并不会调用，
		// 因此在这里为首次进入程序开启下载任务。
		if (isFirstEnter && visibleItemCount > 0) {
			// 从可见图片的第一张加载到可见图片最后一张
			loadBitmaps(firstVisibleItem, visibleItemCount);
			isFirstEnter = false;
		}
	}

	/**
	 * 加载Bitmap对象。此方法会在LruCache中检查所有屏幕中可见的ImageView的Bitmap对象，
	 * 如果发现任何一个ImageView的Bitmap对象不在缓存中，就会开启异步线程去下载图片。
	 * 
	 * @param firstVisibleItem
	 *            第一个可见的ImageView的下标
	 * @param visibleItemCount
	 *            屏幕中总共可见的元素数
	 */
	private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
		try {
			for (int i = firstVisibleItem; i < firstVisibleItem
					+ visibleItemCount; i++) {
				final String imageUrl = Images.imageUrls[i];
				// 从Lrucache中(缓存)获取
				Bitmap bitmap = getBitmapFromMemoryCache(imageUrl);
				if (bitmap == null) {
					/**
					 * 只要发现遍历的照片没在缓存中那么就会自动开启异步线程进行下载
					 */
					BitmapWorkerTask task = new BitmapWorkerTask();
					taskCollection.add(task);
					task.execute(i);
				} else {
					ImageView imageView = (ImageView) mPhotoWall
							.findViewWithTag(imageUrl);
					if (imageView != null && bitmap != null) {
						imageView.setImageBitmap(bitmap);
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取消所有正在下载或等待下载的任务。
	 */
	public void cancelAllTasks() {
		if (taskCollection != null) {
			for (BitmapWorkerTask task : taskCollection) {
				task.cancel(false);
			}
		}
	}

	/**
	 * 异步下载图片的任务。
	 * 
	 * @author guolin
	 */
	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
		/**
		 * 记录每个图片对应的位置
		 */
		private int mItemPosition;

		/**
		 * 图片的URL地址
		 */
		private String mImageUrl;

		/**
		 * 可重复使用的ImageView
		 */
		@Override
		protected Bitmap doInBackground(Integer... params) {
			mItemPosition = params[0];
			mImageUrl = Images.imageUrls[mItemPosition];
//			mItemPosition = imageUrl.lastIndexOf("/");
//			System.out.println("图片的URL地址::" + imageUrl);
			// 在后台开始下载图片
			Bitmap bitmap = downloadBitmap(mImageUrl);
			if (bitmap != null) {
				// 图片下载完成后缓存到LrcCache中
				addBitmapToMemoryCache(mImageUrl, bitmap);
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			// 根据Tag找到相应的ImageView控件，将下载好的图片显示出来。
			ImageView imageView = (ImageView) mPhotoWall
					.findViewWithTag(mImageUrl);
			if (imageView != null && bitmap != null) {
				imageView.setImageBitmap(bitmap);
//				System.out.println("已经到达位置");
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getContext(),
								ImageDetailsActivity.class);
						// int lastSlashIndex = imageUrl.lastIndexOf("/");
						intent.putExtra("image_position", mItemPosition);
						getContext().startActivity(intent);
					}
				});
			}
			taskCollection.remove(this);
		}

		/**
		 * 1 建立HTTP请求，并获取Bitmap对象。
		 * 
		 * @param imageUrl
		 *            图片的URL地址
		 * @return 解析后的Bitmap对象
		 */
		private Bitmap downloadBitmap(String imageUrl) {
			Bitmap bitmap = null;
			HttpURLConnection con = null;
			try {
				URL url = new URL(imageUrl);
				con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(5000);
				con.setReadTimeout(5000);
				con.setDoInput(true);
				con.setDoOutput(true);
				bitmap = BitmapFactory.decodeStream(con.getInputStream());
				// System.out.println("bitmap::"+(bitmap==null));
				int lastSlashIndex = imageUrl.lastIndexOf("/");
				// System.out.println("lastSlashIndex::"+lastSlashIndex);
				String imageName = imageUrl.substring(lastSlashIndex + 1);
				System.out.println("imageName::"+ imageName);
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(new File(generateParentFolder(),
								imageName)));
				bitmap.compress(CompressFormat.JPEG, 100, bos);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (con != null) {
					con.disconnect();
				}
			}
			return bitmap;
		}

	}

	/**
	 * 得到文件存放的路径
	 * 
	 * @return
	 */
	public String generateParentFolder() {
		String parentPath = MyApplication.getCtx()
				.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
				.getAbsolutePath();
		System.out.println("文件存储的路径：："+ parentPath);
		File tempFile = new File(parentPath);
		if (!tempFile.exists()) {
			tempFile.mkdirs();
		}
		return parentPath;
	}

}
