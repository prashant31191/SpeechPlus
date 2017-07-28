package com.shavkunov.razvitie.samo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SpeechFragment extends Fragment {

    private Unbinder unbinder;

    @BindView(R.id.speech_recycler_view)
    RecyclerView speechRecyclerView;

    public static Fragment newInstance() {
        return new SpeechFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speech, container, false);
        unbinder = ButterKnife.bind(this, view);

        speechRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        speechRecyclerView.setAdapter(new SpeechAdapter());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class SpeechHolder extends RecyclerView.ViewHolder {

        public SpeechHolder(View itemView) {
            super(itemView);
        }
    }

    private class SpeechAdapter extends RecyclerView.Adapter<SpeechHolder> {

        @Override
        public SpeechHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.card_view, parent, false);
            return new SpeechHolder(view);
        }

        @Override
        public void onBindViewHolder(SpeechHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
