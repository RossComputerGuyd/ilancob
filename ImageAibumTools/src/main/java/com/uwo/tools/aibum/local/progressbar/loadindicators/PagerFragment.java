package com.uwo.tools.aibum.local.progressbar.loadindicators;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.uwo.tools.aibum.R;
import com.uwo.tools.aibum.local.progressbar.loadindicators.utils.LoaderType;
import com.uwo.tools.aibum.local.progressbar.loadindicators.utils.LoaderView;

/**
 * Created by Adrian on 22-Nov-14.
 */
public class PagerFragment extends Fragment implements View.OnClickListener {
    private LoaderView imageView;
    private TextView textView;
    private Button likeItButton;
    private LoaderType loaderType;
    private Typeface typeface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/chunkfive.ttf");
        loaderType = (LoaderType) getArguments().getSerializable("LOADER");
    }

    public static PagerFragment newInstance(LoaderType describable) {
        PagerFragment fragment = new PagerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("LOADER", describable);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_layout, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getReferences(view);

        textView.setTypeface(typeface);
        textView.setText(loaderType.getFramesAmount() + " frames".toUpperCase());

        likeItButton.setTypeface(typeface);
        likeItButton.setOnClickListener(this);

        imageView.setLoader(loaderType);
    }

    private void getReferences(View view) {
        imageView = (LoaderView) view.findViewById(R.id.imageLoader);
        textView = (TextView) view.findViewById(R.id.text);
        likeItButton = (Button) view.findViewById(R.id.likeButton);
    }

    @Override
    public void onClick(View v) {
//        Tracker t = ((MyApplication) getActivity().getApplication()).getTracker();
//        t.send(new HitBuilders.EventBuilder().
//                setCategory("LIKE").
//                setLabel(loaderType.toString().toUpperCase().replaceAll("_", " " + " liked")).
//                build());
//
//        Toast.makeText(getActivity(), "Like sent", Toast.LENGTH_SHORT).show();
    }
}
