package com.babyraising.friendstation.ui.show;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
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
import com.babyraising.friendstation.response.MomentResponse;
import com.babyraising.friendstation.response.ScoreRecordResponse;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_moment)
public class MomentFragment extends BaseFragment {

    private int selectType = 1;
    private MomentAdapter adapter;
    private List<MomentDetailBean> list;

    @ViewInject(R.id.list)
    private RecyclerView recycleList;

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

        getMomentList();
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

        List<String> testList = new ArrayList<>();
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        adapter = new MomentAdapter(getActivity(), testList);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recycleList.setAdapter(adapter);
        recycleList.setLayoutManager(manager);
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
}