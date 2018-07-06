package com.jaron.fsconnectparent.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.reflect.TypeToken;
import com.jaron.fsconnectparent.AppContext;
import com.jaron.fsconnectparent.R;
import com.jaron.fsconnectparent.account.AccountHelper;
import com.jaron.fsconnectparent.account.activity.LoginActivity;
import com.jaron.fsconnectparent.adapter.HomeworkAdapter;
import com.jaron.fsconnectparent.api.FSConnectApi;
import com.jaron.fsconnectparent.app.AppOperator;
import com.jaron.fsconnectparent.base.BaseBackActivity;
import com.jaron.fsconnectparent.base.BaseRecyclerAdapter;
import com.jaron.fsconnectparent.bean.Homework;
import com.jaron.fsconnectparent.bean.ResultBean;
import com.jaron.fsconnectparent.utils.TDevice;
import com.jaron.fsconnectparent.widget.RecyclerRefreshLayout;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Jaron on 2018/6/8.
 */

public class HomeworkActivity extends BaseBackActivity implements
        BaseRecyclerAdapter.OnItemLongClickListener{
    @BindView(R.id.setting_head_container)
    FrameLayout settingHeadContainer;
    @BindView(R.id.lay_blog_detail_homework)
    RecyclerView mLayHomework;
    @BindView(R.id.lay_refreshLayout)
    RecyclerRefreshLayout mRefreshLayout;

    private HomeworkAdapter mHomeworkAdapter;

    private List<Homework> mHomeworkList;
    private Homework mHomework;
    private int pos;

    private int offset;

    public static void show(Activity activity) {
        Intent intent = new Intent(activity, HomeworkActivity.class);
        activity.startActivity(intent);
    }

    public static void show(Fragment fragment) {
        Intent intent = new Intent(fragment.getActivity(), HomeworkActivity.class);
        fragment.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_homework;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mLayHomework.setLayoutManager(manager);

        mHomeworkAdapter = new HomeworkAdapter(this, getImageLoader(), BaseRecyclerAdapter.ONLY_FOOTER);
        //mCommentAdapter.setOnItemLongClickListener(this);
        mHomeworkAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, long itemId) {
                pos=position;
                mHomework = mHomeworkAdapter.getItem(position);
                HomeworkDetailActivity.show(HomeworkActivity.this,mHomework);
            }
        });
        mLayHomework.setAdapter(mHomeworkAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        offset=0;
        mRefreshLayout.setSuperRefreshLayoutListener(new RecyclerRefreshLayout.SuperRefreshLayoutListener() {
            @Override
            public void onRefreshing() {
                getData(true,0);
                offset=0;
            }

            @Override
            public void onLoadMore() {
                offset+=10;
                getData(false,offset);
            }
        });

        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //第一次请求初始化数据
                getData(true,0);
            }
        });
    }

    /**
     * 检查当前数据,并检查网络状况
     *
     * @return 返回当前登录用户, 未登录或者未通过检查返回0
     */
    private long requestCheck() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort("当前没有可用的网络链接");
            return 0;
        }
        if (!AccountHelper.isLogin()) {
            LoginActivity.show(this);
            return 0;
        }
        // 返回当前登录用户ID
        return AccountHelper.getUserId();
    }


    private void getData(final boolean clearData ,int offset) {
        FSConnectApi.getHomeworkByClass(AccountHelper.getUser().getStudent().getClassId(),offset, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (isDestroy())
                    return;
                mHomeworkAdapter.setState(BaseRecyclerAdapter.STATE_LOAD_ERROR, true);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (isDestroy())
                    return;
                mRefreshLayout.onComplete();
            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (isDestroy())
                    return;
                try {

                    ResultBean<List<Homework>> resultBean = AppOperator.createGson().fromJson(responseString, getCommentType());

                    if (resultBean.isSuccess()) {
                        mHomeworkList = resultBean.getResult();
                        handleData(mHomeworkList, clearData);
                    }
                    else {
                        AppContext.showToastShort(resultBean.getMessage());
                    }

                    mHomeworkAdapter.setState(mHomeworkList== null || mHomeworkList.size() < 10 ?
                                    BaseRecyclerAdapter.STATE_NO_MORE : BaseRecyclerAdapter.STATE_LOAD_MORE, true);
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }
            }
        });
    }

    private void handleData(List<Homework> notices, boolean clearData) {
        if (clearData)
            mHomeworkAdapter.clear();
        mHomeworkAdapter.addAll(notices);
    }

    Type getCommentType() {
        return new TypeToken<ResultBean<List<Homework>>>() {
        }.getType();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ib_navigation_back, R.id.add_homework})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.add_homework:
                //HomeworkAddActivity.show(this);
                break;
        }
    }

    @Override
    public void onLongClick(int position, long itemId) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1 && resultCode == 2) {
            Homework homework = (Homework) intent.getSerializableExtra("homework");
            mHomeworkList.add(0, homework);
            handleData(mHomeworkList, true);
        }
        if (requestCode == 2 && resultCode == 3) {
            boolean changed = intent.getBooleanExtra("changed",false);
            Homework homework = (Homework) intent.getSerializableExtra("homework");
            mHomeworkList.set(pos, homework);
            handleData(mHomeworkList, true);
        }
    }
}
