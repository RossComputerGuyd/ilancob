package com.uwo.tools.load.okhttp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.uwo.tools.load.R;
import com.uwo.tools.load.okhttp.request.OkHttpRequest;
import com.uwo.tools.load.utils.Images;
import com.uwo.tools.load.utils.ViewHolderUtils;

/**
 * Created by SRain on 2015/11/30.
 */
public class OkHttpAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;

    public OkHttpAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return Images.imageUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return Images.imageUrls[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderUtils holder = null;
        if (convertView == null) {
            holder = new ViewHolderUtils();
            convertView = mInflater.inflate(R.layout.photo_layout, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderUtils) convertView.getTag();
        }
        new OkHttpRequest.Builder().url(Images.imageUrls[position]).imageView(holder.imageView).displayImage(null);
        return convertView;
    }
}
