package com.example.informationcollectionservice;

import java.util.List;

public class SysInf {
    private String OSVersion;
    private Integer SDKVersion;
    private Long freeMemory;
    private List<Application> app;
    private List<String>account;
    private List<String>proc;

    public String getOSVersion(){
        return OSVersion;
    }
    public Integer getSDKVersion(){
        return SDKVersion;
    }
    public Long getFreeMemory(){
        return freeMemory;
    }
    public List<Application> getAppList(){
        return app;
    }
    public List<String> getAccountList(){return account;}
    public List<String> getProcessList(){return proc;}


    public void setOSVersion(String version){
        OSVersion = version;
    }
    public void setSDKVersion(Integer versionSDK){
        SDKVersion = versionSDK;
    }
    public void setFreeMemory(Long MEMORY){freeMemory = MEMORY;}
    public void setAppList(List<Application> APP){
        app = APP;
    }
    public void setAccountList(List<String> ACCOUNT){
        account = ACCOUNT;
    }
    public void setProcessList(List<String> PROC){
        proc = PROC;
    }
}
