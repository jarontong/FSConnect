package com.jaron.fsconnectparent.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jaron.fsconnectparent.R;
import com.jaron.fsconnectparent.adapter.GridAdapter;
import com.jaron.fsconnectparent.base.BaseActivity;
import com.jaron.fsconnectparent.bean.Homework;
import com.jaron.fsconnectparent.bean.HomeworkImage;
import com.jaron.fsconnectparent.utils.FullyGridLayoutManager;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jaron on 2018/6/9.
 */

public class HomeworkDetailActivity extends BaseActivity {

    public static final String BUNDLE_KEY = "BUNDLE_KEY";
    @BindView(R.id.homework_time)
    TextView homeworkTime;
    @BindView(R.id.homework_class)
    TextView homeworkClass;
    @BindView(R.id.homework_subject)
    TextView homeworkSubject;
    @BindView(R.id.homework_content)
    TextView homeworkContent;
    @BindView(R.id.homework_image)
    RecyclerView homeworkImage;

    private Homework homework;
    private GridAdapter adapter;
    private List<HomeworkImage> homeworkImageList;
    private ArrayList<String> mImageList = new ArrayList<>();
    private List<LocalMedia> localMediaList = new ArrayList<>();
    private boolean changed=false;

    public static void show(Activity context, Homework homework) {
        Intent intent = new Intent(context, HomeworkDetailActivity.class);
        intent.putExtra(BUNDLE_KEY, homework);
        context.startActivityForResult(intent, 2);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_homework_detail;
    }

    @Override
    protected boolean initBundle(Bundle bundle) {
        homework = (Homework) getIntent().getSerializableExtra(BUNDLE_KEY);
        return !(homework == null || homework.getHomeworkId() <= 0) && super.initBundle(bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        homeworkImage.setLayoutManager(manager);
        homeworkImageList = homework.getHomeworkImageList();
        for (HomeworkImage homeworkImage : homeworkImageList) {
            mImageList.add(homeworkImage.getHomeworkImagePath());
            localMediaList.add(new LocalMedia(homeworkImage.getHomeworkImagePath(), 1, 0, "image"));
        }
        adapter = new GridAdapter(mImageList, this);
        homeworkImage.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (localMediaList.size() > 0) {
                    LocalMedia media = localMediaList.get(position);
                    PictureSelector.create(HomeworkDetailActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, "/homework_image", localMediaList);
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        homeworkTime.setText(sdf.format(homework.getCreatTime()));
        homeworkClass.setText(homework.getaClass().getClassName());
        homeworkSubject.setText(homework.getSubject().getSubjectName());
        homeworkContent.setText(homework.getContent());
    }


    @OnClick({R.id.ib_navigation_back, R.id.homework_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                if(changed) {
                    Intent intent = new Intent();
                    intent.putExtra("changed", changed);
                    intent.putExtra("homework", homework);
                    setResult(3, intent);
                }
                finish();
                break;
            case R.id.homework_update:
                //HomeworkUpdateActivity.show(this,homework);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1 && resultCode == 2) {
            homework = (Homework) intent.getSerializableExtra("homework");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            homeworkTime.setText(sdf.format(homework.getCreatTime()));
            homeworkClass.setText(homework.getaClass().getClassName());
            homeworkSubject.setText(homework.getSubject().getSubjectName());
            homeworkContent.setText(homework.getContent());

            FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
            homeworkImage.setLayoutManager(manager);
            homeworkImageList = homework.getHomeworkImageList();
            mImageList.clear();
            localMediaList.clear();
            for (HomeworkImage homeworkImage : homeworkImageList) {
                mImageList.add(homeworkImage.getHomeworkImagePath());
                localMediaList.add(new LocalMedia(homeworkImage.getHomeworkImagePath(), 1, 0, "image"));
            }
            adapter = new GridAdapter(mImageList, this);
            homeworkImage.setAdapter(adapter);
            adapter.setOnItemClickListener(new GridAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    if (localMediaList.size() > 0) {
                        LocalMedia media = localMediaList.get(position);
                        PictureSelector.create(HomeworkDetailActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, "/homework_image", localMediaList);
                    }
                }
            });
            changed=true;
        }
    }
}
