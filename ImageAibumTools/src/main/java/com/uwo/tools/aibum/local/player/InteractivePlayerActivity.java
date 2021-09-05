package com.uwo.tools.aibum.local.player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.uwo.tools.aibum.R;
import com.uwo.tools.aibum.local.player.interactive.InteractivePlayerView;
import com.uwo.tools.aibum.local.player.interactive.OnActionClickedListener;


/**
 * Created by mertsimsek on 14/08/15.
 * <p/>
 * https://github.com/iammert/InteractivePlayerView
 */
public class InteractivePlayerActivity extends Activity implements OnActionClickedListener {

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, InteractivePlayerActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interactive_player);

        final InteractivePlayerView ipv = (InteractivePlayerView) findViewById(R.id.ipv);
        ipv.setMax(123);
        ipv.setProgress(78);
        ipv.setOnActionClickedListener(this);

        final ImageView control = (ImageView) findViewById(R.id.control);
        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ipv.isPlaying()) {
                    ipv.start();
                    control.setBackgroundResource(R.drawable.pause);
                } else {
                    ipv.stop();
                    control.setBackgroundResource(R.drawable.play);
                }
            }
        });
    }

    @Override
    public void onActionClicked(int id) {
        switch (id) {
            case 1:
                //Called when 1. action is clicked.
                break;
            case 2:
                //Called when 2. action is clicked.
                break;
            case 3:
                //Called when 3. action is clicked.
                break;
            default:
                break;
        }
    }
}
