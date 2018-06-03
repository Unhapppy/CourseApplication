package com.haha.administrator.courseapplication.model;

import java.util.Date;

public class Student {
    private String idStr;
    private String nameStr;
    private String genderStr;
    private Date birthDate;
    private String passwordStr;
    private String speIdStr;

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getNameStr() {
        return nameStr;
    }

    public void setNameStr(String nameStr) {
        this.nameStr = nameStr;
    }

    public String getGenderStr() {
        return genderStr;
    }

    public void setGenderStr(String genderStr) {
        this.genderStr = genderStr;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPasswordStr() {
        return passwordStr;
    }

    public void setPasswordStr(String passwordStr) {
        this.passwordStr = passwordStr;
    }

    public String getSpeIdStr() {
        return speIdStr;
    }

    public void setSpeIdStr(String speIdStr) {
        this.speIdStr = speIdStr;
    }
}
