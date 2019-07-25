package com.example.chatapp;

import com.sendbird.android.User;

public class UserMessage {
    int type;
    String time;
    String message;
    UserMessage(int type, String message, String time){
        this.type = type;
        this.message = message;
        this.time = time;
    }
    public int getType(){
        return this.type;
    }
    public String getMessage(){
        return this.message;
    }
    public String getCreateAt(){
        return this.time;
    }
}
