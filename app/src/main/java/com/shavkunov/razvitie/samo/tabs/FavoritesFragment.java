package com.shavkunov.razvitie.samo.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shavkunov.razvitie.samo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoritesFragment extends Fragment {

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

        favoritesRecyclerView.setNestedScrollingEnabled(false);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        favoritesRecyclerView.setAdapter(new FavoritesAdapter());
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private class FavoritesHolder extends RecyclerView.ViewHolder {

        public FavoritesHolder(View itemView) {
            super(itemView);
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
        public void onBindViewHolder(FavoritesHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }
    }
}
