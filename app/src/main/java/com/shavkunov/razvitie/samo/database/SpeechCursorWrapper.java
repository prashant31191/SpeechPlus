package com.shavkunov.razvitie.samo.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.shavkunov.razvitie.samo.Constants.DbSchema;
import com.shavkunov.razvitie.samo.entity.Patter;

public class SpeechCursorWrapper extends CursorWrapper {

    public SpeechCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Patter getPatter() {
        int id = getInt(getColumnIndex(DbSchema.Cols.ID));
        String url = getString(getColumnIndex(DbSchema.Cols.URL));
        String title = getString(getColumnIndex(DbSchema.Cols.TITLE));
        int isFavorite = getInt(getColumnIndex(DbSchema.Cols.FAVORITE));

        Patter patter = new Patter();
        patter.setId(id);
        patter.setImageUrl(url);
        patter.setTitle(title);
        patter.setFavorite(isFavorite != 0);

        return patter;
    }
}
