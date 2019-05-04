package com.project.pk.patrolmonitor;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Patrol extends Application {

    public void onCreate()
    {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
