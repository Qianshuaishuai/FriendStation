package com.babyraising.friendstation.ui.show;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.andview.refreshview.XRefreshView;
import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.DialogFirstShowAdapter;
import com.babyraising.friendstation.adapter.FindShowAdapter;
import com.babyraising.friendstation.adapter.LookMeRecordAdapter;
import com.babyraising.friendstation.base.BaseFragment;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.FirstShowBean;
import com.babyraising.friendstation.bean.FriendDetailBean;
import com.babyraising.friendstation.bean.ScoreRecordBean;
import com.babyraising.friendstation.bean.TimOnlineBean;
import com.babyraising.friendstation.bean.TimSendBean;
import com.babyraising.friendstation.bean.TimSendBodyBean;
import com.babyraising.friendstation.bean.TimSendMsgContentBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.bean.UserMainPageBean;
import com.babyraising.friendstation.decoration.FirstShowSpaceItemDecoration;
import com.babyraising.friendstation.decoration.SpaceItemDecoration;
import com.babyraising.friendstation.event.DeleteEvent;
import com.babyraising.friendstation.event.ScrollEvent;
import com.babyraising.friendstation.event.UpdateMessageEvent;
import com.babyraising.friendstation.request.UseCoin2Request;
import com.babyraising.friendstation.request.UseCoinRequest;
import com.babyraising.friendstation.response.FriendResponse;
import com.babyraising.friendstation.response.NoticeResponse;
import com.babyraising.friendstation.response.OnlineTimUserResponse;
import com.babyraising.friendstation.response.ScoreRecordResponse;
import com.babyraising.friendstation.response.UmsUpdatePasswordResponse;
import com.babyraising.friendstation.response.UmsUserAllInfoResponse;
import com.babyraising.friendstation.response.UserMainPageResponse;
import com.babyraising.friendstation.ui.main.ChatActivity;
import com.babyraising.friendstation.ui.main.PersonAuthActivity;
import com.babyraising.friendstation.ui.main.PersonInfoActivity;
import com.babyraising.friendstation.ui.main.RankActivity;
import com.babyraising.friendstation.ui.main.RechargeActivity;
import com.babyraising.friendstation.ui.main.TaskActivity;
import com.babyraising.friendstation.ui.main.VoiceSendActivity;
import com.babyraising.friendstation.ui.main.VoiceSignActivity;
import com.babyraising.friendstation.util.DisplayUtils;
import com.babyraising.friendstation.util.GenerateTestUserSig;
import com.babyraising.friendstation.util.RandomUtil;
import com.babyraising.friendstation.util.T;
import com.babyraising.friendstation.view.DScrollView;
import com.google.gson.Gson;

import net.nightwhistler.htmlspanner.TextUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.LogRecord;

@ContentView(R.layout.fragment_find)
public class FindFragment extends BaseFragment {

    private int selectType = 1;
    private FindShowAdapter adapter;
    private DialogFirstShowAdapter showAdapter;
    private List<FriendDetailBean> mlist;
    private int lastVisibleItem = 0;
    private LinearLayoutManager manager;

    private int pageNum = 0;

    private boolean isFirstShow = true;

    private AlertDialog loadingDialog;

    private List<String> commonWordList;

    private AlertDialog findDialog;

    private int isSelect = 0;
    private AlertDialog authDialog;

    @ViewInject(R.id.list)
    private RecyclerView recycleList;

    @ViewInject(R.id.layout_main_tip)
    private RelativeLayout mainTipLayout;

    @ViewInject(R.id.scrollView)
    private DScrollView scrollView;

    @ViewInject(R.id.main_layout)
    private RelativeLayout mainLayout;

    @ViewInject(R.id.tip_content)
    private TextView tipContent;

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

    @ViewInject(R.id.anim_show)
    private ImageView animShow;

    @Event(R.id.layout_match)
    private void matchLayoutClick(View view) {
        UserAllInfoBean userBean = ((FriendStationApplication) getActivity().getApplication()).getUserAllInfo();
        if (userBean == null || userBean.getId() == 0) {
            T.s("你当前的用户信息获取有误，请重新登录");
            return;
        }
        if (TextUtils.isEmpty(userBean.getRecordSign()) && !userBean.getStatusCert().equals("PASS")) {
            authDialog.show();
            return;
        }
        Intent intent = new Intent(getActivity(), VoiceSendActivity.class);
        startActivity(intent);
    }

