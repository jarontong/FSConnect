package com.jaron.fsconnectparent.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jaron.fsconnectparent.R;
import com.jaron.fsconnectparent.activities.GroupListActivity;
import com.jaron.fsconnectparent.activities.SearchFriendActivity;
import com.jaron.fsconnectparent.adapter.ContactListAdapter;
import com.jaron.fsconnectparent.base.BaseFragment;
import com.jaron.fsconnectparent.model.FriendProfile;
import com.jaron.fsconnectparent.model.FriendshipInfo;
import com.jaron.fsconnectparent.model.GroupInfo;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Jaron on 2018/5/25.
 */

public class ContactsFragment extends BaseFragment implements Observer {
    Unbinder unbinder;

    private static final String TAG = "ContactsFragment";
    //@BindView(R.id.liv_letters)
    // LetterIndexView livLetters;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.groupList)
    ListView mGroupListView;
    private Dialog inviteDialog;
    private TextView addFriend, managerGroup, addGroup;

    private ContactListAdapter mGroupListAdapter;
     List<FriendProfile> friends;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    protected void initData() {
        super.initData();
        friends = FriendshipInfo.getInstance().getFriends();
        mGroupListAdapter = new ContactListAdapter(getActivity(), R.layout.item_childmember, friends,false);
        mGroupListView.setAdapter(mGroupListAdapter);
        mGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                friends.get(position).onClick(getActivity());
                return;
            }
        });
        FriendshipInfo.getInstance().addObserver(this);
        mGroupListAdapter.notifyDataSetChanged();
    }

    /*private Map<String, Integer> getLetterMap(List<FriendProfile> friendProfileList) {
        Map<String, Integer> letterMap = new HashMap<>();
        if (friendProfileList == null || friendProfileList.size() == 0) {
            return letterMap;
        }
        String letter = friendProfileList.get(0).getNameHeaderLetter();
        letterMap.put(letter, 0);
        for (int i = 1; i < friendProfileList.size(); i++) {
            if (friendProfileList.get(i).getNameHeaderLetter().equals(letter)) {
                continue;
            }
            letter = friendProfileList.get(i).getNameHeaderLetter();
            letterMap.put(letter, i);
        }
        return letterMap;
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mGroupListAdapter.notifyDataSetChanged();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof FriendshipInfo){
            mGroupListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.add_more, R.id.btnPublicGroup})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_more:
                Intent intent = new Intent(getActivity(), SearchFriendActivity.class);
                getActivity().startActivity(intent);

                break;
            case R.id.btnPublicGroup:
                showGroups(GroupInfo.chatRoom);
                break;
        }
    }

    private void showGroups(String type){
        Intent intent = new Intent(getActivity(), GroupListActivity.class);
        intent.putExtra("type", type);
        getActivity().startActivity(intent);
    }


}
