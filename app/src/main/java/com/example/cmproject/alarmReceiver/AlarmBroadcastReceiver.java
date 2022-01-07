package com.example.cmproject.alarmReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.cmproject.dbHandler.DatabaseHandler;
import com.example.cmproject.objects.Alarm;

public class AlarmBroadcastReceiver extends BroadcastReceiver  {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            //manda para intent para correr na background
            Intent serviceIntent = new Intent(context, AlarmReceiverService.class);
            context.startService(serviceIntent);
        }   //verifica se a data de hoje e hora coincide com o alarme; se sim inicia alarme
            //se alarme tiver mais que um dia meter para o dia a seguir
        else {
            System.out.println("ALARM ENTERED");

            Toast.makeText(context.getApplicationContext(), "Alarm Start", Toast.LENGTH_LONG).show();

            System.out.println(intent.getStringExtra("Name"));
            System.out.println(intent.getStringExtra("Time"));
            System.out.println(intent.getStringExtra("Id"));
            int id = Integer.parseInt(intent.getStringExtra("Id"));

            Alarm alarm = (new DatabaseHandler(context).getAlarm(id));
            createAlarm(context,alarm);


            Intent activity = new Intent(context, AlarmReceiveActivity.class);
            activity.addFlags(activity.FLAG_ACTIVITY_NEW_TASK);
            activity.putExtra("Time", (intent.getStringExtra("Time")));
            activity.putExtra("Name", intent.getStringExtra("Name"));
            context.startActivity(activity);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createAlarm(Context context, Alarm alarm){
        String[] days = alarm.getDays();
        int last_called_alarm = alarm.getLastPos();
        int dayOfWeek = 0,i;
        if((last_called_alarm == days.length))
            i = 0;
        else
            i = last_called_alarm + 1;
        System.out.println("LAST INDEX = " + last_called_alarm);
        label:
        for (; i < days.length; i++) {
            String day = days[i];
            switch (day) {
                case "Monday":
                    dayOfWeek = Calendar.MONDAY;
                    break label;
                case "Tuesday":
                    dayOfWeek = Calendar.TUESDAY;
                    break label;
                case "Wednesday":
                    dayOfWeek = Calendar.WEDNESDAY;
                    break label;
                case "Thursday":
                    dayOfWeek = Calendar.THURSDAY;
                    break label;
                case "Friday":
                    dayOfWeek = Calendar.FRIDAY;
                    break label;
                case "Saturday":
                    dayOfWeek = Calendar.SATURDAY;
                    break label;
                case "Sunday":
                    dayOfWeek = Calendar.SUNDAY;
                    break label;
            }
        }

        alarm.setLastPos(i);
        System.out.println("NEXT INDEX = " + i);

        new DatabaseHandler(context).updateArrayLastPos(alarm);

        System.out.println("ALARME CREATED ID= " + alarm.getId() + " DAY OF WEEK = " + dayOfWeek);
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

        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra("Time", alarm.getHour().toString() + ":" +minutes);
        intent.putExtra("Name", alarm.getName());
        intent.putExtra("Id",alarm.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Also change the time to 24 hours.
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, 24 * 60 * 60 * 1000 , pendingIntent);
        System.out.println("ALARM CREATED");

    }
}
