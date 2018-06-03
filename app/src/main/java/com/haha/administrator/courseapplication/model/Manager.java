package com.haha.administrator.courseapplication.model;

import java.util.Date;

public class Manager {
    private String idStr;
    private String namaStr;
    private String genderStr;
    private Date birthDate;
    private String passwordStr;


    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getNamaStr() {
        return namaStr;
    }

    public void setNamaStr(String namaStr) {
        this.namaStr = namaStr;
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
}
