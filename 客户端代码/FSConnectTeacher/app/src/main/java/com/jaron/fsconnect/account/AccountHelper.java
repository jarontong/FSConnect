package com.jaron.fsconnect.account;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.jaron.fsconnect.api.ApiHttpClient;
import com.jaron.fsconnect.bean.Teacher;
import com.jaron.fsconnect.utils.SharedPreferencesHelper;

import cz.msebera.android.httpclient.Header;

/**
 * 账户辅助类，
 * 用于更新用户信息和保存当前账户等操作
 */
public final class AccountHelper {
    private static final String TAG = AccountHelper.class.getSimpleName();
    private Teacher user;
    private Application application;
    @SuppressLint("StaticFieldLeak")
    private static AccountHelper instances;

    private AccountHelper(Application application) {
        this.application = application;
    }

    public static void init(Application application) {
        if (instances == null)
            instances = new AccountHelper(application);
        else {
            // reload from source
            instances.user = SharedPreferencesHelper.loadFormSource(instances.application, Teacher.class);
            Log.d(TAG, "init reload:" + instances.user);
        }
    }

    public static boolean isLogin() {
        return getUserId()!=null&&getUserId() > 0 && !TextUtils.isEmpty(getCookie());
    }

    public static String getCookie() {
        String cookie = getUser().getCookie();
        return cookie == null ? "" : cookie;
    }

    public static Integer getUserId() {
        return getUser().getTeacherId();
    }

    public static Teacher getUser() {
        if (instances == null) {
            Log.e(TAG,"AccountHelper instances is null, you need call init() method.");
            return new Teacher();
        }
        if (instances.user == null)
            instances.user = SharedPreferencesHelper.loadFormSource(instances.application, Teacher.class);
        if (instances.user == null)
            instances.user = new Teacher();
        return instances.user;
    }

    public static boolean updateUserCache(Teacher user) {
        if (user == null)
            return false;
        // 保留Cookie信息
        if (TextUtils.isEmpty(user.getCookie()) && instances.user != user)
            user.setCookie(instances.user.getCookie());
        instances.user = user;
        boolean bool=SharedPreferencesHelper.save(instances.application, user);
        return bool;
    }

    private static void clearUserCache() {
        instances.user = null;
        SharedPreferencesHelper.remove(instances.application, Teacher.class);
    }

    public static boolean login(final Teacher user, Header[] headers) {
        // 更新Cookie
        String cookie = ApiHttpClient.getCookie(headers);
        if (TextUtils.isEmpty(cookie) || cookie.length() < 6) {
            return false;
        }

        Log.d(TAG, "login:" + user + " cookie：" + cookie);

        user.setCookie(cookie);

        int count = 10;
        boolean saveOk;
        // 保存缓存
        while (!(saveOk = updateUserCache(user)) && count-- > 0) {
            SystemClock.sleep(100);
        }
        if (saveOk) {
            ApiHttpClient.setCookieHeader(getCookie());
            // 登陆成功,重新启动消息服务
            //NoticeManager.init(instances.application);
        }
        return saveOk;
    }

    /**
     * 退出登陆操作需要传递一个View协助完成延迟检测操作
     *
     * @param view     View
     * @param runnable 当清理完成后回调方法
     */
    public static void logout(final View view, final Runnable runnable) {
        // 清除用户缓存
        clearUserCache();
        // 等待缓存清理完成
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.removeCallbacks(this);
                Teacher user = SharedPreferencesHelper.load(instances.application, Teacher.class);
                // 判断当前用户信息是否清理成功
                if (user == null || user.getTeacherId() <= 0) {
                    clearAndPostBroadcast(instances.application);
                    runnable.run();
                } else {
                    view.postDelayed(this, 200);
                }
            }
        }, 200);

    }

    /**
     * 当前用户信息清理完成后调用方法清理服务等信息
     *
     * @param application Application
     */
    private static void clearAndPostBroadcast(Application application) {
        // 清理网络相关
        ApiHttpClient.destroyAndRestore(application);

        // 用户退出时清理红点并退出服务
        //NoticeManager.clear(application, NoticeManager.FLAG_CLEAR_ALL);
        //NoticeManager.exitServer(application);

        // Logout 广播
        //Intent intent = new Intent(Constants.INTENT_ACTION_LOGOUT);
        //application.sendBroadcast(intent);

    }
}