    @Event(R.id.recharge_coin)
    private void rechargeCoin(View view) {
        Intent intent = new Intent(getActivity(), RechargeActivity.class);
        startActivity(intent);
        mainTipLayout.setVisibility(View.GONE);
    }

    @Event(R.id.get_coin)
    private void getCoin(View view) {
        Intent intent = new Intent(getActivity(), TaskActivity.class);
        startActivity(intent);
        mainTipLayout.setVisibility(View.GONE);
    }


    @ViewInject(R.id.tips)
    private TextView tips;

    @Event(R.id.layout_find)
    private void findLayoutClick(View view) {
//        tipFirstLayout.setVisibility(View.VISIBLE);
//        getOnlineUser();
        UserAllInfoBean userBean = ((FriendStationApplication) getActivity().getApplication()).getUserAllInfo();
        if (userBean == null || userBean.getId() == 0) {
            T.s("你当前的用户信息获取有误，请重新登录");
            return;
        }

        if (userBean.getSex() == 2 && (TextUtils.isEmpty(userBean.getRecordSign()) && !userBean.getStatusCert().equals("PASS"))) {
            authDialog.show();
            return;
        }
//        if (userBean.getUserCount().getNumCoin() <= 0) {
//            T.s("你的金币也不足，请先充值");
//            return;
//        }
        translateOneUser2();
    }

    @ViewInject(R.id.tip_list)
    private RecyclerView tipList;

    @Event(R.id.layout_rank)
    private void rankLayoutClick(View view) {
        Intent intent = new Intent(getActivity(), RankActivity.class);
        startActivity(intent);
    }

    @ViewInject(R.id.select_no_show)
    private ImageView selectNoShow;

    @ViewInject(R.id.refresh_layout)
    private SwipeRefreshLayout refreshLayout;

    @Event(R.id.dialog_close)
    private void dialogCloseClick(View view) {
        tipFirstLayout.setVisibility(View.GONE);
    }

    private List<UserMainPageBean> userList = new ArrayList<>();

