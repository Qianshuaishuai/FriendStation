package com.babyraising.friendstation.ui.show;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseFragment;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.request.SetPasswordRequest;
import com.babyraising.friendstation.response.UmsUpdatePasswordResponse;
import com.babyraising.friendstation.ui.main.IntegralMallActivity;
import com.babyraising.friendstation.ui.main.InviteFriendActivity;
import com.babyraising.friendstation.ui.main.InviteFriendDetailActivity;
import com.babyraising.friendstation.ui.main.LookMeRecordActivity;
import com.babyraising.friendstation.ui.main.MyEarningActivity;
import com.babyraising.friendstation.ui.main.MyInfoActivity;
import com.babyraising.friendstation.ui.main.PersonInfoActivity;
import com.babyraising.friendstation.ui.main.RechargeActivity;
import com.babyraising.friendstation.ui.main.SettingActivity;
import com.babyraising.friendstation.ui.main.TaskActivity;
import com.babyraising.friendstation.ui.other.HelpActivity;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.fragment_person)
public class PersonFragment extends BaseFragment {

    private UserAllInfoBean userInfoBean;

    @Event(R.id.layout_invite)
    private void inviteLayoutClick(View view) {
        Intent intent = new Intent(getActivity(), InviteFriendDetailActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_look_me)
    private void lookmeLayoutClick(View view) {
        Intent intent = new Intent(getActivity(), LookMeRecordActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_help)
    private void helpLayoutClick(View view) {
        Intent intent = new Intent(getActivity(), HelpActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_setting)
    private void settingLayoutClick(View view) {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_task)
    private void taskLayoutClick(View view) {
        Intent intent = new Intent(getActivity(), TaskActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_recharge)
    private void rechargeLayoutClick(View view) {
        Intent intent = new Intent(getActivity(), RechargeActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_income)
    private void incomeLayoutClick(View view) {
        Intent intent = new Intent(getActivity(), MyEarningActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_person)
    private void personLayoutClick(View view) {
        Intent intent = new Intent(getActivity(), PersonInfoActivity.class);
        startActivity(intent);
    }


    @ViewInject(R.id.name)
    private TextView name;

    @ViewInject(R.id.number)
    private TextView number;

    @ViewInject(R.id.info)
    private TextView info;

    @ViewInject(R.id.head)
    private ImageView head;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        userInfoBean = ((FriendStationApplication) getActivity().getApplication()).getUserAllInfo();
        ImageOptions options = new ImageOptions.Builder().
                setRadius(DensityUtil.dip2px(8))
                .setCrop(true).build();

        if (!TextUtils.isEmpty(userInfoBean.getAvatar())) {
            x.image().bind(head, userInfoBean.getAvatar(), options);
        }
        if (!TextUtils.isEmpty(userInfoBean.getNickname())) {
            name.setText(userInfoBean.getNickname());
        }

        if (!TextUtils.isEmpty(userInfoBean.getNickname())) {
            name.setText(userInfoBean.getNickname());
        }

        if (!TextUtils.isEmpty(userInfoBean.getMobile())) {
            number.setText("号码 | " + userInfoBean.getMobile());
        }

        info.setText("好友 " + userInfoBean.getUserCount().getNumFriends() + " | 关注 " + userInfoBean.getUserCount().getNumFollow() + " | 粉丝 " + userInfoBean.getUserCount().getNumFans());
    }
}
