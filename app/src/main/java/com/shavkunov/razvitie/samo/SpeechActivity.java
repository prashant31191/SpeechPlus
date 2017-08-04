package com.shavkunov.razvitie.samo;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.shavkunov.razvitie.samo.tabs.FavoritesFragment;
import com.shavkunov.razvitie.samo.tabs.SettingsFragment;
import com.shavkunov.razvitie.samo.tabs.SpeechFragment;

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
                        fragment = SpeechFragment.newInstance();
                        break;
                    case R.id.tab_favorites:
                        fragment = FavoritesFragment.newInstance();
                        break;
                    case R.id.tab_settings:
                        fragment = SettingsFragment.newInstance();
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
}
