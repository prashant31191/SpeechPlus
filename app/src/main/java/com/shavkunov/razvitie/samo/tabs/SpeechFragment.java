package com.shavkunov.razvitie.samo.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SpeechFragment extends Fragment {

    private List<Patter> patters = new ArrayList<>();

    private Unbinder unbinder;
    private SpeechAdapter adapter;

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
        patters = PatterLab.getInstance(getContext()).getList();
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
            holder.titleSpeech.setText(patters.get(position)
                    .getTitle());
        }

        @Override
        public int getItemCount() {
            return patters.size();
        }
    }
}
