package com.shavkunov.razvitie.samo.entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.shavkunov.razvitie.samo.database.SpeechBaseHelper;
import com.shavkunov.razvitie.samo.database.SpeechCursorWrapper;
import com.shavkunov.razvitie.samo.database.SpeechDbSchema;
import com.shavkunov.razvitie.samo.database.SpeechDbSchema.SpeechTable;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatterLab {

    private static final String TAG = "PatterLab";

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
        new PatterTask().execute();
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

    private void addPatter(Patter p) {
        ContentValues values = getContentValues(p);
        database.insert(SpeechTable.NAME, null, values);
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
            if (p != null) {
                for (Patter patter : p) {
                    addPatter(patter);
                }
            }
        }
    }
}
