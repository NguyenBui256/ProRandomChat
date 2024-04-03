package com.example.prorandomchata;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class User implements Serializable {
    protected String userName, userDescription, userGender, userLocation, userPassword,
            userFullname, userAvatarPath = "src/main/resources/img/ava.png";
    public String userPartnerName;

    public List<String> blockUsers = new ArrayList<String>();
    private int userAge;

    public boolean status = false;

    public User(String userName, String userPassword, String userFullname,  String userGender, String userLocation, int userAge) {
        this.userPassword = userPassword;
        this.userName = userName;
        this.userFullname = userFullname;
        this.userGender = userGender;
        this.userLocation = userLocation;
        this.userAge = userAge;
        this.userPartnerName = "";
    }

    public String getUserAvatarPath() {
        return userAvatarPath;
    }

    public void setUserAvatarPath(String userAvatarPath) {
        this.userAvatarPath = userAvatarPath;
    }

    public String getUserFullname() {
        return userFullname;
    }
    public String getUserPassword() {
        return userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public String getUserGender() {
        return userGender;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserFullname(String userFullname) {
        this.userFullname = userFullname;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

}
