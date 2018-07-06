package com.jaron.fsconnect;

import android.Manifest;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.huawei.android.pushagent.PushManager;
import com.jaron.fsconnect.account.AccountHelper;
import com.jaron.fsconnect.account.activity.LoginActivity;
import com.jaron.fsconnect.app.AppOperator;
import com.jaron.fsconnect.base.BaseActivity;
import com.jaron.fsconnect.model.UserInfo;
import com.jaron.fsconnect.presentation.business.InitBusiness;
import com.jaron.fsconnect.presentation.business.LoginBusiness;
import com.jaron.fsconnect.presentation.event.FriendshipEvent;
import com.jaron.fsconnect.presentation.event.GroupEvent;
import com.jaron.fsconnect.presentation.event.MessageEvent;
import com.jaron.fsconnect.presentation.event.RefreshEvent;
import com.jaron.fsconnect.presentation.presenter.SplashPresenter;
import com.jaron.fsconnect.tlslibrary.service.TLSService;
import com.jaron.fsconnect.tlslibrary.service.TlsBusiness;
import com.jaron.fsconnect.ui.NotifyDialog;
import com.jaron.fsconnect.utils.PushUtil;
import com.tencent.TIMCallBack;
import com.tencent.TIMLogLevel;
import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushToken;

import java.util.ArrayList;
import java.util.List;
//import net.oschina.app.improve.main.tabs.DynamicTabFragment;

/**
 * 应用启动界面
 */
public class LaunchActivity extends BaseActivity implements TIMCallBack {

    private static final String TAG = "LaunchActivity";
    private int LOGIN_RESULT_CODE = 100;
    SplashPresenter presenter;
    @Override
    protected int getContentView() {
        return R.layout.app_start;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
    }

    @Override
    protected void initData() {
        super.initData();

        clearNotification();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        AppOperator.runOnThread(new Runnable() {
            @Override
            public void run() {
                redirectTo();
            }
        });
    }

    /**
     * 跳转到主界面
     */
    public void navToHome() {
        //登录之前要初始化群和好友关系链缓存
        FriendshipEvent.getInstance().init();
        GroupEvent.getInstance().init();
        LoginBusiness.loginIm(UserInfo.getInstance().getId(), UserInfo.getInstance().getUserSig(), this);
    }

    /**
     * 是否已有用户登录
     */
    public boolean isUserLogin() {
        return UserInfo.getInstance().getId()!= null && (!TLSService.getInstance().needLogin(UserInfo.getInstance().getId()));
    }

    private void init(){
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        int loglvl = pref.getInt("loglvl", TIMLogLevel.DEBUG.ordinal());
        //初始化IMSDK
        InitBusiness.start(getApplicationContext(),loglvl);
        //初始化TLS
        TlsBusiness.init(getApplicationContext());
        //设置刷新监听
        RefreshEvent.getInstance();
        String id =  TLSService.getInstance().getLastUserIdentifier();
        UserInfo.getInstance().setId(id);
        UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isUserLogin()&&AccountHelper.isLogin()) {
                    navToHome();
                } else {
                    LoginActivity.show(LaunchActivity.this,LOGIN_RESULT_CODE);
                }
            }
        }, 1000);
    }

    private void redirectTo() {
        /*if(AccountHelper.isLogin()){

        }else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        finish();*/
    }

    private void clearNotification(){
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

    }

    @Override
    public void onError(int i, String s) {
        Log.e(TAG, "login error : code " + i + " " + s);
        switch (i) {
            case 6208:
                //离线状态下被其他终端踢下线
                NotifyDialog dialog = new NotifyDialog();
                dialog.show(getString(R.string.kick_logout), getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navToHome();
                    }
                });
                break;
            case 6200:
                Toast.makeText(this,getString(R.string.login_error_timeout),Toast.LENGTH_SHORT).show();
                LoginActivity.show(LaunchActivity.this,LOGIN_RESULT_CODE);
                break;
            default:
                Toast.makeText(this,getString(R.string.login_error),Toast.LENGTH_SHORT).show();
                LoginActivity.show(LaunchActivity.this,LOGIN_RESULT_CODE);
                break;
        }
    }

    @Override
    public void onSuccess() {
//初始化程序后台后消息推送
        PushUtil.getInstance();
        //初始化消息监听
        MessageEvent.getInstance();
        String deviceMan = android.os.Build.MANUFACTURER;
        //注册小米和华为推送
        if (deviceMan.equals("HUAWEI")){
            PushManager.requestToken(this);
        }

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "refreshed token: " + refreshedToken);

        if(!TextUtils.isEmpty(refreshedToken)) {
            TIMOfflinePushToken param = new TIMOfflinePushToken();
            param.setToken(refreshedToken);
            param.setBussid(169);
            TIMManager.getInstance().setOfflinePushToken(param);
        }

        Log.d(TAG, "imsdk env " + TIMManager.getInstance().getEnv());
        Intent intent = new Intent(this, MainActivity.class);
        finish();startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult code:" + requestCode);
        if (LOGIN_RESULT_CODE == requestCode) {
            Log.d(TAG, "login error no " + TLSService.getInstance().getLastErrno());
            if (0 == TLSService.getInstance().getLastErrno()){
                String id = TLSService.getInstance().getLastUserIdentifier();
                UserInfo.getInstance().setId(id);
                UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));
                navToHome();
            } else if (resultCode == RESULT_CANCELED){
                finish();
            }
        }
    }
}
