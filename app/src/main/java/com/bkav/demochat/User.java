package com.bkav.demochat;

public class User {

    private int mId;
    private String mName, mLastMessage;
    private boolean mStatus;

    public User(int id,  String username, String lastmessage, boolean status){
        mId = id;
        mName = username;
        mLastMessage = lastmessage;
        mStatus = status;
    };

    public int getId(){
        return mId;
    }

    public String getUsername(){
        return mName;
    }

    public String getLastMessage(){
        return mLastMessage;
    }

    public boolean getStatus(){
        return mStatus;
    }

    public void setId(int id){
        mId = id;
    }

    public void setUsername(String username){
        mName = username;
    }

    public void setLastMessage(String lastmessage){
        mLastMessage = lastmessage;
    }

    public void setStatus(boolean status){
        mStatus = status;
    }
}
