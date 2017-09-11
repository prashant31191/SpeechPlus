package com.shavkunov.razvitie.samo.controller;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shavkunov.razvitie.samo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsNewPatter extends AppCompatActivity {

    @BindView(R.id.header_logo)
    ImageView headerLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_new_patter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        setImage();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_new_patter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setImage() {
        Glide.with(this)
                .load(R.drawable.logo)
                .into(headerLogo);
    }
}
