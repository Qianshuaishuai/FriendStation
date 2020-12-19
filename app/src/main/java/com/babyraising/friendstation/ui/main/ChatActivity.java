package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.ChatAdapter;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.TIMChatBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.response.UmsUserAllInfoResponse;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageManager;
import com.tencent.imsdk.v2.V2TIMMessageReceipt;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@ContentView(R.layout.activity_chat)
public class ChatActivity extends BaseActivity {

    private UserAllInfoBean currentUserBean;
    private UserAllInfoBean selfUserBean;
    private int currentChatId = 0;
    private Gson gson = new Gson();

    @ViewInject(R.id.name)
    private TextView name;

    @ViewInject(R.id.content)
    private EditText content;

    @ViewInject(R.id.official)
    private ImageView official;

    @ViewInject(R.id.chat_list)
    private RecyclerView chatListRecycleView;

    @ViewInject(R.id.refresh_layout)
    private SwipeRefreshLayout refreshLayout;

    @Event(R.id.common_word)
    private void commonWordClick(View view) {
        Intent intent = new Intent(this, CommonWordActivity.class);
        startActivityForResult(intent, Constant.ACTIVITY_COMMON_REQUEST);
    }

    @Event(R.id.voice)
    private void voiceClick(View view) {
        Intent intent = new Intent(this, VoiceSendActivity.class);
        startActivity(intent);
    }

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @Event(R.id.chat1)
    private void chat1Click(View view) {
        Intent intent = new Intent(this, GiftActivity.class);
        startActivity(intent);
    }

    @Event(R.id.chat2)
    private void chat2Click(View view) {

    }

    @Event(R.id.chat3)
    private void chat3Click(View view) {

    }

    @Event(R.id.chat4)
    private void chat4Click(View view) {

    }

    @Event(R.id.chat5)
    private void chat5Click(View view) {

    }

    @Event(R.id.tip1)
    private void tip1Click(View view) {
        Intent intent = new Intent(this, PersonInfoActivity.class);
        startActivity(intent);
    }

    @Event(R.id.tip2)
    private void tip2Click(View view) {
        Intent intent = new Intent(this, PersonInfoActivity.class);
        startActivity(intent);
    }


    @Event(R.id.tip3)
    private void tip3Click(View view) {
        Intent intent = new Intent(this, MomentSendActivity.class);
        startActivity(intent);
    }


    @Event(R.id.tip4)
    private void tip4Click(View view) {
        Intent intent = new Intent(this, PersonAuthActivity.class);
        startActivity(intent);
    }

    @ViewInject(R.id.tip1)
    private TextView tip1;

    @ViewInject(R.id.tip2)
    private TextView tip2;

    @ViewInject(R.id.tip3)
    private TextView tip3;

    @ViewInject(R.id.tip4)
    private TextView tip4;

    @ViewInject(R.id.layout_official)
    private LinearLayout officialLayout;

    @ViewInject(R.id.layout_main_tip)
    private RelativeLayout mainTipLayout;

    private List<V2TIMMessage> chatList = new ArrayList<>();
    private ChatAdapter adapter;

    @Event(R.id.recharge_coin)
    private void rechargeCoin(View view) {
        Intent intent = new Intent(this, RechargeActivity.class);
        startActivity(intent);
        mainTipLayout.setVisibility(View.GONE);
    }

    @Event(R.id.get_coin)
    private void getCoin(View view) {
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
        mainTipLayout.setVisibility(View.GONE);
    }


