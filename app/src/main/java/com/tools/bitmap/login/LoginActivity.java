package com.tools.bitmap.login;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tools.bitmap.R;

public class LoginActivity extends Activity {
    private ImageView loginImage;
    private Button loginbtn;
    private EditText username;
    private EditText password;

    private Drawable mIconPerson;
    private Drawable mIconLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//不随屏幕旋转
        setContentView(R.layout.activity_login_main);

        mIconPerson = getResources().getDrawable(R.drawable.txt_person_icon);
        mIconPerson.setBounds(5, 1, 60, 50);
        mIconLock = getResources().getDrawable(R.drawable.txt_lock_icon);
        mIconLock.setBounds(5, 1, 60, 50);

        username = (EditText) findViewById(R.id.username);
        username.setCompoundDrawables(mIconPerson, null, null, null);
        password = (EditText) findViewById(R.id.password);
        password.setCompoundDrawables(mIconLock, null, null, null);

        init();
    }

    public void init() {
        //使用TextPaint的仿“粗体”设置setFakeBoldText为true。目前还无法支持仿“斜体”方法
        loginImage = (ImageView) findViewById(R.id.loginImage);
        loginImage.setBackgroundDrawable(new BitmapDrawable(Util.toRoundBitmap(this, "tup.png")));
        loginImage.getBackground().setAlpha(0);
        loginImage.setImageBitmap(Util.toRoundBitmap(this, "tup.png"));

        loginbtn = (Button) findViewById(R.id.loginbtn);
        loginbtn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.getBackground().setAlpha(20);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.getBackground().setAlpha(255);//设置的透明度
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

}
