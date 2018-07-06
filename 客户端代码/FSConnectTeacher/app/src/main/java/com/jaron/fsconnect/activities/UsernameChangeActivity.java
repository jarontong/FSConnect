package com.jaron.fsconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.jaron.fsconnect.AppContext;
import com.jaron.fsconnect.R;
import com.jaron.fsconnect.account.AccountHelper;
import com.jaron.fsconnect.api.FSConnectApi;
import com.jaron.fsconnect.app.AppOperator;
import com.jaron.fsconnect.base.BaseActivity;
import com.jaron.fsconnect.bean.ResultBean;
import com.jaron.fsconnect.bean.Teacher;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendshipManager;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Jaron on 2018/6/9.
 */

public class UsernameChangeActivity extends BaseActivity {
    @BindView(R.id.username_edit)
    TextInputEditText usernameEdit;
    @BindView(R.id.username_til)
    TextInputLayout usernameTil;

    String username;



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
            Type type = new TypeToken<ResultBean<Teacher>>() {
            }.getType();
            ResultBean<Teacher> resultBean = AppOperator.createGson().fromJson(responseString, type);
            int code = resultBean.getCode();
            switch (code) {
                case 1:
                    Teacher teacher=resultBean.getResult();
                    AccountHelper.updateUserCache(teacher);
                    TIMFriendshipManager.getInstance().setNickName(teacher.getTeacherUsername(),timCallBack);
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

    TIMCallBack timCallBack=new TIMCallBack(){
        @Override
        public void onError(int code, String desc) {
            //错误码 code 和错误描述 desc，可用于定位请求失败原因
            //错误码 code 列表请参见错误码表
            Log.e("UsernameChangeActivity", "setFaceUrl failed: " + code + " desc" + desc);
        }
        @Override
        public void onSuccess() {
            Log.e("UsernameChangeActivity", "setFaceUrl succ");
        }
    };

    private void showToast(String message) {
        Toast toast = new Toast(UsernameChangeActivity.this);
        View rootView = LayoutInflater.from(UsernameChangeActivity.this).inflate(R.layout.view_toast, null, false);
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

    public static void show(Fragment fragment,String username) {
        Intent intent = new Intent(fragment.getActivity(), UsernameChangeActivity.class);
        intent.putExtra("username",username);
        fragment.startActivity(intent);
    }

    @Override
    protected boolean initBundle(Bundle bundle) {
        username =getIntent().getStringExtra("username");
        return !(username == null) && super.initBundle(bundle);
    }



    @Override
    protected int getContentView() {
        return R.layout.activity_change_username;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        super.initData();
        usernameEdit.setText(username);
    }

    @OnClick({ R.id.ib_navigation_back, R.id.change_username_button, R.id.account_reset_pwd_form, R.id.lay_reset_pwd_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.change_username_button:
                String username=usernameEdit.getText().toString().trim();
                if(TextUtils.isEmpty(username))
                    usernameTil.setError("用户名不能为空");
                else
                    changeUsername(username);
                break;
            case R.id.account_reset_pwd_form:
                break;
            case R.id.lay_reset_pwd_container:
                break;
        }
    }

    private void changeUsername(String username){
        Teacher teacher=AccountHelper.getUser();
        Integer teacherId=teacher.getTeacherId();
        FSConnectApi.changeUsername(teacherId,username,mHandler);

    }
}
