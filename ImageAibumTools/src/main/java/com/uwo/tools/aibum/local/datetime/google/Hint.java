package com.uwo.tools.aibum.local.datetime.google;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.uwo.tools.aibum.R;

public class Hint extends Activity {

    private Button mHintButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.hint);

        mHintButton = (Button) findViewById(R.id.button1);

        mHintButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
}
