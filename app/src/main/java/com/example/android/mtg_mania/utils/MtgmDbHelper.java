package com.example.android.mtg_mania.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.mtg_mania.MTGMApplicationContext;

public class MtgmDbHelper extends SQLiteOpenHelper {

    private static MtgmDbHelper dbInstance = null;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Mtgm.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String NOT_NULL = "NOT NULL";

    private static final String SQL_DELETE_NOTE_ENTRIES = "DROP TABLE IF EXISTS " + MtgmContract.NoteEntry.TABLE_NAME;
    private static final String SQL_CREATE_NOTE_ENTRIES = "CREATE TABLE IF NOT EXISTS " +
            MtgmContract.NoteEntry.TABLE_NAME + "(" +
            MtgmContract.NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MtgmContract.NoteEntry.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
            MtgmContract.NoteEntry.COLUMN_TEXT + TEXT_TYPE + COMMA_SEP +
            MtgmContract.NoteEntry.COLUMN_DATE + TEXT_TYPE + NOT_NULL + COMMA_SEP +
            MtgmContract.NoteEntry.COLUMN_TIME + TEXT_TYPE + NOT_NULL + " )";

    private MtgmDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized MtgmDbHelper getInstance() {

        Context context = MTGMApplicationContext.getAppContext();

        if (dbInstance == null) {
            dbInstance = new MtgmDbHelper(context);
        }
        return dbInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NOTE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_NOTE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion, newVersion);
    }
}
