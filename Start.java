package com.example.informationcollectionservice;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

@SuppressLint("ValidFragment")
public class Start extends Fragment {
 public Context context;
    public Start()
    {
        this.context = MainActivity.context_main;
    }


    public void runService ()
    {
        this.context.startService(new Intent(this.context,Service.class));
    }


}
