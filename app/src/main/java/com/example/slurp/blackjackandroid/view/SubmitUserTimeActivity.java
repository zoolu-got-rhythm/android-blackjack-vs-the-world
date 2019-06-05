package com.example.slurp.blackjackandroid.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.slurp.blackjackandroid.R;

public class SubmitUserTimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_user_time);


        final LinearLayout hintsLayoutParent = findViewById(R.id.title_container);
        final MenuHintView menuHintView = new MenuHintView(getApplicationContext(), "smile");
        hintsLayoutParent.addView(menuHintView);

    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
}
