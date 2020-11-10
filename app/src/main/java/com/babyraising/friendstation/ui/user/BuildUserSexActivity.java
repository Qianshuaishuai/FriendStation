package com.babyraising.friendstation.ui.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_build_user_sex)
public class BuildUserSexActivity extends BaseActivity {

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Event(R.id.save)
    private void saveClick(View view) {

    }

    @Event(R.id.iv_male_normal)
    private void maleNormalClick(View view){
        sexCount = 0 ;
        maleNormal.setVisibility(View.GONE);
        maleTipNormal.setVisibility(View.GONE);
        maleSelected.setVisibility(View.VISIBLE);
        maleTipSelected.setVisibility(View.VISIBLE);

        femaleNormal.setVisibility(View.VISIBLE);
        femaleTipNormal.setVisibility(View.VISIBLE);
        femaleSelected.setVisibility(View.GONE);
        femaleTipSelected.setVisibility(View.GONE);
    }

    @Event(R.id.iv_female_normal)
    private void femaleNormalClick(View view){
        sexCount = 1 ;
        maleNormal.setVisibility(View.VISIBLE);
        maleTipNormal.setVisibility(View.VISIBLE);
        maleSelected.setVisibility(View.GONE);
        maleTipSelected.setVisibility(View.GONE);

        femaleNormal.setVisibility(View.GONE);
        femaleTipNormal.setVisibility(View.GONE);
        femaleSelected.setVisibility(View.VISIBLE);
        femaleTipSelected.setVisibility(View.VISIBLE);
    }

    private int sexCount = 0;

    @ViewInject(R.id.iv_male_normal)
    private ImageView maleNormal;

    @ViewInject(R.id.iv_male_selected)
    private ImageView maleSelected;

    @ViewInject(R.id.iv_female_normal)
    private ImageView femaleNormal;

    @ViewInject(R.id.iv_female_selected)
    private ImageView femaleSelected;

    @ViewInject(R.id.tip_female_normal)
    private TextView maleTipNormal;

    @ViewInject(R.id.tip_female_selected)
    private TextView maleTipSelected;

    @ViewInject(R.id.tip_male_normal)
    private TextView femaleTipNormal;

    @ViewInject(R.id.tip_male_selected)
    private TextView femaleTipSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {

    }
}
