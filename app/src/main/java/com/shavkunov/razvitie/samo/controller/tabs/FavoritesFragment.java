package com.shavkunov.razvitie.samo.controller.tabs;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shavkunov.razvitie.samo.R;
import com.shavkunov.razvitie.samo.RecyclerViewAdapter;
import com.shavkunov.razvitie.samo.SettingsLayoutManager;
import com.shavkunov.razvitie.samo.entity.Patter;
import com.shavkunov.razvitie.samo.entity.CardLab;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoritesFragment extends Fragment {

    private List<Object> listItems = new ArrayList<>();

    private Unbinder unbinder;

    @Nullable
    @BindView(R.id.favorites_recycler)
    RecyclerView favoritesRecyclerView;

    @Nullable
    @BindView(R.id.empty_image)
    ImageView emptyImage;

    @Nullable
    @BindView(R.id.empty_title)
    TextView emptyTitle;

    @Nullable
    @BindView(R.id.empty_subtitle)
    TextView emptySubtitle;

    public static Fragment newInstance() {
        return new FavoritesFragment();
    }

    @LayoutRes
    private int getLayoutResId(boolean isEmpty) {
        return isEmpty ? R.layout.fragment_empty : R.layout.fragment_favorites;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CardLab cardLab = new CardLab(getActivity());
        addPatters(cardLab);
        boolean isEmpty = listItems.size() == 0;

        View view = inflater.inflate(getLayoutResId(isEmpty), container, false);
        unbinder = ButterKnife.bind(this, view);

        if (isEmpty) {
            setEmptyViews();
        } else {
            setNotEmptyViews(cardLab);
        }

        return view;
    }

    private void setEmptyViews() {
        emptyTitle.setText(R.string.empty_title);
        emptySubtitle.setText(R.string.favorites_subtitle);
        Glide.with(getActivity()).load(R.drawable.cancel).into(emptyImage);
    }

    private void setNotEmptyViews(CardLab cardLab) {
        setRecyclerView();
        cardLab.addNativeAds(listItems, favoritesRecyclerView);
    }

    private void addPatters(CardLab cardLab) {
        for (int i = 0; i < cardLab.getFavoriteList().size(); i++) {
            Patter patter = cardLab.getFavoriteList().get(i);
            listItems.add(patter);
        }
    }

    private void setRecyclerView() {
        favoritesRecyclerView.setLayoutManager(SettingsLayoutManager
                .getLayoutManager(getContext()));
        favoritesRecyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), listItems));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
