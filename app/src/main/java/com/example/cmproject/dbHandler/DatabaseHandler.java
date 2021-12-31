package com.example.cmproject.dbHandler;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cmproject.objects.Alarm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHandler extends DatabaseCreatorHelper {
    private static final String ALARMS_TABLE_NAME = "alarms";
    private static final String ALARM_ID_COLUMN = "id";
    private static final String ALARM_TITLE_COLUMN = "name";
    private static final String ALARM_ACTIVATE_COLUMN = "activate";
    private static final String ALARM_HOUR_COLUMN = "hour";
    private static final String ALARM_MINUTE_COLUMN = "minutes";
    private static final String ALARM_DAYS_COLUMN = "days";

    public DatabaseHandler(Context context) {
        super(context);
    }

    public long addAlarm(Alarm alarm){
        ContentValues values = new ContentValues();
        values.put(ALARM_TITLE_COLUMN, alarm.getName());
        values.put(ALARM_ACTIVATE_COLUMN, alarm.getActivate());
        values.put(ALARM_HOUR_COLUMN, alarm.getHour());
        values.put(ALARM_MINUTE_COLUMN, alarm.getMinutes());
        values.put(ALARM_DAYS_COLUMN, alarm.getDays().toString());
        SQLiteDatabase database = this.getWritableDatabase();
        long id = database.insert(ALARMS_TABLE_NAME, null, values);
        database.close();
        return id;
    }


    public void updateAlarmTime(Alarm alarm){
        ContentValues values = new ContentValues();
        values.put(ALARM_HOUR_COLUMN, alarm.getHour());
        values.put(ALARM_MINUTE_COLUMN, alarm.getMinutes());
        SQLiteDatabase database = this.getWritableDatabase();
        database.update(ALARMS_TABLE_NAME, values, ALARM_ID_COLUMN + "=" + alarm.getId(), null);
        database.close();
    }

    public void updateAlarmeState(Alarm alarm){
        ContentValues values = new ContentValues();
        values.put(ALARM_ACTIVATE_COLUMN, alarm.getActivate());
        SQLiteDatabase database = this.getWritableDatabase();
        database.update(ALARMS_TABLE_NAME, values, ALARM_ID_COLUMN + "=" + alarm.getId(), null);
        database.close();
    }

    public void updateAlarmeDays(Alarm alarm){
        ContentValues values = new ContentValues();
        values.put(ALARM_DAYS_COLUMN, alarm.getDays().toString());
        SQLiteDatabase database = this.getWritableDatabase();
        database.update(ALARMS_TABLE_NAME, values, ALARM_ID_COLUMN + "=" + alarm.getId(), null);
        database.close();
    }

    public List<Alarm> getAlarms() {
        List<Alarm> alarms = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + ALARMS_TABLE_NAME, null);
        while(cursor.moveToNext()){
            @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(ALARM_ID_COLUMN));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ALARM_TITLE_COLUMN));
            @SuppressLint("Range") Boolean activate =  cursor.getInt(cursor.getColumnIndex(ALARM_ACTIVATE_COLUMN)) > 0;
            @SuppressLint("Range") Integer hour = cursor.getInt(cursor.getColumnIndex(ALARM_HOUR_COLUMN));
            @SuppressLint("Range") Integer minutes = cursor.getInt(cursor.getColumnIndex(ALARM_MINUTE_COLUMN));
            @SuppressLint("Range") String days = cursor.getString(cursor.getColumnIndex(ALARM_DAYS_COLUMN));

            Alarm alarm = new Alarm(id,name,activate,hour,minutes);
            alarm.setDays(days);

            alarms.add(alarm);
        }
        cursor.close();
        database.close();
        return alarms;
    }

    public void removeAlarm(long id){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(ALARMS_TABLE_NAME, ALARM_ID_COLUMN + "=?", new String[]{
                String.valueOf(id)
        });
        database.close();
    }
}