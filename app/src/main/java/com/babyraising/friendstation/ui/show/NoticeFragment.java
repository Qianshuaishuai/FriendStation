package com.babyraising.friendstation.ui.show;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.NoticeAdapter;
import com.babyraising.friendstation.base.BaseFragment;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.TimCustomBean;
import com.babyraising.friendstation.bean.UserMainPageBean;
import com.babyraising.friendstation.bean.UserMessageBean;
import com.babyraising.friendstation.request.CodeBodyRequest;
import com.babyraising.friendstation.response.UmsLoginByMobileResponse;
import com.babyraising.friendstation.response.UserMainPageResponse;
import com.babyraising.friendstation.response.UserMessageResponse;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;
import com.tencent.imsdk.message.CustomElement;
import com.tencent.imsdk.message.MessageBaseElement;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@ContentView(R.layout.fragment_notice)
public class NoticeFragment extends BaseFragment {

    private Gson gson = new Gson();

    @ViewInject(R.id.search)
    private EditText search;

    @ViewInject(R.id.list)
    private RecyclerView recycleList;

    @ViewInject(R.id.layout_notice)
    private RelativeLayout toastLayout;

    @ViewInject(R.id.layout_notice_tip)
    private RelativeLayout noticeTipLayout;

    @ViewInject(R.id.refresh_layout)
    private SwipeRefreshLayout refreshLayout;

    @Event(R.id.dialog_close)
    private void dialogCloseClick(View view) {
        noticeTipLayout.setVisibility(View.GONE);
    }

    @Event(R.id.recall)
    private void recallClick(View view) {
        noticeTipLayout.setVisibility(View.GONE);
    }

    private NoticeAdapter adapter;
    private List<V2TIMConversation> list = new ArrayList<>();
    private List<Integer> idList = new ArrayList<>();
    private List<UserMessageBean> userList = new ArrayList<>();

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    private void initView() {
        list = new ArrayList<>();
        list.add(new V2TIMConversation());
        list.add(new V2TIMConversation());
        adapter = new NoticeAdapter(getActivity(), list, userList);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recycleList.setAdapter(adapter);
        recycleList.setLayoutManager(manager);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getConversationList();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getConversationList();
    }

    private void getConversationList() {
        V2TIMValueCallback<V2TIMConversationResult> callback = new V2TIMValueCallback<V2TIMConversationResult>() {

            @Override
            public void onSuccess(V2TIMConversationResult v2TIMConversationResult) {
                System.out.println("getConversationList onSuccess");
                List<V2TIMConversation> newList = v2TIMConversationResult.getConversationList();
                System.out.println(gson.toJson(v2TIMConversationResult));
                list.clear();
                //前两个空的
                list.add(new V2TIMConversation());
                list.add(new V2TIMConversation());
                idList.clear();
                for (int n = 0; n < newList.size(); n++) {
                    list.add(newList.get(n));
                    idList.add(Integer.valueOf(newList.get(n).getUserID()));
                }

                getUserListForMessage();
            }

            @Override
            public void onError(int code, String desc) {

            }
        };
        V2TIMManager.getConversationManager().getConversationList(0, 100, callback);
    }

    private void getUserListForMessage() {
        CommonLoginBean bean = ((FriendStationApplication) getActivity().getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_USER_USERMESSAGELIST);
        for (int i = 0; i < idList.size(); i++) {
            params.addParameter("userIdList", idList.get(i));
        }
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UserMessageResponse response = gson.fromJson(result, UserMessageResponse.class);
                System.out.println("getUserListForMessage:" + result);
                switch (response.getCode()) {
                    case 200:
                        userList.clear();
                        for (int u = 0; u < response.getData().size(); u++) {
                            userList.add(response.getData().get(u));
                        }

                        adapter.notifyDataSetChanged();

                        if (refreshLayout.isRefreshing()) {
                            refreshLayout.setRefreshing(false);
                        }
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
}
