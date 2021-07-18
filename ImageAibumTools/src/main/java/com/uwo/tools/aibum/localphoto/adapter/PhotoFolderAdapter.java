package com.uwo.tools.aibum.localphoto.adapter;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uwo.tools.aibum.R;
import com.uwo.tools.aibum.localphoto.bean.AlbumInfo;
import com.uwo.tools.aibum.localphoto.imgaware.RotateImageViewAware;
import com.uwo.tools.aibum.localphoto.utils.ThumbnailsUtil;
import com.uwo.tools.aibum.localphoto.utils.UniversalImageLoadTool;

public class PhotoFolderAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<AlbumInfo> list;
	private ViewHolder holder;
	
	public PhotoFolderAdapter(Context context,List<AlbumInfo> list){
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
	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		AlbumInfo albumInfo = list.get(paramInt);
		if(null==paramView){
			holder = new ViewHolder();
			paramView = mInflater.inflate(R.layout.photofolder_item, null);
			ImageView image=(ImageView) paramView.findViewById(R.id.iv_folder);
			TextView text=(TextView) paramView.findViewById(R.id.tv_info);
			TextView num=(TextView) paramView.findViewById(R.id.tv_num);
			holder.image=image;
			holder.text=text;
			holder.num=num;
			paramView.setTag(holder);
		}else{
			holder = (ViewHolder) paramView.getTag();
		}
		UniversalImageLoadTool.disPlay(ThumbnailsUtil.MapgetHashValue(albumInfo.getImage_id(), albumInfo.getPath_file()),
				new RotateImageViewAware(holder.image, albumInfo.getPath_absolute()), R.drawable.ic_launcher);
		holder.text.setText(albumInfo.getName_album());
		holder.num.setText("("+list.get(paramInt).getList().size()+"张)");
		return paramView;
	}
	public class ViewHolder{
		public ImageView image;
		public TextView text;
		public TextView num;
	}
}
