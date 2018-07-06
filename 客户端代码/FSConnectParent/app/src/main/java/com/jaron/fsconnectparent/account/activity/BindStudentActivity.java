package com.jaron.fsconnectparent.account.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jaron.fsconnectparent.AppContext;
import com.jaron.fsconnectparent.MainActivity;
import com.jaron.fsconnectparent.R;
import com.jaron.fsconnectparent.account.AccountHelper;
import com.jaron.fsconnectparent.api.FSConnectApi;
import com.jaron.fsconnectparent.bean.Parent;
import com.jaron.fsconnectparent.bean.ResultBean;
import com.jaron.fsconnectparent.utils.DialogHelper;
import com.jaron.fsconnectparent.utils.TDevice;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;


public class BindStudentActivity extends Activity implements View.OnClickListener {

    TextInputEditText studentNameEdit;
    TextInputLayout studentNameTil;
    TextInputEditText studentNumEdit;
    TextInputLayout studentNumTil;
    ProgressBar resetPwdProgress;
    ImageButton ibNavigationBack;
    Button bindStudentButton;
    LinearLayout accountResetPwdForm;
    LinearLayout layResetPwdContainer;
    private InputMethodManager mInputMethodManager;
    String start;
    private ProgressDialog mDialog;
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
            AppContext.showToast(R.string.request_error_hint);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            Type type = new TypeToken<ResultBean<Parent>>() {
            }.getType();
            ResultBean<Parent> resultBean = gsonBuilder.create().fromJson(responseString, type);
            int code = resultBean.getCode();
            switch (code) {
                case 1:
                    Parent parent = resultBean.getResult();
                    TIMFriendshipManager.getInstance().setLocation(parent.getStudent().getStudentName(), callBackListener);
                    AccountHelper.updateUserCache(parent);
                    AppContext.showToast(resultBean.getMessage(), Toast.LENGTH_SHORT);
                    Intent intent = new Intent(BindStudentActivity.this, MainActivity.class);
                    startActivity(intent);
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

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_student);
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        studentNameEdit = findViewById(R.id.student_name_edit);
        studentNameTil = findViewById(R.id.student_name_til);
        studentNumEdit = findViewById(R.id.student_num_edit);
        studentNumTil = findViewById(R.id.student_num_til);
        bindStudentButton = findViewById(R.id.bind_student_button);
        ibNavigationBack=findViewById(R.id.ib_navigation_back);
        resetPwdProgress=findViewById(R.id.reset_pwd_progress);
        accountResetPwdForm=findViewById(R.id.account_reset_pwd_form);
        layResetPwdContainer=findViewById(R.id.lay_reset_pwd_container);

        ibNavigationBack.setOnClickListener(this);
        resetPwdProgress.setOnClickListener(this);
        bindStudentButton.setOnClickListener(this);
        layResetPwdContainer.setOnClickListener(this);

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyBoard(getCurrentFocus().getWindowToken());
    }

    public static void show(Activity context, String start) {
        Intent intent = new Intent(context, BindStudentActivity.class);
        intent.putExtra("start", start);
        context.startActivity(intent);
    }

    public static void show(Fragment fragment) {
        Intent intent = new Intent(fragment.getActivity(), BindStudentActivity.class);
        fragment.startActivity(intent);
    }

    private void bindStudent(String stuName, String stuNum) {
        if (!TDevice.hasInternet()) {
            AppContext.showToast(R.string.footer_type_net_error);
            return;
        }
        String parentTelephone = AccountHelper.getUser().getParentTelephone();
        String parentTruename = AccountHelper.getUser().getParentTruename();
        FSConnectApi.bindStudent(parentTelephone, parentTruename, stuNum, stuName, mHandler);
    }

    private TIMCallBack callBackListener = new TIMCallBack() {
        @Override
        public void onSuccess() {
            if (!isFinishing() && !isDestroyed()) {
                hideWaitDialog();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset_pwd_progress:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
            case R.id.ib_navigation_back:
                if (start.equals("RegisterActivity")) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.bind_student_button:
                String stuName = studentNameEdit.getText().toString().trim();
                String stuNum = studentNumEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(stuName) && !TextUtils.isEmpty(stuNum))
                    bindStudent(stuName, stuNum);
                else {
                    if (TextUtils.isEmpty(stuName))
                        studentNameTil.setError("学生姓名不能为空");
                    if (TextUtils.isEmpty(stuNum))
                        studentNumTil.setError("学生学号不能为空");
                }
                break;
            case R.id.lay_reset_pwd_container:
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

    public ProgressDialog showFocusWaitDialog() {

        String message = getResources().getString(R.string.progress_submit);
        if (mDialog == null) {
            mDialog = DialogHelper.getProgressDialog(this, message, false);//DialogHelp.getWaitDialog(this, message);
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

