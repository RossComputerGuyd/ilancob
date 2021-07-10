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
import com.tools.bitmap.drawable.DrawbleActivity;
import com.tools.bitmap.face.FaceMainActivity;
import com.tools.bitmap.getphoto.GetPhotoMainActivity;
import com.tools.bitmap.header.HeaderMainActivity;
import com.tools.bitmap.imagescan.ScanMainActivity;
import com.tools.bitmap.ken.activity.KenMainActivity;
import com.tools.bitmap.login.LoginActivity;
import com.tools.bitmap.photocat.PhotoCatMainActivity;
import com.tools.bitmap.photoview.activity.LauncherActivity;
import com.tools.bitmap.pictest.PictestMainActivity;
import com.tools.bitmap.rounded.activity.ExampleActivity;
import com.tools.bitmap.running.RuningManActivity;
import com.tools.bitmap.rview.RoudExampleActivity;
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
        adapter.add("图片脸部自动识别，将识别后的局部图片返回");
        adapter.add("非常简单的圆形图片处理工具(zhx)");
        adapter.add("Android自定义圆形+圆角控件(修改布局样式)");
        adapter.add("图片裁剪，代码精简(带拍照，简单实用)");
        adapter.add("ImageScan");
        adapter.add("拍照和相册获取图片并进行裁剪(带大图片处理)");
        adapter.add("圆形，圆角图片，已经封装好的方法，直接调用！");
        adapter.add("Android创建抗锯齿透明背景圆角图像");
        adapter.add("带自定义编辑功能的圆形头像");
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
            case 10:
                cls = FaceMainActivity.class;
                break;
            case 11:
                cls = DrawbleActivity.class;
                break;
            case 12:
                cls = RuningManActivity.class;
                break;
            case 13:
                cls = PhotoCatMainActivity.class;
                break;
            case 14:
                cls = ScanMainActivity.class;
                break;
            case 15:
                cls = GetPhotoMainActivity.class;
                break;
            case 16:
                cls = LoginActivity.class;
                break;
            case 17:
                cls = RoudExampleActivity.class;
                break;
            case 18:
                cls = HeaderMainActivity.class;
                break;
        }

        if (cls != null) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), cls);
            startActivity(intent);
        }
    }
}
