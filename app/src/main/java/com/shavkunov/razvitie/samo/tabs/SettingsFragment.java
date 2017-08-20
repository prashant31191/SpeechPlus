package com.shavkunov.razvitie.samo.tabs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.shavkunov.razvitie.samo.Constants;
import com.shavkunov.razvitie.samo.Constants.DbSchema;
import com.shavkunov.razvitie.samo.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import butterknife.Unbinder;

public class SettingsFragment extends Fragment {

    public static Fragment newInstance() {
        return new SettingsFragment();
    }

    public static final String KEY_ITEM_SELECT = "select";
    public static final String KEY_IS_ONE_CLICK = "oneClick";
    public static final String KEY_CURRENT_LANGUAGE = "language";

    private boolean isOneClick;
    private int positionItem;
    private String currentLanguage;

    private Unbinder unbinder;
    private SharedPreferences preferences;

    @BindView(R.id.image_languages)
    ImageView imageLanguages;

    @BindView(R.id.spinner_languages)
    Spinner spinnerLanguages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        getPreferences();
        editImage();
        editSpinner();
        return view;
    }

    private void editImage() {
        Glide.with(getContext())
                .load(R.drawable.planet)
                .into(imageLanguages);
    }

    private void editSpinner() {
        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getContext(), R.array.array_languages,
                android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguages.setAdapter(adapter);
        spinnerLanguages.setSelection(positionItem);
    }

    @OnItemSelected(R.id.spinner_languages)
    public void onItemSelected(AdapterView<?> parent, View itemSelected,
                               int selectedItemPosition, long selectedId) {
        if (!isOneClick) {
            isOneClick = true;
            currentLanguage = Locale.getDefault().getLanguage();
            switch (currentLanguage) {
                case DbSchema.NAME_RU:
                    setPositionItem(0);
                    break;
                case DbSchema.NAME_UK:
                    setPositionItem(1);
                    break;
                case DbSchema.NAME_BE:
                    setPositionItem(2);
                    break;
                case DbSchema.NAME_KK:
                    setPositionItem(3);
                    break;
                case DbSchema.NAME_TR:
                    setPositionItem(4);
                    break;
                case DbSchema.NAME_PL:
                    setPositionItem(5);
                    break;
                case DbSchema.NAME_PT:
                    setPositionItem(6);
                    break;
                case DbSchema.NAME_EN:
                default:
                    setPositionItem(7);
                    break;
            }

            spinnerLanguages.setSelection(positionItem);
        } else {
            setPositionItem(selectedItemPosition);
        }

        putPreferences();
    }

    private void setPositionItem(int selectedItemPosition) {
        positionItem = selectedItemPosition;

        switch (selectedItemPosition) {
            case 0:
                currentLanguage = DbSchema.NAME_RU;
                break;
            case 1:
                currentLanguage = DbSchema.NAME_UK;
                break;
            case 2:
                currentLanguage = DbSchema.NAME_BE;
                break;
            case 3:
                currentLanguage = DbSchema.NAME_KK;
                break;
            case 4:
                currentLanguage = DbSchema.NAME_TR;
                break;
            case 5:
                currentLanguage = DbSchema.NAME_PL;
                break;
            case 6:
                currentLanguage = DbSchema.NAME_PT;
                break;
            case 7:
            default:
                currentLanguage = DbSchema.NAME_EN;
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getPreferences() {
        positionItem = preferences.getInt(KEY_ITEM_SELECT, 0);
        isOneClick = preferences.getBoolean(KEY_IS_ONE_CLICK, false);
        currentLanguage = preferences.getString(KEY_CURRENT_LANGUAGE, "");
    }

    private void putPreferences() {
        preferences.edit().putBoolean(KEY_IS_ONE_CLICK, isOneClick).apply();
        preferences.edit().putInt(KEY_ITEM_SELECT, positionItem).apply();
        preferences.edit().putString(KEY_CURRENT_LANGUAGE, currentLanguage).apply();
    }
}
