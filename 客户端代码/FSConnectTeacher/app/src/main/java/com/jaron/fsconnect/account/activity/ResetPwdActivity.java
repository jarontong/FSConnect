package com.jaron.fsconnect.account.activity;

import android.app.Activity;
import android.content.Context;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jaron.fsconnect.AppContext;
import com.jaron.fsconnect.R;
import com.jaron.fsconnect.account.AccountHelper;
import com.jaron.fsconnect.account.base.AccountBaseActivity;
import com.jaron.fsconnect.account.bean.PhoneToken;
import com.jaron.fsconnect.api.FSConnectApi;
import com.jaron.fsconnect.bean.ResultBean;
import com.jaron.fsconnect.bean.Teacher;
import com.jaron.fsconnect.utils.TDevice;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class ResetPwdActivity extends AccountBaseActivity {


    @BindView(R.id.ib_navigation_back)
    ImageButton ibNavigationBack;
    @BindView(R.id.password_edit)
    TextInputEditText passwordEdit;
    @BindView(R.id.confirm_password_edit)
    TextInputEditText confirmPasswordEdit;
    @BindView(R.id.reset_pwd_button)
    Button resetPwdButton;
    @BindView(R.id.lay_reset_pwd_container)
    LinearLayout layResetPwdContainer;
    @BindView(R.id.old_password_edit)
    TextInputEditText oldPasswordEdit;
    @BindView(R.id.old_password_til)
    TextInputLayout oldPasswordTil;
    @BindView(R.id.password_til)
    TextInputLayout passwordTil;
    @BindView(R.id.confirm_password_til)
    TextInputLayout confirmPasswordTil;

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
            GsonBuilder gsonBuilder = new GsonBuilder();
            Type type = new TypeToken<ResultBean>() {
            }.getType();
            ResultBean resultBean = gsonBuilder.create().fromJson(responseString, type);
            int code = resultBean.getCode();
            switch (code) {
                case 1:
                    String password = passwordEdit.getText().toString().trim();
                    SharedPreferences sp = getSharedPreferences("hold_account", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(LoginActivity.HOLD_PASSWORD_KEY,password);
                    SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
                    Teacher teacher=AccountHelper.getUser();
                    teacher.setTeacherPassword(password);
                    AccountHelper.updateUserCache(teacher);
                    AppContext.showToast(getResources().getString(R.string.reset_success_hint), Toast.LENGTH_SHORT);
                    finish();
                    break;
                case 0:
                    AppContext.showToast("密码修改失败", Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                String oldpwd=oldPasswordEdit.getText().toString().trim();
                String pwd = passwordEdit.getText().toString().trim();
                String confirmPwd = confirmPasswordEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(oldpwd)&&!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(confirmPwd)) {
                    resetPwdButton.setBackgroundResource(R.drawable.bg_login_submit);
                    resetPwdButton.setTextColor(getResources().getColor(R.color.white));
                } else {
                    resetPwdButton.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    resetPwdButton.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
            }
        };

        oldPasswordEdit.addTextChangedListener(textWatcher);
        oldPasswordEdit.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    oldPasswordEdit.setActivated(true);
                    //registerSmsCodeEdit.setActivated(false);
                    passwordEdit.setActivated(false);
                    confirmPasswordEdit.setActivated(false);
                } else {
                    String input = oldPasswordEdit.getText().toString().trim();
                    if (TextUtils.isEmpty(input)) {
                        oldPasswordTil.setError("密码不能为空");
                        return;
                    }
                    if(!input.equals(AccountHelper.getUser().getTeacherPassword()))
                        oldPasswordTil.setError("密码错误");
                    else
                        oldPasswordTil.setError("");
                }
            }
        });

        passwordEdit.addTextChangedListener(textWatcher);
        passwordEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    oldPasswordEdit.setActivated(false);
                    passwordEdit.setActivated(true);
                    confirmPasswordEdit.setActivated(false);
                    passwordTil.setError("密码至少为6位");
                }else{
                    passwordTil.setError("");
                    String password = passwordEdit.getText().toString().trim();
                    if (password.length()<6&&password.length()>0) {
                        passwordTil.setError("密码至少为6位");
                    }
                }
            }
        });

        confirmPasswordEdit.addTextChangedListener(textWatcher);
        confirmPasswordEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    oldPasswordEdit.setActivated(false);
                    passwordEdit.setActivated(false);
                    confirmPasswordEdit.setActivated(true);
                }else{
                    confirmPasswordTil.setError("");
                    String pwd = passwordEdit.getText().toString().trim();
                    String confirmPwd = confirmPasswordEdit.getText().toString().trim();
                    if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(confirmPwd)&&!pwd.equals(confirmPwd)) {
                        confirmPasswordTil.setError("两次输入的密码不一致");
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();//必须要调用,用来注册本地广播
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyBoard(getCurrentFocus().getWindowToken());
    }

    public static void show(Activity context) {
        Intent intent = new Intent(context, ResetPwdActivity.class);
        context.startActivity(intent);
    }

    /**
     * show the login activity
     *
     * @param fragment fragment
     */
    public static void show(Fragment fragment) {
        Intent intent = new Intent(fragment.getActivity(), ResetPwdActivity.class);
        fragment.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ib_navigation_back, R.id.reset_pwd_button, R.id.lay_reset_pwd_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.reset_pwd_button:
                String oldpwd=oldPasswordEdit.getText().toString().trim();
                String pwd = passwordEdit.getText().toString().trim();
                String confirmPwd = confirmPasswordEdit.getText().toString().trim();
                if(!oldpwd.equals(AccountHelper.getUser().getTeacherPassword()))
                    oldPasswordTil.setError("密码错误");
                else{
                    if(TextUtils.isEmpty(pwd))
                        passwordTil.setError("密码不能为空");
                    else {
                        if (TextUtils.isEmpty(confirmPwd))
                            confirmPasswordTil.setError("密码不能为空");
                        else {
                            if (pwd.equals(confirmPwd))
                                requestResetPwd(pwd);
                            else
                                confirmPasswordTil.setError("两次输入的密码不一致");
                        }
                    }
                }
                break;
            case R.id.lay_reset_pwd_container:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
        }
    }

    private void requestResetPwd(String pwd) {
        if (!TDevice.hasInternet()) {
            showToastForKeyBord(R.string.footer_type_net_error);
            return;
        }
        FSConnectApi.resetPwd(AccountHelper.getUserId(),pwd, mHandler);
    }

}
