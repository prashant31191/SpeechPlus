package com.shavkunov.razvitie.samo;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    protected void replaceFragment() {
        Fragment fragment = createFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    /*
    Переопределить, если необходимо установить слушателя для Navigation Bar Bottom
    Override, if you need to set the listener for Navigation Bar Bottom
     */
    protected void setBottomBar() {
        ButterKnife.bind(this);
    }

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        setBottomBar();
        replaceFragment();
    }
}
