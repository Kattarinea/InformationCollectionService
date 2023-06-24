package com.example.informationcollectionservice;

public class SMS {
    private String id;
    private String address;
    private String msg;
    private String readState;
    private String time;
    private String folderName;

    public String getId(){
        return id;
    }
    public String getAddress(){
        return address;
    }
    public String getMsg(){
        return msg;
    }
    public String getReadState(){
        return readState;
    }
    public String getTime(){
        return time;
    }
    public String getFolderName(){
        return folderName;
    }


    public void setId(String ID){
        id = ID;
    }
    public void setAddress(String ADDRESS){
        address = ADDRESS;
    }
    public void setMsg(String MSG){
        msg = MSG;
    }
    public void setReadState(String READ_STATE){
        readState = READ_STATE;
    }
    public void setTime(String TIME){
        time = TIME;
    }
    public void setFolderName(String FOLDER_NAME){
        folderName = FOLDER_NAME;
    }
}
