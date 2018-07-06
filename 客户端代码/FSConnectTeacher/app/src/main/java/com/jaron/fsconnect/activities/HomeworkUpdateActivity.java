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
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.reflect.TypeToken;
import com.jaron.fsconnect.R;
import com.jaron.fsconnect.account.AccountHelper;
import com.jaron.fsconnect.adapter.GridImageAdapter;
import com.jaron.fsconnect.api.FSConnectApi;
import com.jaron.fsconnect.app.AppOperator;
import com.jaron.fsconnect.base.BaseActivity;
import com.jaron.fsconnect.bean.Class;
import com.jaron.fsconnect.bean.Homework;
import com.jaron.fsconnect.bean.HomeworkImage;
import com.jaron.fsconnect.bean.ResultBean;
import com.jaron.fsconnect.bean.Subject;
import com.jaron.fsconnect.bean.Teacher;
import com.jaron.fsconnect.bean.TeacherSubject;
import com.jaron.fsconnect.utils.DialogHelper;
import com.jaron.fsconnect.utils.FullyGridLayoutManager;
import com.loopj.android.http.TextHttpResponseHandler;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Jaron on 2018/6/9.
 */

public class HomeworkUpdateActivity extends BaseActivity {
    public static final String BUNDLE_KEY = "BUNDLE_KEY";
    @BindView(R.id.spinner_class)
    Spinner spinnerClass;
    @BindView(R.id.spinner_subject)
    Spinner spinnerSubject;
    @BindView(R.id.homework_content_edit)
    TextInputEditText homeworkContentEdit;
    @BindView(R.id.homework_content)
    TextInputLayout homeworkContent;
    @BindView(R.id.homework_image)
    RecyclerView homeworkImage;
    @BindView(R.id.rvMsg)
    RecyclerView rvMsg;
    @BindView(R.id.btnVoice)
    Button btnVoice;

    protected InputMethodManager mInputMethodManager;
    private Teacher teacher;
    private List<Class> classList;
    private List<Subject> subjectList;
    private List<String> classNameList;
    private List<String> subjectNameList;
    private List<TeacherSubject> teacherSubjectList;
    private ArrayAdapter<String> arr_class_adapter;
    private ArrayAdapter<String> arr_subject_adapter;

    private Integer classIdSelected;
    private Integer subjectIdSelected;

    private int maxSelectNum = 6;
    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter adapter;
    private PopupWindow pop;

