package com.davy.davy_wanandroid.core.event;

/**
 * author: Davy
 * date: 2018/9/29
 */
public class NightModeEvent {
    private boolean isNightMode;

    public NightModeEvent(boolean isNightModeState){
        this.isNightMode = isNightModeState;
    }

    public boolean isNightMode() {
        return isNightMode;
    }

    public void setNightMode(boolean isNightModeState){
        this.isNightMode = isNightModeState;
    }
}
