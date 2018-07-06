package com.jaron.fsconnect.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.jaron.fsconnect.R;
import com.jaron.fsconnect.account.AccountHelper;
import com.jaron.fsconnect.account.base.AccountBaseActivity;
import com.jaron.fsconnect.adapter.GridImageAdapter;
import com.jaron.fsconnect.api.FSConnectApi;
import com.jaron.fsconnect.app.AppOperator;
import com.jaron.fsconnect.base.BaseActivity;
import com.jaron.fsconnect.bean.Class;
import com.jaron.fsconnect.bean.Notice;
import com.jaron.fsconnect.bean.ResultBean;
import com.jaron.fsconnect.bean.Teacher;
import com.jaron.fsconnect.bean.TeacherSubject;
import com.jaron.fsconnect.utils.DialogHelper;
import com.jaron.fsconnect.utils.FullyGridLayoutManager;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;

/**
 * Created by Jaron on 2018/6/1.
 */

public class NoticeAddActivity extends BaseActivity {
    @BindView(R.id.ib_navigation_back)
    ImageButton ibNavigationBack;
    @BindView(R.id.notice_add_complete)
    ImageButton noticeAddComplete;
    @BindView(R.id.setting_head_container)
    FrameLayout settingHeadContainer;
    @BindView(R.id.spinner_class)
    Spinner spinnerClass;
    @BindView(R.id.notice_title_edit)
    TextInputEditText noticeTitleEdit;
    @BindView(R.id.notice_content_edit)
    TextInputEditText noticeContentEdit;
    @BindView(R.id.notice_image)
    RecyclerView noticeImage;
    @BindView(R.id.rvMsg)
    RecyclerView rvMsg;
    @BindView(R.id.btnVoice)
    Button btnVoice;

    protected InputMethodManager mInputMethodManager;
    private Teacher teacher;
    private ArrayList<Integer> classIdList;
    private ArrayList<String> classNameList;
    private List<TeacherSubject> teacherSubjectList;
    private ArrayAdapter<String> arr_adapter;

    private Integer classIdSelected;

    private int maxSelectNum = 6;
    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter adapter;
    private PopupWindow pop;

    @Override
    protected int getContentView() {
        return R.layout.activity_add_notice;
    }

    @Override
    protected void initData() {
        super.initData();
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        arr_adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, classNameList);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    @Override
    protected boolean initBundle(Bundle bundle) {
        classIdList = (ArrayList<Integer>) getIntent().getIntegerArrayListExtra("classId");
        classNameList= (ArrayList<String>) getIntent().getStringArrayListExtra("classNameList");
        return !(classIdList == null || classNameList == null) && super.initBundle(bundle);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        noticeImage.setLayoutManager(manager);
        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        noticeImage.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(NoticeAddActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(NoticeAddActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(NoticeAddActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });

    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            showPop();
        }
    };

    private void showPop() {
        View bottomView = View.inflate(NoticeAddActivity.this, R.layout.layout_bottom_dialog, null);
        TextView mAlbum = (TextView) bottomView.findViewById(R.id.tv_choose_one);
        TextView mCamera = (TextView) bottomView.findViewById(R.id.tv_choose_two);
        TextView mCancel = (TextView) bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_choose_one:
                        //相册
                        PictureSelector.create(NoticeAddActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(maxSelectNum)
                                .minSelectNum(1)
                                .imageSpanCount(4)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_choose_two:
                        //拍照
                        PictureSelector.create(NoticeAddActivity.this)
                                .openCamera(PictureMimeType.ofImage())
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_cancel:
                        //取消
                        //closePopupWindow();
                        break;
                }
                closePopupWindow();
            }
        };

        mAlbum.setOnClickListener(clickListener);
        mCamera.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调

                    images = PictureSelector.obtainMultipleResult(data);
                    selectList.addAll(images);

//                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    /**
     * show the login activity
     *
     * @param context context
     */
    public static void show(Context context) {
        Intent intent = new Intent(context, NoticeAddActivity.class);
        context.startActivity(intent);
    }

    /**
     * show the login activity
     *
     * @param context context
     */
    public static void show(Activity context,ArrayList<Integer> classId,ArrayList<String> classNameList) {
        Intent intent = new Intent(context, NoticeAddActivity.class);
        intent.putStringArrayListExtra("classNameList",classNameList);
        intent.putIntegerArrayListExtra("classId",classId);
        context.startActivityForResult(intent, 1);
    }

    /**
     * show the login activity
     *
     * @param fragment fragment
     */
    public static void show(Fragment fragment) {
        Intent intent = new Intent(fragment.getActivity(), NoticeAddActivity.class);
        fragment.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        spinnerClass.setAdapter(arr_adapter);
    }

    @OnClick({R.id.ib_navigation_back, R.id.notice_add_complete, R.id.setting_head_container, R.id.btnVoice, R.id.account_login_form})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.notice_add_complete:
                addNotice();
                break;
            case R.id.setting_head_container:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
            case R.id.btnVoice:
                break;
            case R.id.account_login_form:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
        }
    }

    @OnItemSelected(R.id.spinner_class)//默认callback为ITEM_SELECTED
    void onItemSelected(int position) {
        classIdSelected=classIdList.get(position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyBoard(getCurrentFocus().getWindowToken());

    }

    private void addNotice(){
        String noticeTitle=noticeTitleEdit.getText().toString().trim();
        String noticeContent=noticeContentEdit.getText().toString().trim();
        Integer teacherId=AccountHelper.getUserId();
        int size=selectList.size();
        File[] imageFiles=new File[size];
        for(int i=0;i<size;i++){
            imageFiles[i]=new File(getImagePath(selectList.get(i)));
        }
        File[] voiceFiles=new File[0];
        FSConnectApi.addNotice(noticeTitle,noticeContent,teacherId,classIdSelected,imageFiles,voiceFiles,new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                showFocusWaitDialog();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("statusCode",Integer.toString(statusCode));
                //requestFailureHint(throwable);
                if (throwable != null) {
                    throwable.printStackTrace();
                }
                showToast(getResources().getString(R.string.request_error_hint));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Type type = new TypeToken<ResultBean<Notice>>() {
                    }.getType();
                    ResultBean<Notice> resultBean = AppOperator.createGson().fromJson(responseString, type);
                    if (resultBean.isSuccess()) {
                        Notice notice = resultBean.getResult();
                        String message = resultBean.getMessage();
                        showToast(message);
                        Intent intent=new Intent();
                        intent.putExtra("notice", notice);
                        setResult(2,intent);
                        finish();
                    } else {
                        int code = resultBean.getCode();
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
    }

    private void showToast(String message){
        Toast toast = new Toast(NoticeAddActivity.this);
        View rootView = LayoutInflater.from(NoticeAddActivity.this).inflate(R.layout.view_toast, null, false);
        TextView textView = (TextView) rootView.findViewById(R.id.title_tv);
        textView.setText(message);
        toast.setView(rootView);
        toast.setGravity(Gravity.BOTTOM, 0, getResources().getDimensionPixelSize(R.dimen.toast_y_offset));
        toast.show();
    }

    private String getImagePath(LocalMedia localMedia){
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
