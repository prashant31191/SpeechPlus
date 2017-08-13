package com.shavkunov.razvitie.samo.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.shavkunov.razvitie.samo.R;
import com.shavkunov.razvitie.samo.database.SpeechBaseHelper;
import com.shavkunov.razvitie.samo.database.SpeechCursorWrapper;
import com.shavkunov.razvitie.samo.database.SpeechDbSchema.SpeechTable;

import java.util.ArrayList;
import java.util.List;

public class CardLab {

    public static final int ITEMS_PER_AD = 40;
    private static final int ITEMS_INDEX = 6;

    private static CardLab patterLab;

    private CardView adCardView;
    private SQLiteDatabase database;
    private FragmentActivity fragmentActivity;

    public static CardLab getInstance(FragmentActivity fragmentActivity) {
        if (patterLab == null) {
            patterLab = new CardLab(fragmentActivity);
        }

        return patterLab;
    }

    private CardLab(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        database = new SpeechBaseHelper(fragmentActivity)
                .getWritableDatabase();
    }

    public void addNativeAds(List<Object> listItems, RecyclerView recyclerView) {
        if (listItems.size() != 0 && isOnline()) {
            for (int i = ITEMS_INDEX; i <= listItems.size(); i += ITEMS_PER_AD) {
                final NativeExpressAdView adView = new NativeExpressAdView(fragmentActivity);
                listItems.add(i, adView);
            }

            String adId = fragmentActivity.getString(R.string.ad_id);
            float scale = fragmentActivity.getResources().getDisplayMetrics().density;
            setUpAndLoadNativeAds(recyclerView, listItems, scale, adId);
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) fragmentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =
                cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private void setUpAndLoadNativeAds(RecyclerView speechRecycler, final List<Object> listItems,
                                       final float scale, final String adId) {
        speechRecycler.post(new Runnable() {
            @Override
            public void run() {
                for (int i = ITEMS_INDEX; i <= listItems.size(); i += ITEMS_PER_AD) {
                    final NativeExpressAdView adView =
                            (NativeExpressAdView) listItems.get(i);

                    if (adCardView == null) {
                        adCardView = (CardView) fragmentActivity.findViewById(R.id.ad_card_view);
                    }
                    final int adWidth = adCardView.getWidth() - adCardView.getPaddingLeft()
                            - adCardView.getPaddingLeft();
                    AdSize adSize = new AdSize((int) (adWidth / scale), 150);
                    adView.setAdSize(adSize);
                    adView.setAdUnitId(adId);
                    adView.loadAd(new AdRequest.Builder().build());
                }
            }
        });
    }

    public List<Patter> getList() {
        List<Patter> list = new ArrayList<>();
        SpeechCursorWrapper cursor = queryPatters(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursor.getPatter());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return list;
    }

    public List<Patter> getFavoriteList() {
        List<Patter> list = new ArrayList<>();
        SpeechCursorWrapper cursor = queryPatters(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (cursor.getInt(cursor.getColumnIndex(SpeechTable.Cols.FAVORITE)) == 1) {
                    list.add(cursor.getPatter());
                }
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return list;
    }

    private static ContentValues getContentValues(Patter patter) {
        ContentValues values = new ContentValues();
        values.put(SpeechTable.Cols.ID, patter.getId());
        values.put(SpeechTable.Cols.URL, patter.getImageUrl());
        values.put(SpeechTable.Cols.TITLE, patter.getTitle());
        values.put(SpeechTable.Cols.FAVORITE, patter.isFavorite() ? 1 : 0);
        return values;
    }

    public void addPatter(Patter p) {
        ContentValues values = getContentValues(p);
        database.insert(SpeechTable.NAME, null, values);
    }

    public void updateFavorite(Patter patter) {
        ContentValues values = new ContentValues();
        values.put(SpeechTable.Cols.FAVORITE, patter.isFavorite() ? 1 : 0);
        database.update(SpeechTable.NAME, values,
                "_id = ?",
                new String[] {String.valueOf(patter.getId())});
    }

    public void updateUrlAndTitle(Patter patter) {
        ContentValues values = new ContentValues();
        values.put(SpeechTable.Cols.URL, patter.getImageUrl());
        values.put(SpeechTable.Cols.TITLE, patter.getTitle());
        database.update(SpeechTable.NAME, values,
                "_id = ?",
                new String[] {String.valueOf(patter.getId())});
    }

    private SpeechCursorWrapper queryPatters(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(
                SpeechTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new SpeechCursorWrapper(cursor);
    }
}
