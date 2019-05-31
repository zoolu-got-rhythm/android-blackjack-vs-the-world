package com.example.slurp.blackjackandroid.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.slurp.blackjackandroid.R;

// singleton
public class AnimationManager {
    private ObjectAnimator objectAnimator;
    private Button button;
    private Context context;
    PowerManager powerManager;
    public AnimationManager(Context context, Button button) {
        this.context = context;

        this.powerManager = (PowerManager)
                this.context.getSystemService(Context.POWER_SERVICE);

        this.button = button;
        this.objectAnimator = AnimatorHelper.blinkAnimationEffect(this.button);
    }


    // start
    public void start(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && this.powerManager.isPowerSaveMode()) {
            // set button text to green;
            // Animations are disabled in power save mode, so just show a toast instead.
        }else{
            this.objectAnimator.start();
        }
    }

    // stop

    // pause
    public void pause(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && this.powerManager.isPowerSaveMode()) {
            // Animations are disabled in power save mode, so just show a toast instead.
        }else{
            this.objectAnimator.pause();
        }
    }
}
