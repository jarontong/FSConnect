package com.jaron.fsconnectparent.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.jaron.fsconnectparent.R;
import com.jaron.fsconnectparent.base.BaseRecyclerAdapter;
import com.jaron.fsconnectparent.bean.Notice;
import com.jaron.fsconnectparent.utils.ImageLoader;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Jaron on 2018/6/1.
 */

public class NoticeAdapter extends BaseRecyclerAdapter<Notice> {

    private static final int VIEW_TYPE_DATA_FOOTER = 2000;

    private RequestManager mRequestManager;

    public NoticeAdapter(final Context context, RequestManager requestManager, int mode) {
        super(context, mode);
        this.mRequestManager = requestManager;
    }

    public NoticeAdapter(Context context, int mode) {
        super(context, mode);
    }

    @Override
    protected NoticeHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list_notice, parent, false);
        return new NoticeHolder(view);
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, Notice item, int position) {
        if (holder instanceof NoticeHolder) {
            ((NoticeHolder) holder).addNotice( item, mRequestManager);
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

    static class NoticeHolder extends RecyclerView.ViewHolder {

        private ProgressDialog mDialog;

        @BindView(R.id.iv_portrait)
        CircleImageView ivPortrait;
        @BindView(R.id.notice_title)
        TextView noticeTitle;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.notice_content)
        TextView noticeContent;
        @BindView(R.id.notice_teacher)
        TextView noticeTeacher;

        public NoticeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("DefaultLocale")
        void addNotice( final Notice notice, RequestManager requestManager){
            ImageLoader.loadImage( requestManager,ivPortrait, notice.getTeacher().getHeadIcon(), R.mipmap.widget_default_face);
            noticeTitle.setText(notice.getTitle());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String res = sdf.format(notice.getCreatTime());
            time.setText(res);
            noticeContent.setText(notice.getContent());
            noticeTeacher.setText(notice.getTeacher().getTeacherTruename());
        }

    }
}
