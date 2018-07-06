package com.jaron.fsconnect.presentation.event;

import com.tencent.TIMConversation;
import com.tencent.TIMManager;
import com.tencent.TIMRefreshListener;

import java.util.List;
import java.util.Observable;

public class RefreshEvent extends Observable implements TIMRefreshListener {


    private volatile static RefreshEvent instance;

    private RefreshEvent(){
        //注册监听器
        TIMManager.getInstance().setRefreshListener(this);
    }

    public static RefreshEvent getInstance(){
        if (instance == null) {
            synchronized (RefreshEvent.class) {
                if (instance == null) {
                    instance = new RefreshEvent();
                }
            }
        }
        return instance;
    }

    @Override
    public void onRefresh() {
        setChanged();
        notifyObservers();
    }



    @Override
    public void onRefreshConversation(List<TIMConversation> list) {
        setChanged();
        notifyObservers();

    }
}
