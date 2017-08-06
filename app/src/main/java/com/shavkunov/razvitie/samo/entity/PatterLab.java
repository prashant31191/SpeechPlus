package com.shavkunov.razvitie.samo.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shavkunov.razvitie.samo.database.SpeechBaseHelper;
import com.shavkunov.razvitie.samo.database.SpeechCursorWrapper;
import com.shavkunov.razvitie.samo.database.SpeechDbSchema.SpeechTable;

import java.util.ArrayList;
import java.util.List;

public class PatterLab {

    private static PatterLab patterLab;

    private SQLiteDatabase database;

    public static PatterLab getInstance(Context context) {
        if (patterLab == null) {
            patterLab = new PatterLab(context);
        }

        return patterLab;
    }

    private PatterLab(Context c) {
        database = new SpeechBaseHelper(c)
                .getWritableDatabase();
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

    private static ContentValues getContentValues(Patter patter) {
        ContentValues values = new ContentValues();
        values.put(SpeechTable.Cols.URL, patter.getImageUrl());
        values.put(SpeechTable.Cols.TITLE, patter.getTitle());
        values.put(SpeechTable.Cols.FAVORITE, patter.isFavorite() ? 1 : 0);

        return values;
    }

    public void addPatter(Patter p) {
        ContentValues values = getContentValues(p);
        database.insert(SpeechTable.NAME, null, values);
    }

    public void updatePatter(Patter patter) {
        ContentValues values = getContentValues(patter);
        values.put(SpeechTable.Cols.FAVORITE, true);

        database.update(SpeechTable.NAME, values,
                SpeechTable.Cols.TITLE + " = ?",
                new String[] { patter.getTitle() });
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
