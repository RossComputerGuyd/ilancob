package com.uwo.tools.aibum.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.uwo.tools.aibum.R;
import com.uwo.tools.aibum.photos.Images;

/**
 * Created by SRain on 2015/11/30.
 */
public class VolleyGridAdapter extends BaseAdapter {

    private Context context;
    private int tag;
    private LayoutInflater mInflater;
    private RequestQueue mRequestQueue;

    public VolleyGridAdapter(Context context, int tag) {
        this.context = context;
        this.tag = tag;
        mInflater = LayoutInflater.from(context);
        // 使用Volley框架
        mRequestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public int getCount() {
        return Images.imageUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (holder == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.photo_layout, null);
            holder.image = (ImageView) convertView.findViewById(R.id.photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setImage(holder.image, position);
        return convertView;
    }

    private void setImage(final ImageView imageView, final int i) {
        switch (tag) {
            case 1:
                // Volley使用ImageRequest下载图片
                ImageRequest imgRequest = new ImageRequest(Images.imageUrls[i], new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap arg0) {
                        // TODO Auto-generated method stub
                        Log.e("bitmap", "i:" + i + "\n" + arg0.toString());
                        imageView.setImageBitmap(arg0);
                    }
                }, 300, 200, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        // TODO Auto-generated method stub

                    }
                });
                mRequestQueue.add(imgRequest);
                break;
            case 2:
                //Volley使用ImageLoader
                final LruCache<String, Bitmap> mImageCache = new LruCache<String, Bitmap>(20);
                ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
                    @Override
                    public void putBitmap(String key, Bitmap value) {
                        mImageCache.put(key, value);
                    }

                    @Override
                    public Bitmap getBitmap(String key) {
                        return mImageCache.get(key);
                    }
                };
                ImageLoader mImageLoader = new ImageLoader(mRequestQueue, imageCache);
                // imageView是一个ImageView实例
                // ImageLoader.getImageListener的第二个参数是默认的图片resource id
                // 第三个参数是请求失败时候的资源id，可以指定为0
                ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, android.R.drawable.ic_secure, android.R.drawable.ic_delete);
                mImageLoader.get(Images.imageUrls[i], listener);
                break;
        }
    }

    private class ViewHolder {
        ImageView image;
    }
}
