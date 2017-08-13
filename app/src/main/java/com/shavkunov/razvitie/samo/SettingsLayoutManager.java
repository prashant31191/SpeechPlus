package com.shavkunov.razvitie.samo;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

public final class SettingsLayoutManager {

    public static RecyclerView.LayoutManager getLayoutManager(Context context) {
        RecyclerView.LayoutManager lm;
        switch (getSizeScreen(context)) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                lm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
            default:
                lm = new LinearLayoutManager(context);
                break;
        }

        return lm;
    }

    private static int getSizeScreen(Context context) {
        return context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
    }
}
