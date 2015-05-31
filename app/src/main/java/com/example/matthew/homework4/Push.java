package com.example.matthew.homework4;

import java.io.Serializable;

/**
 * Created by Matthew on 4/19/2015.
 */
public class Push implements Serializable{

    public String mName;
    public String mToDo;
    public String mTimeDateDue;

    public Push(String name, String toDo, String timeDateDue) {
        this.mName = name;
        this.mToDo = toDo;
        this.mTimeDateDue = timeDateDue;
    }

    public String getTitle() {
        return mName;
    }
    public void setTitle(String name) {
        mName = name;
    }
    public String getToDo() {
        return mToDo;
    }
    public void setToDo(String toDo) {
        mToDo = toDo;
    }
    public String getTimeDateDue() {
        return mTimeDateDue;
    }
    public void setTimeDateDue(String timeDateDue) {
        mTimeDateDue = timeDateDue;
    }


}
