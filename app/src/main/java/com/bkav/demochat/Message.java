package com.bkav.demochat;

public class Message {

    private int mIdSender;
    private int mIdReceiver;
    private String mContent;
    private String mDate;

    public Message(int idSender, int idReceiver, String content, String date) {
        this.mIdSender = idSender;
        this.mIdReceiver = idReceiver;
        this.mContent = content;
        this.mDate = date;
    }

    public int getIdSender() {
        return mIdSender;
    }

    public int getIdReceiver() {
        return mIdReceiver;
    }

    public String getContent() {
        return mContent;
    }

    public String getDate() {
        return mDate;
    }

    public void setIdSender(int idSender) {
        this.mIdSender = idSender;
    }

    public void setIdReceiver(int idReceiver) {
        this.mIdReceiver = idReceiver;
    }

    public void setmContent(String content) {
        this.mContent = content;
    }

    public void setmDate(String date) {
        this.mDate = date;
    }
}
