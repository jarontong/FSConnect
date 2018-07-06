/*
package com.jaron.fsconnect.account.activity;

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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.czy.presenter.listener.CallBackListener;
import com.czy.presenter.manager.SelfProfileManager;
import com.czy.tls.callback.RegisterListener;
import com.czy.tls.service.RegisterService;
import com.google.gson.reflect.TypeToken;
import com.jaron.fsconnect.AppContext;
import com.jaron.fsconnect.R;
import com.jaron.fsconnect.account.AccountHelper;
import com.jaron.fsconnect.account.base.AccountBaseActivity;
import com.jaron.fsconnect.api.FSConnectApi;
import com.jaron.fsconnect.app.AppOperator;
import com.jaron.fsconnect.bean.Parent;
import com.jaron.fsconnect.bean.ResultBean;
import com.jaron.fsconnect.utils.AssimilateUtils;
import com.jaron.fsconnect.utils.TDevice;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tencent.imsdk.TIMFriendGenderType;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AccountBaseActivity implements RegisterListener, View.OnClickListener{
    public static final String HOLD_USERNAME_KEY = "holdUsernameKey";
    public static final String HOLD_PASSWORD_KEY = "holdPasswordKey";
    @BindView(R.id.ib_navigation_back)
    ImageButton ibNavigationBack;
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

    private CountDownTimer mTimer;//倒计时
    Parent parent;
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
            requestFailureHint(throwable);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                Type userType = new TypeToken<ResultBean<Parent>>() {
                }.getType();
                ResultBean<Parent> resultBean = AppOperator.createGson().fromJson(responseString, userType);
                if (resultBean.isSuccess()) {
                    parent= resultBean.getResult();
                    if (AccountHelper.login(parent, headers)) {
                        AppContext.showToast(getResources().getString(R.string.register_success_hint), Toast.LENGTH_SHORT);
                        new RegisterService(RegisterActivity.this).register(parent.getParentTelephone(), parent.getParentPassword(), RegisterActivity.this);
                        sendLocalReceiver();
                        holdAccount();
                        Intent intent = new Intent(RegisterActivity.this, BindStudentActivity.class);
                        intent.putExtra("start","RegisterActivity");
                        startActivity(intent);
                        finish();
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
        String phoneNumber = registerMobileEdit.getText().toString().trim();
        String pwd = passwordEdit.getText().toString().trim();
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
        String phoneNumber = registerMobileEdit.getText().toString().trim();
        String pwd = passwordEdit.getText().toString().trim();
        if (!TextUtils.isEmpty(phoneNumber)&&!TextUtils.isEmpty(pwd)) {
            SharedPreferences sp = getSharedPreferences("hold_account", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(HOLD_USERNAME_KEY, phoneNumber);
            editor.putString(HOLD_PASSWORD_KEY,pwd);
            SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
        }
    }

    private CallBackListener callBackListener = new CallBackListener() {
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

    @Override
    public void onRegisterSuccess(String identifier) {
        hideWaitDialog();
        SelfProfileManager.setNickname(parent.getParentUsername(), callBackListener);
        SelfProfileManager.setSignature(parent.getParentTurename(),callBackListener);
        SelfProfileManager.setFaceUrl(parent.getHeadIcon(),callBackListener);
        if(parent.getSex()==0)
            SelfProfileManager.setGender(TIMFriendGenderType.Female,callBackListener);
        else
            SelfProfileManager.setGender(TIMFriendGenderType.Male,callBackListener);
        finish();
    }

    @Override
    public void onRegisterFail(String error) {
        hideWaitDialog();
        AppContext.showToast("注册失败");
    }

    @Override
    public void onRegisterTimeout() {
        hideWaitDialog();
        AppContext.showToast("注册超时");
    }

    @Override
    public void onFormatInvalid() {
        hideWaitDialog();
        AppContext.showToast("输入参数有误");
    }
}
*/
