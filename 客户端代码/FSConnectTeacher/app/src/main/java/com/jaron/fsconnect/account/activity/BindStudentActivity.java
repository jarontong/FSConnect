/*
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
import android.widget.Toast;

import com.czy.presenter.listener.CallBackListener;
import com.czy.presenter.manager.SelfProfileManager;
import com.czy.tls.callback.RegisterListener;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jaron.fsconnect.AppContext;
import com.jaron.fsconnect.MainActivity;
import com.jaron.fsconnect.R;
import com.jaron.fsconnect.account.AccountHelper;
import com.jaron.fsconnect.account.base.AccountBaseActivity;
import com.jaron.fsconnect.api.FSConnectApi;
import com.jaron.fsconnect.bean.Parent;
import com.jaron.fsconnect.bean.ResultBean;
import com.jaron.fsconnect.utils.TDevice;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;



public class BindStudentActivity extends AccountBaseActivity  {

    @BindView(R.id.student_name_edit)
    TextInputEditText studentNameEdit;
    @BindView(R.id.student_name_til)
    TextInputLayout studentNameTil;
    @BindView(R.id.student_num_edit)
    TextInputEditText studentNumEdit;
    @BindView(R.id.student_num_til)
    TextInputLayout studentNumTil;
    @BindView(R.id.reset_pwd_button)
    Button resetPwdButton;

    String start;

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
            Type type = new TypeToken<ResultBean<Parent>>() {
            }.getType();
            ResultBean<Parent> resultBean = gsonBuilder.create().fromJson(responseString, type);
            int code = resultBean.getCode();
            switch (code) {
                case 1:
                    Parent parent=resultBean.getResult();
                    SelfProfileManager.seLocation(parent.getStudent().getStudentName(),callBackListener);
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

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyBoard(getCurrentFocus().getWindowToken());
    }

    public static void show(Activity context,String start) {
        Intent intent = new Intent(context, BindStudentActivity.class);
        intent.putExtra("start",start);
        context.startActivity(intent);
    }


*/
/**
     * show the login activity
     *
     * @param fragment fragment
     *//*


    public static void show(Fragment fragment) {
        Intent intent = new Intent(fragment.getActivity(), BindStudentActivity.class);
        fragment.startActivity(intent);
    }

    @Override
    protected boolean initBundle(Bundle bundle) {
        start =getIntent().getStringExtra("start");
        return !(start == null) && super.initBundle(bundle);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_bind_student;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.reset_pwd_progress, R.id.ib_navigation_back, R.id.account_reset_pwd_form, R.id.lay_reset_pwd_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reset_pwd_progress:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
            case R.id.ib_navigation_back:
                if(start.equals("RegisterActivity")) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.bind_student_button:
                String stuName=studentNameEdit.getText().toString().trim();
                String stuNum=studentNumEdit.getText().toString().trim();
                if(!TextUtils.isEmpty(stuName)&&!TextUtils.isEmpty(stuNum))
                    bindStudent(stuName,stuNum);
                else{
                    if(TextUtils.isEmpty(stuName))
                        studentNameTil.setError("学生姓名不能为空");
                    if(TextUtils.isEmpty(stuNum))
                        studentNumTil.setError("学生学号不能为空");
                }
                break;
            case R.id.lay_reset_pwd_container:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
        }
    }

    private void bindStudent(String stuName,String stuNum){
        if (!TDevice.hasInternet()) {
            showToastForKeyBord(R.string.footer_type_net_error);
            return;
        }
        String parentTelephone=AccountHelper.getUser().getParentTelephone();
        String parentTruename=AccountHelper.getUser().getParentTruename();
        FSConnectApi.bindStudent(parentTelephone,parentTruename,stuNum,stuName,mHandler);
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
}

*/
