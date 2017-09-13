package com.shavkunov.razvitie.samo.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class FavoritesFragment extends Fragment {

    private List<Object> listItems = new ArrayList<>();

    private CardLab cardLab;
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

        cardLab = new CardLab(getActivity());
        addPatters(cardLab);
        setRecyclerView();
        cardLab.addNativeAds(listItems, favoritesRecyclerView);
        return view;
    }

    private void addPatters(CardLab cardLab) {
        for (int i = 0; i < cardLab.getFavoriteList().size(); i++) {
            Patter patter = cardLab.getFavoriteList().get(i);
            listItems.add(patter);
        }
    }

    private void setRecyclerView() {
        favoritesRecyclerView.setItemAnimator(new SlideInLeftAnimator());
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
