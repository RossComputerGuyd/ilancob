package com.uwo.tools.load.fresco;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.uwo.tools.load.R;
import com.uwo.tools.load.utils.Images;

/**
 * Created by SRain on 2015/12/1.
 */
public class FrescoAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;

    public FrescoAdapter(Context context) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_fresco, null);
//            holder.draweeView = (SimpleDraweeView) convertView.findViewById(R.id.my_image_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Uri uri = Uri.parse(Images.imageUrls[position]);
//        holder.draweeView.setImageURI(uri);
        return convertView;
    }

    private class ViewHolder {
        SimpleDraweeView draweeView;
    }
}
