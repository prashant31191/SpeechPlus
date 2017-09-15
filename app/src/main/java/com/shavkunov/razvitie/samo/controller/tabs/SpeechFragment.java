package com.shavkunov.razvitie.samo.controller.tabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shavkunov.razvitie.samo.R;
import com.shavkunov.razvitie.samo.RecyclerViewAdapter;
import com.shavkunov.razvitie.samo.SettingsLayoutManager;
import com.shavkunov.razvitie.samo.SettingsHideAndShowFab;
import com.shavkunov.razvitie.samo.entity.Patter;
import com.shavkunov.razvitie.samo.entity.CardLab;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import butterknife.Optional;
import butterknife.Unbinder;

public class SpeechFragment extends Fragment {

    private static final String TAG = "SpeechFragment";

    private List<Object> listItems = new ArrayList<>();

    private Unbinder unbinder;
    private CardLab cardLab;
    private AsyncTask patterTask;
    private float touchDown;
    private float touchUp;

    @Nullable
    @BindView(R.id.speech_recycler)
    RecyclerView speechRecycler;

    @Nullable
    @BindView(R.id.empty_image)
    ImageView emptyImage;

    @Nullable
    @BindView(R.id.empty_title)
    TextView emptyTitle;

    @Nullable
    @BindView(R.id.empty_subtitle)
    TextView emptySubtitle;

    @Nullable
    @BindView(R.id.fab_my_twisters)
    FloatingActionButton fabTwisters;

    public static Fragment newInstance() {
        return new SpeechFragment();
    }

    @LayoutRes
    private int getLayoutResId(boolean isEmpty) {
        return isEmpty ? R.layout.fragment_empty : R.layout.fragment_speech;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cardLab = new CardLab(getActivity());
        patterTask = new PatterTask().execute();
        addPatters(cardLab);
        boolean isEmpty = listItems.size() == 0;

        View view = inflater.inflate(getLayoutResId(isEmpty), container, false);
        unbinder = ButterKnife.bind(this, view);

        if (isEmpty && !cardLab.isOnline()) {
            setEmptyViews();
        } else if (!isEmpty) {
            setNotEmptyViews(cardLab);
        }

        return view;
    }

    @Optional
    @OnTouch(R.id.nested_twisters)
    public boolean onTouchNested(MotionEvent event) {
        SettingsHideAndShowFab screen = new SettingsHideAndShowFab(touchDown, touchUp, fabTwisters);
        screen.getTouch(event);
        return false;
    }

    @Optional
    @OnClick(R.id.fab_my_twisters)
    public void onFabClick() {
        if (cardLab.isOnline()) {
            updateFragment();
        } else {
            Toast.makeText(getActivity(), R.string.no_connection, Toast.LENGTH_SHORT).show();
        }
    }

    private void setEmptyViews() {
        fabTwisters.setVisibility(View.VISIBLE);
        fabTwisters.setImageResource(R.drawable.refresh);
        emptyTitle.setText(R.string.empty_title);
        emptySubtitle.setText(R.string.no_connection_subtitle);
        Glide.with(getActivity()).load(R.drawable.cancel).into(emptyImage);
    }

    private void setNotEmptyViews(CardLab cardLab) {
        setRecyclerView();
        cardLab.addNativeAds(listItems, speechRecycler);
    }

    private void setRecyclerView() {
        speechRecycler.setLayoutManager(SettingsLayoutManager
                .getLayoutManager(getContext()));
        speechRecycler.setAdapter(new RecyclerViewAdapter(getActivity(), listItems));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

        if (patterTask != null) {
            patterTask.cancel(true);
        }
    }

    private void addPatters(CardLab cardLab) {
        for (int i = 0; i < cardLab.getList().size(); i++) {
            Patter patter = cardLab.getList().get(i);
            listItems.add(patter);
        }
    }

    private class PatterTask extends AsyncTask<Void, Void, Patter[]> {

        @Override
        protected Patter[] doInBackground(Void... params) {
            try {
                final String url = cardLab.getUrlLink();
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ResponseEntity<Patter[]> responseEntity = restTemplate.getForEntity(url, Patter[].class);
                return responseEntity.getBody();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Patter[] p) {
            List<Patter> oldVersion = cardLab.getList();
            List<Patter> addVersion = new ArrayList<>();
            List<Patter> replaceVersion = new ArrayList<>();

            if (p != null) {
                Collections.addAll(addVersion, p);
                Collections.addAll(replaceVersion, p);

                if (replaceVersion.size() != 0 && oldVersion.size() != 0) {
                    for (int i = 0; i < oldVersion.size(); i++) {
                        for (int j = 0; j < replaceVersion.size(); j++) {
                            if ((oldVersion.get(i).getTitle().equals(
                                    replaceVersion.get(j).getTitle())) &&
                                    oldVersion.get(i).getImageUrl().equals(
                                            replaceVersion.get(j).getImageUrl())) {
                                replaceVersion.remove(j);
                                break;
                            }
                        }
                    }
                }

                // Проверяем, есть ли что добавить
                if (addVersion.size() != 0 && oldVersion.size() != 0) {
                    editList(oldVersion, addVersion);
                }

                // Проверяем, есть ли в ReplaceVersion что либо из addVersion
                if (addVersion.size() != 0 && replaceVersion.size() != 0) {
                    editList(addVersion, replaceVersion);
                }

                if (addVersion.size() != 0) {
                    for (Patter patter : addVersion) {
                        cardLab.addPatter(patter);
                    }

                    updateList();
                }

                if (replaceVersion.size() != 0) {
                    for (Patter patter : replaceVersion) {
                        cardLab.updateUrlAndTitle(patter);
                    }

                    updateList();
                }
            }
        }

        private void updateList() {
            listItems.clear();
            addPatters(cardLab);
            updateFragment();
        }

        private void editList(List<Patter> listForView, List<Patter> listForRemove) {
            for (int i = 0; i < listForView.size(); i++) {
                for (int j = 0; j < listForRemove.size(); j++) {
                    if (listForView.get(i).getId() ==
                            listForRemove.get(j).getId()) {
                        listForRemove.remove(j);
                    }
                }
            }
        }
    }

    private void updateFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, SpeechFragment.newInstance());
        ft.commit();
    }
}
