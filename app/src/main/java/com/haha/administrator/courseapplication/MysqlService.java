package com.haha.administrator.courseapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlService extends Service {

    public MysqlService() {
    }

    private MysqlBinder mysqlBinder = new MysqlBinder();

    class MysqlBinder extends Binder {
        public void BinderTest(Context context) {
            Toast.makeText(context, "do binder", Toast.LENGTH_LONG).show();
        }

        public String PasswordQuery(int accountType, String accountId) throws SQLException {
            //PreparedStatement 有效的防止sql注入(SQL语句在程序运行前已经进行了预编译,当运行时动态地把参数传给PreprareStatement时，即使参数里有敏感字符如 or '1=1'也数据库会作为一个参数一个字段的属性值来处理而不会作为一个SQL指令)
            String sql = "";
            String resultStr = "";
            //结果字段
            String resultLabel = "";
            //表名
            String tableName = "";
            //条件字段
            String conditionLabel = "";

            Connection connection=getConnection();

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
        //TODO 更多查询

        public int CourseInsert(String id, String name, String period, String credit,
                                String num, String beTime, String time, String teaId) throws SQLException {
            String sql = "";
            int resultId = 0;
            Connection connection=getConnection();

            sql = "insert into course(cou_id,cou_name,cou_period,cou_credit,cou_begin_end,cou_time,cou_num,tea_id) \n" +
                    "values(?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            Log.e("测试连接", "CourseInsert: "+connection.toString());
            ps.setString(1, id);  //占位符顺序从1开始
            ps.setString(2, name);
            ps.setString(3, period);
            ps.setString(4, credit);
            ps.setInt(5, Integer.parseInt(num));
            ps.setString(6, beTime);
            ps.setInt(7, Integer.parseInt(time));
            ps.setString(8, teaId);
            //ps.setString(2, "123456"); //也可以使用setObject
            ps.executeUpdate();
            Log.e("更新执行", "CourseInsert: "+ps.toString());
            ResultSet rs = ps.getGeneratedKeys();
            Log.e("获得返回值", "CourseInsert: "+rs.toString());
            while (rs.next()) {
                resultId = rs.getInt(1);
                Log.e("返回值检查", "CourseInsert: " + resultId);
            }
            ps.close();
            rs.close();
            connection.close();
            return resultId;
        }

        private Connection getConnection(){
            final Connection[] connection = {null};
            new Thread(new Runnable() {
                @Override
                public void run() {
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
                        connection[0] = DriverManager.getConnection("jdbc:mysql://" + "47.106.91.12" + ":3306/" + "course_system", "root", "951970312");
                        Log.d("数据库连接成功", connection[0].toString());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Log.e("数据库连接失败", e.toString());
                    }
                }
            }).start();
            return connection[0];
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        //throw new UnsupportedOperationException("Not yet implemented");
        return mysqlBinder;
    }

    /**
     * 服务创建时调用
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 每次服务启动时调用
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 销毁时调用
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}

