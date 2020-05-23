package com.rokid.glass.presentationdemo.glass.animation;

import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;

public class BreatheAnimate {
    ValueAnimator valueAnimator;
    public BreatheAnimate(long dur,float min , float max){
        valueAnimator = ValueAnimator.ofFloat(min,max,min);
        valueAnimator.setDuration(dur);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.getAnimatedValue();
    }

    public void startAnimate(){
        if(!valueAnimator.isStarted()) {
            valueAnimator.start();
        }
    }

    public float getValue(){
        return (float) valueAnimator.getAnimatedValue();
    }

}
