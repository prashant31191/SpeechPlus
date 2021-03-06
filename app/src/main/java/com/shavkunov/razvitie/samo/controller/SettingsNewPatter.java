package com.shavkunov.razvitie.samo.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.shavkunov.razvitie.samo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsNewPatter extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1;
    private static final String STATE_IMAGE = "stateImage";
    private static final String STATE_PATTER = "statePatter";
    public static final String EXTRA_IMAGE = "extraImage";
    public static final String EXTRA_PATTER = "extraPatter";

    private String selectedImage;
    private String yourPatter;

    @BindView(R.id.header_logo)
    KenBurnsView headerLogo;

    @BindView(R.id.edit_patter)
    MaterialEditText editPatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_new_patter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            selectedImage = savedInstanceState.getString(STATE_IMAGE);
            yourPatter = savedInstanceState.getString(STATE_PATTER);
        }

        if (yourPatter != null) {
            editPatter.setText(yourPatter);
        }

        if (selectedImage != null) {
            setupImage(selectedImage);
        } else {
            setupImage(R.drawable.logo);
        }
    }

    private void setupImage(int standardImage) {
        this.setupImage(null, standardImage);
    }

    private void setupImage(String selectedImage) {
        this.setupImage(selectedImage, 0);
    }

    private void setupImage(String selectedImage, int standardImage) {
        if (selectedImage != null && standardImage != 0) {
            return;
        }

        if (selectedImage != null) {
            Glide.with(this).load(selectedImage).into(headerLogo);
        }

        if (standardImage != 0) {
            Glide.with(this).load(standardImage).into(headerLogo);
        }
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
        saveEditText();

        if (!yourPatter.equals("")) {
            Snackbar.make(view, R.string.save_button_tip, Snackbar.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.putExtra(EXTRA_IMAGE, selectedImage);
            intent.putExtra(EXTRA_PATTER, yourPatter);
            setResult(RESULT_OK, intent);
        } else {
            editPatter.setError(getString(R.string.edit_patter_empty));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    selectedImage = String.valueOf(intent.getData());
                    setupImage(selectedImage);
                }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveEditText();
    }

    private void saveEditText() {
        yourPatter = editPatter.getText().toString();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(STATE_IMAGE, selectedImage);
        savedInstanceState.putString(STATE_PATTER, yourPatter);
    }
}
