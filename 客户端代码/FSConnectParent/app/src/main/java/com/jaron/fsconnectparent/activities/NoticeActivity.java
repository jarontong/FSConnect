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
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jaron.fsconnectparent.AppContext;
import com.jaron.fsconnectparent.R;
import com.jaron.fsconnectparent.account.AccountHelper;
import com.jaron.fsconnectparent.account.activity.LoginActivity;
import com.jaron.fsconnectparent.adapter.NoticeAdapter;
import com.jaron.fsconnectparent.api.FSConnectApi;
import com.jaron.fsconnectparent.app.AppOperator;
import com.jaron.fsconnectparent.base.BaseBackActivity;
import com.jaron.fsconnectparent.base.BaseRecyclerAdapter;
import com.jaron.fsconnectparent.bean.Notice;
import com.jaron.fsconnectparent.bean.ResultBean;
import com.jaron.fsconnectparent.bean.TeacherSubject;
import com.jaron.fsconnectparent.utils.TDevice;
import com.jaron.fsconnectparent.widget.RecyclerRefreshLayout;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by  fei
 * on  16/11/17
 * desc:详情评论列表ui
 */
public class NoticeActivity extends BaseBackActivity implements
        BaseRecyclerAdapter.OnItemLongClickListener{


    @BindView(R.id.ib_navigation_back)
    ImageButton ibNavigationBack;
    @BindView(R.id.add_notice)
    TextView addNotice;
    @BindView(R.id.setting_head_container)
    FrameLayout settingHeadContainer;
    @BindView(R.id.lay_blog_detail_notice)
    RecyclerView mLayNotices;
    @BindView(R.id.lay_refreshLayout)
    RecyclerRefreshLayout mRefreshLayout;

    private NoticeAdapter mNoticeAdapter;

    private List<Notice> mNoticeList;
    private Notice mNotice;
    private int pos;

    private ArrayList<Integer> classIdList;
    private ArrayList<String> classNameList;
    private List<TeacherSubject> teacherSubjectList;

    private int offset;
    public static void show(Activity activity) {
        Intent intent = new Intent(activity, NoticeActivity.class);
        activity.startActivity(intent);
    }

    public static void show(Fragment fragment) {
        Intent intent = new Intent(fragment.getActivity(), NoticeActivity.class);
        fragment.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_notice;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        mRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mLayNotices.setLayoutManager(manager);

        mNoticeAdapter = new NoticeAdapter(this, getImageLoader(), BaseRecyclerAdapter.ONLY_FOOTER);
        mNoticeAdapter.setOnItemLongClickListener(this);
        mNoticeAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, long itemId) {
                pos=position;
                mNotice = mNoticeAdapter.getItem(position);
                NoticeDetailActivity.show(NoticeActivity.this,mNotice);
            }
        });
        mLayNotices.setAdapter(mNoticeAdapter);
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
        FSConnectApi.getNoticesByClass(AccountHelper.getUser().getStudent().getClassId(),offset, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (isDestroy())
                    return;
                mNoticeAdapter.setState(BaseRecyclerAdapter.STATE_LOAD_ERROR, true);
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

                    ResultBean<List<Notice>> resultBean = AppOperator.createGson().fromJson(responseString, getCommentType());

                    if (resultBean.isSuccess()) {
                        mNoticeList = resultBean.getResult();
                        handleData(mNoticeList, clearData);
                    }else {
                        AppContext.showToastShort(resultBean.getMessage());
                    }

                    mNoticeAdapter.setState(mNoticeList == null || mNoticeList.size() < 10 ?
                                    BaseRecyclerAdapter.STATE_NO_MORE : BaseRecyclerAdapter.STATE_LOAD_MORE, true);
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }
            }
        });
    }

    private void handleData(List<Notice> notices, boolean clearData) {
        if (clearData)
            mNoticeAdapter.clear();
        mNoticeAdapter.addAll(notices);
    }

    Type getCommentType() {
        return new TypeToken<ResultBean<List<Notice>>>() {
        }.getType();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ib_navigation_back, R.id.add_notice, R.id.lay_blog_detail_notice, R.id.lay_refreshLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.add_notice:
                /*teacherSubjectList = AccountHelper.getUser().getTeacherSubjectList();
                Log.d("teacherSubjectList",teacherSubjectList.toString());
                classIdList=new ArrayList<Integer>();
                classNameList=new ArrayList<String>();
                int size = teacherSubjectList.size();
                for (int i = 0; i < size; i++) {
                    classIdList.add(teacherSubjectList.get(i).getaClass().getClassId());
                    classNameList.add(teacherSubjectList.get(i).getaClass().getClassName());
                }
                NoticeAddActivity.show(this,classIdList,classNameList);*/
                break;
            case R.id.lay_blog_detail_notice:
                break;
            case R.id.lay_refreshLayout:
                break;
        }
    }

    @Override
    public void onLongClick(int position, long itemId) {

        /*final Notice notice = mNoticeAdapter.getItem(position);
        if (notice == null) return;

        String[] items;
        // if (AccountHelper.getUserId() == (int) comment.getAuthor().getId()) {
        //   items = new String[]{getString(R.string.copy), getString(R.string.delete)};
        //} else {
        items = new String[]{getString(R.string.copy)};
        // }

        DialogHelper.getSelectDialog(this, items, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                switch (i) {
                    case 0:
                        TDevice.copyTextToBoard(HTMLUtil.delHTMLTag(comment.getContent()));
                        break;
                    case 1:
                        // TODO: 2016/11/30 delete comment
                        break;
                    default:
                        break;
                }
            }
        }).show();*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1 && resultCode == 2) {
            Notice notice = (Notice) intent.getSerializableExtra("notice");
            mNoticeList.add(0, notice);
            handleData(mNoticeList, true);
        }
        if (requestCode == 2 && resultCode == 3) {
            boolean changed = intent.getBooleanExtra("changed",false);
            Notice notice = (Notice) intent.getSerializableExtra("notice");
            mNoticeList.set(pos, notice);
            handleData(mNoticeList, true);
        }
    }

}
