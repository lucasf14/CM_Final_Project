package com.example.cmproject.fileHandler;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.cmproject.dbHandler.DatabaseHandler;
import com.example.cmproject.objects.Alarm;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlarmHandler {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    private Context context;

    public interface Callback{
        void onCompleteAdd(Alarm alarm);
        void onCompleteRemove(int position);
        void onCompleteGetList(List<Alarm> alarm);
        void onCompleteUpdateAlarm(int position);
        void onCompleteStartAlarm(int position,int dayOfWeek);
    }

    public AlarmHandler(Context context) {
        this.context = context;
    }

    public void executeAsyncAddAlarm(Alarm alarm, Callback callback) {
        executor.execute(() -> {
            new DatabaseHandler(context).addAlarm(alarm);
            handler.post(() -> {
                callback.onCompleteAdd(alarm);
            });

        });
    }

    public void executeAsyncUpdateAlarmName(Alarm alarm,int position, Callback callback) {
        executor.execute(() -> {
            new DatabaseHandler(context).updateAlarmName(alarm);
            handler.post(() -> {
                callback.onCompleteUpdateAlarm(position);
            });

        });
    }

    public void executeAsyncUpdateAlarmTime(Alarm alarm,int position, Callback callback) {
        executor.execute(() -> {
            new DatabaseHandler(context).updateAlarmTime(alarm);
            handler.post(() -> {
                callback.onCompleteUpdateAlarm(position);
            });

        });
    }

    public void executeAsyncUpdateAlarmState(Alarm alarm,int position, Callback callback) {
        executor.execute(() -> {
            new DatabaseHandler(context).updateAlarmeState(alarm);
            handler.post(() -> {
                callback.onCompleteUpdateAlarm(position);
            });

        });
    }

    public void executeAsyncUpdateAlarmDays(Alarm alarm, int position,Callback callback) {
        executor.execute(() -> {
            new DatabaseHandler(context).updateAlarmeDays(alarm);
            handler.post(() -> {
                callback.onCompleteUpdateAlarm(position);
            });

        });
    }

    public void executeAsyncUpdateArrayLastPost(Alarm alarm, int dayOfWeek,int position,Callback callback) {
        executor.execute(() -> {
            new DatabaseHandler(context).updateArrayLastPos(alarm);
            handler.post(() -> {
                callback.onCompleteStartAlarm(position, dayOfWeek);
            });

        });
    }


    public void executeAsyncRemoveAlarm(Alarm alarm,int position, Callback callback) {
        executor.execute(() -> {
            new DatabaseHandler(context).removeAlarm(alarm.getId());
            handler.post(() -> {
                callback.onCompleteRemove(position);
            });
        });
    }

    public void executeAsyncGetAllAlarms(Callback callback) {
        executor.execute(() -> {
            handler.post(() -> {
                callback.onCompleteGetList(new DatabaseHandler(context).getAlarms());
            });
        });
    }
}
