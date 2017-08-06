package com.shavkunov.razvitie.samo.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.shavkunov.razvitie.samo.database.SpeechDbSchema.SpeechTable;
import com.shavkunov.razvitie.samo.entity.Patter;

public class SpeechCursorWrapper extends CursorWrapper {

    public SpeechCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Patter getPatter() {
        String url = getString(getColumnIndex(SpeechTable.Cols.URL));
        String title = getString(getColumnIndex(SpeechTable.Cols.TITLE));
        int isFavorite = getInt(getColumnIndex(SpeechTable.Cols.FAVORITE));

        Patter patter = new Patter();
        patter.setImageUrl(url);
        patter.setTitle(title);
        patter.setFavorite(isFavorite != 0);

        return patter;
    }
}
