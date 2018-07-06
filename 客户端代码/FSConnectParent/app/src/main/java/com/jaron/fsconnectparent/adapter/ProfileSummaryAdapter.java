package com.jaron.fsconnectparent.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jaron.fsconnectparent.R;
import com.jaron.fsconnectparent.model.FriendProfile;
import com.jaron.fsconnectparent.model.GroupProfile;
import com.jaron.fsconnectparent.model.ProfileSummary;
import com.jaron.fsconnectparent.utils.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

/**
 * 好友或群等资料摘要列表的adapter
 */
public class ProfileSummaryAdapter extends ArrayAdapter<ProfileSummary> {


    private int resourceId;
    private View view;
    private ViewHolder viewHolder;
    private Context context;
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ProfileSummaryAdapter(Context context, int resource, List<ProfileSummary> objects) {
        super(context, resource, objects);
        resourceId = resource;
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.avatar = (CircleImageView) view.findViewById(R.id.avatar);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.des = (TextView) view.findViewById(R.id.description);
            view.setTag(viewHolder);
        }
        ProfileSummary data = getItem(position);
        Log.d("Tag",String.valueOf(data.getAvatarUrl()==null));
        if(data.getAvatarUrl()==null)
            viewHolder.avatar.setImageResource(R.drawable.head_other);
        else if(data.getClass().equals(GroupProfile.class))
            ImageLoader.loadImage(Glide.with(context),viewHolder.avatar, data.getAvatarUrl(), R.drawable.head_group);
        else if(data.getClass().equals(FriendProfile.class))
            ImageLoader.loadImage(Glide.with(context),viewHolder.avatar, data.getAvatarUrl(), R.drawable.head_other);
        viewHolder.name.setText(data.getName());
        return view;
    }


    public class ViewHolder{
        public ImageView avatar;
        public TextView name;
        public TextView des;
    }
}
