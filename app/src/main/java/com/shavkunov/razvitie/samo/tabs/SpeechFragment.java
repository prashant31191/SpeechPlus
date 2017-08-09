package com.shavkunov.razvitie.samo.tabs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.shavkunov.razvitie.samo.R;
import com.shavkunov.razvitie.samo.RecyclerViewAdapter;
import com.shavkunov.razvitie.samo.entity.Patter;
import com.shavkunov.razvitie.samo.entity.PatterLab;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SpeechFragment extends Fragment {

    private static final String TAG = "SpeechFragment";
    public static final int ITEMS_PER_AD = 45;

    private List<Object> listItems = new ArrayList<>();

    private Unbinder unbinder;
    private PatterLab patterLab;
    private AsyncTask patterTask;
    private RecyclerView.Adapter adapter;

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
        patterLab = PatterLab.getInstance(getContext());
        patterTask = new PatterTask().execute();
        addPatters(patterLab);
        setRecyclerView();

        addNativeAds();
        return view;
    }

    private void setRecyclerView() {
        speechRecycler.setHasFixedSize(true);
        speechRecycler.setNestedScrollingEnabled(false);
        speechRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecyclerViewAdapter(getContext(), listItems);
        speechRecycler.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        patterTask.cancel(true);
    }

    private void addPatters(PatterLab patterLab) {
        for (int i = 0; i < patterLab.getList().size(); i++) {
            Patter patter = patterLab.getList().get(i);
            listItems.add(patter);
        }
    }

    private void addNativeAds() {
        if (listItems.size() != 0 && isOnline()) {
            for (int i = 0; i <= listItems.size(); i += ITEMS_PER_AD) {
                final NativeExpressAdView adView = new NativeExpressAdView(getActivity());
                listItems.add(i, adView);
            }

            setUpAndLoadNativeAds();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void setUpAndLoadNativeAds() {
        speechRecycler.post(new Runnable() {
            @Override
            public void run() {
                final float scale = getActivity().getResources().getDisplayMetrics().density;
                for (int i = 0; i <= listItems.size(); i += ITEMS_PER_AD) {
                    final NativeExpressAdView adView =
                            (NativeExpressAdView) listItems.get(i);
                    final CardView adCardView = (CardView) getActivity().findViewById(R.id.ad_card_view);
                    final int adWidth = adCardView.getWidth() - adCardView.getPaddingLeft()
                            - adCardView.getPaddingLeft();
                    AdSize adSize = new AdSize((int) (adWidth / scale), 150);
                    adView.setAdSize(adSize);
                    adView.setAdUnitId(getString(R.string.ad_id));
                }

                loadNativeAd(0);
            }
        });
    }

    private void loadNativeAd(final int index) {
        if (index >= listItems.size()) {
            return;
        }

        Object item = listItems.get(index);
        if (!(item instanceof NativeExpressAdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a Native"
                    + " Express ad.");
        }

        final NativeExpressAdView adView = (NativeExpressAdView) item;
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                loadNativeAd(index + ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("SpeechFragment", "The previous Native Express ad failed to load. Attempting to"
                        + " load the next Native Express ad in the items list.");
                loadNativeAd(index + ITEMS_PER_AD);
            }
        });

        adView.loadAd(new AdRequest.Builder().build());
    }

    private class PatterTask extends AsyncTask<Void, Void, Patter[]> {

        @Override
        protected Patter[] doInBackground(Void... params) {
            try {
                final String url = "https://speechapp-service.herokuapp.com/get";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ResponseEntity<Patter[]> responseEntity = restTemplate.getForEntity(url, Patter[].class);
                return responseEntity.getBody();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Patter[] p) {
            List<Patter> oldVersion = patterLab.getList();
            List<Patter> newVersion = new ArrayList<>();

            if (p != null) {
                Collections.addAll(newVersion, p);
            }

            if (newVersion.size() != 0 && oldVersion.size() != 0) {
                for (int i = 0; i < oldVersion.size(); i++) {
                    for (int j = 0; j < newVersion.size(); j++) {
                        if (oldVersion.get(i).getTitle().equals(
                                newVersion.get(j).getTitle())) {
                            newVersion.remove(j);
                            break;
                        }
                    }
                }
            }

            if (newVersion.size() != 0) {
                for (Patter patter : newVersion) {
                    patterLab.addPatter(patter);
                }

                addPatters(patterLab);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
