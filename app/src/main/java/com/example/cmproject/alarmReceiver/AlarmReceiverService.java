package com.example.cmproject.alarmReceiver;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.cmproject.fileHandler.AlarmHandler;
import com.example.cmproject.fragments.FirstFragment;
import com.example.cmproject.objects.Alarm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlarmReceiverService extends IntentService implements AlarmHandler.Callback{

    public AlarmReceiverService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //meter os alarmes a dar quando reiniciar a app
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        new AlarmHandler(this).executeAsyncGetAllAlarms(AlarmReceiverService.this);
        /*
        Intent alarmIntent = new Intent(this, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0, 5000, pendingIntent);*/
    }

    @Override
    public void onCompleteAdd(Alarm alarm) {

    }

    @Override
    public void onCompleteRemove(int position) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCompleteGetList(List<Alarm> alarm) {
        for (Alarm alm : alarm) {
            String[] days = alm.getDays();
            int found = 0, array_index = -1;
            int dayOfWeek = 0;

            Date dat  = new Date();//initializes to now
            Calendar cal_alarm = Calendar.getInstance();
            Calendar cal_now = Calendar.getInstance();
            cal_now.setTime(dat); //tempo de agora

            for (int i = 0; i < days.length; i++) {
                String day = days[i];
                switch (day) {
                    case "Monday":
                        dayOfWeek = Calendar.MONDAY;
                        break;
                    case "Tuesday":
                        dayOfWeek = Calendar.TUESDAY;
                        break;
                    case "Wednesday":
                        dayOfWeek = Calendar.WEDNESDAY;
                        break;
                    case "Thursday":
                        dayOfWeek = Calendar.THURSDAY;
                        break;
                    case "Friday":
                        dayOfWeek = Calendar.FRIDAY;
                        break;
                    case "Saturday":
                        dayOfWeek = Calendar.SATURDAY;
                        break;
                    case "Sunday":
                        dayOfWeek = Calendar.SUNDAY;
                }
                //cria alarme com as nossas especificações
                cal_alarm.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                cal_alarm.set(Calendar.HOUR_OF_DAY,5);//set the alarm time
                cal_alarm.set(Calendar.MINUTE, 59);
                cal_alarm.set(Calendar.SECOND,0);
                //no caso do horario ser depois; cria alarme
                if(!cal_alarm.before(cal_now)){
                    found = 1;
                    array_index = i;
                    break;
                }
            }

            if(found != 1)
                array_index = 0;

            alm.setLastPos(array_index);
            createAlarm(dayOfWeek,alm);
            new AlarmHandler(this).executeAsyncUpdateArrayLastPost(alm, dayOfWeek, 0, AlarmReceiverService.this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createAlarm(int dayOfWeek, Alarm alarm){
        Calendar calender= Calendar.getInstance();
        calender.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        calender.set(Calendar.HOUR, alarm.getHour());
        calender.set(Calendar.MINUTE, alarm.getMinutes());
        calender.set(Calendar.SECOND, 0);
        calender.set(Calendar.MILLISECOND, 0);
        Long alarmTime = calender.getTimeInMillis();
        String minutes = "";
        if(alarm.getMinutes() < 10){
            minutes += "0" + alarm.getMinutes();
        }

        Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
        intent.putExtra("Time", alarm.getHour().toString() + ":" +minutes);
        intent.putExtra("Name", alarm.getName());
        intent.putExtra("Id",alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Also change the time to 24 hours.
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, 24 * 60 * 60 * 1000 , pendingIntent);

    }

    @Override
    public void onCompleteUpdateAlarm(int position) {

    }

    @Override
    public void onCompleteStartAlarm(int position, int dayOfWeek) {

    }

}