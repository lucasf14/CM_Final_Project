package com.example.cmproject.objects;

public class Alarm {
    private long id;
    private String name;
    private Boolean activate;
    private Integer hour;
    private Integer minutes;
    private int lastPos;
    private String[] days;

    public Alarm(long id, String name){
        this.id = id;
        this.name = name;
    }
    public Alarm(String name, Integer hour, Integer minutes) {
        this.name = name;
        this.hour = hour;
        this.minutes = minutes;
    }

    public Alarm(long id, String name, Boolean activate, Integer hour, Integer minutes){
        this.id = id;
        this.name = name;
        this.activate = activate;
        this.hour = hour;
        this.minutes = minutes;
    }

    public Alarm(long id, String name, Boolean activate, Integer hour, Integer minutes, int lastPos){
        this.id = id;
        this.name = name;
        this.activate = activate;
        this.hour = hour;
        this.minutes = minutes;
        this.lastPos = lastPos;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActivate() {
        return activate;
    }

    public void setActivate(Boolean activate) {
        this.activate = activate;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        if(hour > 24){
            hour = hour - 24;
        }
        else if( hour < 0){
            hour = 0;
        }
        hour = Math.round(hour);
        this.hour = hour;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        if(minutes > 60){
            minutes = minutes - 60;
        }
        else if( minutes < 0){
            minutes = 0;
        }
        minutes = Math.round(minutes);
        this.minutes = minutes;
    }


    public String[] getDays() {
        return days;
    }

    public void setDays(String[] days) {
        this.days = days;
    }

    public void setDays(String days) {
        this.days = days.substring(1,days.length()-1).split(", ");
    }

    public int getLastPos() {
        return lastPos;
    }

    public void setLastPos(int lastPos) {
        this.lastPos = lastPos;
    }

    @Override
    public String toString() {
        return name;
    }
}
