package com.tools.bitmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tools.bitmap.circle.CircleActivity;
import com.tools.bitmap.colorart.activity.ColorArtActivity;
import com.tools.bitmap.crop.CropActivity;
import com.tools.bitmap.cropper.CropperMainActivity;
import com.tools.bitmap.custom.SamplesActivity;
import com.tools.bitmap.ken.activity.KenMainActivity;
import com.tools.bitmap.photoview.activity.LauncherActivity;
import com.tools.bitmap.pictest.PictestMainActivity;
import com.tools.bitmap.rounded.activity.ExampleActivity;
import com.tools.bitmap.shape.SampleActivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener {

    private View view;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        init();
        return view;
    }

    private void init() {
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        adapter.add("拍照和相册选择图片裁剪后更换头像demo");
        adapter.add("git圆形的ImageView");
        adapter.add("git带圆角的ImageView");
        adapter.add("支持双击或双指缩放的ImageView，在ViewPager等Scrolling view中正常使用，相比上面的AndroidTouchGallery，不仅支持ViewPager，同时支持单个ImageView");
        adapter.add("根据图片的均色设置背景色显示文字和图片，类似itune11中效果");
        adapter.add("实现Ken Burns effect效果，达到身临其境效果的ImageView");
        adapter.add("各种形状的ImageView, 相比上面的圆形ImageView，多了更多形状");
        adapter.add("可以自定义各种形状的ImageView, 并且支持边框");
        adapter.add("图片局部剪切工具，可触摸控制选择区域或旋转");
        adapter.add("图片裁剪Activity");
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Class<?> cls = null;
        switch (position) {
            case 0:
                cls = PictestMainActivity.class;
                break;
            case 1:
                cls = CircleActivity.class;
                break;
            case 2:
                cls = ExampleActivity.class;
                break;
            case 3:
                cls = LauncherActivity.class;
                break;
            case 4:
                cls = ColorArtActivity.class;
                break;
            case 5:
                cls = KenMainActivity.class;
                break;
            case 6:
                cls = SamplesActivity.class;
                break;
            case 7:
                cls = SampleActivity.class;
                break;
            case 8:
                cls = CropperMainActivity.class;
                break;
            case 9:
                cls = CropActivity.class;
                break;
        }
        if (cls != null) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), cls);
            startActivity(intent);
        }
    }
}
