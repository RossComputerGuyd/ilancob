package com.uwo.tools.aibum.localphoto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.uwo.tools.aibum.R;
import com.uwo.tools.aibum.localphoto.adapter.PhotoFolderAdapter;
import com.uwo.tools.aibum.localphoto.bean.AlbumInfo;
import com.uwo.tools.aibum.localphoto.bean.PhotoInfo;
import com.uwo.tools.aibum.localphoto.utils.ThumbnailsUtil;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PhotoFolder extends Fragment {

    public interface OnPageLodingClickListener {
        public void onPageLodingClickListener(List<PhotoInfo> list);
    }

    private OnPageLodingClickListener onPageLodingClickListener;

    private ListView lView;
    private ContentResolver cr;
    private List<AlbumInfo> listImageInfo = new ArrayList<AlbumInfo>();

    private PhotoFolderAdapter listAdapter;

    private ProgressBar pBar;
    private TextView tView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lView = (ListView) getView().findViewById(R.id.lv_photofolder);

        pBar = (ProgressBar) getView().findViewById(R.id.pb_load);
        tView = (TextView) getView().findViewById(R.id.tv_tip);

        cr = getActivity().getContentResolver();
        listImageInfo.clear();

        new ImageAsyncTask().execute();

        lView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paramAdapterView,
                                    View paramView, int paramInt, long paramLong) {
                onPageLodingClickListener.onPageLodingClickListener(listImageInfo.get(paramInt).getList());
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (onPageLodingClickListener == null) {
            onPageLodingClickListener = (OnPageLodingClickListener) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photofolder, container, false);
    }

    private class ImageAsyncTask extends AsyncTask<Void, Void, Object> {
        @Override
        protected Object doInBackground(Void... params) {
            //获取缩略图
            ThumbnailsUtil.clear();
            String[] projection = {Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA};
            Cursor cur = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);

            if (cur != null && cur.moveToFirst()) {
                int image_id;
                String image_path;
                int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
                int dataColumn = cur.getColumnIndex(Thumbnails.DATA);
                do {
                    image_id = cur.getInt(image_idColumn);
                    image_path = cur.getString(dataColumn);
                    ThumbnailsUtil.put(image_id, "file://" + image_path);
                } while (cur.moveToNext());
            }
            //获取原图
            Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, "date_modified DESC");
            String _path = "_data";
            String _album = "bucket_display_name";
            HashMap<String, AlbumInfo> myhash = new HashMap<String, AlbumInfo>();
            AlbumInfo albumInfo = null;
            PhotoInfo photoInfo = null;
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int index = 0;
                    int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                    String path = cursor.getString(cursor.getColumnIndex(_path));
                    String album = cursor.getString(cursor.getColumnIndex(_album));
                    List<PhotoInfo> stringList = new ArrayList<PhotoInfo>();
                    photoInfo = new PhotoInfo();
                    if (myhash.containsKey(album)) {
                        albumInfo = myhash.remove(album);
                        if (listImageInfo.contains(albumInfo))
                            index = listImageInfo.indexOf(albumInfo);
                        photoInfo.setImage_id(_id);
                        photoInfo.setPath_file("file://" + path);
                        photoInfo.setPath_absolute(path);
                        albumInfo.getList().add(photoInfo);
                        listImageInfo.set(index, albumInfo);
                        myhash.put(album, albumInfo);
                    } else {
                        albumInfo = new AlbumInfo();
                        stringList.clear();
                        photoInfo.setImage_id(_id);
                        photoInfo.setPath_file("file://" + path);
                        photoInfo.setPath_absolute(path);
                        stringList.add(photoInfo);
                        albumInfo.setImage_id(_id);
                        albumInfo.setPath_file("file://" + path);
                        albumInfo.setPath_absolute(path);
                        albumInfo.setName_album(album);
                        albumInfo.setList(stringList);
                        listImageInfo.add(albumInfo);
                        myhash.put(album, albumInfo);
                    }
                } while (cursor.moveToNext());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);
            pBar.setVisibility(View.GONE);
            tView.setVisibility(View.GONE);
            if (getActivity() != null) {
                listAdapter = new PhotoFolderAdapter(getActivity(), listImageInfo);
                lView.setAdapter(listAdapter);
            }
        }
    }

}
