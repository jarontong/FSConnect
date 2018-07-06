package com.jaron.fsconnect.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaron.fsconnect.R;
import com.jaron.fsconnect.adapter.GridAdapter;
import com.jaron.fsconnect.base.BaseActivity;
import com.jaron.fsconnect.bean.Notice;
import com.jaron.fsconnect.bean.NoticeImage;
import com.jaron.fsconnect.utils.FullyGridLayoutManager;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jaron on 2018/6/6.
 */

public class NoticeDetailActivity extends BaseActivity {

    public static final String BUNDLE_KEY = "BUNDLE_KEY";
    @BindView(R.id.ib_navigation_back)
    ImageButton ibNavigationBack;
    @BindView(R.id.notice_time)
    TextView noticeTime;
    @BindView(R.id.notice_class)
    TextView noticeClass;
    @BindView(R.id.notice_title)
    TextView noticeTitle;
    @BindView(R.id.notice_content)
    TextView noticeContent;
    @BindView(R.id.notice_image)
    RecyclerView noticeImage;
    @BindView(R.id.account_login_form)
    LinearLayout accountLoginForm;

    private Notice notice;
    private GridAdapter adapter;
    private List<NoticeImage> noticeImageList;
    private ArrayList<String> mImageList = new ArrayList<>();
    private List<LocalMedia> localMediaList = new ArrayList<>();
    private boolean changed=false;

    public static void show(Activity context, Notice notice) {
        Intent intent = new Intent(context, NoticeDetailActivity.class);
        intent.putExtra(BUNDLE_KEY, notice);
        context.startActivityForResult(intent,2);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_notice_detail;
    }

    @Override
    protected boolean initBundle(Bundle bundle) {
        notice = (Notice) getIntent().getSerializableExtra(BUNDLE_KEY);
        return !(notice == null || notice.getNoticeId() <= 0) && super.initBundle(bundle);
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
        noticeImage.setLayoutManager(manager);
        noticeImageList = notice.getNoticeImageList();
        for (NoticeImage noticeImage : noticeImageList) {
            mImageList.add(noticeImage.getNoticeImagePath());
            localMediaList.add(new LocalMedia(noticeImage.getNoticeImagePath(), 1, 0, "image"));
        }
        adapter = new GridAdapter(mImageList, this);
        noticeImage.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (localMediaList.size() > 0) {
                    LocalMedia media = localMediaList.get(position);
                    PictureSelector.create(NoticeDetailActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, "/notice_image", localMediaList);
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        noticeTime.setText(sdf.format(notice.getCreatTime()));
        noticeClass.setText(notice.getaClass().getClassName());
        noticeTitle.setText(notice.getTitle());
        noticeContent.setText(notice.getContent());
    }


    @OnClick({R.id.ib_navigation_back, R.id.notice_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                if(changed) {
                    Intent intent = new Intent();
                    intent.putExtra("changed", changed);
                    intent.putExtra("notice", notice);
                    setResult(3, intent);
                }
                finish();
                break;
            case R.id.notice_update:
                NoticeUpdateActivity.show(this,notice);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1 && resultCode == 2) {
            notice = (Notice) intent.getSerializableExtra("notice");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            noticeTime.setText(sdf.format(notice.getCreatTime()));
            noticeClass.setText(notice.getaClass().getClassName());
            noticeTitle.setText(notice.getTitle());
            noticeContent.setText(notice.getContent());

            FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
            noticeImage.setLayoutManager(manager);
            noticeImageList = notice.getNoticeImageList();
            mImageList.clear();
            localMediaList.clear();
            for (NoticeImage noticeImage : noticeImageList) {
                mImageList.add(noticeImage.getNoticeImagePath());
                localMediaList.add(new LocalMedia(noticeImage.getNoticeImagePath(), 1, 0, "image"));
            }
            adapter = new GridAdapter(mImageList, this);
            noticeImage.setAdapter(adapter);
            adapter.setOnItemClickListener(new GridAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    if (localMediaList.size() > 0) {
                        LocalMedia media = localMediaList.get(position);
                        PictureSelector.create(NoticeDetailActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, "/notice_image", localMediaList);
                    }
                }
            });
            changed=true;
        }
    }
}
