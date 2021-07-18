package com.uwo.tools.aibum.localphoto;

import java.util.ArrayList;
import java.util.List;

import com.uwo.tools.aibum.R;
import com.uwo.tools.aibum.localphoto.adapter.PhotoGridAdapter;
import com.uwo.tools.aibum.localphoto.bean.PhotoInfo;
import com.uwo.tools.aibum.localphoto.bean.PhotoList;
import com.uwo.tools.aibum.localphoto.utils.UniversalImageLoadTool;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class PhotoGrid extends Fragment {

    public interface OnPhotoClickListener {
        public void onPhotoClickListener(String path_absolute);
    }

    private OnPhotoClickListener onPhotoClickListener;

    private GridView gridView;
    private PhotoGridAdapter photoAdapter;

    private List<PhotoInfo> list;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        gridView = (GridView) getView().findViewById(R.id.gv_photos);

        Bundle args = getArguments();

        PhotoList photoGrid = (PhotoList) args.getSerializable("list");
        list = new ArrayList<PhotoInfo>();
        list.addAll(photoGrid.getList());
        photoAdapter = new PhotoGridAdapter(getActivity(), list);

        gridView.setAdapter(photoAdapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                onPhotoClickListener.onPhotoClickListener(list.get(position).getPath_absolute());
            }
        });
        gridView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) {
                    UniversalImageLoadTool.resume();
                } else {
                    UniversalImageLoadTool.pause();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (onPhotoClickListener == null) {
            onPhotoClickListener = (OnPhotoClickListener) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photogrid, container, false);
    }
}
