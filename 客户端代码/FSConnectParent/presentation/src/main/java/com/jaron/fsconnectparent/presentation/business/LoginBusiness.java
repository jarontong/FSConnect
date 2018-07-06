package com.jaron.fsconnectparent.presentation.business;

import com.tencent.TIMCallBack;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.jaron.fsconnectparent.sdk.Constant;

/**
 * 登录
 */
public class LoginBusiness {

    private static final String TAG = "LoginBusiness";

    private LoginBusiness(){}


    public static void loginIm(String identify, String userSig, TIMCallBack callBack){

        if (identify == null || userSig == null) return;
        TIMUser user = new TIMUser();
        user.setAccountType(String.valueOf(Constant.ACCOUNT_TYPE));
        user.setAppIdAt3rd(String.valueOf(Constant.SDK_APPID));
        user.setIdentifier(identify);
        //发起登录请求
        TIMManager.getInstance().login(
                Constant.SDK_APPID,
                user,
                userSig,                    //用户帐号签名，由私钥加密获得，具体请参考文档
                callBack);
    }


    public static void logout(TIMCallBack callBack){
        TIMManager.getInstance().logout(callBack);
    }
}
