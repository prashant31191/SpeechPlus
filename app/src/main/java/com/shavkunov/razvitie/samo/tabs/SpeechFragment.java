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

    private List<Patter> patters = new ArrayList<>();

    private Unbinder unbinder;
    private SpeechAdapter adapter;
    private PatterLab patterLab;
    private AsyncTask patterTask;

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
        adapter = new SpeechAdapter();
        speechRecycler.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        patterTask.cancel(true);
    }

    private void updatePatters(PatterLab patterLab) {
        patters = patterLab.getList();
    }

    public class SpeechHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_speech)
        ImageView imageSpeech;

        @BindView(R.id.title_speech)
        TextView titleSpeech;

        @BindView(R.id.favorite_button_speech)
        ShineButton favoriteButtonSpeech;

        public SpeechHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            favoriteButtonSpeech.init(getActivity());
        }
    }

    private class SpeechAdapter extends RecyclerView.Adapter<SpeechHolder> {

        @Override
        public SpeechHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.card_view_speech, parent, false);
            return new SpeechHolder(view);
        }

        @Override
        public void onBindViewHolder(final SpeechHolder holder, int position) {
            Glide.with(holder.itemView.getContext())
                    .load(patters.get(position).getImageUrl())
                    .into(holder.imageSpeech);
            holder.titleSpeech.setText(patters.get(position).getTitle());
            holder.favoriteButtonSpeech.setChecked(patters.get(position).isFavorite());
            holder.favoriteButtonSpeech.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    patterLab.updatePatter(patters.get(holder.getAdapterPosition()),
                            holder.favoriteButtonSpeech.isChecked());
                }
            });
        }

        @Override
        public int getItemCount() {
            return patters.size();
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
