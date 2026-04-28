package com.example.ecossistemamobileweektech;

public class Participant {
    private String name;
    private String ra;
    private String course;
    private String series;
    private String activity;
    private boolean coffee;
    private boolean attended;

    public Participant(String name, String ra, String course, String series, String activity, boolean coffee) {
        this.name = name;
        this.ra = ra;
        this.course = course;
        this.series = series;
        this.activity = activity;
        this.coffee = coffee;
        this.attended = false;
    }

    public String getName() { return name; }
    public String getRa() { return ra; }
    public String getCourse() { return course; }
    public String getSeries() { return series; }
    public String getActivity() { return activity; }
    public boolean isCoffee() { return coffee; }
    public boolean isAttended() { return attended; }
    public void setAttended(boolean attended) { this.attended = attended; }
}