/*
package com.jaron.fsconnect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
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
import com.jaron.fsconnect.utils.AssimilateUtils;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

*/
/**
 * Created by Jaron on 2018/6/9.
 *//*


public class TelephoneChangeActivity extends BaseActivity {

    String phone;
    @BindView(R.id.phone_edit)
    TextInputEditText phoneEdit;
    @BindView(R.id.phone_til)
    TextInputLayout phoneTil;

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
                    Teacher teacher = resultBean.getResult();
                    AccountHelper.updateUserCache(teacher);
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
        Toast toast = new Toast(TelephoneChangeActivity.this);
        View rootView = LayoutInflater.from(TelephoneChangeActivity.this).inflate(R.layout.view_toast, null, false);
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
        Intent intent = new Intent(fragment.getActivity(), TelephoneChangeActivity.class);
        intent.putExtra("phone", phone);
        fragment.startActivity(intent);
    }

    @Override
    protected boolean initBundle(Bundle bundle) {
        phone = getIntent().getStringExtra("username");
        return !(phone == null) && super.initBundle(bundle);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_change_phone;
    }
*/
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }*//*


    @Override
    protected void initData() {
        super.initData();
        phoneEdit.setText(phone);
    }

    @OnClick({R.id.ib_navigation_back, R.id.change_phone_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.change_phone_button:
                String phone = phoneEdit.getText().toString().trim();
                if (!AssimilateUtils.machPhoneNum(phone)) {
                    phoneTil.setError(getString(R.string.hint_username_ok));
                } else
                    changePhone(phone);
                break;
        }
    }

    private void changePhone(String phone) {
        Teacher teacher = AccountHelper.getUser();
        Integer teacherId = teacher.getTeacherId();
        FSConnectApi.changePhone(teacherId, phone, mHandler);

    }
}
*/