    @Event(R.id.select_no_show)
    private void noShowClick(View view) {
        if (isSelect == 0) {
            selectNoShow.setImageResource(R.mipmap.dialog_check_selected1);
            isSelect = 1;
        } else {
            selectNoShow.setImageResource(R.mipmap.dialog_check_normal);
            isSelect = 0;
        }
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
            getUserList();
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
            getUserList();
        }
    }

    @ViewInject(R.id.layout_first_tip)
    private RelativeLayout tipFirstLayout;

    @Event(R.id.dialog_layout_bottom)
    private void dialogLayoutBottom(View view) {
        List<Integer> selectInt = new ArrayList<>();
        List<FirstShowBean> showBeans = showAdapter.getSelectList();

        for (int s = 0; s < showBeans.size(); s++) {
            if (showBeans.get(s).getIsSelect() == 1) {
                selectInt.add(showBeans.get(s).getUserId());
            }
        }

        Gson gson = new Gson();
        System.out.println(gson.toJson(selectInt));

        if (selectInt.size() == 0) {
            T.s("请先选择你要搭讪的对象");
            return;
        }

        useCoin2(selectInt);
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
            getUserList();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void goToPersonInfo(int userId) {
        Intent intent = new Intent(getActivity(), PersonInfoActivity.class);
        intent.putExtra("mode", 1);
        intent.putExtra("user-id", userId);
        startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
//        getFriendList();
        initData();
        initAuthTip();
        initVoiceTip();
//        initLoadingTip();
        EventBus.getDefault().register(this);
    }

    private void initData() {
        if (((FriendStationApplication) getActivity().getApplication()).getUserAllInfo() != null) {
            switch (((FriendStationApplication) getActivity().getApplication()).getUserAllInfo().getSex()) {
                case 0:
                    commonWordList = ((FriendStationApplication) getActivity().getApplication()).getCommonWordBoyData();
                    break;
                case 1:
                    commonWordList = ((FriendStationApplication) getActivity().getApplication()).getCommonWordBoyData();
                    break;
                case 2:
                    commonWordList = ((FriendStationApplication) getActivity().getApplication()).getCommonWordGirlData();
                    break;
            }
        } else {
            commonWordList = ((FriendStationApplication) getActivity().getApplication()).getCommonWordBoyData();
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onScrollEvent(ScrollEvent event) {
//        System.out.println("fffffff");
//        if (tips.getVisibility() == View.GONE){
//            getUserListMore();
//            tips.setVisibility(View.VISIBLE);
//            scrollView.fullScroll(View.FOCUS_DOWN);
//        }
    }

    private void translateOneUser2() {
        findDialog.show();
        CommonLoginBean bean = ((FriendStationApplication) getActivity().getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_USER_USERRECOMMENDLIST);
        params.addQueryStringParameter("userIdList", "");
        params.addQueryStringParameter("type", 0);
        params.addQueryStringParameter("userNum", 8);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UserMainPageResponse response = gson.fromJson(result, UserMainPageResponse.class);
                System.out.println("translateOneUser2:" + result);
                switch (response.getCode()) {
                    case 200:
//                        List<UserMainPageBean> list = response.getData();
//                        if (list != null) {
//                            Random random = new Random();
//                            int n = random.nextInt(list.size());
//                            int userId = 0;
//                            int index = n - 1;
//                            if (index >= 0) {
//                                userId = list.get(index).getId();
//                            } else {
//                                userId = list.get(0).getId();
//                            }
//                            goToChat2(userId);
//                        }
                        showAdapter = new DialogFirstShowAdapter(response.getData());
                        GridLayoutManager manager2 = new GridLayoutManager(getActivity(), 4);

                        int width1 = DisplayUtils.dp2px(getActivity(), 229);
                        int itemWidth = DisplayUtils.dp2px(getActivity(), 55); //每个item的宽度

                        tipList.setLayoutManager(manager2);
                        tipList.setAdapter(showAdapter);
                        tipList.addItemDecoration(new FirstShowSpaceItemDecoration((width1 - itemWidth * 4) / 8));
                        tipFirstLayout.setVisibility(View.VISIBLE);
                        break;
                    default:
                        T.s(response.getMsg());
                        break;
                }

                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
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
                if (findDialog.isShowing()) {
                    findDialog.cancel();
                }
            }
        });
    }

    private void translateOneUser(List<Integer> onlineuserId) {
        int userId = 0;
        if (onlineuserId.size() > 0) {
            Random random = new Random();
            int n = random.nextInt(onlineuserId.size());
            int index = n - 1;
            if (index >= 0) {
                userId = onlineuserId.get(index);
            } else {
                userId = onlineuserId.get(0);
            }
        }

        if (userId == 0) {
            T.s("没有在线用户");
            return;
        }

        System.out.println("translateOnline:" + userId);

        CommonLoginBean bean = ((FriendStationApplication) getActivity().getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_USER_USERRECOMMENDLIST);
        params.addQueryStringParameter("userIdList", userId);
        params.addQueryStringParameter("type", 0);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UserMainPageResponse response = gson.fromJson(result, UserMainPageResponse.class);
                System.out.println("translateOneUser:" + result);
                switch (response.getCode()) {
                    case 200:
                        if (response.getData().size() > 0) {
                            System.out.println(response.getData().get(0).getId());
                        }
                        break;
                    default:
                        T.s(response.getMsg());
                        break;
                }

                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
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

    private void getOnlineUser() {
        Gson gson = new Gson();
        TimOnlineBean bean = new TimOnlineBean();
        List<String> idList = new ArrayList<>();
        for (int i = 1; i < 30; i++) {
            idList.add(String.valueOf(i));
        }
        bean.setTo_Account(idList);
        String userSig = GenerateTestUserSig.genTestUserSig(Constant.TIM_ADMIN_ACCOUNT);
        String url = Constant.URL_GET_IIM_OFFINE + "?sdkappid=" + Constant.TIM_SDK_APPID + "&identifier=" + Constant.TIM_ADMIN_ACCOUNT + "&usersig=" + userSig + "&random=" + RandomUtil.getRandom() + "&contenttype=json";
        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);
        params.setBodyContent(gson.toJson(bean));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                OnlineTimUserResponse response = gson.fromJson(result, OnlineTimUserResponse.class);
                System.out.println("OnlineUser:" + result);
                List<Integer> onlineuserId = new ArrayList<>();
                for (int o = 0; o < response.getQueryResult().size(); o++) {
                    if (response.getQueryResult().get(o).getState().equals("PushOnline") || response.getQueryResult().get(o).getState().equals("Online")) {
                        onlineuserId.add(Integer.valueOf(response.getQueryResult().get(o).getTo_Account()));
                    }
                }

                translateOneUser(onlineuserId);
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

    public int getCurrentSelectType() {
        return selectType;
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

        mlist = new ArrayList<>();

        userList = new ArrayList<>();
        adapter = new FindShowAdapter(this, userList);
//        adapter = new FindShowAdapter(this, userList);
        manager = new LinearLayoutManager(getActivity());
        recycleList.setLayoutManager(manager);
        recycleList.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                getUserList();
                randomUserList();
            }
        });
        recycleList.setHasFixedSize(true);
        recycleList.setNestedScrollingEnabled(false);
