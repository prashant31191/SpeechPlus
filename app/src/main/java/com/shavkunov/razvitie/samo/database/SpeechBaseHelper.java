package com.shavkunov.razvitie.samo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shavkunov.razvitie.samo.Constants.DbSchema;

public class SpeechBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "speechBase.db";

    private static String CREATE_TABLE_RU;
    private static String CREATE_TABLE_BE;
    private static String CREATE_TABLE_EN;
    private static String CREATE_TABLE_KK;
    private static String CREATE_TABLE_PL;
    private static String CREATE_TABLE_PT;
    private static String CREATE_TABLE_TR;
    private static String CREATE_TABLE_UK;

    private static String CREATE_TABLE_MY_TWISTERS;

    public SpeechBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);

        CREATE_TABLE_RU = onCreateTable(DbSchema.NAME_RU);
        CREATE_TABLE_BE = onCreateTable(DbSchema.NAME_BE);
        CREATE_TABLE_EN = onCreateTable(DbSchema.NAME_EN);
        CREATE_TABLE_KK = onCreateTable(DbSchema.NAME_KK);
        CREATE_TABLE_PL = onCreateTable(DbSchema.NAME_PL);
        CREATE_TABLE_PT = onCreateTable(DbSchema.NAME_PT);
        CREATE_TABLE_TR = onCreateTable(DbSchema.NAME_TR);
        CREATE_TABLE_UK = onCreateTable(DbSchema.NAME_UK);

        CREATE_TABLE_MY_TWISTERS = onCreateTable(DbSchema.MY_TWISTERS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RU);
        db.execSQL(CREATE_TABLE_BE);
        db.execSQL(CREATE_TABLE_EN);
        db.execSQL(CREATE_TABLE_KK);
        db.execSQL(CREATE_TABLE_PL);
        db.execSQL(CREATE_TABLE_PT);
        db.execSQL(CREATE_TABLE_TR);
        db.execSQL(CREATE_TABLE_UK);

        db.execSQL(CREATE_TABLE_MY_TWISTERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String onCreateTable(final String DATABASE_NAME) {
        return "create table " + DATABASE_NAME + "(" +
                " _id integer primary key autoincrement, " +
                DbSchema.Cols.ID + ", " +
                DbSchema.Cols.URL + ", " +
                DbSchema.Cols.TITLE + ", " +
                DbSchema.Cols.FAVORITE + ")";
    }
}
