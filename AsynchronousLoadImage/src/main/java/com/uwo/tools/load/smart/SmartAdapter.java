package com.uwo.tools.load.smart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.loopj.android.image.SmartImageView;
import com.uwo.tools.load.R;
import com.uwo.tools.load.utils.Images;

/**
 * Created by SRain on 2015/12/1.
 */
public class SmartAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;

    public SmartAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_smart, null);
            holder.myImage = (SmartImageView) convertView.findViewById(R.id.my_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.myImage.setImageUrl(Images.imageUrls[position]);
        return convertView;
    }

    private class ViewHolder {
        SmartImageView myImage;
    }
}
