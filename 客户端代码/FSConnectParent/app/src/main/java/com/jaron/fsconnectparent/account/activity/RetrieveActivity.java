/*
package com.jaron.fsconnect.account.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jaron.fsconnect.AppContext;
import com.jaron.fsconnect.R;
import com.jaron.fsconnect.account.base.AccountBaseActivity;
import com.jaron.fsconnect.account.bean.PhoneToken;
import com.jaron.fsconnect.api.FSConnectApi;
import com.jaron.fsconnect.bean.ResultBean;
import com.jaron.fsconnect.utils.AssimilateUtils;
import com.jaron.fsconnect.utils.TDevice;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

*/
/**
 * Created by Jaron on 2017/5/8.
 *//*

//忘记密码
public class RetrieveActivity extends AccountBaseActivity {

    @BindView(R.id.ib_navigation_back)
    ImageButton ibNavigationBack;
    @BindView(R.id.retrieve_pwd_mobile_edit)
    TextInputEditText retrievePwdMobileEdit;
    @BindView(R.id.retrieve_pwd_sms_code_edit)
    TextInputEditText retrievePwdSmsCodeEdit;
    @BindView(R.id.retrieve_pwd_get_sms_code)
    Button retrievePwdGetSmsCode;
    @BindView(R.id.retrieve_pwd_button)
    Button retrievePwdButton;
    @BindView(R.id.retrieve_pwd_progress)
    ProgressBar retrievePwdProgress;
    @BindView(R.id.lay_retrieve_pwd_container)
    LinearLayout layRetrievePwdContainer;

    private boolean mMachPhoneNum;

    private CountDownTimer mTimer;

    private int mRequestType;

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
            if (mRequestType == 1) {
                if (mTimer != null) {
                    mTimer.onFinish();
                    mTimer.cancel();
                }
            }
            requestFailureHint(throwable);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            try {
                GsonBuilder gsonBuilder = new GsonBuilder();
                int code;
                switch (mRequestType) {
                    //第一步请求发送验证码
                    case 1:
                        Type type = new TypeToken<ResultBean>() {
                        }.getType();
                        ResultBean resultBean = gsonBuilder.create().fromJson(responseString, type);
                        code = resultBean.getCode();
                        switch (code) {
                            case 1:
                                //发送验证码成功,请求进入下一步
                                //mRequestType = 2;
                                retrievePwdSmsCodeEdit.setText(null);
                                AppContext.showToast(R.string.send_sms_code_success_hint, Toast.LENGTH_SHORT);
                                break;
                            case 218://需修改,无该账号
                                showToastForKeyBord(resultBean.getMessage());
                                break;
                            case 0:
                                //异常错误，发送验证码失败,回收timer,需重新请求发送验证码
                                if (mTimer != null) {
                                    mTimer.onFinish();
                                    mTimer.cancel();
                                }
                                showToastForKeyBord(resultBean.getMessage());
                                break;
                            default:
                                break;
                        }

                        break;
                    //第二步请求进行重置密码
                    case 2:

                        Type phoneType = new TypeToken<ResultBean<PhoneToken>>() {
                        }.getType();

                        ResultBean<PhoneToken> phoneTokenResultBean = gsonBuilder.create().fromJson(responseString, phoneType);

                        code = phoneTokenResultBean.getCode();
                        switch (code) {
                            case 1:
                                if (phoneTokenResultBean.isSuccess()) {
                                    PhoneToken phoneToken = phoneTokenResultBean.getResult();
                                    if (phoneToken != null) {
                                        if (mTimer != null) {
                                            mTimer.onFinish();
                                            mTimer.cancel();
                                        }
                                        Intent intent = new Intent(RetrieveActivity.this, RestPwdActivity.class);
                                        intent.putExtra("phoneToken", phoneToken);
                                        startActivity(intent);
                                    }
                                } else {
                                    showToastForKeyBord(phoneTokenResultBean.getMessage());
                                }
                                break;
                            case 215://手机验证码错误
                                showToastForKeyBord(phoneTokenResultBean.getMessage());
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
                onFailure(statusCode, headers, responseString, e);
            }

        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_retrieve_pwd;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        retrievePwdMobileEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    retrievePwdMobileEdit.setActivated(true);
                    retrievePwdSmsCodeEdit.setActivated(false);
                }else{
                    String input=retrievePwdMobileEdit.getText().toString().trim();
                    mMachPhoneNum= AssimilateUtils.machPhoneNum(input);
                    if(!mMachPhoneNum){
                        showToastForKeyBord(R.string.hint_username_ok);
                    }
                }
            }
        });
        retrievePwdMobileEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                String smsCode = retrievePwdSmsCodeEdit.getText().toString().trim();
                String input = s.toString().trim();
                mMachPhoneNum = AssimilateUtils.machPhoneNum(input);
                if (mMachPhoneNum) {
                    if (!TextUtils.isEmpty(smsCode)) {
                        retrievePwdButton.setBackgroundResource(R.drawable.bg_login_submit);
                        retrievePwdButton.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        retrievePwdButton.setBackgroundResource(R.drawable.bg_login_submit_lock);
                        retrievePwdButton.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                    }
                } else {
                    retrievePwdButton.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    retrievePwdButton.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
            }
        });

        retrievePwdSmsCodeEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    String username = retrievePwdMobileEdit.getText().toString().trim();
                    if (TextUtils.isEmpty(username)) {
                        showToastForKeyBord(R.string.message_username_null);
                    }
                    retrievePwdMobileEdit.setActivated(false);
                    retrievePwdSmsCodeEdit.setActivated(true);
                }
            }
        });
        retrievePwdSmsCodeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (length > 0 && mMachPhoneNum) {
                    retrievePwdButton.setBackgroundResource(R.drawable.bg_login_submit);
                    retrievePwdButton.setTextColor(getResources().getColor(R.color.white));
                } else {
                    retrievePwdButton.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    retrievePwdButton.setTextColor(getResources().getColor(R.color.account_lock_font_color));
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

    @OnClick({R.id.ib_navigation_back, R.id.retrieve_pwd_get_sms_code, R.id.retrieve_pwd_button,R.id.lay_retrieve_pwd_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.retrieve_pwd_get_sms_code:
                requestSmsCode();
                break;
            case R.id.retrieve_pwd_button:
                requestRetrieve();
                break;
            case R.id.lay_retrieve_pwd_container:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
        }
    }

    private void requestSmsCode() {
        if (!mMachPhoneNum) {
            showToastForKeyBord(R.string.hint_username_ok);
            return;
        }
        if (!TDevice.hasInternet()) {
            showToastForKeyBord(R.string.footer_type_net_error);
            return;
        }

        if (retrievePwdGetSmsCode.getTag() == null) {
            mRequestType = 1;
            retrievePwdGetSmsCode.setAlpha(0.6f);
            retrievePwdGetSmsCode.setTag(true);
            mTimer = new CountDownTimer(60 * 1000, 1000) {

                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long millisUntilFinished) {
                    retrievePwdGetSmsCode.setText(String.format("%s%s%d%s",
                            getResources().getString(R.string.register_sms_hint), "(", millisUntilFinished / 1000, ")"));
                }

                @Override
                public void onFinish() {
                    retrievePwdGetSmsCode.setTag(null);
                    retrievePwdGetSmsCode.setText(getResources().getString(R.string.register_sms_hint));
                    retrievePwdGetSmsCode.setAlpha(1.0f);
                }
            }.start();
            String phoneNumber = retrievePwdMobileEdit.getText().toString().trim();
            FSConnectApi.sendSmsCode(phoneNumber, FSConnectApi.RESET_PWD_INTENT, mHandler);
        } else {
            AppContext.showToast(getResources().getString(R.string.register_sms_wait_hint), Toast.LENGTH_SHORT);
        }
    }

    private void requestRetrieve() {
        if (!mMachPhoneNum) {
            showToastForKeyBord(R.string.hint_username_ok);
            return;
        }
        if (!TDevice.hasInternet()) {
            showToastForKeyBord(R.string.footer_type_net_error);
            return;
        }
        String phoneNumber=retrievePwdMobileEdit.getText().toString().trim();
        String smsCode=retrievePwdSmsCodeEdit.getText().toString().trim();
        mRequestType=2;
        FSConnectApi.retrieve(phoneNumber,smsCode,mHandler);
    }


}
*/
