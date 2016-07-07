package com.example.shashank.bunkmeter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

/**
 * Created by Shashank Rao M on 05-07-2016.
 */
public class DB_Helper extends SQLiteOpenHelper {
    public DB_Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String DATABASE_NAME = "Bunkmeterdb.db";
    private static final int DATABASE_VERSION=1;
    public static final String KEY = "key";
    public static final String TABLE_NAME = "BunkOMeter";
    public static final String COLUMN_SUBJECT_NAME ="subject_name";
    public static final String COLUMN_HOURS_BUNKED = "hours_bunked";
    public static final String COLUMN_MAX_HOURS_BUNKED = "max_bunk_hours";
    private static String TABLE_CREATE =
            "CREATE TABLE  " + TABLE_NAME +"("+
                    KEY + "  INTEGER PRIMARY KEY AUTOINCREMENT , "+
                    COLUMN_SUBJECT_NAME + "  TEXT ,"+
                    COLUMN_MAX_HOURS_BUNKED+ "  INTEGER,"+
                    COLUMN_HOURS_BUNKED + "  INTEGER"+")";
    @Override
    public void onCreate(final SQLiteDatabase db) {
           db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion)
        {
            db.execSQL("DROP TABLE "+TABLE_NAME);
            onCreate(db);
        }
    }
}
