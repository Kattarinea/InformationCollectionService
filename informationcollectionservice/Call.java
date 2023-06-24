package com.example.informationcollectionservice;

public class Call {
    private String id;
    private String date;
    private String number;
    private String name;
    private String type;
    private String duration;

    public String getId(){
        return id;
    }
    public String getDate(){
        return date;
    }
    public String getNumber(){
        return number;
    }
    public String getName(){
        return name;
    }
    public String getType(){
        return type;
    }
    public String getDuration(){
        return duration;
    }


    public void setId(String ID){
        id = ID;
    }
    public void setDate(String DATE){
        date = DATE;
    }
    public void setNumber(String NUMBER){
        number = NUMBER;
    }
    public void setName(String NAME){
        name = NAME;
    }
    public void setType(String TYPE){
        type = TYPE;
    }
    public void setDuration(String DURATION){
        duration = DURATION;
    }
}
