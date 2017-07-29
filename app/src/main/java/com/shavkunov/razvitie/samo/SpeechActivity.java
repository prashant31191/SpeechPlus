package com.shavkunov.razvitie.samo;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;

public class SpeechActivity extends SingleFragmentActivity {

    private Fragment fragment;

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    @Override
    protected void setBottomBar() {
        super.setBottomBar();
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_home:
                        fragment = new SpeechFragment();
                        break;
                    case R.id.tab_favorites:
                        fragment = new FavoritesFragment();
                        break;
                    case R.id.tab_settings:
                        fragment = new SettingsFragment();
                        break;
                }

                SpeechActivity.super.replaceFragment();
            }
        });
    }

    @Override
    protected Fragment createFragment() {
        return fragment;
    }
}
