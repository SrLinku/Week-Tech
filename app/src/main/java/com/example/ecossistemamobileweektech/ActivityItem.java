package com.example.ecossistemamobileweektech;

public class ActivityItem {
    private String name;
    private boolean project;
    private boolean approved;

    public ActivityItem(String name, boolean project, boolean approved) {
        this.name = name;
        this.project = project;
        this.approved = approved;
    }

    public String getName() { return name; }
    public boolean isProject() { return project; }
    public boolean isApproved() { return approved; }
}