package com.shavkunov.razvitie.samo.tabs;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shavkunov.razvitie.samo.R;
import com.shavkunov.razvitie.samo.RecyclerViewAdapter;
import com.shavkunov.razvitie.samo.SettingsLayoutManager;
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
import butterknife.Unbinder;

public class SpeechFragment extends Fragment {

    private static final String TAG = "SpeechFragment";

    private List<Object> listItems = new ArrayList<>();

    private Unbinder unbinder;
    private CardLab patterLab;
    private AsyncTask patterTask;
    private RecyclerView.Adapter adapter;

    @BindView(R.id.speech_recycler)
    RecyclerView speechRecycler;

    public static Fragment newInstance() {
        return new SpeechFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speech, container, false);
        unbinder = ButterKnife.bind(this, view);
        patterLab = CardLab.getInstance(getActivity());
        patterTask = new PatterTask().execute();
        setRecyclerView();
        addPatters(patterLab);
        patterLab.addNativeAds(listItems, speechRecycler);
        return view;
    }

    private void setRecyclerView() {
        speechRecycler.setHasFixedSize(true);
        speechRecycler.setNestedScrollingEnabled(false);
        speechRecycler.setLayoutManager(SettingsLayoutManager
                .getLayoutManager(getContext()));
        adapter = new RecyclerViewAdapter(getActivity(), listItems);
        speechRecycler.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        patterTask.cancel(true);
    }

    private void addPatters(CardLab patterLab) {
        for (int i = 0; i < patterLab.getList().size(); i++) {
            Patter patter = patterLab.getList().get(i);
            listItems.add(patter);
        }
    }

    private class PatterTask extends AsyncTask<Void, Void, Patter[]> {

        @Override
        protected Patter[] doInBackground(Void... params) {
            try {
                final String url = "https://speechapp-service.herokuapp.com/get";
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
            List<Patter> oldVersion = patterLab.getList();
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
                        patterLab.addPatter(patter);
                    }

                    updateListAndAdapter();
                }

                if (replaceVersion.size() != 0) {
                    for (Patter patter : replaceVersion) {
                        patterLab.updateUrlAndTitle(patter);
                    }

                    updateListAndAdapter();
                }
            }
        }

        private void updateListAndAdapter() {
            listItems.clear();
            addPatters(patterLab);
            adapter.notifyDataSetChanged();
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
}
