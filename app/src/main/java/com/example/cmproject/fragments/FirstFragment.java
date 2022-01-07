package com.example.cmproject.fragments;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.app.AlertDialog;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cmproject.alarmReceiver.AlarmBroadcastReceiver;
import com.example.cmproject.alarmReceiver.AlarmReceiveActivity;
import com.example.cmproject.PersonalRecyclerView;
import com.example.cmproject.R;
import com.example.cmproject.fileHandler.AlarmHandler;
import com.example.cmproject.objects.Alarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FirstFragment extends Fragment implements PersonalRecyclerView.ItemClickListener, AlarmHandler.Callback {

    private RecyclerView recyclerView;
    private ArrayList<Alarm> alarms;
    private PersonalRecyclerView adapter;
    private Context context;


    public static FirstFragment newInstance() {
        FirstFragment fragment = new FirstFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alarm, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                String result = bundle.getString("bundleKey");
                // Do something with the result
                onCompleteAdd(new Alarm(99999,"Teste"));
            }
        });

        context = getContext();

        loadDBInfo();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        loadDBInfo();
    }

    public void loadDBInfo() {
        new AlarmHandler(getContext()).executeAsyncGetAllAlarms(FirstFragment.this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_alarme, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addAlarmeButton:
                // User chose the "Settings" item, show the app settings UI...
                CreateAlarmFragment frag = (CreateAlarmFragment) getActivity().getSupportFragmentManager().findFragmentByTag("noteOptions");
                if(frag == null) {
                    CreateAlarmFragment noteChanger = CreateAlarmFragment.newInstance();
                    FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                    trans.replace(R.id.ContentActivityFrame, noteChanger, "noteOptions");
                    trans.addToBackStack("Top");
                    trans.commit();
                }else{
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onViewItemClick(View view, int position) {

    }

    @Override
    public void buttonOnClick(View view, int position) {
        PersonalRecyclerView adapter = (PersonalRecyclerView) recyclerView.getAdapter();
        Alarm alarm = adapter.getItem(position);
        System.out.println(alarm.getName());
        String[] options = {"Rename","Update Hours","Update Days","Delete","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose an option")
                .setItems(options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //rename alarm
                                renameAlarm(alarm, position);
                                break;
                            case 1:
                                //update hours
                                updateHours(alarm,position);
                                break;
                            case 2:
                                //update days
                                updateDays(alarm, position);
                                break;
                            case 3:
                                new AlarmHandler(getContext()).executeAsyncRemoveAlarm(alarm,position,FirstFragment.this);
                                break;
                            case 4:
                                dialog.dismiss();
                                break;
                        }

                    }
                });
        builder.show();
    }

    public void renameAlarm(Alarm alarm, int position){
        EditText edittext = new EditText(getContext());
        edittext.setHint("New medicine");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change Alarm name")
                .setView(edittext)
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int b) {
                        String text = edittext.getText().toString();
                        if (text.equals("") || alarm.getName().equals(text)) {
                            dialog.dismiss();
                            return;
                        }
                        else {
                            alarm.setName(text);
                            new AlarmHandler(getContext()).executeAsyncUpdateAlarmName(alarm,position,FirstFragment.this);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int b) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void updateHours(Alarm alarm, int position) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = hourOfDay + ":" + minute;
                        SimpleDateFormat horas = new SimpleDateFormat("HH:mm");
                        try {
                            Date date = horas.parse(time);
                            alarm.setHour(hourOfDay);
                            alarm.setMinutes(minute);
                            System.out.println(alarm.getHour()+":"+alarm.getMinutes());
                            new AlarmHandler(getContext()).executeAsyncUpdateAlarmTime(alarm,position,FirstFragment.this);

                            //guardar data
                        } catch (ParseException e) {
                            System.out.println("Erro nas datas");
                        }
                    }
                },alarm.getHour(),alarm.getMinutes(),true);
            timePickerDialog.show();


    }

    public void updateDays(Alarm alarm, int position){
        String[] items = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        boolean[] checked = {false,false,false,false,false,false,false};
        String[] days = alarm.getDays();
        for (String day : days) {
            System.out.println("OLA AMIGOS" + day);
            switch (day) {
                case "Monday":
                    checked[0] = true;
                    break;
                case "Tuesday":
                    checked[1] = true;
                    break;
                case "Wednesday":
                    checked[2] = true;
                    break;
                case "Thursday":
                    checked[3] = true;
                    break;
                case "Friday":
                    checked[4] = true;
                    break;
                case "Saturday":
                    checked[5] = true;
                    break;
                case "Sunday":
                    checked[6] = true;
                    break;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change Alarm name")
                .setMultiChoiceItems(items, checked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        switch(which){
                            case 0:
                                if(isChecked)
                                    Toast.makeText(context, "Selected Monday", Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                if(isChecked)
                                    Toast.makeText(context, "Selected Tuesday", Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                if(isChecked)
                                    Toast.makeText(context, "Selected Wednesday", Toast.LENGTH_LONG).show();
                                break;
                            case 3:
                                if(isChecked)
                                    Toast.makeText(context, "Selected Thursday", Toast.LENGTH_LONG).show();
                                break;
                            case 4:
                                if(isChecked)
                                    Toast.makeText(context, "Selected Friday", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                })
                .setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int b) {
                        StringBuilder array = new StringBuilder("[");
                        int first = 0;
                        for(int i = 0 ; i < checked.length ; i++){
                            if(checked[i] && first == 0){
                                array.append(items[i]);
                                first = 1;
                            }else if(checked[i]){
                                array.append(", ").append(items[i]);
                            }
                        }
                        array.append("]");
                        System.out.println(array);
                        alarm.setDays(array.toString());
                        new AlarmHandler(getContext()).executeAsyncUpdateAlarmDays(alarm,position,FirstFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int b) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCompleteAdd(Alarm alarm) {
        /*eliminar anteriores*/
        alarms.clear();
        adapter.notifyDataSetChanged();
        /*vai buscar todos*/
        loadDBInfo();
    }

    @Override
    public void onCompleteRemove(int position) {
        /*Elimina Alarme*/
        if(alarms.get(position).getActivate()){
            deleteAlarm(alarms.get(position));
        }

        alarms.remove(position);
        adapter.notifyItemChanged(position);
        adapter.notifyItemRangeRemoved(0, alarms.size());
    }

    @Override
    public void onCompleteGetList(List<Alarm> alarmsBD) {
        /*remove old adapter and reatch new one*/

        alarms = (ArrayList<Alarm>) alarmsBD;
        for(Alarm alarm: alarms){
            System.out.println("ID = " + alarm.getId());
            System.out.println("NAME = " + alarm.getName());
            System.out.println("HOUR = " + alarm.getHour());
            System.out.println("MINUTES = " + alarm.getMinutes());
            System.out.println("DAYS = " + Arrays.toString(alarm.getDays()));
            System.out.println("ACTIVATE = " + alarm.getActivate());
            System.out.println("LAST POS = " + alarm.getLastPos());
            System.out.println();
        }

        adapter = new PersonalRecyclerView(getContext(),alarms);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCompleteUpdateAlarm(int position) {
        adapter.notifyItemChanged(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCompleteStartAlarm(int position, int dayOfWeek) {
        Alarm alarm = adapter.getItem(position);
        if(alarm.getActivate().equals(true))
        {
            createAlarm(dayOfWeek,alarm);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void switchActivate(View view, int position) {
        Alarm alarm = adapter.getItem(position);
        alarm.setActivate(true);
        setAlarm(alarms.get(position), position);
        new AlarmHandler(getContext()).executeAsyncUpdateAlarmState(alarm,position,FirstFragment.this);
    }

    @Override
    public void switchDeactivate(View view, int position) {
        Alarm alarm = adapter.getItem(position);
        alarm.setActivate(false);
        deleteAlarm(alarms.get(position));
        new AlarmHandler(getContext()).executeAsyncUpdateAlarmState(alarm,position,FirstFragment.this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setAlarm(Alarm alarm, int position) {
        // Add this day of the week line to your existing code
        String[] days = alarm.getDays();
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
        alarm.setLastPos(array_index);
        new AlarmHandler(getContext()).executeAsyncUpdateArrayLastPost(alarm,dayOfWeek,position,FirstFragment.this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createAlarm(int dayOfWeek, Alarm alarm){
        System.out.println("ALARME CREATED ID= " + alarm.getId() + " DAY OF WEEK = " + dayOfWeek);
        Calendar calender= Calendar.getInstance();
        calender.setTimeInMillis(System.currentTimeMillis());

        //Set day of the week
        calender.set(Calendar.DAY_OF_WEEK, dayOfWeek);

        calender.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calender.set(Calendar.MINUTE, alarm.getMinutes());
        calender.set(Calendar.SECOND, 0);
        calender.set(Calendar.MILLISECOND, 0);

        String minutes = "";
        if(alarm.getMinutes() < 10){
            minutes += "0" + alarm.getMinutes();
        }else{
            minutes = alarm.getMinutes().toString();
        }

        Integer id = (int)alarm.getId();
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        String time = alarm.getHour().toString() + ":" +minutes;

        intent.putExtra("Time", time);
        intent.putExtra("Name", alarm.getName());
        intent.putExtra("Id", id.toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) alarm.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        //Also change the time to 24 hours.
        Long alarmTime = calender.getTimeInMillis();
        System.out.println(alarmTime);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        }

        Toast.makeText(context.getApplicationContext(), "Alarm Set", Toast.LENGTH_LONG).show();

    }

    public void deleteAlarm(Alarm alarm){
        Intent intent = new Intent(context, AlarmReceiveActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), (int) alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context.getApplicationContext(), "Alarm Disabled", Toast.LENGTH_LONG).show();
    }
}