package com.shavkunov.razvitie.samo.tabs;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shavkunov.razvitie.samo.R;
import com.shavkunov.razvitie.samo.RecyclerViewAdapter;
import com.shavkunov.razvitie.samo.SettingsLayoutManager;
import com.shavkunov.razvitie.samo.SettingsTwisters;
import com.shavkunov.razvitie.samo.entity.CardLab;
import com.shavkunov.razvitie.samo.entity.Patter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MyTwistersFragment extends Fragment {

    private List<Object> listItems = new ArrayList<>();

    private SettingsTwisters settingsTwisters;

    private CardLab cardLab;
    private Unbinder unbinder;

    @BindView(R.id.my_twisters_recycler)
    RecyclerView myTwistersRecycler;

    @BindView(R.id.fab_my_twisters)
    FloatingActionButton fabMyTwisters;

    public static Fragment newInstance() {
        return new MyTwistersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_twisters, containter, false);
        unbinder = ButterKnife.bind(this, view);
        cardLab = new CardLab(getActivity());
        addPatters(cardLab);
        setRecyclerView();
        cardLab.addNativeAds(listItems, myTwistersRecycler);
        return view;
    }

    @OnClick(R.id.fab_my_twisters)
    public void onFabClick() {
        settingsTwisters = SettingsTwisters.newInstance();
        settingsTwisters.setParentFab(fabMyTwisters);
        settingsTwisters.show(getActivity().getSupportFragmentManager(),
                settingsTwisters.getTag());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (settingsTwisters.isAdded()) {
            settingsTwisters.dismiss();
            settingsTwisters.show(getActivity().getSupportFragmentManager(),
                    settingsTwisters.getTag());
        }
    }

    private void addPatters(CardLab cardLab) {
        for (int i = 0; i < cardLab.getFavoriteList().size(); i++) {
            Patter patter = cardLab.getFavoriteList().get(i);
            listItems.add(patter);
        }
    }

    private void setRecyclerView() {
        myTwistersRecycler.setLayoutManager(SettingsLayoutManager.
                getLayoutManager(getContext()));
        myTwistersRecycler.setAdapter(new RecyclerViewAdapter(getActivity(), listItems, true));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
