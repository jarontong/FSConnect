package com.jaron.fsconnect.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jaron.fsconnect.R;
import com.jaron.fsconnect.model.Conversation;
import com.jaron.fsconnect.model.NomalConversation;
import com.jaron.fsconnect.utils.ImageLoader;
import com.jaron.fsconnect.utils.TimeUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.tencent.TIMConversationType.C2C;
import static com.tencent.TIMConversationType.Group;


public class ConversationAdapter extends ArrayAdapter<Conversation> {

    private int resourceId;
    private View view;
    private ViewHolder viewHolder;
    private Context context;
    public ConversationAdapter(Context context, int resource, List<Conversation> objects) {
        super(context, resource, objects);
        resourceId = resource;
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null){
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }else{
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) view.findViewById(R.id.name);
            viewHolder.avatar = (CircleImageView) view.findViewById(R.id.avatar);
            viewHolder.lastMessage = (TextView) view.findViewById(R.id.last_message);
            viewHolder.time = (TextView) view.findViewById(R.id.message_time);
            viewHolder.unread = (TextView) view.findViewById(R.id.unread_num);
            view.setTag(viewHolder);
        }
        final Conversation data = getItem(position);
        viewHolder.tvName.setText(data.getName());
        viewHolder.lastMessage.setText(data.getLastMessageSummary());
        viewHolder.time.setText(TimeUtil.getTimeStr(data.getLastMessageTime()));
        if(!data.getClass().equals(NomalConversation.class))
            viewHolder.avatar.setImageResource(R.drawable.ic_news);
        else{
            if(data.getType()==C2C)
                ImageLoader.loadImage(Glide.with(context),viewHolder.avatar, data.getAvatar(), R.drawable.head_other);
            if(data.getType()==Group)
                ImageLoader.loadImage(Glide.with(context),viewHolder.avatar, data.getAvatar(), R.drawable.head_group);
        }

        long unRead = data.getUnreadNum();
        if (unRead <= 0){
            viewHolder.unread.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.unread.setVisibility(View.VISIBLE);
            String unReadStr = String.valueOf(unRead);
            if (unRead < 10){
                viewHolder.unread.setBackground(getContext().getResources().getDrawable(R.drawable.point1));
            }else{
                viewHolder.unread.setBackground(getContext().getResources().getDrawable(R.drawable.point2));
                if (unRead > 99){
                    unReadStr = getContext().getResources().getString(R.string.time_more);
                }
            }
            viewHolder.unread.setText(unReadStr);
        }
        return view;
    }

    public class ViewHolder{
        public TextView tvName;
        public CircleImageView avatar;
        public TextView lastMessage;
        public TextView time;
        public TextView unread;

    }
}
/*
public class ConversationAdapter extends BaseRecyclerAdapter<Conversation> {
    private static final int VIEW_TYPE_DATA_FOOTER = 2000;
    private RequestManager mRequestManager;
    public ConversationAdapter(Context context,  int mode) {
        super(context, mode);
    }

    @Override
    protected ConversationHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_conversation, parent, false);
        return new ConversationHolder(view);
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder holder, Conversation item, int position) {

        if (holder instanceof ConversationHolder) {
            ((ConversationHolder) holder).addConversation(item);
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

    static class ConversationHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatar)
        CircleImageView avatar;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.last_message)
        TextView lastMessage;
        @BindView(R.id.message_time)
        TextView messageTime;
        @BindView(R.id.unread_num)
        TextView unreadNum;

        ConversationHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @SuppressLint("DefaultLocale")
        void addConversation( final Conversation data){
            name.setText(data.getName());
            avatar.setImageResource(data.getAvatar());
            lastMessage.setText(data.getLastMessageSummary());
            messageTime.setText(TimeUtil.getTimeStr(data.getLastMessageTime()));
            //setImageFromNet(ivPortrait, teacher.getHeadIcon(), R.mipmap.widget_default_face);
            long unRead = data.getUnreadNum();
            if (unRead <= 0) {
                unreadNum.setVisibility(View.INVISIBLE);
            } else {
                unreadNum.setVisibility(View.VISIBLE);
                String unReadStr = String.valueOf(unRead);
                if (unRead < 10) {
                    unreadNum.setBackground(getContext().getResources().getDrawable(R.drawable.point1));
                } else {
                    unreadNum.setBackground(getContext().getResources().getDrawable(R.drawable.point2));
                    if (unRead > 99) {
                        unReadStr = getContext().getResources().getString(R.string.time_more);
                    }
                }
                unreadNum.setText(unReadStr);
            }
        }
    }

}*/
