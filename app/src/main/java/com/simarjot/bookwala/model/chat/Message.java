package com.simarjot.bookwala.model.chat;

import java.sql.Timestamp;
import java.util.Date;

public class Message {
    public static final int SENT = 11;
    public static final int RECIEVED = 12;
    private String mSender;
    private String mMessageBody;
    private Date mCreatedAt;


    public Message() {
        mCreatedAt = new Date();
    }

    public Message(String sender, String messageBody) {
        mSender = sender;
        mMessageBody = messageBody;
        mCreatedAt = new Timestamp(System.currentTimeMillis());
    }

    public String getSender() {
        return mSender;
    }

    public void setSender(String sender) {
        mSender = sender;
    }

    public String getMessageBody() {
        return mMessageBody;
    }

    public void setMessageBody(String messageBody) {
        mMessageBody = messageBody;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
    }
}
