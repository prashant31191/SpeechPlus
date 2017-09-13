package com.shavkunov.razvitie.samo.tabs;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private Unbinder unbinder;

    @Nullable
    @BindView(R.id.favorites_recycler)
    RecyclerView favoritesRecyclerView;

    @Nullable
    @BindView(R.id.text1)
    TextView textView;

    public static Fragment newInstance() {
        return new FavoritesFragment();
    }

    @LayoutRes
    private int getLayoutResId(boolean isEmpty) {
        if (isEmpty) {
            return R.layout.fragment_favorites_empty;
        } else {
            return R.layout.fragment_favorites;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CardLab cardLab = new CardLab(getActivity());
        addPatters(cardLab);
        boolean isEmpty;

        if (listItems.size() == 0) {
            isEmpty = true;
        } else {
            isEmpty = false;
        }

        View view = inflater.inflate(getLayoutResId(isEmpty), container, false);
        unbinder = ButterKnife.bind(this, view);

        if (!isEmpty) {
            setRecyclerView();
            cardLab.addNativeAds(listItems, favoritesRecyclerView);
        }
        return view;
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
