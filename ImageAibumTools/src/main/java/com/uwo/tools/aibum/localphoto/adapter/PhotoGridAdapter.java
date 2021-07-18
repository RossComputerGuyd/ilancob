package com.uwo.tools.aibum.localphoto.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.uwo.tools.aibum.R;
import com.uwo.tools.aibum.localphoto.bean.PhotoInfo;
import com.uwo.tools.aibum.localphoto.imgaware.RotateImageViewAware;
import com.uwo.tools.aibum.localphoto.utils.ThumbnailsUtil;
import com.uwo.tools.aibum.localphoto.utils.UniversalImageLoadTool;

public class PhotoGridAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<PhotoInfo> list;
    private ViewHolder viewHolder;
    private int width;

    public PhotoGridAdapter(Context context, List<PhotoInfo> list) {
        DisplayMetrics dm = new DisplayMetrics();
        ((FragmentActivity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels / 3;
        mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int paramInt) {
        return list.get(paramInt);
    }

    @Override
    public long getItemId(int paramInt) {
        return paramInt;
    }

    @Override
    public View getView(int paramInt, View convertView, ViewGroup paramViewGroup) {
        PhotoInfo photoInfo = list.get(paramInt);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.photogrid_item, null);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_thumbnail);
            viewHolder.image = imageView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LayoutParams layoutParams = viewHolder.image.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = width;
        viewHolder.image.setLayoutParams(layoutParams);
        if (photoInfo != null) {
            UniversalImageLoadTool.disPlay(ThumbnailsUtil.MapgetHashValue(photoInfo.getImage_id(), photoInfo.getPath_file()),
                    new RotateImageViewAware(viewHolder.image, photoInfo.getPath_absolute()), R.drawable.ic_launcher);
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
    }
}
