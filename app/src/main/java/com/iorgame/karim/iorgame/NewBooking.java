package com.iorgame.karim.iorgame;

/**
 * Created by karim on 29/05/2017.
 */

public class NewBooking {
    private String user_name;
    private String user_password;
    private String date;

    public NewBooking(String user_name, String user_password, String date) {
        this.user_name = user_name;
        this.user_password = user_password;
        this.date = date;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
