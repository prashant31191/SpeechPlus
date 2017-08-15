package com.shavkunov.razvitie.samo.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.shavkunov.razvitie.samo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SettingsFragment extends Fragment {

    public static Fragment newInstance() {
        return new SettingsFragment();
    }

    private Unbinder unbinder;

    @BindView(R.id.image_languages)
    ImageView imageLanguages;

    @BindView(R.id.spinner_languages)
    Spinner spinnerLanguages;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);

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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        Glide.get(getContext()).clearMemory();
    }
}
