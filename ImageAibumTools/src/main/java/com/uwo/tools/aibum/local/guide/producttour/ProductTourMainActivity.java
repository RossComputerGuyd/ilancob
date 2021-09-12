package com.uwo.tools.aibum.local.guide.producttour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.uwo.tools.aibum.R;
import com.uwo.tools.aibum.local.basic.BasicActivity;

/**
 * 
 */
//public class ProductTourMainActivity extends AppCompatActivity {
public class ProductTourMainActivity extends BasicActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkShowTutorial();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_tour_main);
        Button button = (Button) findViewById(R.id.btn_product_tour);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductTourMainActivity.this, ProductTourActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        Button button2 = (Button) findViewById(R.id.btn_product_tour2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductTourMainActivity.this, ProductTour2Activity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        Button button3 = (Button) findViewById(R.id.btn_product_tour3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductTourMainActivity.this, ProductTour3Activity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    @Override
    protected void initView() {
        
    }

    @Override
    protected void initData() {

    }

    private void checkShowTutorial() {
        int oldVersionCode = PrefConstants.getAppPrefInt(this, "version_code");
        int currentVersionCode = SAppUtil.getAppVersionCode(this);
        if (currentVersionCode > oldVersionCode) {
            startActivity(new Intent(ProductTourMainActivity.this, ProductTourActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            PrefConstants.putAppPrefInt(this, "version_code", currentVersionCode);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void actionStart(Activity activity) {
        Intent intent = new Intent();
        intent.setClass(activity, ProductTourMainActivity.class);
        activity.startActivity(intent);
    }
}
