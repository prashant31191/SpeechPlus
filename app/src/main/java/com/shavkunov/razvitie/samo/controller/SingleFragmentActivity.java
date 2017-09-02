package com.shavkunov.razvitie.samo.controller;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.ads.MobileAds;
import com.shavkunov.razvitie.samo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    protected abstract Fragment createFragment();

    protected void replaceFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, createFragment());
        ft.commit();
    }

    /**
     * Переопределить, если необходимо установить слушателя для Navigation Bar Bottom
     * Override, if you need to set the listener for Navigation Bar Bottom
     */
    protected void setBottomBar() {}

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        MobileAds.initialize(this, getString(R.string.ad_api));
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setBottomBar();
        replaceFragment();
    }
}
