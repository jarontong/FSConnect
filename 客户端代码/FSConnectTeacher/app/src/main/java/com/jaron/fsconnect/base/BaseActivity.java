package com.jaron.fsconnect.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.jaron.fsconnect.R;
import com.jaron.fsconnect.utils.DialogHelper;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Jaron on 2017/3/25.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    private boolean mIsDestroy;
    protected RequestManager mImageLoader;
    private Unbinder mUnbinder;
    private ProgressDialog mDialog;
    protected InputMethodManager mInputMethodManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (initBundle(getIntent().getExtras())) {
            setContentView(getContentView());

            initWindow();

            mUnbinder= ButterKnife.bind(this);//ButterKnife是一个专注于Android系统的View注入框架,可以减少大量的findViewById以及setOnClickListener代码，可视化一键生成。绑定Activity 必须在setContentView之后
            mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            initWidget();
            initData();
        } else {
            finish();
        }

    }
    protected abstract int getContentView();

    protected boolean initBundle(Bundle bundle) {
        return true;
    }

    protected  void initWindow(){}

    protected  void initWidget(){
    }

    protected  void initData(){}

    public synchronized RequestManager getImageLoader() {
        if (mImageLoader == null)
            mImageLoader = Glide.with(this);
        return mImageLoader;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIsDestroy = true;
        mUnbinder.unbind();
    }

    public boolean isDestroy() {
        return mIsDestroy;
    }

    @Override
    public void onClick(View v) {

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

    protected void hideKeyBoard(IBinder windowToken) {
        InputMethodManager inputMethodManager = this.mInputMethodManager;
        if (inputMethodManager == null) return;
        boolean active = inputMethodManager.isActive();
        if (active) {
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
        }
    }
}
