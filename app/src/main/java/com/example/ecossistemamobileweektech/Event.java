package com.example.ecossistemamobileweektech;

public class Event {
    private String title;
    private String speaker;
    private String time;
    private String type; // Palestra, Projeto, etc.

    public Event(String title, String speaker, String time, String type) {
        this.title = title;
        this.speaker = speaker;
        this.time = time;
        this.type = type;
    }

    public String getTitle() { return title; }
    public String getSpeaker() { return speaker; }
    public String getTime() { return time; }
    public String getType() { return type; }
}