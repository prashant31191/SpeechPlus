package com.shavkunov.razvitie.samo.entity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.shavkunov.razvitie.samo.Constants.DbSchema;
import com.shavkunov.razvitie.samo.Constants.Url;
import com.shavkunov.razvitie.samo.R;
import com.shavkunov.razvitie.samo.database.SpeechBaseHelper;
import com.shavkunov.razvitie.samo.database.SpeechCursorWrapper;
import com.shavkunov.razvitie.samo.controller.tabs.SettingsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.shavkunov.razvitie.samo.Constants.Admob.*;

public class CardLab {

    private static String AD_ERROR = "adError";

    private String tableForDb;

    private CardView adCardView;
    private SQLiteDatabase database;
    private FragmentActivity fragmentActivity;
    private SharedPreferences preferences;

    public CardLab(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        database = new SpeechBaseHelper(fragmentActivity)
                .getWritableDatabase();
        preferences = PreferenceManager.getDefaultSharedPreferences(fragmentActivity);
    }

    public void addNativeAds(List<Object> listItems, RecyclerView recyclerView) {
        if (listItems.size() != 0 && isOnline()) {
            for (int i = AD_START_INDEX; i <= listItems.size(); i += AD_PER) {
                final NativeExpressAdView adView = new NativeExpressAdView(fragmentActivity);
                listItems.add(i, adView);
            }

            String adId = fragmentActivity.getString(R.string.ad_id);
            float scale = fragmentActivity.getResources().getDisplayMetrics().density;
            setUpAndLoadNativeAds(recyclerView, listItems, scale, adId);
        }
    }

    public boolean isOnline() {
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
                for (int i = AD_START_INDEX; i <= listItems.size(); i += AD_PER) {
                    final NativeExpressAdView adView =
                            (NativeExpressAdView) listItems.get(i);

                    if (adCardView == null) {
                        adCardView = (CardView) fragmentActivity.findViewById(R.id.ad_card_view);
                    }

                    try {
                        if (adCardView != null) {
                            final int adWidth = adCardView.getWidth() - adCardView.getPaddingLeft()
                                    - adCardView.getPaddingRight() - 20;
                            AdSize adSize = new AdSize((int) (adWidth / scale), AD_HEIGHT);
                            adView.setAdSize(adSize);
                            adView.setAdUnitId(adId);
                            adView.loadAd(new AdRequest.Builder().build());
                        }
                    } catch (IllegalArgumentException e) {
                        Log.d(AD_ERROR, e.getMessage());
                    }
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
                if (cursor.getInt(cursor.getColumnIndex(DbSchema.Cols.FAVORITE)) == 1) {
                    list.add(cursor.getPatter());
                }
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return list;
    }

    private ContentValues getContentValues(Patter patter) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.Cols.ID, patter.getId());
        values.put(DbSchema.Cols.URL, patter.getImageUrl());
        values.put(DbSchema.Cols.TITLE, patter.getTitle());
        values.put(DbSchema.Cols.FAVORITE, patter.isFavorite() ? 1 : 0);
        return values;
    }

    public void addPatter(Patter p) {
        ContentValues values = getContentValues(p);
        database.insert(getTableForDb(), null, values);
    }

    public void updateFavorite(Patter patter) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.Cols.FAVORITE, patter.isFavorite() ? 1 : 0);
        database.update(getTableForDb(), values,
                "_id = ?",
                new String[]{String.valueOf(patter.getId())});
    }

    public void updateUrlAndTitle(Patter patter) {
        ContentValues values = new ContentValues();
        values.put(DbSchema.Cols.URL, patter.getImageUrl());
        values.put(DbSchema.Cols.TITLE, patter.getTitle());
        database.update(getTableForDb(), values,
                "_id = ?",
                new String[]{String.valueOf(patter.getId())});
    }

    private SpeechCursorWrapper queryPatters(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(
                getTableForDb(),
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new SpeechCursorWrapper(cursor);
    }

    public String getTableForDb() {
        boolean isOneClick = preferences.getBoolean(SettingsFragment.KEY_IS_ONE_CLICK, false);
        String currentLanguage = preferences.getString(SettingsFragment.KEY_CURRENT_LANGUAGE,
                Locale.getDefault().getLanguage());

        if (!isOneClick) {
            checkTable(currentLanguage);
            return tableForDb;
        } else {
            checkTable(currentLanguage);
            return tableForDb;
        }
    }

    private void checkTable(String currentLanguage) {
        switch (currentLanguage) {
            case DbSchema.NAME_RU:
                tableForDb = DbSchema.NAME_RU;
                break;
            case DbSchema.NAME_UK:
                tableForDb = DbSchema.NAME_UK;
                break;
            case DbSchema.NAME_BE:
                tableForDb = DbSchema.NAME_BE;
                break;
            case DbSchema.NAME_KK:
                tableForDb = DbSchema.NAME_KK;
                break;
            case DbSchema.NAME_TR:
                tableForDb = DbSchema.NAME_TR;
                break;
            case DbSchema.NAME_PL:
                tableForDb = DbSchema.NAME_PL;
                break;
            case DbSchema.NAME_PT:
                tableForDb = DbSchema.NAME_PT;
                break;
            case DbSchema.NAME_EN:
            default:
                tableForDb = DbSchema.NAME_EN;
                break;
        }
    }

    public String getUrlLink() {
        switch (getTableForDb()) {
            case DbSchema.NAME_RU:
                return Url.RU;
            case DbSchema.NAME_UK:
                return Url.UK;
            case DbSchema.NAME_BE:
                return Url.BE;
            case DbSchema.NAME_KK:
                return Url.KK;
            case DbSchema.NAME_TR:
                return Url.TR;
            case DbSchema.NAME_PL:
                return Url.PL;
            case DbSchema.NAME_PT:
                return Url.PT;
            case DbSchema.NAME_EN:
            default:
                return Url.EN;
        }
    }
}
