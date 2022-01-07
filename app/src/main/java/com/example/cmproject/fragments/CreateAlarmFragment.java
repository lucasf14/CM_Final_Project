package com.example.cmproject.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.cmproject.PersonalRecyclerView;
import com.example.cmproject.R;
import com.example.cmproject.fileHandler.AlarmHandler;
import com.example.cmproject.objects.Alarm;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateAlarmFragment  extends Fragment implements AlarmHandler.Callback {
    private Button buttonHours;
    private int hour;
    private int minutes;
    private EditText nameView;
    private String[] arrayDias = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    private boolean array[] = {false,false,false,false,false,false,false};

    public static CreateAlarmFragment newInstance() {
        CreateAlarmFragment fragment = new CreateAlarmFragment();
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
        return inflater.inflate(R.layout.create_alarm, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameView = (EditText) getActivity().findViewById(R.id.textAlarmName);
        buttonHours = (Button) getActivity().findViewById(R.id.buttonHours);
        Calendar rightNow = Calendar.getInstance();
        String texto = rightNow.get(Calendar.HOUR)+":"+rightNow.get(Calendar.MINUTE);

        buttonHours.setText(texto);
        buttonHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateHours(buttonHours);
            }
        });

        ToggleButton toggleMonday = (ToggleButton) getActivity().findViewById(R.id.toggleMonday);
        toggleMonday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // The toggle is enabled
                // The toggle is disabled
                array[1] = isChecked;
            }
        });
        ToggleButton toggleTuesday = (ToggleButton) getActivity().findViewById(R.id.toggleTuesday);
        toggleTuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // The toggle is enabled
                // The toggle is disabled
                array[2] = isChecked;
            }
        });
        ToggleButton toggleWednesday = (ToggleButton) getActivity().findViewById(R.id.toggleWednesday);
        toggleWednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // The toggle is enabled
                // The toggle is disabled
                array[3] = isChecked;
            }
        });
        ToggleButton toggleThursday = (ToggleButton) getActivity().findViewById(R.id.toggleThursday);
        toggleThursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // The toggle is enabled
                // The toggle is disabled
                array[4] = isChecked;
            }
        });
        ToggleButton toggleFriday = (ToggleButton) getActivity().findViewById(R.id.toggleFriday);
        toggleFriday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // The toggle is enabled
                // The toggle is disabled
                array[5] = isChecked;
            }
        });
        ToggleButton toggleSaturday = (ToggleButton) getActivity().findViewById(R.id.toggleSaturday);
        toggleSaturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // The toggle is enabled
                // The toggle is disabled
                array[6] = isChecked;
            }
        });
        ToggleButton toggleSunday = (ToggleButton) getActivity().findViewById(R.id.toggleSunday);
        toggleSunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                array[0] = isChecked;
            }
        });

        Button buttonCreate = (Button) getActivity().findViewById(R.id.buttonCreateAlarm);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alarm alarm = new Alarm(nameView.getText().toString(),hour,minutes);
                String dias = "[";
                int first = 0;
                for(int i = 0 ; i < array.length; i++){
                    if (array[i] && first == 0) {
                        dias += arrayDias[i];
                        first = 1;
                    }else if(array[i]){
                        dias += "," + arrayDias[i];
                    }
                }
                dias += "]";
                alarm.setDays(dias);
                System.out.println(dias);
                alarm.setActivate(false);
                alarm.setLastPos(-1);
                //System.out.println(Arrays.toString(alarm.getDays()));
                new AlarmHandler(getContext()).executeAsyncAddAlarm(alarm, CreateAlarmFragment.this);
                Bundle result = new Bundle();
                result.putString("bundleKey", "result");
                getParentFragmentManager().setFragmentResult("requestKey", result);
                FragmentManager manager = getParentFragmentManager();
                manager.popBackStack();
            }
        });
    }


    public void updateHours(Button button) {
        Calendar rightNow = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = hourOfDay + ":" + minute;
                        SimpleDateFormat horas = new SimpleDateFormat("HH:mm");
                        try {
                            Date date = horas.parse(time);
                            hour = hourOfDay;
                            minutes = minute;
                            button.setText(hour + ":" + minutes);
                            //guardar data
                        } catch (ParseException e) {
                            System.out.println("Erro nas datas");
                        }
                    }
                },rightNow.get(Calendar.HOUR),rightNow.get(Calendar.MINUTE),true);
        timePickerDialog.show();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.goBackButton:
                FragmentManager manager = getParentFragmentManager();
                manager.popBackStack();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onCompleteAdd(Alarm alarm) {

    }

    @Override
    public void onCompleteRemove(int position) {

    }

    @Override
    public void onCompleteGetList(List<Alarm> alarm) {

    }

    @Override
    public void onCompleteUpdateAlarm(int position) {

    }

    @Override
    public void onCompleteStartAlarm(int position, int dayOfWeek) {

    }
}