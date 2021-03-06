package com.shavkunov.razvitie.samo.controller.tabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shavkunov.razvitie.samo.R;
import com.shavkunov.razvitie.samo.RecyclerViewAdapter;
import com.shavkunov.razvitie.samo.SettingsLayoutManager;
import com.shavkunov.razvitie.samo.SettingsHideAndShowFab;
import com.shavkunov.razvitie.samo.controller.SettingsNewPatter;
import com.shavkunov.razvitie.samo.entity.CardLab;
import com.shavkunov.razvitie.samo.entity.Patter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import butterknife.Optional;
import butterknife.Unbinder;
import top.wefor.circularanim.CircularAnim;

public class MyTwistersFragment extends Fragment {

    private static final int REQUEST_CODE_SETTINGS = 0;

    private List<Object> listItems = new ArrayList<>();
    private Unbinder unbinder;
    private CardLab cardLab;

    @Nullable
    @BindView(R.id.my_twisters_recycler)
    RecyclerView myTwistersRecycler;

    @Nullable
    @BindView(R.id.fab_my_twisters)
    FloatingActionButton fabMyTwisters;

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
        return new MyTwistersFragment();
    }

    @LayoutRes
    private int getLayoutResId(boolean isEmpty) {
        return isEmpty ? R.layout.fragment_empty : R.layout.fragment_my_twisters;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter,
                             Bundle savedInstanceState) {
        cardLab = new CardLab(getActivity());
        addPatters(cardLab);
        boolean isEmpty = listItems.size() == 0;

        View view = inflater.inflate(getLayoutResId(isEmpty), containter, false);
        unbinder = ButterKnife.bind(this, view);
        fabMyTwisters.setVisibility(View.VISIBLE);

        if (isEmpty) {
            setEmptyViews();
        } else {
            setNotEmptyViews(cardLab);
        }

        return view;
    }

    private void setEmptyViews() {
        fabMyTwisters.setImageResource(R.drawable.plus);
        emptyTitle.setText(R.string.empty_title);
        emptySubtitle.setText(R.string.my_twisters_subtitle);
        Glide.with(getActivity()).load(R.drawable.cancel).into(emptyImage);
    }

    private void setNotEmptyViews(CardLab cardLab) {
        setRecyclerView();
        cardLab.addNativeAds(listItems, myTwistersRecycler);
    }

    @Optional
    @OnTouch(R.id.nested_twisters)
    public boolean onTouchNested(MotionEvent event) {
        getTouch(event);
        return false;
    }

    @Optional
    @OnTouch(R.id.my_twisters_recycler)
    public boolean onTouchRecycler(MotionEvent event) {
        getTouch(event);
        return false;
    }

    private void getTouch(MotionEvent event) {
        SettingsHideAndShowFab screen = new SettingsHideAndShowFab(fabMyTwisters);
        screen.getTouch(event);
    }

    @OnClick(R.id.fab_my_twisters)
    public void onFabClick() {
        CircularAnim.fullActivity(getActivity(), fabMyTwisters)
                .duration(500)
                .colorOrImageRes(R.color.colorPrimaryDark)
                .go(new CircularAnim.OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                    }
                });
        startActivityForResult(new Intent(getActivity(), SettingsNewPatter.class),
                REQUEST_CODE_SETTINGS);
    }

    private void addPatters(CardLab cardLab) {
        for (int i = 0; i < cardLab.getYourPattersList().size(); i++) {
            Patter patter = cardLab.getYourPattersList().get(i);
            listItems.add(patter);
        }
    }

    private void setRecyclerView() {
        myTwistersRecycler.setLayoutManager(SettingsLayoutManager.
                getLayoutManager(getContext()));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), listItems, true);
        myTwistersRecycler.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Patter patter = new Patter();
            patter.setImageUrl(data.getStringExtra(SettingsNewPatter.EXTRA_IMAGE));
            patter.setTitle(data.getStringExtra(SettingsNewPatter.EXTRA_PATTER));
            cardLab.addYourPatter(patter);
            updateFragment();
        }
    }

    private void updateFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, MyTwistersFragment.newInstance());
        ft.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
