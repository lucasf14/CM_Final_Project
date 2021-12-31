package com.example.cmproject.dbHandler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseCreatorHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME="projeto.db.";
    private static final String ALARMS_TABLE_NAME = "alarms";
    private static final String ALARM_ID_COLUMN = "id";
    private static final String ALARM_TITLE_COLUMN = "name";
    private static final String ALARM_ACTIVATE_COLUMN = "activate";
    private static final String ALARM_HOUR_COLUMN = "hour";
    private static final String ALARM_MINUTE_COLUMN = "minutes";
    private static final String ALARM_DAYS_COLUMN = "days";


    private static final String ALARM_TABLE_CREATE =
            "CREATE TABLE " + ALARMS_TABLE_NAME + " (" +
                    ALARM_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ALARM_TITLE_COLUMN + " TEXT, " +
                    ALARM_ACTIVATE_COLUMN + " BOOLEAN," +
                    ALARM_HOUR_COLUMN + " INTEGER, " +
                    ALARM_MINUTE_COLUMN + " INTEGER, " +
                    ALARM_DAYS_COLUMN + " TEXT);";
                    //Married boolean DEFAULT false

    public DatabaseCreatorHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ALARM_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
