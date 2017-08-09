package com.shavkunov.razvitie.samo.tabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.shavkunov.razvitie.samo.R;
import com.shavkunov.razvitie.samo.RecyclerViewAdapter;
import com.shavkunov.razvitie.samo.entity.Patter;
import com.shavkunov.razvitie.samo.entity.PatterLab;

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

    private List<Object> patters = new ArrayList<>();

    private Unbinder unbinder;
    private PatterLab patterLab;
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
        patterLab = PatterLab.getInstance(getContext());
        patterTask = new PatterTask().execute();
        updatePatters(patterLab);
        setRecyclerView();
        return view;
    }

    private void setRecyclerView() {
        speechRecycler.setNestedScrollingEnabled(false);
        speechRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerViewAdapter(getContext(), patters);
        speechRecycler.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        patterTask.cancel(true);
    }

    private void updatePatters(PatterLab patterLab) {
        for (int i = 0; i < patterLab.getList().size(); i++) {
            patters.add(patterLab.getList().get(i));
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
            List<Patter> newVersion = new ArrayList<>();

            if (p != null) {
                Collections.addAll(newVersion, p);
            }

            if (newVersion.size() != 0 && oldVersion.size() != 0) {
                for (int i = 0; i < oldVersion.size(); i++) {
                    for (int j = 0; j < newVersion.size(); j++) {
                        if (oldVersion.get(i).getTitle().equals(
                                newVersion.get(j).getTitle())) {
                            newVersion.remove(j);
                            break;
                        }
                    }
                }
            }

            if (newVersion.size() != 0) {
                for (Patter patter : newVersion) {
                    patterLab.addPatter(patter);
                }

                updatePatters(patterLab);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
