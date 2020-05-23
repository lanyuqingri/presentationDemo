package com.rokid.glass.presentationdemo.glass.animation;

public class FrameAnimation {
    protected int frame;
    protected int step;
    protected float value;
    public FrameAnimation(int step){
        this.step = step;
    }
    public void startAnimation(){
        int x = (frame++)%step;
        value = (float) x/(float) step;
    }
}