    private Homework homework;
    private List<HomeworkImage> homeworkImageList;
    private ArrayList<String> mImageList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_update_homework;
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
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        teacher = AccountHelper.getUser();
        teacherSubjectList = teacher.getTeacherSubjectList();
        classList = new ArrayList<Class>();
        classNameList = new ArrayList<String>();
        subjectList = new ArrayList<Subject>();
        subjectNameList = new ArrayList<String>();
        int size = teacherSubjectList.size();
        for (int i = 0; i < size; i++) {
            classList.add(teacherSubjectList.get(i).getaClass());
            classNameList.add(teacherSubjectList.get(i).getaClass().getClassName());
            subjectList.add(teacherSubjectList.get(i).getSubject());
            subjectNameList.add(teacherSubjectList.get(i).getSubject().getSubjectName());
        }
        arr_class_adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, classNameList);
        arr_class_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClass.setAdapter(arr_class_adapter);
        arr_subject_adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item, subjectNameList);
        arr_subject_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(arr_subject_adapter);

        homeworkContentEdit.setText(homework.getContent());
        int position=0,pos=0;
        for (int i = 0; i < size; i++) {
            if(classNameList.get(i).equals(homework.getaClass().getClassName()))
                position=i;
            if(subjectNameList.get(i).equals(homework.getSubject().getSubjectName()))
                pos=i;
        }
        spinnerClass.setSelection(position);
        spinnerSubject.setSelection(pos);
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        homeworkImage.setLayoutManager(manager);
        homeworkImageList = homework.getHomeworkImageList();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (HomeworkImage homeworkImage : homeworkImageList) {
                        FutureTarget<File> future = Glide.with(HomeworkUpdateActivity.this)
                                .load(homeworkImage.getHomeworkImagePath())
                                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                        File cacheFile = future.get();
                        selectList.add(new LocalMedia(cacheFile.getAbsolutePath(), 1, 0, "image"));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        homeworkImage.setAdapter(adapter);
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
                            PictureSelector.create(HomeworkUpdateActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(HomeworkUpdateActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(HomeworkUpdateActivity.this).externalPictureAudio(media.getPath());
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
        View bottomView = View.inflate(HomeworkUpdateActivity.this, R.layout.layout_bottom_dialog, null);
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
                        PictureSelector.create(HomeworkUpdateActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .maxSelectNum(maxSelectNum)
                                .minSelectNum(1)
                                .imageSpanCount(4)
                                .selectionMode(PictureConfig.MULTIPLE)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_choose_two:
                        //拍照
                        PictureSelector.create(HomeworkUpdateActivity.this)
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

    public static void show(Context context) {
        Intent intent = new Intent(context, NoticeUpdateActivity.class);

        context.startActivity(intent);
    }

    public static void show(Activity context, Homework homework) {
        Intent intent = new Intent(context, NoticeUpdateActivity.class);
        intent.putExtra(BUNDLE_KEY, homework);
        context.startActivityForResult(intent, 1);
    }

    public static void show(Fragment fragment) {
        Intent intent = new Intent(fragment.getActivity(), NoticeUpdateActivity.class);
        fragment.startActivity(intent);
    }

    @Override
    protected boolean initBundle(Bundle bundle) {
        homework = (Homework) getIntent().getSerializableExtra(BUNDLE_KEY);
        Log.d("notice",homework.toString());
        return !(homework == null || homework.getHomeworkId() <= 0) && super.initBundle(bundle);
    }

    @OnClick({R.id.ib_navigation_back, R.id.notice_add_complete, R.id.setting_head_container, R.id.account_login_form})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.notice_add_complete:
                updateHomework();
                break;
            case R.id.setting_head_container:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
            case R.id.account_login_form:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
        }
    }

    /*    @OnItemSelected({R.id.spinner_class,R.id.spinner_subject})//默认callback为ITEM_SELECTED
    public void onClassItemSelected(View view,int position) {
        switch (view.getId()) {
            case R.id.spinner_class:
                classIdSelected = classList.get(position).getClassId();
                break;
            case R.id.spinner_subject:
                subjectIdSelected = subjectList.get(position).getSubjectId();
                break;
        }
    }*/

    @OnItemSelected(R.id.spinner_class)//默认callback为ITEM_SELECTED
    public void onClassItemSelected(int position) {
        classIdSelected = classList.get(position).getClassId();
    }

    @OnItemSelected(R.id.spinner_subject)
    public void onSubjectItemSelected(int position) {
        subjectIdSelected = subjectList.get(position).getSubjectId();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyBoard(getCurrentFocus().getWindowToken());
    }

    private void updateHomework() {
        String homeworkContent = homeworkContentEdit.getText().toString().trim();
        Integer teacherId = AccountHelper.getUserId();
        int size = selectList.size();
        File[] imageFiles = new File[size];
        for (int i = 0; i < size; i++) {
            imageFiles[i] = new File(getImagePath(selectList.get(i)));
        }
        File[] voiceFiles = new File[0];
        FSConnectApi.updateHomework(homework.getHomeworkId(),subjectIdSelected,teacherId,classIdSelected, homeworkContent, imageFiles, voiceFiles, new TextHttpResponseHandler() {

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
                    Type type = new TypeToken<ResultBean<Homework>>() {
                    }.getType();
                    ResultBean<Homework> resultBean = AppOperator.createGson().fromJson(responseString, type);
                    if (resultBean.isSuccess()) {
                        Homework homework = resultBean.getResult();
                        String message = resultBean.getMessage();
                        showToast(message);
                        Intent intent = new Intent();
                        intent.putExtra("homework", homework);
                        setResult(2, intent);
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

    private void showToast(String message) {
        Toast toast = new Toast(HomeworkUpdateActivity.this);
        View rootView = LayoutInflater.from(HomeworkUpdateActivity.this).inflate(R.layout.view_toast, null, false);
        TextView textView = (TextView) rootView.findViewById(R.id.title_tv);
        textView.setText(message);
        toast.setView(rootView);
        toast.setGravity(Gravity.BOTTOM, 0, getResources().getDimensionPixelSize(R.dimen.toast_y_offset));
        toast.show();
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
