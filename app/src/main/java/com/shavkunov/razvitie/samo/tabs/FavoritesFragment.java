package com.shavkunov.razvitie.samo.tabs;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.shavkunov.razvitie.samo.R;
import com.shavkunov.razvitie.samo.SettingsLayoutManager;
import com.shavkunov.razvitie.samo.entity.Patter;
import com.shavkunov.razvitie.samo.entity.CardLab;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class FavoritesFragment extends Fragment {

    private List<Patter> patters = new ArrayList<>();

    private CardLab patterLab;
    private Unbinder unbinder;

    @BindView(R.id.favorites_recycler)
    RecyclerView favoritesRecyclerView;

    public static Fragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        unbinder = ButterKnife.bind(this, view);
        patterLab = CardLab.getInstance(getActivity());
        patters = patterLab.getFavoriteList();
        setRecyclerView();
        return view;
    }

    private void setRecyclerView() {
        favoritesRecyclerView.setItemAnimator(new SlideInLeftAnimator());
        favoritesRecyclerView.setNestedScrollingEnabled(false);
        favoritesRecyclerView.setLayoutManager(SettingsLayoutManager
                .getLayoutManager(getContext()));
        favoritesRecyclerView.setAdapter(new FavoritesAdapter());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public class FavoritesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_speech)
        ImageView imageSpeech;

        @BindView(R.id.title_speech)
        TextView titleSpeech;

        @BindView(R.id.favorite_button_speech)
        ShineButton favoriteButtonSpeech;

        public FavoritesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            favoriteButtonSpeech.init(getActivity());
        }
    }

    private class FavoritesAdapter extends RecyclerView.Adapter<FavoritesHolder> {

        @Override
        public FavoritesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.card_view_speech, parent, false);
            return new FavoritesHolder(view);
        }

        @Override
        public void onBindViewHolder(final FavoritesHolder holder, int position) {
            Glide.with(holder.itemView.getContext())
                    .load(patters.get(position).getImageUrl())
                    .into(holder.imageSpeech);
            holder.titleSpeech.setText(patters.get(position).getTitle());
            holder.favoriteButtonSpeech.setChecked(patters.get(position).isFavorite());
            holder.favoriteButtonSpeech.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (patters.size() != 0) {
                        patters.get(holder.getAdapterPosition())
                                .setFavorite(holder.favoriteButtonSpeech.isChecked());
                        patterLab.updateFavorite(patters.get(holder.getAdapterPosition()));
                        patters.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return patters.size();
        }
    }
}
