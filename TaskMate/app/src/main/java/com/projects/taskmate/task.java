package com.projects.taskmate;

public class task {
    private String title;
    private  String description;
    private boolean isDone;
    private   boolean isHighPriority;
    private String dueDate;


    public String getDueDate() {
        return dueDate;
    }

    public task(String title, String description, boolean isDone, boolean isHighPriority, String dueDate) {
        this.title = title;
        this.description = description;
        this.isDone = isDone;
        this.isHighPriority = isHighPriority;
        this.dueDate = dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate=dueDate;
    }

    public  void setHighPriority(boolean HighPriority) {
        isHighPriority = HighPriority;
    }

    public  boolean isHighPriority() {
        return isHighPriority;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        this.isDone = done;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
