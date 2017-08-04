package com.shavkunov.razvitie.samo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shavkunov.razvitie.samo.database.SpeechDbSchema.SpeechTable;

public class SpeechBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "speechBase.db";

    public SpeechBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + SpeechTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                SpeechTable.Cols.URL + ", " +
                SpeechTable.Cols.TITLE + ", " +
                SpeechTable.Cols.FAVORITE + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
