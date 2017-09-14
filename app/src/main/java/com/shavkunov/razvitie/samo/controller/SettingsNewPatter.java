package com.shavkunov.razvitie.samo.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
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
import butterknife.OnClick;

public class SettingsNewPatter extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1;

    private String selectedImage;

    @BindView(R.id.header_logo)
    ImageView headerLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_new_patter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.fab_new_patter)
    public void onFabButtonClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        Intent chosenIntent = Intent.createChooser(intent,
                getString(R.string.choose_gallery));
        startActivityForResult(chosenIntent, GALLERY_REQUEST);
    }

    @OnClick(R.id.save_button)
    public void onSaveButtonClick(View view) {
        Snackbar.make(view, R.string.save_button_tip, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    selectedImage = String.valueOf(uri);
                    Glide.with(this)
                            .load(selectedImage)
                            .into(headerLogo);
                }
        }
    }
}