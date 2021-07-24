package com.uwo.tools.load;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.uwo.tools.load.okhttp.OkMainActivity;
import com.uwo.tools.load.volley.VolleyActivity;
import com.uwo.tools.load.xutils.XUtilsMainActivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener {

    private View view;
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
        adapter.add("Volley框架使用");
        adapter.add("OKHttp");
        adapter.add("xutils");
        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Class cls = null;
        switch (position) {
            case 0:
                cls = VolleyActivity.class;
                break;
            case 1:
                cls = OkMainActivity.class;
                break;
            case 2:
                cls = XUtilsMainActivity.class;
                break;
        }
        staActivity(cls);
    }

    private void staActivity(Class cls) {
        if (cls == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        startActivity(intent);
    }
}
