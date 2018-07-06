package com.jaron.fsconnectparent.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jaron.fsconnectparent.R;
import com.jaron.fsconnectparent.adapter.ContactListAdapter;
import com.jaron.fsconnectparent.model.FriendProfile;
import com.jaron.fsconnectparent.model.FriendshipInfo;
import com.jaron.fsconnectparent.ui.TemplateTitle;

import java.util.ArrayList;
import java.util.List;

public class ChooseFriendActivity extends Activity {


    private ContactListAdapter mGroupListAdapter;
    private ListView mGroupListView;
    private List<FriendProfile> selectList = new ArrayList<>();
    private List<String> mAlreadySelect = new ArrayList<>();
    private List<FriendProfile> alreadySelectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend);
        TemplateTitle title = (TemplateTitle) findViewById(R.id.chooseTitle);
        List<String> selected = getIntent().getStringArrayListExtra("selected");
        if (selected != null){
            mAlreadySelect.addAll(selected);
        }
        title.setMoreTextAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectList.size() == 0){
                    Toast.makeText(ChooseFriendActivity.this, getString(R.string.choose_need_one), Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putStringArrayListExtra("select", getSelectIds());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        final List<FriendProfile> friends = FriendshipInfo.getInstance().getFriends();
        for (String id : mAlreadySelect) {
            for (FriendProfile profile : friends) {
                if (id.equals(profile.getIdentify())) {
                    profile.setIsSelected(true);
                    alreadySelectList.add(profile);
                }
            }
        }
        mGroupListView = (ListView) findViewById(R.id.groupList);
        mGroupListAdapter = new ContactListAdapter(this, R.layout.item_childmember,friends, true);
        mGroupListView.setAdapter(mGroupListAdapter);
        mGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FriendProfile profile = friends.get(position);
                if (alreadySelectList.contains(profile)) return;
                onSelect(profile);
                mGroupListAdapter.notifyDataSetChanged();
            }
        });
        mGroupListAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        for (FriendProfile item : selectList){
            item.setIsSelected(false);
        }
        for (FriendProfile item : alreadySelectList){
            item.setIsSelected(false);
        }
    }

    private void onSelect(FriendProfile profile){
        if (!profile.isSelected()){
            selectList.add(profile);
        }else{
            selectList.remove(profile);
        }
        profile.setIsSelected(!profile.isSelected());
    }

    private ArrayList<String> getSelectIds(){
        ArrayList<String> result = new ArrayList<>();
        for (FriendProfile item : selectList){
            result.add(item.getIdentify());
        }
        return result;
    }
}
