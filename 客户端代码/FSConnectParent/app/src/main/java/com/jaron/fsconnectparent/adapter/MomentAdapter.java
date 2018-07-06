package com.jaron.fsconnectparent.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.jaron.fsconnectparent.R;
import com.jaron.fsconnectparent.base.BaseRecyclerAdapter;
import com.jaron.fsconnectparent.bean.Moment;
import com.jaron.fsconnectparent.bean.MomentImage;
import com.jaron.fsconnectparent.utils.ImageLoader;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Jaron on 2018/6/17.
 */

public class MomentAdapter extends BaseRecyclerAdapter<Moment> {

    private static final int VIEW_TYPE_DATA_FOOTER = 2000;

    private RequestManager mRequestManager;
    private Context context;
    private Activity activity;

    public MomentAdapter(Context context, int mode) {
        super(context, mode);
    }

    public MomentAdapter(Context context, Activity activity, int mode, RequestManager mRequestManager) {
        super(context, mode);
        this.context = context;
        this.activity = activity;
        this.mRequestManager = mRequestManager;
    }

    @Override
    protected MomentHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list_moment, parent, false);
        return new MomentHolder(view, context, activity);
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, Moment item, int position) {
        if (holder instanceof MomentHolder) {
            ((MomentHolder) holder).addMoment(item, mRequestManager);
        }
    }

    static class MomentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_portrait)
        CircleImageView ivPortrait;
        @BindView(R.id.username)
        TextView username;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.homework_content)
        TextView homeworkContent;
        @BindView(R.id.fiv1)
        ImageView fiv1;
        @BindView(R.id.fiv2)
        ImageView fiv2;
        @BindView(R.id.fiv3)
        ImageView fiv3;
        @BindView(R.id.fiv4)
        ImageView fiv4;
        @BindView(R.id.fiv5)
        ImageView fiv5;
        @BindView(R.id.fiv6)
        ImageView fiv6;
        Context context;
        private GridAdapter adapter;
        Activity activity;

        private List<MomentImage> momentImageList;
        private ArrayList<String> mImageList = new ArrayList<>();
        private List<LocalMedia> localMediaList = new ArrayList<>();

        MomentHolder(View view, Context context, Activity activity) {
            super(view);
            ButterKnife.bind(this, view);
            this.context = context;
            this.activity = activity;
            fiv1.setOnClickListener(this);
            fiv2.setOnClickListener(this);
            fiv3.setOnClickListener(this);
            fiv4.setOnClickListener(this);
            fiv5.setOnClickListener(this);
            fiv6.setOnClickListener(this);

        }

        @SuppressLint("DefaultLocale")
        void addMoment(final Moment moment, RequestManager requestManager) {
            ImageView[] fiv = {fiv1, fiv2, fiv3, fiv4, fiv5, fiv6};
            if (moment.getTeacher() != null) {
                ImageLoader.loadImage(requestManager, ivPortrait, moment.getTeacher().getHeadIcon(), R.mipmap.widget_default_face);
                username.setText(moment.getTeacher().getTeacherUsername());
            }
            if (moment.getParent() != null) {
                ImageLoader.loadImage(requestManager, ivPortrait, moment.getParent().getHeadIcon(), R.mipmap.widget_default_face);
                username.setText(moment.getParent().getParentUsername());
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String res = sdf.format(moment.getCreateTime());
            time.setText(res);
            homeworkContent.setText(moment.getContent());


            momentImageList = moment.getMomentImageList();
            if (momentImageList != null) {
                localMediaList.clear();
                for (int i = 0; i < momentImageList.size(); i++) {
                    MomentImage momentImage = momentImageList.get(i);
                    mImageList.add(momentImage.getMomentImagePath());
                    fiv[i].setVisibility(View.VISIBLE);
                    ImageLoader.loadImage(requestManager, fiv[i], momentImage.getMomentImagePath());
                    Log.d("momentImage",momentImage.toString());
                    //Log.d("getMimentImagePath",momentImage.getMimentImagePath());
                    localMediaList.add(new LocalMedia(momentImage.getMomentImagePath(), 1, 0, "image"));
                }

            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fiv1:
                    PictureSelector.create(activity).themeStyle(R.style.picture_default_style).openExternalPreview(0, "/moment_image", localMediaList);
                    break;
                case R.id.fiv2:
                    PictureSelector.create(activity).themeStyle(R.style.picture_default_style).openExternalPreview(1, "/moment_image", localMediaList);
                    break;
                case R.id.fiv3:
                    PictureSelector.create(activity).themeStyle(R.style.picture_default_style).openExternalPreview(2, "/moment_image", localMediaList);
                    break;
                case R.id.fiv4:
                    PictureSelector.create(activity).themeStyle(R.style.picture_default_style).openExternalPreview(3, "/moment_image", localMediaList);
                    break;
                case R.id.fiv5:
                    PictureSelector.create(activity).themeStyle(R.style.picture_default_style).openExternalPreview(4, "/moment_image", localMediaList);
                    break;
                case R.id.fiv6:
                    PictureSelector.create(activity).themeStyle(R.style.picture_default_style).openExternalPreview(5, "/moment_image", localMediaList);
                    break;
            }
        }
    }

}
