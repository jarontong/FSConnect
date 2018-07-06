package com.jaron.fsconnect.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.jaron.fsconnect.AppContext;
import com.jaron.fsconnect.MainActivity;
import com.jaron.fsconnect.R;
import com.jaron.fsconnect.account.AccountHelper;
import com.jaron.fsconnect.account.activity.LoginActivity;
import com.jaron.fsconnect.account.activity.ResetPwdActivity;
import com.jaron.fsconnect.activities.PhoneChangeActivity;
//import com.jaron.fsconnect.activities.TelephoneChangeActivity;
import com.jaron.fsconnect.activities.UsernameChangeActivity;
import com.jaron.fsconnect.api.FSConnectApi;
import com.jaron.fsconnect.app.AppOperator;
import com.jaron.fsconnect.base.BaseFragment;
import com.jaron.fsconnect.bean.ResultBean;
import com.jaron.fsconnect.bean.Teacher;
import com.jaron.fsconnect.presentation.business.LoginBusiness;
import com.jaron.fsconnect.utils.DialogHelper;
import com.jaron.fsconnect.widget.SolarSystemView;
import com.loopj.android.http.TextHttpResponseHandler;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.tencent.TIMCallBack;
import com.tencent.TIMFriendGenderType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMManager;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Jaron on 2018/5/25.
 */

