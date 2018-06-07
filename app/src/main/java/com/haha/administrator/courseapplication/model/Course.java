package com.haha.administrator.courseapplication.model;

public class Course {
    private String idStr;
    private String nameStr;
    private String periodStr;
    private String creditStr;
    private int numInt;
    private String beStr;
    private int timeInt;
    private String teaStr;
    private String teaNameStr;

    //注意，数据库模型中course不含成绩字段，为了方便将成绩加入以在特殊情况下使用
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

    public String getPeriodStr() {
        return periodStr;
    }

    public void setPeriodStr(String periodStr) {
        this.periodStr = periodStr;
    }

    public String getCreditStr() {
        return creditStr;
    }

    public void setCreditStr(String creditStr) {
        this.creditStr = creditStr;
    }

    public int getNumInt() {
        return numInt;
    }

    public void setNumInt(int numInt) {
        this.numInt = numInt;
    }

    public String getBeStr() {
        return beStr;
    }

    public void setBeStr(String beStr) {
        this.beStr = beStr;
    }

    public int getTimeInt() {
        return timeInt;
    }

    public void setTimeInt(int timeInt) {
        this.timeInt = timeInt;
    }

    public String getTeaStr() {
        return teaStr;
    }

    public void setTeaStr(String teaStr) {
        this.teaStr = teaStr;
    }

    public String getTeaNameStr() {
        return teaNameStr;
    }

    public void setTeaNameStr(String teaNameStr) {
        this.teaNameStr = teaNameStr;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }
}
