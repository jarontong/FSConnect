package com.jaron.fsconnectparent.account.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.SharedPreferencesCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.reflect.TypeToken;
import com.huawei.android.pushagent.PushManager;
import com.jaron.fsconnectparent.AppContext;
import com.jaron.fsconnectparent.R;
import com.jaron.fsconnectparent.account.AccountHelper;
import com.jaron.fsconnectparent.account.base.AccountBaseActivity;
import com.jaron.fsconnectparent.api.FSConnectApi;
import com.jaron.fsconnectparent.app.AppOperator;
import com.jaron.fsconnectparent.bean.Parent;
import com.jaron.fsconnectparent.bean.ResultBean;
import com.jaron.fsconnectparent.model.UserInfo;
import com.jaron.fsconnectparent.presentation.business.LoginBusiness;
import com.jaron.fsconnectparent.presentation.event.FriendshipEvent;
import com.jaron.fsconnectparent.presentation.event.GroupEvent;
import com.jaron.fsconnectparent.presentation.event.MessageEvent;
import com.jaron.fsconnectparent.tlslibrary.activity.ImgCodeActivity;
import com.jaron.fsconnectparent.tlslibrary.service.Constants;
import com.jaron.fsconnectparent.tlslibrary.service.TLSService;
import com.jaron.fsconnectparent.utils.AssimilateUtils;
import com.jaron.fsconnectparent.utils.PushUtil;
import com.jaron.fsconnectparent.utils.TDevice;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendAllowType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushToken;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;

public class RegisterActivity extends AccountBaseActivity implements View.OnClickListener{
    public static final String HOLD_USERNAME_KEY = "holdUsernameKey";
    public static final String HOLD_PASSWORD_KEY = "holdPasswordKey";
    @BindView(R.id.register_mobile_edit)
    TextInputEditText registerMobileEdit;

    @BindView(R.id.password_edit)
    TextInputEditText passwordEdit;
    @BindView(R.id.confirm_password_edit)
    TextInputEditText confirmPasswordEdit;

    @BindView(R.id.register_button)
    Button registerButton;
    @BindView(R.id.has_account)
    TextView hasAccount;
    @BindView(R.id.lay_register_container)
    LinearLayout layRegisterContainer;
    @BindView(R.id.register_truename_edit)
    TextInputEditText registerTruenameEdit;
    @BindView(R.id.register_mobile_til)
    TextInputLayout registerMobileTil;
    @BindView(R.id.password_til)
    TextInputLayout passwordTil;
    @BindView(R.id.confirm_password_til)
    TextInputLayout confirmPasswordTil;
    @BindView(R.id.register_truename_til)
    TextInputLayout registerTruenameTil;

    private boolean mMachPhoneNum;
    private TLSService tlsService;
    String phoneNumber;
    String pwd;
    Parent user;

