package com.davy.davy_wanandroid.core.event;

/**
 * author: Davy
 * date: 18/10/1
 */
public class CollectEvent {

    private boolean isCancelCollectSucces;

    public CollectEvent(boolean isCancelCollectSucces){
        this.isCancelCollectSucces = isCancelCollectSucces;
    }

    public boolean isCancelCollectSucces(){
        return isCancelCollectSucces;
    }
}
