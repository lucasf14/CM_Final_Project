package com.example.cmproject.dbHandler;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cmproject.objects.Alarm;
import com.example.cmproject.objects.Contact;

import java.util.ArrayList;
import java.util.Arrays;
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
    private static final String ALARM_LAST_ARRAY_POS = "lastPos";
    private static final String CONTACTS_TABLE_NAME = "contacts";
    private static final String CONTACT_ID_COLUMN = "id";
    private static final String CONTACT_PERSON_COLUMN = "name";
    private static final String CONTACT_NUMBER_COLUMN = "number";

    public DatabaseHandler(Context context) {
        super(context);
    }

    public long addAlarm(Alarm alarm){
        ContentValues values = new ContentValues();
        values.put(ALARM_TITLE_COLUMN, alarm.getName());
        values.put(ALARM_ACTIVATE_COLUMN, alarm.getActivate());
        values.put(ALARM_HOUR_COLUMN, alarm.getHour());
        values.put(ALARM_MINUTE_COLUMN, alarm.getMinutes());
        values.put(ALARM_DAYS_COLUMN, Arrays.toString(alarm.getDays()));
        values.put(ALARM_LAST_ARRAY_POS, alarm.getLastPos());
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

    public void updateAlarmName(Alarm alarm){
        ContentValues values = new ContentValues();
        values.put(ALARM_TITLE_COLUMN, alarm.getName());
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
        values.put(ALARM_DAYS_COLUMN, Arrays.toString(alarm.getDays()));
        SQLiteDatabase database = this.getWritableDatabase();
        database.update(ALARMS_TABLE_NAME, values, ALARM_ID_COLUMN + "=" + alarm.getId(), null);
        database.close();
    }

    public void updateArrayLastPos(Alarm alarm){
        ContentValues values = new ContentValues();
        values.put(ALARM_LAST_ARRAY_POS, alarm.getLastPos());
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

    public Alarm getAlarm(int id){
        Alarm alarm = new Alarm(id,"Teste");
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + ALARMS_TABLE_NAME + " WHERE " + ALARM_ID_COLUMN + " = " + id, null);
        while(cursor.moveToNext()){
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ALARM_TITLE_COLUMN));
            @SuppressLint("Range") Boolean activate =  cursor.getInt(cursor.getColumnIndex(ALARM_ACTIVATE_COLUMN)) > 0;
            @SuppressLint("Range") Integer hour = cursor.getInt(cursor.getColumnIndex(ALARM_HOUR_COLUMN));
            @SuppressLint("Range") Integer minutes = cursor.getInt(cursor.getColumnIndex(ALARM_MINUTE_COLUMN));
            @SuppressLint("Range") String days = cursor.getString(cursor.getColumnIndex(ALARM_DAYS_COLUMN));
            alarm = new Alarm(id,name,activate,hour,minutes);
            alarm.setDays(days);
        }
        cursor.close();
        database.close();
        return alarm;
    }

    public void removeAlarm(long id){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(ALARMS_TABLE_NAME, ALARM_ID_COLUMN + "=?", new String[]{
                String.valueOf(id)
        });
        database.close();
    }

    public long addContact(Contact contact){
        ContentValues values = new ContentValues();
        values.put(CONTACT_PERSON_COLUMN, contact.getName());
        values.put(CONTACT_NUMBER_COLUMN, contact.getContact());
        SQLiteDatabase database = this.getWritableDatabase();
        long id = database.insert(CONTACTS_TABLE_NAME, null, values);
        database.close();
        return id;
    }

    public void removeContact(long id){
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(CONTACTS_TABLE_NAME, CONTACT_ID_COLUMN + "=?", new String[]{
                String.valueOf(id)
        });
        database.close();
    }

    public void updateContactInfo(Contact contact){
        ContentValues values = new ContentValues();
        values.put(CONTACT_PERSON_COLUMN, contact.getName());
        values.put(CONTACT_NUMBER_COLUMN, contact.getContact());
        SQLiteDatabase database = this.getWritableDatabase();
        database.update(CONTACTS_TABLE_NAME, values, CONTACT_PERSON_COLUMN + "=" + contact.getId(), null);
        database.close();
    }

    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + CONTACTS_TABLE_NAME, null);
        while(cursor.moveToNext()){
            @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(CONTACT_ID_COLUMN));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(CONTACT_PERSON_COLUMN));
            @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER_COLUMN));

            Contact contact = new Contact(id,name,number);

            contacts.add(contact);
        }
        cursor.close();
        database.close();
        return contacts;
    }
}