    private CountDownTimer mTimer;//倒计时
    private TextHttpResponseHandler mHandler = new TextHttpResponseHandler() {

        @Override
        public void onStart() {
            super.onStart();
            showWaitDialog(R.string.progress_submit);
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

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.d("statusCode",String.valueOf(statusCode));
            requestFailureHint(throwable);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                Type userType = new TypeToken<ResultBean<Parent>>() {
                }.getType();
                ResultBean<Parent> resultBean = AppOperator.createGson().fromJson(responseString, userType);
                if (resultBean.isSuccess()) {

                    user= resultBean.getResult();
                    if (AccountHelper.login(user, headers)) {
                        AppContext.showToast(getResources().getString(R.string.register_success_hint), Toast.LENGTH_SHORT);
                        tlsService.TLSStrAccReg("86_" + phoneNumber, pwd, new StrAccRegListener());
                    } else {
                        showToastForKeyBord("注册异常");
                    }
                } else {
                    int code = resultBean.getCode();
                    String message = resultBean.getMessage();
                    if (code == 218) {
                        passwordEdit.setFocusableInTouchMode(false);
                        passwordEdit.clearFocus();
                        registerMobileEdit.requestFocus();
                        registerMobileEdit.setFocusableInTouchMode(true);
                        message += "," + getResources().getString(R.string.message_username_error);
                        registerMobileEdit.setBackgroundResource(R.drawable.bg_login_input_error);
                        registerMobileEdit.setHint(message);
                    } else if (code == 219) {
                        showToastForKeyBord(resultBean.getMessage());
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                onFailure(statusCode, headers, responseString, e);
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        registerMobileEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    registerMobileEdit.setActivated(true);
                    //registerSmsCodeEdit.setActivated(false);
                    passwordEdit.setActivated(false);
                    confirmPasswordEdit.setActivated(false);
                    registerTruenameEdit.setActivated(false);
                } else {
                    String input = registerMobileEdit.getText().toString().trim();
                    mMachPhoneNum = AssimilateUtils.machPhoneNum(input);
                    if (!mMachPhoneNum) {
                        showToastForKeyBord(R.string.hint_username_ok);
                    }
                }
            }
        });
        registerMobileEdit.addTextChangedListener(new TextWatcher() {
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
                String confirmPwd=confirmPasswordEdit.getText().toString().trim();
                String turename=registerTruenameEdit.getText().toString().trim();
                String input = s.toString().trim();
                mMachPhoneNum = AssimilateUtils.machPhoneNum(input);
                if (mMachPhoneNum) {
                    registerMobileTil.setError("");
                    if (!TextUtils.isEmpty(pwd)&&!TextUtils.isEmpty(confirmPwd)&&!TextUtils.isEmpty(turename)&&pwd.equals(confirmPwd)) {
                        registerButton.setBackgroundResource(R.drawable.bg_login_submit);
                        registerButton.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        registerButton.setBackgroundResource(R.drawable.bg_login_submit_lock);
                        registerButton.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                    }
                } else {
                    registerButton.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    registerButton.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
            }
        });

        passwordEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    registerMobileEdit.setActivated(false);
                    passwordEdit.setActivated(true);
                    passwordTil.setError("密码至少为6位");
                    confirmPasswordEdit.setActivated(false);
                    registerTruenameEdit.setActivated(false);
                }else{
                passwordTil.setError("");
                    String password = passwordEdit.getText().toString().trim();
                    if (password.length()<6&&password.length()>0) {
                        passwordTil.setError("密码至少为6位");
                    }
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
                String confirmPassword = confirmPasswordEdit.getText().toString().trim();
                String turename=registerTruenameEdit.getText().toString().trim();
                if(s.length()>=6){
                    passwordTil.setError("");
                }else {
                    registerButton.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    registerButton.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                    passwordTil.setError("密码至少为6位");
                }
                if(confirmPassword.equals(s.toString().trim())){
                    confirmPasswordTil.setError("");
                }else{
                    registerButton.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    registerButton.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                    confirmPasswordTil.setError("两次密码不相同");
                }
                if (  !TextUtils.isEmpty(turename)&&mMachPhoneNum) {
                    registerButton.setBackgroundResource(R.drawable.bg_login_submit);
                    registerButton.setTextColor(getResources().getColor(R.color.white));
                } else {
                    registerButton.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    registerButton.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
            }
        });

        confirmPasswordEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    registerMobileEdit.setActivated(false);
                    //registerSmsCodeEdit.setActivated(false);
                    passwordEdit.setActivated(false);
                    confirmPasswordEdit.setActivated(true);
                    confirmPasswordTil.setError("两次密码不相同");
                    registerTruenameEdit.setActivated(false);
                }else{
                    String password = passwordEdit.getText().toString().trim();
                    String confirmPassword=confirmPasswordEdit.getText().toString().trim();
                    if (confirmPassword.length()>0&&!password.equals(confirmPassword)) {
                        passwordTil.setError("两次密码不相同");
                    }
                }
            }
        });
        confirmPasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                String password = passwordEdit.getText().toString().trim();
                String turename=registerTruenameEdit.getText().toString().trim();
                if(password.equals(s.toString().trim())){
                    confirmPasswordTil.setError("");
                }else{
                    registerButton.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    registerButton.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                    confirmPasswordTil.setError("两次密码不相同");
                }
                if (!TextUtils.isEmpty(turename)&&mMachPhoneNum) {
                    registerButton.setBackgroundResource(R.drawable.bg_login_submit);
                    registerButton.setTextColor(getResources().getColor(R.color.white));
                } else {
                    registerButton.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    registerButton.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
            }
        });

        registerTruenameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    registerMobileEdit.setActivated(false);
                    //registerSmsCodeEdit.setActivated(false);
                    passwordEdit.setActivated(false);
                    confirmPasswordEdit.setActivated(false);
                    registerTruenameEdit.setActivated(true);
                }
            }
        });
        registerTruenameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                if (mMachPhoneNum) {
                    registerButton.setBackgroundResource(R.drawable.bg_login_submit);
                    registerButton.setTextColor(getResources().getColor(R.color.white));
                } else {
                    registerButton.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    registerButton.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyBoard(getCurrentFocus().getWindowToken());
    }

    @Override
    protected void initData() {
        super.initData();
        tlsService = TLSService.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ib_navigation_back, R.id.register_button, R.id.has_account, R.id.lay_register_container})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.register_button:
                requestRegister();
                break;
            case R.id.has_account:
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.lay_register_container:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
            default:
                break;
        }
    }


    private void requestRegister() {
        if (!mMachPhoneNum) {
            showToastForKeyBord(R.string.hint_username_ok);
            return;
        }

        if (!TDevice.hasInternet()) {
            showToastForKeyBord(R.string.footer_type_net_error);
            return;
        }
         phoneNumber = registerMobileEdit.getText().toString().trim();
         pwd = passwordEdit.getText().toString().trim();
        String confirmPassword=confirmPasswordEdit.getText().toString().trim();
        String truename=registerTruenameEdit.getText().toString().trim();
        if(!pwd.equals(confirmPassword)){
            showToastForKeyBord(R.string.confirm_password_error);
            return;
        }
        if(TextUtils.isEmpty(truename)){
            showToastForKeyBord(R.string.truename_error);
            return;
        }
        FSConnectApi.register(phoneNumber, pwd,truename, mHandler);
    }

    private void holdAccount() {
        if (!TextUtils.isEmpty(phoneNumber)&&!TextUtils.isEmpty(pwd)) {
            SharedPreferences sp = getSharedPreferences("hold_account", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(HOLD_USERNAME_KEY, phoneNumber);
            editor.putString(HOLD_PASSWORD_KEY,pwd);
            SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
        }
    }

    private TIMCallBack timCallBack = new TIMCallBack() {
        @Override
        public void onSuccess() {
            if (!isFinishing() && !isDestroyed()) {
                hideWaitDialog();
                finish();
            }
        }

        @Override
        public void onError(int code, String desc) {
            if (!isFinishing() && !isDestroyed()) {
                hideWaitDialog();
                AppContext.showToast("code:" + code + " describe:" + desc);
            }
        }
    };

    TIMCallBack aCallBack = new TIMCallBack() {

        @Override
        public void onError(int i, String s) {
            Log.e("RegisterActivity", "login error : code " + i + " " + s);
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
                PushManager.requestToken(RegisterActivity.this);
            }

            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            if(!TextUtils.isEmpty(refreshedToken)) {
                TIMOfflinePushToken param = new TIMOfflinePushToken();
                param.setToken(refreshedToken);
                param.setBussid(169);
                TIMManager.getInstance().setOfflinePushToken(param);
            }

            TIMFriendshipManager.getInstance().setNickName(user.getParentUsername(), timCallBack);
            TIMFriendshipManager.getInstance().setAllowType(TIMFriendAllowType.TIM_FRIEND_NEED_CONFIRM, timCallBack);
            /*if(user.getSex()==null||user.getSex()==0)
                TIMFriendshipManager.getInstance().setGender(TIMFriendGenderType.Female,timCallBack);
            else
                TIMFriendshipManager.getInstance().setGender(TIMFriendGenderType.Male,timCallBack);*/
            TIMFriendshipManager.getInstance().setSelfSignature(String.valueOf(user.getParentId()),timCallBack);
            TIMFriendshipManager.getInstance().setFaceUrl(user.getHeadIcon(), timCallBack);

            Intent intent = new Intent(RegisterActivity.this, BindStudentActivity.class);
            intent.putExtra("start","RegisterActivity");
            startActivity(intent);
            finish();

        }
    };


    class StrAccRegListener implements TLSStrAccRegListener {
        @Override
        public void OnStrAccRegSuccess(TLSUserInfo tlsUserInfo) {
            tlsService.TLSPwdLogin("86_" + phoneNumber, pwd, new TLSPwdLoginListener() {
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
                    Intent intent = new Intent(RegisterActivity.this, ImgCodeActivity.class);
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
}
