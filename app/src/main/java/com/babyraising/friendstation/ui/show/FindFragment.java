package com.babyraising.friendstation.ui.show;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.DialogFirstShowAdapter;
import com.babyraising.friendstation.adapter.LookMeRecordAdapter;
import com.babyraising.friendstation.base.BaseFragment;
import com.babyraising.friendstation.bean.FirstShowBean;
import com.babyraising.friendstation.decoration.FirstShowSpaceItemDecoration;
import com.babyraising.friendstation.decoration.SpaceItemDecoration;
import com.babyraising.friendstation.ui.main.RankActivity;
import com.babyraising.friendstation.ui.main.VoiceSendActivity;
import com.babyraising.friendstation.util.DisplayUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_find)
public class FindFragment extends BaseFragment {

    private int selectType = 1;
    private LookMeRecordAdapter adapter;
    private DialogFirstShowAdapter showAdapter;

    private int isSelect = 0;

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

    @Event(R.id.layout_match)
    private void matchLayoutClick(View view) {
        Intent intent = new Intent(getActivity(), VoiceSendActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_find)
    private void findLayoutClick(View view) {
        tipFirstLayout.setVisibility(View.VISIBLE);
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

    @ViewInject(R.id.layout_first_tip)
    private RelativeLayout tipFirstLayout;

    @Event(R.id.dialog_layout_bottom)
    private void dialogLayoutBottom(View view) {
        tipFirstLayout.setVisibility(View.GONE);
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
        testList.add("1");
        adapter = new LookMeRecordAdapter(testList);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recycleList.setLayoutManager(manager);
        recycleList.setAdapter(adapter);

        List<FirstShowBean> testList123 = new ArrayList<>();
        testList123.add(new FirstShowBean());
        testList123.add(new FirstShowBean());
        testList123.add(new FirstShowBean());
        testList123.add(new FirstShowBean());
        testList123.add(new FirstShowBean());
        testList123.add(new FirstShowBean());
        testList123.add(new FirstShowBean());
        testList123.add(new FirstShowBean());

        showAdapter = new DialogFirstShowAdapter(testList123);
        GridLayoutManager manager2 = new GridLayoutManager(getActivity(), 4);

        WindowManager wm1 = this.getActivity().getWindowManager();
        int width1 = DisplayUtils.dp2px(getActivity(), 229);
        int itemWidth = DisplayUtils.dp2px(getActivity(), 55); //每个item的宽度

        tipList.setLayoutManager(manager2);
        tipList.setAdapter(showAdapter);
        tipList.addItemDecoration(new FirstShowSpaceItemDecoration((width1 - itemWidth * 4) / 8));
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
}
