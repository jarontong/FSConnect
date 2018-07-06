package com.jaron.fsconnect.activities;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jaron.fsconnect.utils.ImageLoader;
import com.tencent.TIMFriendResult;
import com.tencent.TIMFriendStatus;
import com.tencent.TIMValueCallBack;
import com.jaron.fsconnect.presentation.presenter.FriendshipManagerPresenter;
import com.jaron.fsconnect.presentation.viewfeatures.FriendshipManageView;
import com.jaron.fsconnect.R;
import com.jaron.fsconnect.model.FriendshipInfo;
import com.jaron.fsconnect.ui.LineControllerView;
import com.jaron.fsconnect.ui.ListPickerDialog;
import com.jaron.fsconnect.ui.NotifyDialog;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 申请添加好友界面
 */
public class AddFriendActivity extends FragmentActivity implements View.OnClickListener, FriendshipManageView {

    private CircleImageView avatar;
    private TextView tvName, btnAdd;
    private EditText editRemark, editMessage;
    private LineControllerView idField,nickname;
    private FriendshipManagerPresenter presenter;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        avatar=(CircleImageView)findViewById(R.id.avatar);
        tvName = (TextView) findViewById(R.id.name);
        idField = (LineControllerView) findViewById(R.id.id);
        nickname=(LineControllerView)findViewById(R.id.nickname);
        ImageLoader.loadImage(Glide.with(this),avatar, getIntent().getStringExtra("avatar"), R.drawable.head_other);
        id = getIntent().getStringExtra("id");
        tvName.setText(getIntent().getStringExtra("name"));
        nickname.setContent(getIntent().getStringExtra("nickname"));
        idField.setContent(id.substring(id.length()-11));
        btnAdd = (TextView) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        editMessage = (EditText) findViewById(R.id.editMessage);
        editRemark = (EditText) findViewById(R.id.editNickname);
        presenter = new FriendshipManagerPresenter(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAdd) {
            presenter.addFriend(id, editRemark.getText().toString(), editMessage.getText().toString());
        }
    }

    @Override
    public void onAddFriend(TIMFriendStatus status) {
        switch (status){
            case TIM_ADD_FRIEND_STATUS_PENDING:
                Toast.makeText(this, getResources().getString(R.string.add_friend_succeed), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case TIM_FRIEND_STATUS_SUCC:
                Toast.makeText(this, getResources().getString(R.string.add_friend_added), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case TIM_ADD_FRIEND_STATUS_FRIEND_SIDE_FORBID_ADD:
                Toast.makeText(this, getResources().getString(R.string.add_friend_refuse_all), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case TIM_ADD_FRIEND_STATUS_IN_OTHER_SIDE_BLACK_LIST:
                Toast.makeText(this, getResources().getString(R.string.add_friend_to_blacklist), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case TIM_ADD_FRIEND_STATUS_IN_SELF_BLACK_LIST:
                NotifyDialog dialog = new NotifyDialog();
                dialog.show(getString(R.string.add_friend_del_black_list), getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FriendshipManagerPresenter.delBlackList(Collections.singletonList(id), new TIMValueCallBack<List<TIMFriendResult>>() {
                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(AddFriendActivity.this, getResources().getString(R.string.add_friend_del_black_err), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                                Toast.makeText(AddFriendActivity.this, getResources().getString(R.string.add_friend_del_black_succ), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                break;
            default:
                Toast.makeText(this, getResources().getString(R.string.add_friend_error), Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onDelFriend(TIMFriendStatus status) {

    }


}
