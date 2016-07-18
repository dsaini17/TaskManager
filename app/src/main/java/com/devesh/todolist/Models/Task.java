package com.devesh.todolist.Models;

/**
 * Created by Devesh on 13-07-2016.
 */
public class Task {

    public int id;
    public String todotask;
    public String date;
    public int priority;
    public int done;

    public Task(int id,String todotask, String date, int priority, int done) {
        this.id = id;
        this.todotask = todotask;
        this.date = date;
        this.priority = priority;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public String getTodotask() {
        return todotask;
    }

    public String getDate() {
        return date;
    }

    public int getPriority() {
        return priority;
    }

    public int getDone() {
        return done;
    }
}
