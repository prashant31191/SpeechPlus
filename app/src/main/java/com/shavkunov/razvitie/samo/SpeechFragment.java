package com.shavkunov.razvitie.samo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

        speechRecycler.setNestedScrollingEnabled(false);
        speechRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        speechRecycler.setAdapter(new SpeechAdapter());
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
            View view = inflater.inflate(R.layout.card_view_speech, parent, false);
            return new SpeechHolder(view);
        }

        @Override
        public void onBindViewHolder(SpeechHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }
}
