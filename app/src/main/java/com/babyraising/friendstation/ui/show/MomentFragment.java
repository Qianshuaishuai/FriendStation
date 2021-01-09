package com.babyraising.friendstation.ui.show;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.MomentAdapter;
import com.babyraising.friendstation.base.BaseFragment;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.MomentDetailBean;
import com.babyraising.friendstation.bean.ScoreRecordBean;
import com.babyraising.friendstation.bean.TimSendBean;
import com.babyraising.friendstation.bean.TimSendBodyBean;
import com.babyraising.friendstation.bean.TimSendMsgContentBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.request.FollowRequest;
import com.babyraising.friendstation.request.LikeDetailRequest;
import com.babyraising.friendstation.request.LikeRequest;
import com.babyraising.friendstation.request.UpdateAlbumRequest;
import com.babyraising.friendstation.response.MomentResponse;
import com.babyraising.friendstation.response.ScoreRecordResponse;
import com.babyraising.friendstation.response.UploadPicResponse;
import com.babyraising.friendstation.ui.main.ChatActivity;
import com.babyraising.friendstation.ui.main.MomentSendActivity;
import com.babyraising.friendstation.ui.main.PersonAuthActivity;
import com.babyraising.friendstation.ui.main.PersonInfoActivity;
import com.babyraising.friendstation.ui.main.VoiceSignActivity;
import com.babyraising.friendstation.util.GenerateTestUserSig;
import com.babyraising.friendstation.util.RandomUtil;
import com.babyraising.friendstation.util.T;
import com.babyraising.friendstation.util.WxShareUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@ContentView(R.layout.fragment_moment)
public class MomentFragment extends BaseFragment {

    private String showContent = "";
    private String showImgfilePath = "";
    private AlertDialog authDialog;

    private List<String> commonWordList;

    private int selectType = 1;
    private MomentAdapter adapter;
    private List<MomentDetailBean> list;

    @ViewInject(R.id.list)
    private RecyclerView recycleList;

    @ViewInject(R.id.main_layout)
    private RelativeLayout mainLayout;

    @ViewInject(R.id.type_tv1)
    private TextView typeTv1;

    @ViewInject(R.id.type_tv2)
    private TextView typeTv2;

    @ViewInject(R.id.type_tv3)
    private TextView typeTv3;

    @ViewInject(R.id.type_view1)
    private View typeV1;

    @ViewInject(R.id.type_view2)
    private View typeV2;

    @ViewInject(R.id.type_view3)
    private View typeV3;

    @ViewInject(R.id.layout_share_all)
    private RelativeLayout shareAllLayout;

    @ViewInject(R.id.anim_show)
    private ImageView animShow;

