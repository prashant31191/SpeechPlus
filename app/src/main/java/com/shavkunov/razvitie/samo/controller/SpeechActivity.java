package com.shavkunov.razvitie.samo.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.MobileAds;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.shavkunov.razvitie.samo.R;
import com.shavkunov.razvitie.samo.tabs.FavoritesFragment;
import com.shavkunov.razvitie.samo.tabs.MyTwistersFragment;
import com.shavkunov.razvitie.samo.tabs.SettingsFragment;
import com.shavkunov.razvitie.samo.tabs.SpeechFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpeechActivity extends AppCompatActivity {

    private Fragment fragment;

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    private void setBottomBar() {
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_home:
                        fragment = SpeechFragment.newInstance();
                        break;
                    case R.id.tab_favorites:
                        fragment = FavoritesFragment.newInstance();
                        break;
                    case R.id.tab_my_tongue:
                        fragment = MyTwistersFragment.newInstance();
                        break;
                    case R.id.tab_settings:
                        fragment = SettingsFragment.newInstance();
                        break;
                }

                replaceFragment();
            }
        });
    }

    private Fragment createFragment() {
        return fragment;
    }

    private void replaceFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, createFragment());
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_rating) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.shavkunov.razvitie.samo"));
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        MobileAds.initialize(this, getString(R.string.ad_api));
        ButterKnife.bind(this);

        setBottomBar();
        replaceFragment();
    }
}