public class UserInfoFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.user_view_solar_system)
    SolarSystemView userViewSolarSystem;
    @BindView(R.id.iv_portrait)
    CircleImageView ivPortrait;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.rl_show_my_info)
    LinearLayout rlShowMyInfo;
    @BindView(R.id.username_text)
    TextView usernameText;
    @BindView(R.id.phone_text)
    TextView phoneText;
    @BindView(R.id.truename_text)
    TextView truenameText;
    @BindView(R.id.iv_gender)
    ImageView ivGender;
    @BindView(R.id.student_name_text)
    TextView studentNameText;
    @BindView(R.id.rl_logout)
    LinearLayout rlLogout;
    @BindView(R.id.gender_text)
    TextView genderText;

    private List<LocalMedia> localMediaList = new ArrayList<>();
    private PopupWindow pop;
    private Activity activity;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_user_home;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
    }

    @Override
    protected void initData() {
        super.initData();
        requestLogin(AccountHelper.getUser().getTeacherTelephone(),AccountHelper.getUser().getTeacherPassword());
        updateView(AccountHelper.getUser());

    }

    public void requestLogin(String username, String password) {
        FSConnectApi.login(username, password, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                //showFocusWaitDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                showToast(getString(R.string.request_error_hint));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Type type = new TypeToken<ResultBean<Teacher>>() {
                    }.getType();
                    //GsonBuilder gsonBuilder = new GsonBuilder();
                    ResultBean<Teacher> resultBean = AppOperator.createGson().fromJson(responseString, type);
                    if (resultBean.isSuccess()) {
                        Teacher user = resultBean.getResult();
                        AccountHelper.login(user, headers);
                    } else {
                        int code = resultBean.getCode();
                        String message = resultBean.getMessage();
                        if (code == 211) {
                            message += "," + getResources().getString(R.string.message_username_error);
                        } else if (code == 212) {
                            message += "," + getResources().getString(R.string.message_pwd_error);
                        }
                        showToast(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                //hideWaitDialog();
            }

            @Override
            public void onCancel() {
                super.onCancel();
                //hideWaitDialog();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mIsUploadIcon = false;
        if (AccountHelper.isLogin()) {
            Teacher user = AccountHelper.getUser();
            updateView(user);
        }
    }


    private void updateView(Teacher teacher) {
        setImageFromNet(ivPortrait, teacher.getHeadIcon(), R.mipmap.widget_default_face);
        tvNick.setText(teacher.getTeacherUsername());
        truenameText.setText(teacher.getTeacherTruename());
        usernameText.setText(teacher.getTeacherUsername());
        phoneText.setText(teacher.getTeacherTelephone());
        if(teacher.getTeacherSex()==1)
            genderText.setText("男");
        if(teacher.getTeacherSex()==0)
            genderText.setText("女");
        switch (teacher.getTeacherSex()) {
            case 1:
                ivGender.setVisibility(View.VISIBLE);
                ivGender.setImageResource(R.mipmap.ic_male);
                break;
            case 0:
                ivGender.setVisibility(View.VISIBLE);
                ivGender.setImageResource(R.mipmap.ic_female);
                break;
            default:
                ivGender.setVisibility(View.INVISIBLE);
                break;
        }

    }

    @OnClick({R.id.rl_username, R.id.iv_portrait, R.id.rl_phone,  R.id.rl_logout, R.id.rl_gender})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_username:
                UsernameChangeActivity.show(this, AccountHelper.getUser().getTeacherUsername());
                break;
            case R.id.rl_phone:
                //PhoneChangeActivity.show(this, AccountHelper.getUser().getTeacherTelephone());
                break;
            case R.id.rl_logout:
                AccountHelper.logout(rlLogout, new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        //getActivity().finish();
                        AppContext.showToastShort(getString(R.string.logout_success_hint));
                    }
                });
                String user=TIMManager.getInstance().getLoginUser();
                Log.d("user",user);

                LoginBusiness.logout(new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Log.d("error1",String.valueOf(i)+s);
                        if (getActivity() != null){
                            Toast.makeText(getActivity(), getResources().getString(R.string.setting_logout_fail), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onSuccess() {
                        if (getActivity() != null && getActivity() instanceof MainActivity){
                            ((MainActivity) getActivity()).logout();
                        }
                    }
                });
                break;
            case R.id.iv_portrait:
                showAvatarOperation();
                break;
            case R.id.rl_gender:
                showPop();
                break;
        }
    }

    private void showAvatarOperation() {
        if (!AccountHelper.isLogin()) {
            LoginActivity.show(getActivity());
        } else {

            DialogHelper.getSelectDialog(getActivity(),
                    getString(R.string.action_select),
                    getResources().getStringArray(R.array.avatar_option), "取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Teacher user = AccountHelper.getUser();
                            switch (i) {
                                case 0:
                                    PictureSelector.create(UserInfoFragment.this)
                                            .openGallery(PictureMimeType.ofImage())
                                            .maxSelectNum(1).minSelectNum(1)
                                            .imageSpanCount(4).isCamera(true)
                                            .enableCrop(true).freeStyleCropEnabled(true)
                                            .showCropFrame(true).showCropGrid(true)
                                            .selectionMode(PictureConfig.SINGLE)
                                            .forResult(PictureConfig.CHOOSE_REQUEST);
                                    break;

                                case 1:
                                    if (user == null
                                            || TextUtils.isEmpty(user.getHeadIcon())) return;
                                    localMediaList.clear();
                                    localMediaList.add(new LocalMedia(user.getHeadIcon(), 1, 0, "image"));
                                    PictureSelector.create(UserInfoFragment.this).themeStyle(R.style.picture_default_style).openExternalPreview(0, "/teacher_icon", localMediaList);
                                    break;
                            }
                        }
                    }).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    images = PictureSelector.obtainMultipleResult(data);
                    File headIcon = new File(getImagePath(images.get(0)));
                    FSConnectApi.changeIcon(AccountHelper.getUserId(), headIcon, new TextHttpResponseHandler() {
                        @Override
                        public void onStart() {
                            super.onStart();
                            showFocusWaitDialog();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.d("statusCode", Integer.toString(statusCode));
                            //requestFailureHint(throwable);
                            if (throwable != null) {
                                throwable.printStackTrace();
                            }
                            showToast(getResources().getString(R.string.request_error_hint));
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            try {
                                Type type = new TypeToken<ResultBean<Teacher>>() {
                                }.getType();
                                ResultBean<Teacher> resultBean = AppOperator.createGson().fromJson(responseString, type);
                                if (resultBean.isSuccess()) {
                                    Teacher teacher=resultBean.getResult();
                                    updateView(teacher);
                                    AccountHelper.updateUserCache(teacher);
                                    TIMFriendshipManager.getInstance().setFaceUrl(teacher.getHeadIcon(),timCallBack);
                                    String message = resultBean.getMessage();
                                    showToast(message);
                                } else {
                                    String message = resultBean.getMessage();
                                    showToast(message);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                onFailure(statusCode, headers, responseString, e);
                            }
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
                    });

                    setImageFromNet(ivPortrait, images.get(0).getCutPath(), R.mipmap.widget_default_face);
                    break;
            }
        }
    }

    TIMCallBack timCallBack=new TIMCallBack(){
        @Override
        public void onError(int code, String desc) {
            //错误码 code 和错误描述 desc，可用于定位请求失败原因
            //错误码 code 列表请参见错误码表
            Log.e("UserInfoFragment", "setFaceUrl failed: " + code + " desc" + desc);
        }
        @Override
        public void onSuccess() {
            Log.e("UserInfoFragment", "setFaceUrl succ");
        }
    };

    private void showPop() {
        activity = getActivity();
        View bottomView = View.inflate(activity, R.layout.layout_bottom_dialog, null);
        TextView man = (TextView) bottomView.findViewById(R.id.tv_choose_one);
        TextView woman = (TextView) bottomView.findViewById(R.id.tv_choose_two);
        TextView mCancel = (TextView) bottomView.findViewById(R.id.tv_cancel);
        man.setText("男");
        woman.setText("女");

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.5f;
        activity.getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_choose_one:
                        changeGender(1);
                        break;
                    case R.id.tv_choose_two:
                        changeGender(0);
                        break;
                    case R.id.tv_cancel:
                        //取消
                        //closePopupWindow();
                        break;
                }
                closePopupWindow();
            }
        };

        man.setOnClickListener(clickListener);
        woman.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

    private void changeGender(Integer sex) {
        FSConnectApi.changeGender(AccountHelper.getUserId(), sex, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                showFocusWaitDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("statusCode", Integer.toString(statusCode));
                //requestFailureHint(throwable);
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
                        updateView(teacher);
                        AccountHelper.updateUserCache(teacher);
                        if(teacher.getTeacherSex()==0)
                        TIMFriendshipManager.getInstance().setGender(TIMFriendGenderType.Female,timCallBack);
                        else
                            TIMFriendshipManager.getInstance().setGender(TIMFriendGenderType.Male,timCallBack);
                        AppContext.showToast(resultBean.getMessage(), Toast.LENGTH_SHORT);
                        break;
                    case 0:
                        AppContext.showToast(resultBean.getMessage(), Toast.LENGTH_SHORT);
                        break;
                    default:
                        break;
                }
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
        });
    }

    private String getImagePath(LocalMedia localMedia) {
        String path = "";
        if (localMedia.isCut() && !localMedia.isCompressed()) {
            // 裁剪过
            path = localMedia.getCutPath();
        } else if (localMedia.isCompressed() || (localMedia.isCut() && localMedia.isCompressed())) {
            // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
            path = localMedia.getCompressPath();
        } else {
            // 原图
            path = localMedia.getPath();
        }
        // 图片
        if (localMedia.isCompressed()) {
            Log.i("compress image result:", new File(localMedia.getCompressPath()).length() / 1024 + "k");
            Log.i("压缩地址::", localMedia.getCompressPath());
        }

        Log.i("原图地址::", localMedia.getPath());
        if (localMedia.isCut()) {
            Log.i("裁剪地址::", localMedia.getCutPath());
        }
        return path;
    }
}
