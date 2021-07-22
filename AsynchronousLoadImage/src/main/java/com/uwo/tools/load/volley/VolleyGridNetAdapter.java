package com.uwo.tools.load.volley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.uwo.tools.load.R;
import com.uwo.tools.load.utils.Images;

/**
 * Created by SRain on 2015/11/30.
 */
public class VolleyGridNetAdapter extends BaseAdapter {

    private Context context;
    private int tag;
    private LayoutInflater mInflater;
    private RequestQueue mRequestQueue;

    public VolleyGridNetAdapter(Context context, int tag) {
        this.context = context;
        this.tag = tag;
        mInflater = LayoutInflater.from(context);
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
//        ViewHolder holder = null;
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = mInflater.inflate(R.layout.item_volley_net, null);
//            holder.imageView = (NetworkImageView) convertView.findViewById(R.id.photo);
//            convertView.setTag(holder.imageView);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }

        convertView = mInflater.inflate(R.layout.item_volley_net, null);
        NetworkImageView imageView = (NetworkImageView) convertView.findViewById(R.id.photo);
        LruImageCache lruImageCache = LruImageCache.instance();
        ImageLoader imageLoader = new ImageLoader(mRequestQueue, lruImageCache);
        imageView.setImageUrl(Images.imageUrls[position], imageLoader);
        return convertView;
    }

    private class ViewHolder {
        NetworkImageView imageView;
    }
}
