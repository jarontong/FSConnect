package com.jaron.fsconnect.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.jaron.fsconnect.R;
import com.jaron.fsconnect.base.BaseRecyclerAdapter;
import com.jaron.fsconnect.bean.Homework;
import com.jaron.fsconnect.utils.ImageLoader;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Jaron on 2018/6/9.
 */

public class HomeworkAdapter extends BaseRecyclerAdapter<Homework> {
    private static final int VIEW_TYPE_DATA_FOOTER = 2000;

    private RequestManager mRequestManager;

    public HomeworkAdapter(final Context context, RequestManager requestManager, int mode) {
        super(context, mode);
        this.mRequestManager = requestManager;
    }

    public HomeworkAdapter(Context context, int mode) {
        super(context, mode);
    }

    @Override
    protected HomeworkHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list_homework, parent, false);
        return new HomeworkHolder(view);
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, Homework item, int position) {
        if (holder instanceof HomeworkHolder) {
            ((HomeworkHolder) holder).addHomework(item, mRequestManager);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = super.getItemViewType(position);
        if (type == VIEW_TYPE_NORMAL && isRealDataFooter(position)) {
            return VIEW_TYPE_DATA_FOOTER;
        }
        return type;
    }

    private boolean isRealDataFooter(int position) {
        return getIndex(position) == getCount() - 1;
    }

    static class HomeworkHolder extends RecyclerView.ViewHolder {

        private ProgressDialog mDialog;

        @BindView(R.id.iv_portrait)
        CircleImageView ivPortrait;
        @BindView(R.id.homework_subject)
        TextView homeworkSubject;
        @BindView(R.id.homework_class)
        TextView homeworkClass;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.homework_content)
        TextView homeworkContent;
        @BindView(R.id.homework_teacher)
        TextView homeworkTeacher;

        HomeworkHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @SuppressLint("DefaultLocale")
        void addHomework( final Homework homework, RequestManager requestManager){
            ImageLoader.loadImage( requestManager,ivPortrait, homework.getTeacher().getHeadIcon(), R.mipmap.widget_default_face);
            homeworkSubject.setText(homework.getSubject().getSubjectName());
            homeworkClass.setText(homework.getaClass().getClassName());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String res = sdf.format(homework.getCreatTime());
            time.setText(res);
            homeworkContent.setText(homework.getContent());
            homeworkTeacher.setText(homework.getTeacher().getTeacherTruename());
        }
    }


}
