package com.jaron.fsconnectparent.presentation.presenter;

import android.util.Log;

import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupCacheInfo;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.jaron.fsconnectparent.presentation.event.FriendshipEvent;
import com.jaron.fsconnectparent.presentation.event.GroupEvent;
import com.jaron.fsconnectparent.presentation.event.MessageEvent;
import com.jaron.fsconnectparent.presentation.event.RefreshEvent;
import com.jaron.fsconnectparent.presentation.viewfeatures.ConversationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 会话界面逻辑
 */
public class ConversationPresenter implements Observer {

    private static final String TAG = "ConversationPresenter";
    private ConversationView view;

    public ConversationPresenter(ConversationView view){
        //注册消息监听
        MessageEvent.getInstance().addObserver(this);
        //注册刷新监听
        RefreshEvent.getInstance().addObserver(this);
        //注册好友关系链监听
        FriendshipEvent.getInstance().addObserver(this);
        //注册群关系监听
        GroupEvent.getInstance().addObserver(this);
        this.view = view;
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent){
            TIMMessage msg = (TIMMessage) data;
            view.updateMessage(msg);
        }else if (observable instanceof FriendshipEvent){
            FriendshipEvent.NotifyCmd cmd = (FriendshipEvent.NotifyCmd) data;
            switch (cmd.type){
                case ADD_REQ:
                case READ_MSG:
                case ADD:
                    view.updateFriendshipMessage();
                    break;
            }
        }else if (observable instanceof GroupEvent){
            GroupEvent.NotifyCmd cmd = (GroupEvent.NotifyCmd) data;
            switch (cmd.type){
                case UPDATE:
                case ADD:
                    view.updateGroupInfo((TIMGroupCacheInfo) cmd.data);
                    break;
                case DEL:
                    view.removeConversation((String) cmd.data);
                    break;

            }
        }else if (observable instanceof RefreshEvent){
            view.refresh();
        }
    }



    public void getConversation(){
        List<TIMConversation> list = TIMManager.getInstance().getConversionList();
        List<TIMConversation> result = new ArrayList<>();
        for (TIMConversation conversation : list){
            if (conversation.getType() == TIMConversationType.System) continue;
            result.add(conversation);
            conversation.getMessage(1, null, new TIMValueCallBack<List<TIMMessage>>() {
                @Override
                public void onError(int i, String s) {
                    Log.e(TAG, "get message error" + s);
                }

                @Override
                public void onSuccess(List<TIMMessage> timMessages) {
                    if (timMessages.size() > 0) {
                        view.updateMessage(timMessages.get(0));
                    }

                }
            });

        }
        view.initView(result);
    }

    public boolean delConversation(TIMConversationType type, String id){
        return TIMManager.getInstance().deleteConversationAndLocalMsgs(type, id);
    }


}
