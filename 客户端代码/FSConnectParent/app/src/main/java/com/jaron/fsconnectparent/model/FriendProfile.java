package com.jaron.fsconnectparent.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tencent.TIMFriendGenderType;
import com.tencent.TIMUserProfile;
import com.jaron.fsconnectparent.R;
import com.jaron.fsconnectparent.activities.AddFriendActivity;
import com.jaron.fsconnectparent.activities.ProfileActivity;

/**
 * 好友资料
 */
public class FriendProfile implements ProfileSummary {


    private TIMUserProfile profile;
    private boolean isSelected;

    public FriendProfile(TIMUserProfile profile){
        this.profile = profile;
    }


    /**
     * 获取头像资源
     */
    @Override
    public int getAvatarRes() {
        return R.drawable.head_other;
    }

    /**
     * 获取头像地址
     */
    @Override
    public String getAvatarUrl() {
        return profile.getFaceUrl();
    }

    /**
     * 获取名字
     */
    @Override
    public String getName() {
        if (!profile.getRemark().equals("")){
            return profile.getRemark();
        }else if (!profile.getNickName().equals("")){
            return profile.getNickName();
        }
        return profile.getIdentifier().substring(profile.getIdentifier().length()-11);
    }

    /**
     * 获取描述信息
     */
    @Override
    public String getDescription() {
        return null;
    }

    /**
     * 显示详情
     *
     * @param context 上下文
     */
    @Override
    public void onClick(Context context) {
        Log.d("profile.getIdentifier()",profile.getIdentifier());

        if (FriendshipInfo.getInstance().isFriend(profile.getIdentifier())){
            Log.d("d","d");
            ProfileActivity.navToProfile(context, profile.getIdentifier());
        }else{
            Intent person = new Intent(context,AddFriendActivity.class);
            person.putExtra("id",profile.getIdentifier());
            person.putExtra("name",getName());
            person.putExtra("nickname",getNickname());
            person.putExtra("avatar",getAvatarUrl());
            context.startActivity(person);
        }
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    /**
     * 获取用户ID
     */
    @Override
    public String getIdentify(){
        return profile.getIdentifier();
    }

    public TIMUserProfile getUserProfile() {
        return profile;
    }
    /**
     * 获取用户备注名
     */
    public String getRemark(){
        return profile.getRemark();
    }

    public String getNickname() {
        return profile.getNickName();
    }

    public String getSelfSignature() {
        return profile.getSelfSignature();
    }

    public TIMFriendGenderType getGender() {
        return profile.getGender();
    }

}
