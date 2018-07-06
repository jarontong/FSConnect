package com.jaron.fsconnect.model;

import android.content.Context;

import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.jaron.fsconnect.FSConnectApplication;
import com.jaron.fsconnect.R;
import com.jaron.fsconnect.activities.ChatActivity;

/**
 * 好友或群聊的会话
 */
public class NomalConversation extends Conversation {


    private TIMConversation conversation;



    //最后一条消息
    private Message lastMessage;


    public NomalConversation(TIMConversation conversation){
        this.conversation = conversation;
        type = conversation.getType();
        identify = conversation.getPeer();
    }


    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }


    @Override
    public String getAvatar() {
        String image="";
        switch (type){
            case C2C:
                FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
                if(profile!=null)
                    image=profile.getAvatarUrl();

            case Group:
                GroupProfile groupProfile=GroupInfo.getInstance().getGroupProfile(GroupInfo.publicGroup,identify);
                if(groupProfile!=null)
                    image=groupProfile.getAvatarUrl();
        }
        return image;
    }

    /**
     * 跳转到聊天界面或会话详情
     *
     * @param context 跳转上下文
     */
    @Override
    public void navToDetail(Context context) {
        ChatActivity.navToChat(context,identify,type);
    }

    /**
     * 获取最后一条消息摘要
     */
    @Override
    public String getLastMessageSummary(){
        if (conversation.hasDraft()){
            TextMessage textMessage = new TextMessage(conversation.getDraft());
            if (lastMessage == null || lastMessage.getMessage().timestamp() < conversation.getDraft().getTimestamp()){
                return FSConnectApplication.getContext().getString(R.string.conversation_draft) + textMessage.getSummary();
            }else{
                return lastMessage.getSummary();
            }
        }else{
            if (lastMessage == null) return "";
            return lastMessage.getSummary();
        }
    }

    /**
     * 获取名称
     */
    @Override
    public String getName() {
        if (type == TIMConversationType.Group){
            name=GroupInfo.getInstance().getGroupName(identify);
            if (name.equals("")) name = identify;
        }else{
            FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
            String id=identify.substring(identify.length()-11);
            name=profile == null?id:profile.getName();
        }
        return name;
    }


    /**
     * 获取未读消息数量
     */
    @Override
    public long getUnreadNum(){
        if (conversation == null) return 0;
        return conversation.getUnreadMessageNum();
    }

    /**
     * 将所有消息标记为已读
     */
    @Override
    public void readAllMessage(){
        if (conversation != null){
            conversation.setReadMessage();
        }
    }


    /**
     * 获取最后一条消息的时间
     */
    @Override
    public long getLastMessageTime() {
        if (conversation.hasDraft()){
            if (lastMessage == null || lastMessage.getMessage().timestamp() < conversation.getDraft().getTimestamp()){
                return conversation.getDraft().getTimestamp();
            }else{
                return lastMessage.getMessage().timestamp();
            }
        }
        if (lastMessage == null) return 0;
        return lastMessage.getMessage().timestamp();
    }

    /**
     * 获取会话类型
     */
    @Override
    public TIMConversationType getType(){
        return conversation.getType();
    }
}
