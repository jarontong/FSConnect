package com.jaron.fsconnectparent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.jaron.fsconnectparent.activities.DialogActivity;
import com.jaron.fsconnectparent.adapter.SectionsPagerAdapter;
import com.jaron.fsconnectparent.base.BaseActivity;
import com.jaron.fsconnectparent.fragments.ContactsFragment;
import com.jaron.fsconnectparent.fragments.ExploreFragment;
import com.jaron.fsconnectparent.fragments.MessageFragment;
import com.jaron.fsconnectparent.fragments.UserInfoFragment;
import com.jaron.fsconnectparent.model.FriendshipInfo;
import com.jaron.fsconnectparent.model.GroupInfo;
import com.jaron.fsconnectparent.model.UserInfo;
import com.jaron.fsconnectparent.presentation.event.MessageEvent;
import com.jaron.fsconnectparent.tlslibrary.service.TlsBusiness;
import com.jaron.fsconnectparent.ui.NotifyDialog;
import com.tencent.TIMManager;
import com.tencent.TIMUserStatusListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Jaron on 2017/5/15.
 */

public class MainActivity extends BaseActivity implements BottomNavigationBar
        .OnTabSelectedListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;

    private List<Fragment> fragments;

    private static final String TAG = "MainActivity";

    @Override
    protected int getContentView() {
        return R.layout.activity_nav;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MessageEvent.getInstance().clear();
        FriendshipInfo.getInstance().clear();
        GroupInfo.getInstance().clear();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_selected, R.string.title_message)
                        .setInactiveIconResource(R.drawable.ic_home_normal))
                .addItem(new BottomNavigationItem(R.drawable.ic_course_selected, R.string.title_contact)
                        .setInactiveIconResource(R.drawable.ic_course_normal))
                .addItem(new BottomNavigationItem(R.drawable.ic_explore_selected, R.string.title_explore)
                        .setInactiveIconResource(R.drawable.ic_explore_normal))
                .addItem(new BottomNavigationItem(R.drawable.ic_account_selected, R.string.title_account)
                        .setInactiveIconResource(R.drawable.ic_account_normal))
                .initialise();

        fragments = new ArrayList<Fragment>();
        fragments.add(new MessageFragment());
        fragments.add(new ContactsFragment());
        fragments.add(new ExploreFragment());
        fragments.add(new UserInfoFragment());

        viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0);
    }

    @Override
    protected void initData() {
        super.initData();
        //互踢下线逻辑
        TIMManager.getInstance().setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                Log.d(TAG, "receive force offline message");
                Intent intent = new Intent(MainActivity.this, DialogActivity.class);
                startActivity(intent);
            }

            @Override
            public void onUserSigExpired() {
                //票据过期，需要重新登录
                new NotifyDialog().show(getString(R.string.tls_expire), getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
            }
        });
    }

    public void logout(){
        TlsBusiness.logout(UserInfo.getInstance().getId());
        UserInfo.getInstance().setId(null);
        MessageEvent.getInstance().clear();
        FriendshipInfo.getInstance().clear();
        GroupInfo.getInstance().clear();
        Intent intent = new Intent(this,LaunchActivity.class);
        finish();
        startActivity(intent);

    }

   /* public void setMsgUnread(boolean noUnread){
        msgUnread.setVisibility(noUnread? View.GONE:View.VISIBLE);
    }*/

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        bottomNavigationBar.selectTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onTabSelected(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {}

    @Override
    public void onTabReselected(int position) {}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
    }
}
