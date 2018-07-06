package com.jaron.fsconnect.account.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.reflect.TypeToken;
import com.huawei.android.pushagent.PushManager;
import com.jaron.fsconnect.AppContext;
import com.jaron.fsconnect.LaunchActivity;
import com.jaron.fsconnect.MainActivity;
import com.jaron.fsconnect.R;
import com.jaron.fsconnect.account.AccountHelper;
import com.jaron.fsconnect.account.base.AccountBaseActivity;
import com.jaron.fsconnect.api.FSConnectApi;
import com.jaron.fsconnect.app.AppOperator;
import com.jaron.fsconnect.bean.ResultBean;
import com.jaron.fsconnect.bean.Teacher;
import com.jaron.fsconnect.model.UserInfo;
import com.jaron.fsconnect.presentation.business.LoginBusiness;
import com.jaron.fsconnect.presentation.event.FriendshipEvent;
import com.jaron.fsconnect.presentation.event.GroupEvent;
import com.jaron.fsconnect.presentation.event.MessageEvent;
import com.jaron.fsconnect.tlslibrary.activity.ImgCodeActivity;
import com.jaron.fsconnect.tlslibrary.service.Constants;
import com.jaron.fsconnect.tlslibrary.service.StrAccountLogin;
import com.jaron.fsconnect.tlslibrary.service.TLSService;
import com.jaron.fsconnect.ui.NotifyDialog;
import com.jaron.fsconnect.utils.AssimilateUtils;
import com.jaron.fsconnect.utils.PushUtil;
import com.jaron.fsconnect.utils.TDevice;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendAllowType;
import com.tencent.TIMFriendGenderType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushToken;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import tencent.tls.account.acc_ask_code;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;
import tencent.tls.request.TransReqContext;
import tencent.tls.request.req_global;

import static com.tencent.qalsdk.service.QalService.context;

/**
 * Created by Jaron on 2017/3/25.
 */

public class LoginActivity extends AccountBaseActivity implements TLSPwdLoginListener {
    public static final String HOLD_USERNAME_KEY = "holdUsernameKey";
    public static final String HOLD_PASSWORD_KEY = "holdPasswordKey";
    public static final String TAG = "LoginActivity";

    @BindView(R.id.username_til)
    TextInputLayout usernameTil;
    @BindView(R.id.password_til)
    TextInputLayout passwordTil;
    @BindView(R.id.username_edit)
    AutoCompleteTextView usernameEdit;
    @BindView(R.id.password_edit)
    TextInputEditText passwordEdit;
    @BindView(R.id.sign_in_button)
    Button signInButton;

    private int openType;
    private boolean mMachPhoneNum;
    private TLSService tlsService;
    String username;
    String password;
    Teacher user;
    boolean register=false;

    private static final String NAME_USER_INFO = "userInfo";
    private static final String LAST_IDENTIFIER = "lastIdentifier";

