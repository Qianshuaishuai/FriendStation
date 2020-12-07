package com.babyraising.friendstation.ui.show;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.fragment_find)
public class FindFragment extends BaseFragment {

    @ViewInject(R.id.list)
    private RecyclerView list;

    @ViewInject(R.id.type_tv1)
    private TextView typeTv1;

    @ViewInject(R.id.type_tv2)
    private TextView typeTv2;

    @ViewInject(R.id.type_tv3)
    private TextView typeTv3;

    @ViewInject(R.id.type_view1)
    private TextView typeV1;

    @ViewInject(R.id.type_view2)
    private TextView typeV2;

    @ViewInject(R.id.type_view3)
    private TextView typeV3;

    @Event(R.id.layout_match)
    private void matchLayoutClick(View view) {

    }

    @Event(R.id.layout_find)
    private void findLayoutClick(View view) {

    }

    @Event(R.id.layout_rank)
    private void rankLayoutClick(View view) {

    }

    @Event(R.id.type_tv1)
    private void typeTv1Click(View view) {

    }

    @Event(R.id.type_tv2)
    private void typeTv2Click(View view) {

    }

    @Event(R.id.type_tv3)
    private void typeTv3Click(View view) {

    }

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
}
