package com.jaron.fsconnectparent.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.jaron.fsconnectparent.AppContext;
import com.jaron.fsconnectparent.R;
import com.jaron.fsconnectparent.account.AccountHelper;
import com.jaron.fsconnectparent.api.FSConnectApi;
import com.jaron.fsconnectparent.app.AppOperator;
import com.jaron.fsconnectparent.bean.Parent;
import com.jaron.fsconnectparent.bean.ResultBean;
import com.jaron.fsconnectparent.utils.AssimilateUtils;
import com.jaron.fsconnectparent.utils.DialogHelper;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Jaron on 2018/6/10.
 */

public class PhoneChangeActivity extends Activity implements View.OnClickListener  {
    protected InputMethodManager mInputMethodManager;
    private ProgressDialog mDialog;
    String phone;
    TextInputEditText edit;
    TextInputLayout til;
    ImageButton ibNavigationBack;
    Button changePhoneButton;
    LinearLayout accountResetPwdForm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_change_password);
        edit = findViewById(R.id.edit);
        til=findViewById(R.id.til);
        ibNavigationBack=findViewById(R.id.ib_navigation_back);
        changePhoneButton=findViewById(R.id.change_phone_button);
        accountResetPwdForm=findViewById(R.id.account_reset_pwd_form);
        ibNavigationBack.setOnClickListener(this);
        changePhoneButton.setOnClickListener(this);
        accountResetPwdForm.setOnClickListener(this);
        phone = getIntent().getStringExtra("username");

    }

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
            if (throwable != null) {
                throwable.printStackTrace();
            }
            showToast(getResources().getString(R.string.request_error_hint));
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            Type type = new TypeToken<ResultBean<Parent>>() {
            }.getType();
            ResultBean<Parent> resultBean = AppOperator.createGson().fromJson(responseString, type);
            int code = resultBean.getCode();
            switch (code) {
                case 1:
                    Parent parent = resultBean.getResult();
                    AccountHelper.updateUserCache(parent);
                    //TIMFriendshipManager.getInstance().set(parent.getParentUsername(),timCallBack);
                    AppContext.showToast(resultBean.getMessage(), Toast.LENGTH_SHORT);
                    finish();
                    break;
                case 0:
                    AppContext.showToast(resultBean.getMessage(), Toast.LENGTH_SHORT);
                    break;
                default:
                    break;
            }
        }
    };

    private void showToast(String message) {
        Toast toast = new Toast(PhoneChangeActivity.this);
        View rootView = LayoutInflater.from(PhoneChangeActivity.this).inflate(R.layout.view_toast, null, false);
        TextView textView = (TextView) rootView.findViewById(R.id.title_tv);
        textView.setText(message);
        toast.setView(rootView);
        toast.setGravity(Gravity.BOTTOM, 0, getResources().getDimensionPixelSize(R.dimen.toast_y_offset));
        toast.show();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyBoard(getCurrentFocus().getWindowToken());
    }

    public static void show(Fragment fragment, String phone) {
        Intent intent = new Intent(fragment.getActivity(), PhoneChangeActivity.class);
        intent.putExtra("phone", phone);
        fragment.startActivity(intent);
    }

    protected void initData() {
        edit.setText(phone);
    }

    private void changePhone(String phone) {
        Parent parent = AccountHelper.getUser();
        Integer parentId = parent.getParentId();
        FSConnectApi.changePhone(parentId, phone, mHandler);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.change_phone_button:
                String phone = edit.getText().toString().trim();
                if (!AssimilateUtils.machPhoneNum(phone)) {
                    til.setError(getString(R.string.hint_username_ok));
                } else
                    changePhone(phone);
                break;
            case R.id.account_reset_pwd_form:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
        }
    }

    protected void hideKeyBoard(IBinder windowToken) {
        InputMethodManager inputMethodManager = this.mInputMethodManager;
        if (inputMethodManager == null) return;
        boolean active = inputMethodManager.isActive();
        if (active) {
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
        }
    }


    public ProgressDialog showFocusWaitDialog() {

        String message = getResources().getString(R.string.progress_submit);
        if (mDialog == null) {
            mDialog = DialogHelper.getProgressDialog(this, message, false);//DialogHelp.getWaitDialog(this, message);
        }
        mDialog.show();

        return mDialog;
    }

    protected ProgressDialog showWaitDialog(@StringRes int messageId) {
        if (mDialog == null) {
            if (messageId <= 0) {
                mDialog = DialogHelper.getProgressDialog(this, true);
            } else {
                String message = getResources().getString(messageId);
                mDialog = DialogHelper.getProgressDialog(this, message, true);
            }
        }
        mDialog.show();

        return mDialog;
    }

    public void hideWaitDialog() {
        ProgressDialog dialog = mDialog;
        if (dialog != null) {
            mDialog = null;
            try {
                dialog.cancel();
                // dialog.dismiss();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