    private int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
        initTim();
    }

    private void initTim() {
        V2TIMManager.getMessageManager().addAdvancedMsgListener(new V2TIMAdvancedMsgListener() {
            @Override
            public void onRecvNewMessage(V2TIMMessage msg) {
                super.onRecvNewMessage(msg);
            }

            @Override
            public void onRecvC2CReadReceipt(List<V2TIMMessageReceipt> receiptList) {
                super.onRecvC2CReadReceipt(receiptList);
            }

            @Override
            public void onRecvMessageRevoked(String msgID) {
                super.onRecvMessageRevoked(msgID);
            }
        });
    }

    private void sendTextMessage(String text) {
        V2TIMMessage message = V2TIMManager.getMessageManager().createTextMessage(text);
        sendMessage(message);
    }

    private void sendMessage(V2TIMMessage message) {
        String currentUserId = String.valueOf(currentChatId);
        V2TIMSendCallback callback = new V2TIMSendCallback<V2TIMMessage>() {
            @Override
            public void onSuccess(V2TIMMessage message) {
                System.out.println("sendMessage success:" + gson.toJson(message));
                getMessageList();
            }

            @Override
            public void onError(int code, String desc) {
                System.out.println("sendMessage error:" + desc);
            }

            @Override
            public void onProgress(int progress) {

            }
        };
        V2TIMManager.getMessageManager().sendMessage(message, currentUserId, "", V2TIMMessage.V2TIM_PRIORITY_HIGH, false, null, callback);
    }

    private void initData() {
        Intent intent = getIntent();
        status = intent.getIntExtra("status", 0);
        currentChatId = intent.getIntExtra("chat-user-id", 0);
        getCurrentUserInfo(currentChatId);
        if (status == Constant.OFFICIAL_INTO_CHAT_CODE) {
            officialLayout.setVisibility(View.VISIBLE);
            mainTipLayout.setVisibility(View.VISIBLE);
        } else {
            officialLayout.setVisibility(View.GONE);
            mainTipLayout.setVisibility(View.GONE);
        }

        selfUserBean = ((FriendStationApplication) getApplication()).getUserAllInfo();
    }

    private void initView() {
        tip1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tip2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tip3.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tip4.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendTextMessage(content.getText().toString());
                    content.setText("");
                }
                return false;
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (chatList.size() > 0) {
                    getMessageListMore(chatList.get(0));
                } else {
                    if (refreshLayout.isRefreshing()) {
                        refreshLayout.setRefreshing(false);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.ACTIVITY_COMMON_REQUEST) {
            String word = data.getStringExtra("common-word");
            if (!TextUtils.isEmpty(word)) {
                sendTextMessage(word);
            }
        }
    }

    private void getMessageList() {
        V2TIMValueCallback<List<V2TIMMessage>> callback = new V2TIMValueCallback<List<V2TIMMessage>>() {
            @Override
            public void onSuccess(List<V2TIMMessage> v2TIMMessages) {
                System.out.println("getMessageList success");
                chatList.clear();
                for (int n = 0; n < v2TIMMessages.size(); n++) {
                    chatList.add(v2TIMMessages.get(n));
                }
                Collections.sort(chatList, new Comparator<V2TIMMessage>() {
                    @Override
                    public int compare(V2TIMMessage o1, V2TIMMessage o2) {
                        return (int) (o1.getMessage().getClientTime() - o2.getMessage().getClientTime());
                    }
                });
                adapter.notifyDataSetChanged();
                goToListBottom();
            }

            @Override
            public void onError(int code, String desc) {

            }
        };
        V2TIMManager.getMessageManager().getC2CHistoryMessageList(String.valueOf(currentChatId), 10, null, callback);
    }

    private void getMessageListMore(V2TIMMessage lastMessage) {
        V2TIMValueCallback<List<V2TIMMessage>> callback = new V2TIMValueCallback<List<V2TIMMessage>>() {
            @Override
            public void onSuccess(List<V2TIMMessage> v2TIMMessages) {
                System.out.println("getMessageListMore success");
                for (int n = 0; n < v2TIMMessages.size(); n++) {
                    chatList.add(v2TIMMessages.get(n));
                }
                Collections.sort(chatList, new Comparator<V2TIMMessage>() {
                    @Override
                    public int compare(V2TIMMessage o1, V2TIMMessage o2) {
                        return (int) (o1.getMessage().getClientTime() - o2.getMessage().getClientTime());
                    }
                });
                adapter.notifyDataSetChanged();

                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onError(int code, String desc) {
                System.out.println("getMessageListMore onError:" + desc);

                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
            }
        };
        V2TIMManager.getMessageManager().getC2CHistoryMessageList(String.valueOf(currentChatId), 10, lastMessage, callback);
    }


    private void getCurrentUserInfo(int currentChatId) {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_GET_FULL_BYID);
        params.addParameter("userId", currentChatId);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                UmsUserAllInfoResponse response = gson.fromJson(result, UmsUserAllInfoResponse.class);
                System.out.println("CurrentUserInfo" + result);
                switch (response.getCode()) {
                    case 200:
                        currentUserBean = response.getData();
                        updateCurrentInfo();
                        getMessageList();
                        break;
                    case 401:

                        break;
                    default:
                        T.s("获取聊天用户资料失败!");
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

    private void goToListBottom(){
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager)  chatListRecycleView.getLayoutManager();
        linearLayoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, Integer.MIN_VALUE);
    }

    private void updateCurrentInfo() {
        name.setText(currentUserBean.getNickname());
        chatList = new ArrayList<>();
        adapter = new ChatAdapter(this, chatList, selfUserBean, currentUserBean);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        chatListRecycleView.setLayoutManager(manager);
        chatListRecycleView.setAdapter(adapter);
    }
}
