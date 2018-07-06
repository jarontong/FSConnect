package com.jaron.fsconnect.tlslibrary.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jaron.fsconnect.tlslibrary.helper.MResource;
import com.jaron.fsconnect.tlslibrary.helper.SmsContentObserver;
import com.jaron.fsconnect.tlslibrary.service.Constants;
import com.jaron.fsconnect.tlslibrary.service.TLSService;

public class PhonePwdRegisterActivity extends Activity {

    private final static String TAG = "PhonePwdRegisterActivity";

    private TLSService tlsService;
    private SmsContentObserver smsContentObserver = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(MResource.getIdByName(getApplication(), "layout", "tencent_tls_ui_activity_phone_pwd_register"));

        // 设置返回按钮
        findViewById(MResource.getIdByName(getApplication(), "id", "btn_back"))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PhonePwdRegisterActivity.this.onBackPressed();
                    }
                });

        tlsService = TLSService.getInstance();
        tlsService.initPhonePwdRegisterService(this,
                (EditText) findViewById(MResource.getIdByName(getApplication(), "id", "selectCountryCode")),
                (EditText) findViewById(MResource.getIdByName(getApplication(), "id", "phone")),
                (EditText) findViewById(MResource.getIdByName(getApplication(), "id", "txt_checkcode")),
                (Button) findViewById(MResource.getIdByName(getApplication(), "id", "btn_requirecheckcode")),
                (Button) findViewById(MResource.getIdByName(getApplication(), "id", "btn_verify"))
        );

/*        smsContentObserver = new SmsContentObserver(new Handler(),
                this,
                (EditText) findViewById(MResource.getIdByName(getApplication(), "id", "txt_checkcode")),
                Constants.PHONEPWD_REGISTER_SENDER);

        //注册短信变化监听
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsContentObserver);*/
    }

    protected void onDestroy() {
        super.onDestroy();
        if (smsContentObserver != null) {
            this.getContentResolver().unregisterContentObserver(smsContentObserver);
        }
    }
}
