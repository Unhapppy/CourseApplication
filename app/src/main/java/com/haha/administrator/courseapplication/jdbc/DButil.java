package com.haha.administrator.courseapplication.jdbc;

import android.util.Log;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DButil {

    public static String PasswordQuery(int accountType, String accountId) throws SQLException {
        //PreparedStatement 有效的防止sql注入(SQL语句在程序运行前已经进行了预编译,当运行时动态地把参数传给PreprareStatement时，即使参数里有敏感字符如 or '1=1'也数据库会作为一个参数一个字段的属性值来处理而不会作为一个SQL指令)
        String sql = "";
        String resultStr = "";
        //结果字段
        String resultLabel = "";
        //表名
        String tableName = "";
        //条件字段
        String conditionLabel = "";

        Connection connection = getConnection();

        //账号类型老师1
        //学生2
        //管理员3
        if (accountType == 1) {
            resultLabel = "tea_password";
            tableName = "teacher";
            conditionLabel = "tea_id";
        } else if (accountType == 2) {
            resultLabel = "stu_password";
            tableName = "student";
            conditionLabel = "stu_id";
        } else {
            resultLabel = "man_password";
            tableName = "manager";
            conditionLabel = "man_id";
        }
        sql = "select " + resultLabel + " from " + tableName + " where " + conditionLabel + "=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        //Log.e("测试连接", "PasswordQuery: "+connection.toString());
        ps.setString(1, accountId);  //占位符顺序从1开始
        //ps.setString(2, "123456"); //也可以使用setObject
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            resultStr = rs.getString(resultLabel);
            Log.e("返回值检查", "PasswordQuery: " + resultStr);
        }
        ps.close();
        rs.close();
        connection.close();
        return resultStr;
    }

    public static int CourseInsert(String id, String name, String period, String credit,
                                   String num, String beTime, String time, String teaId) throws SQLException {
        String sql = "";
        int resultId = 0;
        Connection connection = getConnection();

        sql = "insert into course(cou_id,cou_name,cou_period,cou_credit,cou_begin_end,cou_time,cou_num,tea_id) " +
                "values(?,?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        //Log.e("测试连接", "PasswordQuery: "+connection.toString());
        ps.setString(1, id);  //占位符顺序从1开始
        ps.setString(2, name);
        ps.setString(3, period);
        ps.setString(4, credit);
        ps.setString(5, beTime);
        ps.setInt(6, Integer.parseInt(time));
        ps.setInt(7, Integer.parseInt(num));
        ps.setString(8, teaId);
        //ps.setString(2, "123456"); //也可以使用setObject
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        while (rs.next()) {
            resultId = rs.getInt(1);
            Log.e("返回值检查", "PasswordQuery: " + resultId);
        }
        ps.close();
        rs.close();
        connection.close();
        return resultId;
    }

    public static void StudentInsert(String id, String name, String gender,
                                String birth, String password, String sprId) throws SQLException, ParseException {
        String sql = "";
        //int resultId = 0;
        Connection connection = getConnection();

        if(gender.equals("nan")){
            gender="男";
        }
        if(gender.equals("nv")){
            gender="女";
        }
        sql="insert into student(stu_id,stu_name,stu_gender,stu_birth,stu_password,spe_id) value(?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,id);
        ps.setString(2,name);
        ps.setString(3,gender);
        ps.setDate(4,DateGenerate(birth));
        ps.setString(5,password);
        ps.setString(6,sprId);;
        ps.executeUpdate();
        Log.d("updateCheck", "StudentInsert: "+"do execute");
        ps.close();
        connection.close();
    }

    public static void TeacherInsert(String id, String name, String gender,
                                     String birth, String password, String depId,String power) throws SQLException, ParseException {
        String sql = "";
        //int resultId = 0;
        Connection connection = getConnection();

        if(gender.equals("nan")){
            gender="男";
        }
        if(gender.equals("nv")){
            gender="女";
        }
        sql="insert into teacher(tea_id,tea_name,tea_gender,tea_birth,tea_password,dep_id,power) value(?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,id);
        ps.setString(2,name);
        ps.setString(3,gender);
        ps.setDate(4,DateGenerate(birth));
        ps.setString(5,password);
        ps.setString(6,depId);
        ps.setString(7,power);
        ps.executeUpdate();
        Log.d("updateCheck", "TeacherInsert: "+"do execute");
        ps.close();
        connection.close();
    }

    public static Date DateGenerate(String string) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date myDate = dateFormat.parse(string);
        Date dateObj=new Date(myDate.getTime());
        return dateObj;
    }

    public static void SelectControl(String flag) throws SQLException {
        //0 未开始 1开始
        String sql = "";
        //String resultStr = "";
        Connection connection = getConnection();

        sql = "update select_time set cou_control=? limit 1";
        PreparedStatement ps = connection.prepareStatement(sql);
        //Log.e("测试连接", "PasswordQuery: "+connection.toString());
        ps.setString(1, flag);
        ps.executeUpdate();
        ps.close();
        connection.close();
    }

    //select returnColumn from table where column = condition
    public static String QueryOneString(String returnColumn, String table, String column, String condition) throws SQLException {
        String sql = "";
        String resultStr = "";
        Connection connection = getConnection();
        sql = "select " + returnColumn + " from " + table + " where " + column + " = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        //Log.e("测试连接", "PasswordQuery: "+connection.toString());
        ps.setString(1, condition);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            resultStr = rs.getString(returnColumn);
        }
        ps.close();
        rs.close();
        connection.close();
        return resultStr;
    }

    public static List<String> QueryMoreString(String returnColumn, String table, String column, String condition)throws SQLException {
        String sql = "";
        List<String> resultList=new ArrayList<>();
        Connection connection = getConnection();
        sql = "select " + returnColumn + " from " + table + " where " + column + " like  ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        //Log.e("测试连接", "PasswordQuery: "+connection.toString());
        ps.setString(1, "%"+condition+"%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            resultList .add( rs.getString(returnColumn));
        }
        ps.close();
        rs.close();
        connection.close();
        return resultList;
    }


    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("驱动加载失败", e.toString());
        }
        try {
            //IP为服务器ip，DBNAME为数据库名字，DBUSER为数据库用户名，DBPASS为数据库密码
            //Connection connection = DriverManager.getConnection("jdbc:mysql://"+IP+":3306/"+DBNAME,DBUSER,DBPASS);
            //+"?autoReconnect=true"
            connection = DriverManager.getConnection("jdbc:mysql://" + "47.106.91.12" + ":3306/" + "course_system" +
                    "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false", "root", "951970312");
            Log.d("数据库连接成功", connection.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("数据库连接失败", e.toString());
        }
        return connection;
    }

}