//        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                View view = ((ScrollView)v).getChildAt(((ScrollView)v).getChildCount()-1);
//                int d = view.getBottom();
//                d -= (((ScrollView)v).getHeight()+scrollY);
//                if (d == 0) {
//                    System.out.println("------滚动到最下方------");
//                } else {
//                    System.out.println("------没有滚到最下方------");
//                }
//            }
//        });

        scrollView.setOnScrollChanged(new DScrollView.OnScrollChanged() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt, boolean isBottom) {
                if (isBottom) {
                    if (tips.getVisibility() == View.GONE) {
                        getUserListMore();
                        tips.setVisibility(View.VISIBLE);
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                }
            }
        });

    }

    private void getUserFullInfo() {
        CommonLoginBean bean = ((FriendStationApplication) getActivity().getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_FULL);
        params.addHeader("Authorization", bean.getAccessToken());
        System.out.println(bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUserAllInfoResponse response = gson.fromJson(result, UmsUserAllInfoResponse.class);
                System.out.println("getUserFullInfo" + result);
                switch (response.getCode()) {
                    case 200:
                        ((FriendStationApplication) getActivity().getApplication()).saveUserAllInfo(response.getData());
                        System.out.println("coin_num:" + response.getData().getUserCount().getNumCoin());
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

    private void useCoin2(final List<Integer> selectInt) {
        final UserAllInfoBean userBean = ((FriendStationApplication) getActivity().getApplication()).getUserAllInfo();
        if (userBean.getUserCount().getNumCoin() <= 0) {
            mainTipLayout.setVisibility(View.VISIBLE);
            T.s("你当前金币余额不足，请充值");
            return;
        }
        Gson gson = new Gson();
        CommonLoginBean bean = ((FriendStationApplication) getActivity().getApplication()).getUserInfo();
        UseCoin2Request request = new UseCoin2Request();
//        request.setGivenId(String.valueOf(userId));
        request.setType("message");
        request.setSendNum(selectInt.size());
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_COIN_RECORD_SAVE_BYNUM);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        System.out.println("useCoin2:" + gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUpdatePasswordResponse response = gson.fromJson(result, UmsUpdatePasswordResponse.class);
                System.out.println("useCoin2:" + result);
                switch (response.getCode()) {
                    case 200:
                        UserAllInfoBean userAllInfoBean = ((FriendStationApplication) getActivity().getApplication()).getUserAllInfo();
                        for (int a = 0; a < selectInt.size(); a++) {
                            adminSendMessage(userAllInfoBean.getId(), selectInt.get(a));
                        }
                        T.s("搭讪成功");
                        showAnimation();
                        ((FriendStationApplication) getActivity().getApplication()).isUpdateDoTask(getActivity(), mainLayout, 11);
                        tipFirstLayout.setVisibility(View.GONE);
                        break;

                    case 500:
                        mainTipLayout.setVisibility(View.VISIBLE);
                        T.s("你当前金币余额不足，请充值");
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

    private void useCoin(final int selfId, final int userId) {
        final UserAllInfoBean userBean = ((FriendStationApplication) getActivity().getApplication()).getUserAllInfo();
        if (userBean.getUserCount().getNumCoin() <= 0) {
            mainTipLayout.setVisibility(View.VISIBLE);
            T.s("你当前金币余额不足，请充值");
            return;
        }
        Gson gson = new Gson();
        CommonLoginBean bean = ((FriendStationApplication) getActivity().getApplication()).getUserInfo();
        UseCoinRequest request = new UseCoinRequest();
        request.setGivenId(String.valueOf(userId));
        request.setType("message");
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_COIN_RECORD_SAVE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUpdatePasswordResponse response = gson.fromJson(result, UmsUpdatePasswordResponse.class);
                System.out.println("useCoin:" + result);
                switch (response.getCode()) {
                    case 200:
                        adminSendMessage(selfId, userId);
                        T.s("搭讪成功");
                        ((FriendStationApplication) getActivity().getApplication()).isUpdateDoTask(getActivity(), mainLayout, 11);
                        showAnimation();
                        getUserFullInfo();
                        break;

                    case 500:
                        mainTipLayout.setVisibility(View.VISIBLE);
                        T.s("你当前金币余额不足，请充值");
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


    private void randomUserList() {
        Collections.shuffle(userList); // 混乱的意思
        adapter.notifyDataSetChanged();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserList();
        getNotice();
//        if (isFirstShow) {
//            loadingDialog.show();
//            isFirstShow = false;
//        }

    }

    public void goToChat(int userId) {
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
//        intent.putExtra("chat-user-id", userId);
//        startActivity(intent);
        useCoin(userBean.getId(), userId);
    }

    public void goToChat2(int userId) {
        UserAllInfoBean userBean = ((FriendStationApplication) getActivity().getApplication()).getUserAllInfo();
        if (userBean == null || userBean.getId() == 0) {
            T.s("你当前的用户信息获取有误，请重新登录");
            return;
        }

        if (TextUtils.isEmpty(userBean.getRecordSign()) && !userBean.getStatusCert().equals("PASS")) {
            authDialog.show();
            return;
        }

        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("chat-user-id", userId);
        intent.putExtra("chat-up", 1);
        startActivity(intent);
    }

    private void getFriendList() {
        CommonLoginBean bean = ((FriendStationApplication) getActivity().getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS);
        params.addQueryStringParameter("type", selectType);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                FriendResponse response = gson.fromJson(result, FriendResponse.class);
                System.out.println("FriendList:" + result);
                switch (response.getCode()) {
                    case 200:
                        mlist.clear();
                        List<FriendDetailBean> newList = response.getData().getRecords();
                        for (int l = 0; l < newList.size(); l++) {
                            mlist.add(newList.get(l));
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

    private void getNotice() {
        CommonLoginBean bean = ((FriendStationApplication) getActivity().getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_NOTICE);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                NoticeResponse response = gson.fromJson(result, NoticeResponse.class);
                System.out.println("getNotice:" + result);
                switch (response.getCode()) {
                    case 200:
                        if (response.getData().size() > 0) {
                            if (!TextUtils.isEmpty(response.getData().get(0))) {
                                tipContent.setText(response.getData().get(0));
                            }
                        }
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

    private void getUserList() {
        CommonLoginBean bean = ((FriendStationApplication) getActivity().getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_USER_USERINFOV0LIST);
        int type = 0;
        switch (selectType) {
            case 1:
                type = 1;
                break;
            case 2:
                type = 0;
                break;
            case 3:
                type = 2;
                break;
        }
        params.addQueryStringParameter("pageNum", 1);
        params.addQueryStringParameter("pageSize", 20);
//        params.addQueryStringParameter("type", type);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UserMainPageResponse response = gson.fromJson(result, UserMainPageResponse.class);
                System.out.println("getUserList:" + result);
                switch (response.getCode()) {
                    case 200:
                        userList.clear();
                        for (int u = 0; u < response.getData().size(); u++) {
                            userList.add(response.getData().get(u));
                        }

                        adapter.notifyDataSetChanged();
                        pageNum = 2;
                        break;
                    default:
                        T.s(response.getMsg());
                        break;
                }

                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }

//                if (loadingDialog.isShowing()) {
//                    loadingDialog.cancel();
//                }
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

    private void getUserListMore() {
        CommonLoginBean bean = ((FriendStationApplication) getActivity().getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_USER_USERINFOV0LIST);
        int type = 0;
        switch (selectType) {
            case 1:
                type = 1;
                break;
            case 2:
                type = 0;
                break;
            case 3:
                type = 2;
                break;
        }
        params.addQueryStringParameter("pageNum", pageNum);
        params.addQueryStringParameter("pageSize", 20);
//        params.addQueryStringParameter("type", type);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UserMainPageResponse response = gson.fromJson(result, UserMainPageResponse.class);
                System.out.println("getUserListMore:" + result);
                switch (response.getCode()) {
                    case 200:
                        for (int u = 0; u < response.getData().size(); u++) {
                            userList.add(response.getData().get(u));
                        }

                        adapter.notifyDataSetChanged();
                        pageNum++;
                        tips.setVisibility(View.GONE);
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
                EventBus.getDefault().post(new UpdateMessageEvent());
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

    private void initVoiceTip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_matching_tip, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        findDialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        final TextView content = (TextView) view.findViewById(R.id.content);
        findDialog.setCanceledOnTouchOutside(false);
    }

    private void initLoadingTip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_first_loading_tip, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        loadingDialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        final TextView content = (TextView) view.findViewById(R.id.content);
        loadingDialog.setCanceledOnTouchOutside(false);
    }
}
