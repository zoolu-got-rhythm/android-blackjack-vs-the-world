package com.example.slurp.blackjackandroid.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.animation.Animation;
import android.widget.Button;

public class AnimatorHelper {
    public static ObjectAnimator blinkAnimationEffect(Button btn){
        ObjectAnimator anim = ObjectAnimator.ofInt(btn, "textColor", Color.DKGRAY,
                Color.GREEN, Color.DKGRAY);
        anim.setDuration(700);
        anim.setEvaluator(new ArgbEvaluator());
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        return anim;
    }
}
