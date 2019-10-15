package com.example.android.mtg_mania.utils;

import android.provider.BaseColumns;

public final class MtgmContract {

    private MtgmContract(){}

    public static final class NoteEntry implements BaseColumns {

        public final static String TABLE_NAME = "notes";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_TEXT = "text";
        public final static String COLUMN_DATE = "date";
        public final static String COLUMN_TIME = "time";
    }
}
