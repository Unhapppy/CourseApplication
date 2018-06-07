package com.haha.administrator.courseapplication.model;

import java.util.Date;

public class Student {
    private String idStr;
    private String nameStr;
    private String genderStr;
    private Date birthDate;
    private String passwordStr;
    private String speIdStr;
    private String speName;

    //注意，数据库模型中student不含成绩字段，为了方便将成绩加入以在特殊情况下使用
    private float grade;

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

    public String getSpeName() {
        return speName;
    }

    public void setSpeName(String speName) {
        this.speName = speName;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }
}
