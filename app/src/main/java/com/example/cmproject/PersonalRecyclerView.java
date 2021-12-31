package com.example.cmproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmproject.objects.Alarm;

import java.util.List;

public class PersonalRecyclerView extends RecyclerView.Adapter<PersonalRecyclerView.ViewHolder> {

    private List<Alarm> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public PersonalRecyclerView(Context context, List<Alarm> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.alarms_view, parent, false);
        return new ViewHolder(view);
    }


    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String hours, minutes;
        Alarm alarm = mData.get(position);
        holder.nameTextView.setText(alarm.getName());
        if(alarm.getHour() < 10)
            hours = "0"+alarm.getHour();
        else
            hours = alarm.getHour().toString();
        if(alarm.getMinutes() < 10)
            minutes = "0"+alarm.getMinutes();
        else
            minutes = alarm.getMinutes().toString();

        holder.timeTextView.setText(hours+":"+minutes);
        holder.daysTextView.setText(getDaysText(alarm));
        holder.switchView.setChecked(alarm.getActivate());

        holder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    System.out.println("VALIDO");
                }
                else {
                    System.out.println("INVALIDO");
                }
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView;
        TextView daysTextView;
        TextView timeTextView;
        Button settings;
        Switch switchView;



        ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.alarmeView);
            daysTextView = itemView.findViewById(R.id.daysView);
            timeTextView = itemView.findViewById(R.id.timeView);
            switchView = itemView.findViewById(R.id.activateView);
            settings = itemView.findViewById(R.id.buttonSettings);
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.buttonOnClick(v, getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Alarm getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    private String getDaysText(Alarm alarm){
        int weekdays = 0, weekends = 0;
        String final_string = "";
        for(int i = 0 ; i < alarm.getDays().length;i++){
            if(alarm.getDays()[i].equals("Monday")  || alarm.getDays()[i].equals("Tuesday") || alarm.getDays()[i].equals("Wednesday") || alarm.getDays()[i].equals("Thursday") || alarm.getDays()[i].equals("Friday")){
                weekdays += 1;
                final_string += alarm.getDays()[i] + " -";
            }else{
                weekends += 1;
                final_string += alarm.getDays()[i] + " -";
            }
        }
        if(weekdays == 5 && weekends == 0){
            final_string = "Working days only";
        }else if(weekends == 2 && weekdays == 5){
            final_string = "Every Day";
        } else if (weekends == 2 && weekdays == 0) {
            final_string = "weekends";
        }else if(final_string.endsWith("-")){
            final_string = final_string.substring(0,final_string.length() - 1);
        }

        return final_string;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void buttonOnClick(View view, int position);
    }
}
