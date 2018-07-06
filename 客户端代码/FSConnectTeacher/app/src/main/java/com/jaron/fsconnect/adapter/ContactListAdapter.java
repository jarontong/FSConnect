package com.jaron.fsconnect.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jaron.fsconnect.R;
import com.jaron.fsconnect.model.FriendProfile;
import com.jaron.fsconnect.model.ProfileSummary;
import com.jaron.fsconnect.utils.ImageLoader;
import com.jaron.fsconnect.utils.TimeUtil;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 分组列表Adapters
 */
public class ContactListAdapter extends ArrayAdapter<FriendProfile> {
    private int resourceId;
    private Context mContext;
    private boolean selectable;
    private ViewHolder viewHolder;
    private View view;
    private List<FriendProfile> mMembers;

    public ContactListAdapter(Context context, int resource,  List<FriendProfile> objects, boolean selectable){
        super(context, resource, objects);
        mContext = context;
        mMembers = objects;
        this.selectable = selectable;
        resourceId=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null){
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }else{
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.avatar = (CircleImageView) view.findViewById(R.id.avatar);
            viewHolder.tag = (ImageView) view.findViewById(R.id.chooseTag);
            view.setTag(viewHolder);
        }
        final FriendProfile data =  getItem(position);
        viewHolder.name.setText(data.getName());
        Log.d("data.getAvatarUrl()",data.getAvatarUrl());
            ImageLoader.loadImage(Glide.with(mContext),viewHolder.avatar, data.getAvatarUrl(), R.drawable.head_other);
        viewHolder.tag.setVisibility(selectable? View.VISIBLE : View.GONE);
        viewHolder.tag.setImageResource(data.isSelected()?R.drawable.selected:R.drawable.unselected);
        return view;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    public class ViewHolder{
        public TextView name;
        public CircleImageView avatar;
        public ImageView tag;

    }



}