    @Event(R.id.layout_share_1)
    private void share1Click(View view) {
        try {
            if (TextUtils.isEmpty(showImgfilePath) || showImgfilePath.indexOf("http") == -1) {
                WxShareUtils.descShare(getActivity(), showContent, 1);
            } else {
                WxShareUtils.imageShare(getActivity(), showImgfilePath, showContent, 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            T.s("获取分享信息失败");
        }
        shareAllLayout.setVisibility(View.GONE);
    }

    @Event(R.id.layout_share_2)
    private void share2Click(View view) {
        try {
            if (TextUtils.isEmpty(showImgfilePath) || showImgfilePath.indexOf("http") == -1) {
                WxShareUtils.descShare(getActivity(), showContent, 2);
            } else {
                WxShareUtils.imageShare(getActivity(), showImgfilePath, showContent, 2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            T.s("获取分享信息失败");
        }
        shareAllLayout.setVisibility(View.GONE);
    }

    @Event(R.id.add)
    private void addClick(View view) {
        Intent intent = new Intent(getContext(), MomentSendActivity.class);
        startActivity(intent);
    }

    @Event(R.id.type_tv1)
    private void typeTv1Click(View view) {
        if (selectType != 1) {
            selectType = 1;
            typeTv1.setTextColor(getActivity().getResources().getColor(R.color.colorShowSelected));
            typeTv2.setTextColor(getActivity().getResources().getColor(R.color.colorShowNormal));
            typeTv3.setTextColor(getActivity().getResources().getColor(R.color.colorShowNormal));
            typeTv1.setTextSize(16);
            typeTv2.setTextSize(15);
            typeTv3.setTextSize(15);
            typeV1.setVisibility(View.VISIBLE);
            typeV2.setVisibility(View.GONE);
            typeV3.setVisibility(View.GONE);
        }
    }

    @Event(R.id.type_tv2)
    private void typeTv2Click(View view) {
        if (selectType != 2) {
            selectType = 2;
            typeTv1.setTextColor(getActivity().getResources().getColor(R.color.colorShowNormal));
            typeTv2.setTextColor(getActivity().getResources().getColor(R.color.colorShowSelected));
            typeTv3.setTextColor(getActivity().getResources().getColor(R.color.colorShowNormal));
            typeTv1.setTextSize(15);
            typeTv2.setTextSize(16);
            typeTv3.setTextSize(15);
            typeV1.setVisibility(View.GONE);
            typeV2.setVisibility(View.VISIBLE);
            typeV3.setVisibility(View.GONE);
        }
    }

    @Event(R.id.type_tv3)
    private void typeTv3Click(View view) {
        if (selectType != 3) {
            selectType = 3;
            typeTv1.setTextColor(getActivity().getResources().getColor(R.color.colorShowNormal));
            typeTv2.setTextColor(getActivity().getResources().getColor(R.color.colorShowNormal));
            typeTv3.setTextColor(getActivity().getResources().getColor(R.color.colorShowSelected));
            typeTv1.setTextSize(15);
            typeTv2.setTextSize(15);
            typeTv3.setTextSize(16);
            typeV1.setVisibility(View.GONE);
            typeV2.setVisibility(View.GONE);
            typeV3.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initData();
        initAuthTip();
    }

    private void initData() {
        commonWordList = ((FriendStationApplication) getActivity().getApplication()).getCommonWordData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getMomentList();
    }

    private void showAnimation() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.success_show);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animShow.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animShow.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animShow.setVisibility(View.VISIBLE);
        animShow.startAnimation(animation);
        animation.setRepeatCount(1);
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

    private void initView() {
        typeTv1.setTextColor(getActivity().getResources().getColor(R.color.colorShowSelected));
        typeTv2.setTextColor(getActivity().getResources().getColor(R.color.colorShowNormal));
        typeTv3.setTextColor(getActivity().getResources().getColor(R.color.colorShowNormal));
        typeTv1.setTextSize(16);
        typeTv2.setTextSize(15);
        typeTv3.setTextSize(15);
        typeV1.setVisibility(View.VISIBLE);
        typeV2.setVisibility(View.GONE);
        typeV3.setVisibility(View.GONE);

        list = new ArrayList<>();
        adapter = new MomentAdapter(this, list);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recycleList.setAdapter(adapter);
        recycleList.setLayoutManager(manager);
    }

    public void goToPersonInfo(int userId) {
        Intent intent = new Intent(getActivity(), PersonInfoActivity.class);
        intent.putExtra("mode", 1);
        intent.putExtra("user-id", userId);
        startActivity(intent);
    }

    public void shareContent(int position) {
        showContent = list.get(position).getContent();
        showImgfilePath = list.get(position).getPicUrl1();
        shareAllLayout.setVisibility(View.VISIBLE);
    }

    public void followUser(int id) {
        FollowRequest request = new FollowRequest();
        request.setFollowId(id);
        CommonLoginBean bean = ((FriendStationApplication) getActivity().getApplication()).getUserInfo();
        Gson gson = new Gson();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_USERFOLLOW_SAVE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                System.out.println("followUser:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("关注成功");
                        getMomentList();

                        break;
                    default:
                        T.s(response.getMsg());
                        break;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("错误处理:" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void cancelFollowUser(int id) {
        FollowRequest request = new FollowRequest();
        request.setFollowId(id);
        CommonLoginBean bean = ((FriendStationApplication) getActivity().getApplication()).getUserInfo();
        Gson gson = new Gson();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_USERFOLLOW_DELETE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                System.out.println("cancelFollowUser:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("取消关注成功");
                        getMomentList();

                        break;
                    default:
                        T.s(response.getMsg());
                        break;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("错误处理:" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void likeUser(int momentId) {
        LikeRequest request = new LikeRequest();
        LikeDetailRequest request1 = new LikeDetailRequest();
        request1.setMomentId(momentId);
        request.setMomentLikeSaveV0(request1);
        CommonLoginBean bean = ((FriendStationApplication) getActivity().getApplication()).getUserInfo();
        Gson gson = new Gson();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_MOMENTLIKE_SAVE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request1));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                System.out.println("likeUser:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("点赞成功");
                        getMomentList();
                        ((FriendStationApplication) getActivity().getApplication()).isUpdateDoTask(getActivity(), mainLayout, 8);
                        break;
                    default:
                        T.s(response.getMsg());
                        break;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("错误处理:" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void cancelLikeUser(int momentId) {
        LikeRequest request = new LikeRequest();
        LikeDetailRequest request1 = new LikeDetailRequest();
        request1.setMomentId(momentId);
        request.setMomentLikeSaveV0(request1);
        CommonLoginBean bean = ((FriendStationApplication) getActivity().getApplication()).getUserInfo();
        Gson gson = new Gson();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_MOMENTLIKE_DELETE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request1));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                System.out.println("cancelLikeUser:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("取消点赞成功");
                        getMomentList();

                        break;
                    default:
                        T.s(response.getMsg());
                        break;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("错误处理:" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public void goToChat(int position) {
        UserAllInfoBean userBean = ((FriendStationApplication) getActivity().getApplication()).getUserAllInfo();
        if (userBean == null || userBean.getId() == 0) {
            T.s("你当前的用户信息获取有误，请重新登录");
            return;
        }

        if (TextUtils.isEmpty(userBean.getRecordSign()) && !userBean.getStatusCert().equals("PASS")) {
            authDialog.show();
            return;
        }

//        Intent intent = new Intent(getActivity(), ChatActivity.class);
//        intent.putExtra("chat-user-id", list.get(position).getUserId());
//        startActivity(intent);
        adminSendMessage(userBean.getId(), list.get(position).getUserId());
        T.s("搭讪成功");
        showAnimation();
    }

    private void getMomentList() {
        CommonLoginBean bean = ((FriendStationApplication) getActivity().getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_MOMENT);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                MomentResponse response = gson.fromJson(result, MomentResponse.class);
                System.out.println("MomentRecord:" + result);
                switch (response.getCode()) {
                    case 200:
                        list.clear();
                        List<MomentDetailBean> newList = response.getData().getRecords();
                        for (int l = 0; l < newList.size(); l++) {
                            list.add(newList.get(l));
                        }
                        adapter.notifyDataSetChanged();
                        break;
                    default:

                        break;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("错误处理:" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initAuthTip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_person_auth, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        authDialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        final Button left = (Button) view.findViewById(R.id.cancel);
        final Button right = (Button) view.findViewById(R.id.sure);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPersonInfo();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPersonAuth();
            }
        });

    }

    private void goToPersonInfo() {
        Intent intent = new Intent(getActivity(), VoiceSignActivity.class);
        startActivity(intent);
        if (authDialog.isShowing()) {
            authDialog.cancel();
        }
    }

    private void goToPersonAuth() {
        Intent intent = new Intent(getActivity(), PersonAuthActivity.class);
        startActivity(intent);

        if (authDialog.isShowing()) {
            authDialog.cancel();
        }
    }

    private void adminSendMessage(int sendId, int receiveId) {
        if (sendId == 0 || receiveId == 0) {
            System.out.println("接收方或发送方id为0");
            return;
        }

        if (sendId == receiveId) {
            System.out.println("接收方和发送方相同");
            return;
        }

        System.out.println("sendId:" + sendId + ", receiveId:" + receiveId);
        Gson gson = new Gson();
        Random random = new Random();
        int n = random.nextInt(commonWordList.size());
        String word = "";
        int index = n - 1;
        if (index >= 0) {
            word = commonWordList.get(index);
        } else {
            word = commonWordList.get(0);
        }
        TimSendMsgContentBean contentBean = new TimSendMsgContentBean();
        contentBean.setText(word);
        TimSendBodyBean bodyBean = new TimSendBodyBean();
        bodyBean.setMsgContent(contentBean);
        bodyBean.setMsgType("TIMTextElem");
        List<TimSendBodyBean> bodyList = new ArrayList<>();
        bodyList.add(bodyBean);
        TimSendBean bean = new TimSendBean();
        bean.setFrom_Account(String.valueOf(sendId));
        bean.setTo_Account(String.valueOf(receiveId));
        bean.setMsgBody(bodyList);
        bean.setSyncOtherMachine(1);
        bean.setMsgRandom(RandomUtil.getRandomInt());
        int nowTime = (int) new Date().getTime();
        if (nowTime < 0) {
            nowTime = Math.abs(nowTime);
        }
        bean.setMsgTimeStamp(nowTime);
        String userSig = GenerateTestUserSig.genTestUserSig(Constant.TIM_ADMIN_ACCOUNT);
        String url = Constant.URL_TIM_SENDMSG + "?sdkappid=" + Constant.TIM_SDK_APPID + "&identifier=" + Constant.TIM_ADMIN_ACCOUNT + "&usersig=" + userSig + "&random=" + RandomUtil.getRandom() + "&contenttype=json";
        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);
        params.setBodyContent(gson.toJson(bean));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("adminSendMessage:" + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("错误处理:" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
