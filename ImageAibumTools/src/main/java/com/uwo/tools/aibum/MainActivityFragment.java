package com.uwo.tools.aibum;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.uwo.tools.aibum.cropper.MyCropperActivity;
import com.uwo.tools.aibum.getphoto.GetPhotoMainActivity;
import com.uwo.tools.aibum.imagescan.ScanMainActivity;
import com.uwo.tools.aibum.localphoto.AlbumActivity;
import com.uwo.tools.aibum.photo.PhotoAlbumActivity;
import com.uwo.tools.aibum.photos.PhotosMainActivity;
import com.uwo.tools.aibum.recylcler.HomeActivity;
import com.uwo.tools.aibum.volley.VolleyActivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener {

    private View view;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        init();
        return view;
    }

    private void init() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        adapter.add("Android例子源码本地相册图片一次性多选有注释");
        adapter.add("相册");
        adapter.add("RecyclerView 瀑布流、ListView、GridView实例(zhx)");
        adapter.add("照片墙画廊图库");
        adapter.add("照片墙");
        adapter.add("Volley框架获取网络图片");
        adapter.add("图片剪裁/lib");
        adapter.add("通过Action获取本地相册");
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Class<?> cls = null;
        switch (position){
            case 0:
                cls = PhotoAlbumActivity.class;
                break;
            case 1:
                cls = ScanMainActivity.class;
                break;
            case 2:
                cls = HomeActivity.class;
                break;
            case 3:
                cls = AlbumActivity.class;
                break;
            case 4:
                cls = PhotosMainActivity.class;
                break;
            case 5:
                cls = VolleyActivity.class;
                break;
            case 6:
                cls = MyCropperActivity.class;
                break;
            case 7:
                cls = GetPhotoMainActivity.class;
                break;
        }
        startAc(cls);
    }

    private void startAc(Class<?> cls) {
        if(cls != null){
            Intent intent = new Intent();
            intent.setClass(getActivity(), cls);
            startActivity(intent);
        }
    }
}