    public static void show(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void show(Activity context, int requestCode) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    public static void show(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), LoginActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    private void showError(TextInputLayout textInputLayout, String error) {
        textInputLayout.setError(error);
        textInputLayout.getEditText().setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        usernameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    usernameEdit.setActivated(true);
                    passwordEdit.setActivated(false);
                } else {
                    String input = usernameEdit.getText().toString().trim();
                    mMachPhoneNum = AssimilateUtils.machPhoneNum(input);
                    if (!mMachPhoneNum && !input.isEmpty()) {
                        usernameTil.setError(getString(R.string.hint_username_ok));
                    }
                }
            }
        });
        usernameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                String pwd = passwordEdit.getText().toString().trim();
                mMachPhoneNum = AssimilateUtils.machPhoneNum(s.toString().trim());
                if (mMachPhoneNum) {
                    usernameTil.setError("");

                    if (!TextUtils.isEmpty(pwd)) {
                        signInButton.setBackgroundResource(R.drawable.bg_login_submit);
                        signInButton.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        signInButton.setBackgroundResource(R.drawable.bg_login_submit_lock);
                        signInButton.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                    }
                } else {
                    signInButton.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    signInButton.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }


            }
        });

        passwordEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    usernameEdit.setActivated(false);
                    passwordEdit.setActivated(true);
                    if (passwordEdit.getText().toString().trim().length() < 6)
                        passwordTil.setError("密码至少为6位");
                } else {
                    if (passwordEdit.getText().toString().trim().isEmpty())
                        passwordTil.setError("");
                }
            }
        });
        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                String pwd = passwordEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd) && pwd.length() >= 6) {
                    passwordTil.setError("");
                    signInButton.setBackgroundResource(R.drawable.bg_login_submit);
                    signInButton.setTextColor(getResources().getColor(R.color.white));
                } else {
                    passwordTil.setError("密码至少为6位");
                    signInButton.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    signInButton.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();//必须要,用来注册本地广播
        tlsService = TLSService.getInstance();
        //初始化控件状态数据
        SharedPreferences sp = getSharedPreferences("hold_account", Context.MODE_PRIVATE);
        String holdUsername = sp.getString(HOLD_USERNAME_KEY, null);
        String holdPwd = sp.getString(HOLD_PASSWORD_KEY, null);
        if (holdUsername != null)
            usernameEdit.setText(holdUsername);
        if (holdPwd != null)
            passwordEdit.setText(holdPwd);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyBoard(getCurrentFocus().getWindowToken());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({ R.id.sign_in_button, R.id.bt_login_register, R.id.ib_navigation_back, R.id.lay_login_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*case R.id.forget_password:
                intent = new Intent(LoginActivity.this, RetrieveActivity.class);
                startActivity(intent);
                break;*/
            case R.id.sign_in_button:
                loginRequest();
                break;
            /*case R.id.bt_login_register:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;*/
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.lay_login_container:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
            default:
                break;
            /*case R.id.et_login_username:
                mEtLoginPwd.clearFocus();
                mEtLoginUsername.setFocusableInTouchMode(true);
                mEtLoginUsername.requestFocus();
                break;
            case R.id.et_login_pwd:
                mEtLoginUsername.clearFocus();
                mEtLoginPwd.setFocusableInTouchMode(true);
                mEtLoginPwd.requestFocus();
                break;*/
        }
    }

    private void loginRequest() {
        username = usernameEdit.getText().toString().trim();
        password = passwordEdit.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            showError(usernameTil, getString(R.string.message_username_null));
        } else if (!AssimilateUtils.machPhoneNum(username)) {
            showError(usernameTil, getString(R.string.hint_username_ok));
        } else if (TextUtils.isEmpty(password) || password.length() < 6) {
            showError(passwordTil, getString(R.string.register_pwd_hint));
        } else {
            //登录成功,请求数据进入用户个人中心页面

            if (TDevice.hasInternet()) {
                requestLogin(username, password);
            } else {
                showToastForKeyBord(R.string.footer_type_net_error);
            }

        }
    }


    public void requestLogin(String name, String pwd) {
        FSConnectApi.login(name, pwd, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                showFocusWaitDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("statusCode", String.valueOf(statusCode));
                requestFailureHint(throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Type type = new TypeToken<ResultBean<Teacher>>() {
                    }.getType();
                    ResultBean<Teacher> resultBean = AppOperator.createGson().fromJson(responseString, type);
                    if (resultBean.isSuccess()) {
                        user = resultBean.getResult();
                        if (AccountHelper.login(user, headers)) {
                            //logSucceed();
                            tlsService.TLSPwdLogin("86_" + username, password, LoginActivity.this);
                        } else {
                            showToastForKeyBord("登录异常");
                        }
                    } else {
                        int code = resultBean.getCode();
                        String message = resultBean.getMessage();
                        if (code == 211) {
                            passwordEdit.setFocusableInTouchMode(false);
                            passwordEdit.clearFocus();
                            usernameEdit.requestFocus();
                            usernameEdit.setFocusableInTouchMode(true);
                            message += "," + getResources().getString(R.string.message_username_error);
                            usernameEdit.setBackgroundResource(R.drawable.bg_login_input_error);
                        } else if (code == 212) {
                            usernameEdit.setFocusableInTouchMode(false);
                            usernameEdit.clearFocus();
                            passwordEdit.requestFocus();
                            passwordEdit.setFocusableInTouchMode(true);
                            message += "," + getResources().getString(R.string.message_pwd_error);
                            passwordEdit.setBackgroundResource(R.drawable.bg_login_input_error);
                        }
                        showToastForKeyBord(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideWaitDialog();
            }

            @Override
            public void onCancel() {
                super.onCancel();
                hideWaitDialog();
            }
        });
    }

    private void holdAccount() {
        String username = usernameEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            SharedPreferences sp = getSharedPreferences("hold_account", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(HOLD_USERNAME_KEY, username);
            editor.putString(HOLD_PASSWORD_KEY, password);
            SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
        }
    }

    TIMCallBack aCallBack = new TIMCallBack() {

        @Override
        public void onError(int i, String s) {
            Log.e(TAG, "login error : code " + i + " " + s);
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
                PushManager.requestToken(LoginActivity.this);
            }

            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(TAG, "refreshed token: " + refreshedToken);

            if(!TextUtils.isEmpty(refreshedToken)) {
                TIMOfflinePushToken param = new TIMOfflinePushToken();
                param.setToken(refreshedToken);
                param.setBussid(169);
                TIMManager.getInstance().setOfflinePushToken(param);
            }

            if(register){
                TIMFriendshipManager.getInstance().setNickName(user.getTeacherUsername(), timCallBack);
                TIMFriendshipManager.getInstance().setAllowType(TIMFriendAllowType.TIM_FRIEND_NEED_CONFIRM, timCallBack);
                if(user.getTeacherSex()==0)
                    TIMFriendshipManager.getInstance().setGender(TIMFriendGenderType.Female,timCallBack);
                else
                    TIMFriendshipManager.getInstance().setGender(TIMFriendGenderType.Male,timCallBack);
                TIMFriendshipManager.getInstance().setSelfSignature(String.valueOf(user.getTeacherId()),timCallBack);
                TIMFriendshipManager.getInstance().setFaceUrl(user.getHeadIcon(), timCallBack);
            }

            Log.d(TAG, "imsdk env " + TIMManager.getInstance().getEnv());
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
    };

    @Override
    public void OnPwdLoginSuccess(TLSUserInfo tlsUserInfo) {
        View view;
        if ((view = getCurrentFocus()) != null) {
            hideKeyBoard(view.getWindowToken());
        }
        AppContext.showToast(R.string.login_success_hint);
        sendLocalReceiver();
        //后台异步同步数据
        //ContactsCacheManager.sync();
        holdAccount();
        TLSService.getInstance().setLastErrno(0);
        String id = TLSService.getInstance().getLastUserIdentifier();
        UserInfo.getInstance().setId(id);
        UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));
        FriendshipEvent.getInstance().init();
        GroupEvent.getInstance().init();
        LoginBusiness.loginIm(UserInfo.getInstance().getId(), UserInfo.getInstance().getUserSig(), aCallBack);

    }

    @Override
    public void OnPwdLoginReaskImgcodeSuccess(byte[] bytes) {
        ImgCodeActivity.fillImageview(bytes);
    }

    @Override
    public void OnPwdLoginNeedImgcode(byte[] bytes, TLSErrInfo tlsErrInfo) {
        hideWaitDialog();
        AppContext.showToast("需要验证码");
        Intent intent = new Intent(this, ImgCodeActivity.class);
        intent.putExtra(Constants.EXTRA_IMG_CHECKCODE, bytes);
        intent.putExtra(Constants.EXTRA_LOGIN_WAY, Constants.PHONEPWD_LOGIN);
        startActivity(intent);
    }

    @Override
    public void OnPwdLoginFail(TLSErrInfo tlsErrInfo) {

        if (tlsErrInfo.ErrCode == TLSErrInfo.LOGIN_NO_ACCOUNT) {
            register=true;
            tlsService.TLSStrAccReg("86_" + username, password, new StrAccRegListener());
        } else {
            hideWaitDialog();
            AppContext.showToast("登录失败," + tlsErrInfo.ErrCode+tlsErrInfo.Msg);
        }
    }

    @Override
    public void OnPwdLoginTimeout(TLSErrInfo tlsErrInfo) {
        hideWaitDialog();
        AppContext.showToast("登录超时" + tlsErrInfo.ErrCode);
    }

    TIMCallBack timCallBack = new TIMCallBack() {
        @Override
        public void onError(int i, String s) {
            hideWaitDialog();
            Log.e(TAG,"code:" + i + " describe:" + s);
        }

        @Override
        public void onSuccess() {
            hideWaitDialog();
            Log.d(TAG,"设置成功");
        }
    };

    class StrAccRegListener implements TLSStrAccRegListener {
        @Override
        public void OnStrAccRegSuccess(TLSUserInfo tlsUserInfo) {
            tlsService.TLSPwdLogin("86_" + username, password, new TLSPwdLoginListener() {
                @Override
                public void OnPwdLoginSuccess(TLSUserInfo userInfo) {
                    View view;
                    if ((view = getCurrentFocus()) != null) {
                        hideKeyBoard(view.getWindowToken());
                    }
                    AppContext.showToast(R.string.login_success_hint);
                    sendLocalReceiver();
                    holdAccount();

                    TLSService.getInstance().setLastErrno(0);
                    String id = TLSService.getInstance().getLastUserIdentifier();
                    UserInfo.getInstance().setId(id);
                    UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));
                    FriendshipEvent.getInstance().init();
                    GroupEvent.getInstance().init();
                    LoginBusiness.loginIm(UserInfo.getInstance().getId(), UserInfo.getInstance().getUserSig(), aCallBack);
                }

                @Override
                public void OnPwdLoginReaskImgcodeSuccess(byte[] picData) {
                    AppContext.showToast("PwdLoginReaskImgcode" );
                    ImgCodeActivity.fillImageview(picData);
                }

                @Override
                public void OnPwdLoginNeedImgcode(byte[] picData, TLSErrInfo errInfo) {
                    Intent intent = new Intent(LoginActivity.this, ImgCodeActivity.class);
                    intent.putExtra(Constants.EXTRA_IMG_CHECKCODE, picData);
                    intent.putExtra(Constants.EXTRA_LOGIN_WAY, Constants.USRPWD_LOGIN);
                    startActivity(intent);
                }

                @Override
                public void OnPwdLoginFail(TLSErrInfo errInfo) {
                    hideWaitDialog();
                    AppContext.showToast("登录失败," + errInfo.ErrCode+errInfo.Msg);
                }

                @Override
                public void OnPwdLoginTimeout(TLSErrInfo errInfo) {
                    hideWaitDialog();
                    Log.d("登录超时",errInfo.Msg);

                    AppContext.showToast("登录超时" + errInfo.ErrCode);
                }
            });
        }

        @Override
        public void OnStrAccRegFail(TLSErrInfo tlsErrInfo) {
            Log.d("注册失败",tlsErrInfo.Msg);
            AppContext.showToast("注册失败," + tlsErrInfo.Msg);
        }

        @Override
        public void OnStrAccRegTimeout(TLSErrInfo tlsErrInfo) {
            Log.d("注册超时",tlsErrInfo.Msg);
            AppContext.showToast("注册超时");
        }
    }

    final static int STR_ACCOUNT_REG_REQUEST = 20001;
    final static int SMS_LOGIN_REQUEST = 20002;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == STR_ACCOUNT_REG_REQUEST) {
            if (RESULT_OK == resultCode) {
                setResult(RESULT_OK, data);
                finish();
            }
        } else if (requestCode == SMS_LOGIN_REQUEST) {
            if (RESULT_OK == resultCode) {
                // 返回 ok 表示短信登录界面的处理是 ok 并不需要此 Activity 做什么
                setResult(RESULT_OK, data);
                finish();
            }
        }

        if(requestCode == com.tencent.connect.common.Constants.REQUEST_API) {
            if (resultCode == com.tencent.connect.common.Constants.RESULT_LOGIN) {
                tlsService.onActivityResultForQQLogin(requestCode, requestCode, data);
            }
        }

    }
}
