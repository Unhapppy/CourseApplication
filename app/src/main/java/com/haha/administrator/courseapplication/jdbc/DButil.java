package com.haha.administrator.courseapplication.jdbc;

import android.util.Log;
import android.widget.ListView;


import com.haha.administrator.courseapplication.model.Course;
import com.haha.administrator.courseapplication.model.Manager;
import com.haha.administrator.courseapplication.model.Student;
import com.haha.administrator.courseapplication.model.Teacher;

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

        if (gender.equals("nan")) {
            gender = "男";
        }
        if (gender.equals("nv")) {
            gender = "女";
        }
        sql = "insert into student(stu_id,stu_name,stu_gender,stu_birth,stu_password,spe_id) value(?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, id);
        ps.setString(2, name);
        ps.setString(3, gender);
        ps.setDate(4, DateGenerate(birth));
        ps.setString(5, password);
        ps.setString(6, sprId);
        ps.executeUpdate();
        Log.d("updateCheck", "StudentInsert: " + "do execute");
        ps.close();
        connection.close();
    }

    public static void TeacherInsert(String id, String name, String gender,
                                     String birth, String password, String depId, String power) throws SQLException, ParseException {
        String sql = "";
        //int resultId = 0;
        Connection connection = getConnection();

        if (gender.equals("nan")) {
            gender = "男";
        }
        if (gender.equals("nv")) {
            gender = "女";
        }
        sql = "insert into teacher(tea_id,tea_name,tea_gender,tea_birth,tea_password,dep_id,power) value(?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, id);
        ps.setString(2, name);
        ps.setString(3, gender);
        ps.setDate(4, DateGenerate(birth));
        ps.setString(5, password);
        ps.setString(6, depId);
        ps.setString(7, power);
        ps.executeUpdate();
        Log.d("updateCheck", "TeacherInsert: " + "do execute");
        ps.close();
        connection.close();
    }

    public static Date DateGenerate(String string) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date myDate = dateFormat.parse(string);
        Date dateObj = new Date(myDate.getTime());
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

    public static boolean CanSelect() throws SQLException {
        //0 未开始 1开始
        String sql = "";
        //String resultStr = "";
        Connection connection = getConnection();
        String flagStr="";
        int flag=-1;

        sql = "select cou_control from select_time limit 1";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            flagStr=rs.getString("cou_control");
        }
        flag=Integer.parseInt(flagStr);
        if(flag==1){
            return true;
        }else {
            return false;
        }
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

    public static List<String> QueryMoreString(String returnColumn, String table, String column, String condition) throws SQLException {
        String sql = "";
        Connection connection = getConnection();
        List<String> resultList = new ArrayList<>();
        sql = "select " + returnColumn + " from " + table + " where " + column + " like  ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        //Log.e("测试连接", "PasswordQuery: "+connection.toString());
        ps.setString(1, "%" + condition + "%");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            resultList.add(rs.getString(returnColumn));
        }
        ps.close();
        rs.close();
        connection.close();
        return resultList;
    }

    public static Student queryOneStudent(String request) throws SQLException {
        Student student = new Student();
        String sql = "";
        Connection connection = getConnection();
        if (request.matches("[0-9]{1,}")) {
            //是学号的情况
            sql = "select student.*,specialty.spe_name from student,specialty where student.stu_id=? and student.spe_id=specialty.spe_id;";
            PreparedStatement ps = connection.prepareStatement(sql);
            //Log.e("测试连接", "PasswordQuery: "+connection.toString());
            ps.setString(1, request);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                student.setIdStr(rs.getString("stu_id"));
                student.setNameStr(rs.getString("stu_name"));
                student.setGenderStr(rs.getString("stu_gender"));
                student.setBirthDate(rs.getDate("stu_birth"));
                student.setPasswordStr(rs.getString("stu_password"));
                student.setSpeIdStr(rs.getString("spe_id"));
                student.setSpeName(rs.getString("spe_name"));
            }
            ps.close();
            rs.close();
            connection.close();
            return student;
        } else {
            //是名字
            sql = "select student.*,specialty.spe_name from student,specialty where student.stu_name=? and student.spe_id=specialty.spe_id;";
            PreparedStatement ps = connection.prepareStatement(sql);
            //Log.e("测试连接", "PasswordQuery: "+connection.toString());
            ps.setString(1, request);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                student.setIdStr(rs.getString("stu_id"));
                student.setNameStr(rs.getString("stu_name"));
                student.setGenderStr(rs.getString("stu_gender"));
                student.setBirthDate(rs.getDate("stu_birth"));
                student.setPasswordStr(rs.getString("stu_password"));
                student.setSpeIdStr(rs.getString("spe_id"));
                student.setSpeName(rs.getString("spe_name"));
            }
            ps.close();
            rs.close();
            connection.close();
            return student;
        }
    }

    public static void modifyStu(String id, String name, String gender,
                                 String birth, String password, String sprId) throws SQLException, ParseException {
        String sql = "";
        //int resultId = 0;
        Connection connection = getConnection();
        if (gender.equals("nan")) {
            gender = "男";
        }
        if (gender.equals("nv")) {
            gender = "女";
        }
        sql = "update student set stu_name=?,stu_gender=?,stu_birth=?,stu_password=?,spe_id=? where stu_id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, gender);
        ps.setDate(3, DateGenerate(birth));
        ps.setString(4, password);
        ps.setString(5, sprId);
        ps.setString(6, id);
        ps.executeUpdate();
        Log.d("updateCheck", "StudentInsert: " + "do execute");
        ps.close();
        connection.close();
    }

    public static void modifyTea(String id, String name, String gender,
                                 String birth, String password, String depId, String power) throws SQLException, ParseException {
        String sql = "";
        //int resultId = 0;
        Connection connection = getConnection();
        if (gender.equals("nan")) {
            gender = "男";
        }
        if (gender.equals("nv")) {
            gender = "女";
        }
        sql = "update teacher set tea_name=?,tea_gender=?,tea_birth=?,tea_password=?,dep_id=? ,power=? where tea_id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, gender);
        ps.setDate(3, DateGenerate(birth));
        ps.setString(4, password);
        ps.setString(5, depId);
        ps.setString(6, power);
        ps.setString(7, id);
        ps.executeUpdate();
        ps.close();
        connection.close();
    }

    public static Teacher queryOneTeacher(String request) throws SQLException {
        Teacher teacher = new Teacher();
        String sql = "";
        Connection connection = getConnection();
        if (request.matches("[0-9]{1,}")) {
            //是工号的情况
            sql = "select teacher.*,department.dep_name from teacher,department where teacher.tea_id=? and teacher.dep_id=department.dep_id";
            PreparedStatement ps = connection.prepareStatement(sql);
            //Log.e("测试连接", "PasswordQuery: "+connection.toString());
            ps.setString(1, request);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                teacher.setIdStr(rs.getString("tea_id"));
                teacher.setNameStr(rs.getString("tea_name"));
                teacher.setGenderStr(rs.getString("tea_gender"));
                teacher.setBirthDate(rs.getDate("tea_birth"));
                teacher.setPasswordStr(rs.getString("tea_password"));
                teacher.setDepIdStr(rs.getString("dep_id"));
                teacher.setPower(rs.getString("power"));
                teacher.setDepName(rs.getString("dep_name"));
            }
            ps.close();
            rs.close();
            connection.close();
            return teacher;
        } else {
            //是名字
            sql = "select teacher.*,department.dep_name from teacher,department where teacher.tea_name=? and teacher.dep_id=department.dep_id";
            PreparedStatement ps = connection.prepareStatement(sql);
            //Log.e("测试连接", "PasswordQuery: "+connection.toString());
            ps.setString(1, request);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                teacher.setIdStr(rs.getString("tea_id"));
                teacher.setNameStr(rs.getString("tea_name"));
                teacher.setGenderStr(rs.getString("tea_gender"));
                teacher.setBirthDate(rs.getDate("tea_birth"));
                teacher.setPasswordStr(rs.getString("tea_password"));
                teacher.setDepIdStr(rs.getString("dep_id"));
                teacher.setPower(rs.getString("power"));
                teacher.setDepName(rs.getString("dep_name"));
            }
            ps.close();
            rs.close();
            connection.close();
            return teacher;
        }
    }

    public static void modifyCou(String id, String name, String period,
                                 String credit, String number, String be, String time, String teaId) throws SQLException {
        //此处time number对应数据库类型是int
        String sql = "";
        //int resultId = 0;
        Connection connection = getConnection();
        sql = "update course set cou_name=?,cou_period=?,cou_credit=?,cou_num=?,cou_begin_end=? ,cou_time=? ,tea_id=?,where cou_id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, period);
        ps.setString(3, credit);
        ps.setInt(4, Integer.parseInt(number));
        ps.setString(5, be);
        ps.setInt(6, Integer.parseInt(time));
        ps.setString(7, teaId);
        ps.setString(8, id);
        ps.executeUpdate();
        ps.close();
        connection.close();
    }

    //TODO 补完查询一个课程界面，所有课程界面，写选课，写成绩
    public static Course queryOneCourse(String request) throws SQLException {
        Course course = new Course();
        String sql = "";
        Connection connection = getConnection();

        if (request.matches("[0-9]{1,}")) {
            //是工号的情况
            sql = "select * from course where cou_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            //Log.e("测试连接", "PasswordQuery: "+connection.toString());
            ps.setString(1, request);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                course.setIdStr(rs.getString("cou_id"));
                course.setNameStr(rs.getString("cou_name"));
                course.setPeriodStr(rs.getString("cou_period"));
                course.setCreditStr(rs.getString("cou_credit"));
                course.setNumInt(rs.getInt("cou_num"));
                course.setBeStr(rs.getString("cou_begin_end"));
                course.setTimeInt(rs.getInt("cou_time"));
                course.setTeaStr(rs.getString("tea_id"));
            }
            ps.close();
            rs.close();
            connection.close();
            return course;
        } else {
            //是名字
            sql = "select * from course where cou_name = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            //Log.e("测试连接", "PasswordQuery: "+connection.toString());
            ps.setString(1, request);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                course.setIdStr(rs.getString("cou_id"));
                course.setNameStr(rs.getString("cou_name"));
                course.setPeriodStr(rs.getString("cou_period"));
                course.setCreditStr(rs.getString("cou_credit"));
                course.setNumInt(rs.getInt("cou_num"));
                course.setBeStr(rs.getString("cou_begin_end"));
                course.setTimeInt(rs.getInt("cou_time"));
                course.setTeaStr(rs.getString("tea_id"));
            }
            ps.close();
            rs.close();
            connection.close();
            return course;
        }
    }

    public static List<Course> queryAllCoures() throws SQLException {
        List<Course> resultList = new ArrayList<Course>();
        String sql = "";
        Connection connection = getConnection();
        sql = "select * from course";
        PreparedStatement ps = connection.prepareStatement(sql);
        //Log.e("测试连接", "PasswordQuery: "+connection.toString());
        //Log.d("预处理", "queryAllCoures: ");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Course course = new Course();
            course.setIdStr(rs.getString("cou_id"));
            course.setNameStr(rs.getString("cou_name"));
            course.setPeriodStr(rs.getString("cou_period"));
            course.setCreditStr(rs.getString("cou_credit"));
            course.setNumInt(rs.getInt("cou_num"));
            course.setBeStr(rs.getString("cou_begin_end"));
            course.setTimeInt(rs.getInt("cou_time"));
            course.setTeaStr(rs.getString("tea_id"));
            //Log.d("sql所有课程", "queryAllCoures: " + course.toString());
            resultList.add(course);
        }
        ps.close();
        rs.close();
        connection.close();
        return resultList;
    }


    public static void deleteOneRecord(String table, String column, String condition) throws SQLException {
        String sql = "";
        Connection connection = getConnection();
        sql = "delete from " + table + " where " + column + " = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        //Log.e("测试连接", "PasswordQuery: "+connection.toString());
        ps.setString(1, condition);
        ps.execute();
        ps.close();
        connection.close();
    }

    public static Manager queryManPerson(String id) throws SQLException {
        Manager manager = new Manager();
        String sql = "";
        Connection connection = getConnection();
        sql = "select * from manager where man_id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        //Log.e("测试连接", "PasswordQuery: "+connection.toString());
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            manager.setIdStr(rs.getString("man_id"));
            manager.setNameStr(rs.getString("man_name"));
            manager.setGenderStr(rs.getString("man_gender"));
            manager.setBirthDate(rs.getDate("man_birth"));
            manager.setPasswordStr(rs.getString("man_password"));
        }
        ps.close();
        rs.close();
        connection.close();
        return manager;
    }

    public static List<Course> querySelectedCourse(String id) throws SQLException {
        List<Course> resultList = new ArrayList<Course>();
        String sql = "";
        Connection connection = getConnection();
        sql = "select course.* from stucou,course where stucou.stu_id=? and course.cou_id=stucou.cou_id";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,id);
        //Log.e("测试连接", "PasswordQuery: "+connection.toString());
        //Log.d("预处理", "queryAllCoures: ");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Course course = new Course();
            course.setIdStr(rs.getString("cou_id"));
            course.setNameStr(rs.getString("cou_name"));
            course.setPeriodStr(rs.getString("cou_period"));
            course.setCreditStr(rs.getString("cou_credit"));
            course.setNumInt(rs.getInt("cou_num"));
            course.setBeStr(rs.getString("cou_begin_end"));
            course.setTimeInt(rs.getInt("cou_time"));
            course.setTeaStr(rs.getString("tea_id"));
            //Log.d("sql所有课程", "queryAllCoures: " + course.toString());
            resultList.add(course);
        }
        ps.close();
        rs.close();
        connection.close();
        return resultList;
    }

    public static int querySeletedNumber(String couId) throws SQLException {
        String sql = "";
        int resultInt=0;
        Connection connection = getConnection();
        sql = "select count(*) as countNum from stucou where cou_id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,couId);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            resultInt=rs.getInt("countNum");
        }
        ps.close();
        rs.close();
        connection.close();
        return resultInt;
    }

    public static boolean isTimeRepeated(String id,int time) throws SQLException {

        String sql = "";
        int resultInt=0;
        Connection connection = getConnection();
        sql = "select count(*) as count from course,stucou where cou_time=? and stucou.stu_id=? and course.cou_id = stucou.cou_id";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1,time);
        ps.setString(2,id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            resultInt=rs.getInt(1);
        }
        //Log.e("重复", "isTimeRepeated: "+resultInt);
        ps.close();
        rs.close();
        connection.close();
        return resultInt!=0;
    }

    public static void stuCouInsert(String stuId,String couId) throws SQLException {
        String sql = "";
        //int resultId = 0;
        Connection connection = getConnection();

        sql = "insert into stucou(stu_id,cou_id) value(?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, stuId);
        ps.setString(2, couId);
        ps.executeUpdate();
        //Log.d("updateCheck", "StudentInsert: " + "do execute");
        ps.close();
        connection.close();
    }

    /**
     * 此方法查询选中某课的学生
     * @param id 传入的是该课程的id
     * @return resultList
     * @throws SQLException
     */
    public static List<Student> querySelectedStudent(String id) throws SQLException {
        List<Student> resultList = new ArrayList<>();
        String sql = "";
        Connection connection = getConnection();
        sql = "select student.*,specialty.spe_name,grade from student,stucou,specialty where student.stu_id=stucou.stu_id and student.spe_id = specialty.spe_id and stucou.cou_id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,id);
        //Log.e("测试连接", "PasswordQuery: "+connection.toString());
        //Log.d("预处理", "queryAllCoures: ");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Student student=new Student();
            student.setIdStr(rs.getString("stu_id"));
            student.setNameStr(rs.getString("stu_name"));
            student.setGenderStr(rs.getString("stu_gender"));
            student.setBirthDate(rs.getDate("stu_birth"));
            student.setPasswordStr(rs.getString("stu_password"));
            student.setSpeIdStr(rs.getString("spe_id"));
            student.setSpeName(rs.getString("spe_name"));
            student.setGrade(rs.getFloat("grade"));
            //Log.d("sql所有课程", "queryAllCoures: " + course.toString());
            resultList.add(student);
        }
        ps.close();
        rs.close();
        connection.close();
        return resultList;
    }

    /**
     * 此方法查询的是老师的课程
     * @param id 老师id
     * @return
     * @throws SQLException
     */
    public static List<Course> queryTeaCoures(String id) throws SQLException {
        List<Course> resultList = new ArrayList<Course>();
        String sql = "";
        Connection connection = getConnection();
        sql = "select * from course where tea_id =? order by cou_time";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Course course = new Course();
            course.setIdStr(rs.getString("cou_id"));
            course.setNameStr(rs.getString("cou_name"));
            course.setPeriodStr(rs.getString("cou_period"));
            course.setCreditStr(rs.getString("cou_credit"));
            course.setNumInt(rs.getInt("cou_num"));
            course.setBeStr(rs.getString("cou_begin_end"));
            course.setTimeInt(rs.getInt("cou_time"));
            course.setTeaStr(rs.getString("tea_id"));
            resultList.add(course);
        }
        ps.close();
        rs.close();
        connection.close();
        return resultList;
    }

    /**
     *
     * @param id 学生id
     * @return
     * @throws SQLException
     */
    public static List<Course> queryStuCoures(String id) throws SQLException {
        List<Course> resultList = new ArrayList<Course>();
        String sql = "";
        Connection connection = getConnection();
        sql = "select course.*,teacher.tea_name,stucou.grade from course,stucou,teacher where course.cou_id=stucou.cou_id and teacher.tea_id=course.tea_id and stucou.stu_id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1,id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Course course = new Course();
            course.setIdStr(rs.getString("cou_id"));
            course.setNameStr(rs.getString("cou_name"));
            course.setPeriodStr(rs.getString("cou_period"));
            course.setCreditStr(rs.getString("cou_credit"));
            course.setNumInt(rs.getInt("cou_num"));
            course.setBeStr(rs.getString("cou_begin_end"));
            course.setTimeInt(rs.getInt("cou_time"));
            course.setTeaStr(rs.getString("tea_id"));
            course.setTeaNameStr(rs.getString("tea_name"));
            course.setGrade(rs.getFloat("grade"));
            resultList.add(course);
        }
        ps.close();
        rs.close();
        connection.close();
        return resultList;
    }

    public static void teaGradeInsert(String stuId,String couId,String grade) throws SQLException {
        String sql = "";
        //int resultId = 0;
        Connection connection = getConnection();

        sql = "update stucou set grade=? where stu_id=? and cou_id=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setFloat(1,Float.parseFloat(grade));
        ps.setString(2, stuId);
        ps.setString(3,couId);
        ps.executeUpdate();
        //Log.d("updateCheck", "StudentInsert: " + "do execute");
        ps.close();
        connection.close();
    }

    public static boolean comparePassword(String id,String password,String idType,String passType,String table) throws SQLException {
        String sql = "";
        int resultInt = 0;
        Connection connection = getConnection();

        sql = "select count("+passType+") as count from "+table+" where "+idType+"=? and "+passType+"=?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, id);
        ps.setString(2, password);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            resultInt=rs.getInt("count");
        }
        //Log.d("updateCheck", "StudentInsert: " + "do execute");
        ps.close();
        rs.close();
        connection.close();
        if(resultInt==1){
            return true;
        }else{
            return false;
        }
    }

    public static void updatePassword(String id,String password,String idType,String passType,String table) throws SQLException {
        String sql = "";
        //int resultId = 0;
        Connection connection = getConnection();

        sql = "update "+table+" set "+passType+"=? where "+idType+"=?";
        Log.d("更新", "updatePassword: "+sql);
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, password);
        ps.setString(2, id);
        ps.executeUpdate();
        //Log.d("updateCheck", "StudentInsert: " + "do execute");
        ps.close();
        connection.close();
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
