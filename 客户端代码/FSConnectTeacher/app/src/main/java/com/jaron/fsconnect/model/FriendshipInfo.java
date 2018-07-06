package com.jaron.fsconnect.model;


import android.util.Log;

import com.tencent.TIMFriendGroup;
import com.tencent.TIMFriendshipProxy;
import com.tencent.TIMUserProfile;
import com.jaron.fsconnect.presentation.event.FriendshipEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * 好友列表缓存数据结构
 */
public class FriendshipInfo extends Observable implements Observer {

    private final String TAG = "FriendshipInfo";

    private List<FriendProfile> friends;

    private static FriendshipInfo instance;

    private FriendshipInfo(){
        friends = new ArrayList<>();
        FriendshipEvent.getInstance().addObserver(this);
        refresh();
    }

    public synchronized static FriendshipInfo getInstance(){
        if (instance == null){
            instance = new FriendshipInfo();
        }
        return instance;
    }

    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable the {@link Observable} object.
     * @param data       the data passed to {@link Observable#notifyObservers(Object)}.
     */
    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof FriendshipEvent){
            if (data instanceof FriendshipEvent.NotifyCmd){
                FriendshipEvent.NotifyCmd cmd = (FriendshipEvent.NotifyCmd) data;
                Log.d(TAG, "get notify type:" + cmd.type);
                switch (cmd.type){
                    case REFRESH:
                    case DEL:
                    case ADD:
                    case PROFILE_UPDATE:
                    case ADD_REQ:
                    case GROUP_UPDATE:
                        refresh();
                        break;

                }
            }
        }
    }


    private void refresh(){
        friends.clear();
        Log.d(TAG, "get friendship info id :" + UserInfo.getInstance().getId());
        List<TIMUserProfile> timUserProfiles = TIMFriendshipProxy.getInstance().getFriends();
        if (timUserProfiles == null) return;
        for (TIMUserProfile profile : timUserProfiles){
            friends.add(new FriendProfile(profile));
        }
        setChanged();
        notifyObservers();
    }


    /**
     * 获取好友列表摘要
     */
    public List<FriendProfile> getFriends(){
        return friends;
    }

    /**
     * 判断是否是好友
     *
     * @param identify 需判断的identify
     */
    public boolean isFriend(String identify){
        for (FriendProfile profile : friends){

            if (identify.equals(profile.getIdentify())) return true;
        }
        return false;
    }


    /**
     * 获取好友资料
     *
     * @param identify 好友id
     */
    public FriendProfile getProfile(String identify){
        for (FriendProfile profile : friends){
            if (identify.equals(profile.getIdentify())) return profile;
        }
        return null;
    }

    /**
     * 清除数据
     */
    public void clear(){
        if (instance == null) return;
        friends.clear();
        instance = null;
    }


}
