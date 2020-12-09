package com.babyraising.friendstation.ui.show;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.NoticeAdapter;
import com.babyraising.friendstation.base.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.fragment_notice)
public class NoticeFragment extends BaseFragment {

    @ViewInject(R.id.search)
    private EditText search;

    @ViewInject(R.id.list)
    private RecyclerView recycleList;

    @ViewInject(R.id.layout_notice)
    private RelativeLayout toastLayout;

    private NoticeAdapter adapter;

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
        List<String> testList = new ArrayList<>();
        testList.add("1");
        testList.add("2");
        adapter = new NoticeAdapter(getActivity(),testList);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recycleList.setAdapter(adapter);
        recycleList.setLayoutManager(manager);
    }
}
