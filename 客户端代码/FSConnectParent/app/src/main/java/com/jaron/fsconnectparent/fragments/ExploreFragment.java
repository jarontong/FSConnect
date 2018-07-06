package com.jaron.fsconnectparent.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.jaron.fsconnectparent.AppContext;
import com.jaron.fsconnectparent.R;
import com.jaron.fsconnectparent.account.AccountHelper;
import com.jaron.fsconnectparent.activities.HomeworkActivity;
import com.jaron.fsconnectparent.activities.MomentAddActivity;
import com.jaron.fsconnectparent.activities.NoticeActivity;
import com.jaron.fsconnectparent.adapter.MomentAdapter;
import com.jaron.fsconnectparent.api.FSConnectApi;
import com.jaron.fsconnectparent.app.AppOperator;
import com.jaron.fsconnectparent.base.BaseFragment;
import com.jaron.fsconnectparent.base.BaseRecyclerAdapter;
import com.jaron.fsconnectparent.bean.Moment;
import com.jaron.fsconnectparent.bean.ResultBean;
import com.jaron.fsconnectparent.model.FriendProfile;
import com.jaron.fsconnectparent.model.FriendshipInfo;
import com.jaron.fsconnectparent.widget.RecyclerRefreshLayout;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Jaron on 2018/5/25.
 */

public class ExploreFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.lay_blog_detail_dongtai)
    RecyclerView mLayMoment;
    @BindView(R.id.lay_refreshLayout)
    RecyclerRefreshLayout mRefreshLayout;

    private MomentAdapter momentAdapter;
    private int offset;
    List<Integer> userIdList;
    List<FriendProfile> friends;
    List<Moment> momentList;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_explore;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.notice, R.id.homework})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.notice:
                NoticeActivity.show(this.getActivity());
                break;
            case R.id.homework:
                HomeworkActivity.show(this.getActivity());
                break;

        }
    }

    @OnClick(R.id.add_moment)
    public void onViewClicked() {
        MomentAddActivity.show(getActivity());
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mLayMoment.setLayoutManager(manager);

        momentAdapter = new MomentAdapter(getContext(), getActivity(), BaseRecyclerAdapter.ONLY_FOOTER, getImgLoader());
        //mCommentAdapter.setOnItemLongClickListener(this);

        mLayMoment.setAdapter(momentAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        friends = FriendshipInfo.getInstance().getFriends();
        userIdList=new ArrayList<Integer>();
        for(FriendProfile friendProfile:friends){
            if(!friendProfile.getSelfSignature().equals(""))
            userIdList.add(Integer.valueOf(friendProfile.getSelfSignature()));
        }
        userIdList.add(AccountHelper.getUserId());
        offset=0;
        mRefreshLayout.setSuperRefreshLayoutListener(new RecyclerRefreshLayout.SuperRefreshLayoutListener() {
            @Override
            public void onRefreshing() {
                getData(userIdList,true,0);
                offset=0;
            }

            @Override
            public void onLoadMore() {
                offset+=10;
                getData(userIdList,false,offset);
            }
        });

        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //第一次请求初始化数据
                getData(userIdList,true,0);

            }
        });
    }

    private void getData(List<Integer> userIdList,final boolean clearData ,int offset) {
        FSConnectApi.getMoment(userIdList,offset, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("statusCode",String.valueOf(statusCode));
                throwable.printStackTrace();
                momentAdapter.setState(BaseRecyclerAdapter.STATE_LOAD_ERROR, true);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mRefreshLayout.onComplete();
            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {

                    ResultBean<List<Moment>> resultBean = AppOperator.createGson().fromJson(responseString, getCommentType());

                    if (resultBean.isSuccess()) {
                        momentList = resultBean.getResult();
                        handleData(momentList, clearData);
                    }else {
                        AppContext.showToastShort(resultBean.getMessage());
                    }

                    momentAdapter.setState(momentList == null || momentList.size() < 10 ?
                            BaseRecyclerAdapter.STATE_NO_MORE : BaseRecyclerAdapter.STATE_LOAD_MORE, true);
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }
            }
        });
    }

    Type getCommentType() {
        return new TypeToken<ResultBean<List<Moment>>>() {
        }.getType();
    }

    private void handleData(List<Moment> momentList, boolean clearData) {
        if (clearData)
            momentAdapter.clear();
        momentAdapter.addAll(momentList);
    }
}
