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

public class MysqlService extends Service {
    Connection connection;

    public MysqlService() {
    }

    private MysqlBinder mysqlBinder = new MysqlBinder();

    class MysqlBinder extends Binder {
        public void BinderTest(Context context) {
            Toast.makeText(context, "do binder", Toast.LENGTH_LONG).show();
        }

        public String PasswordQuery(int accountType,String accountId) throws SQLException {
            //PreparedStatement 有效的防止sql注入(SQL语句在程序运行前已经进行了预编译,当运行时动态地把参数传给PreprareStatement时，即使参数里有敏感字符如 or '1=1'也数据库会作为一个参数一个字段的属性值来处理而不会作为一个SQL指令)
            String sql = "";
            String resultStr = "";

            //结果字段
            String resultLabel="";
            //表名
            String tableName="";
            //条件字段
            String conditionLabel="";

            //账号类型老师1
            //学生2
            //管理员3
            if(accountType==1){
                resultLabel="tea_password";
                tableName="teacher";
                conditionLabel="tea_id";
            }else if(accountType==2){
                resultLabel="stu_password";
                tableName="student";
                conditionLabel="stu_id";
            }else{
                resultLabel="man_password";
                tableName="manager";
                conditionLabel="man_id";
            }
            sql="select "+resultLabel+" from "+tableName+" where "+conditionLabel+"=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            //Log.e("测试连接", "PasswordQuery: "+connection.toString());
            ps.setString(1,accountId);  //占位符顺序从1开始
            //ps.setString(2, "123456"); //也可以使用setObject
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultStr = rs.getString(resultLabel);
                Log.e("返回值检查", "PasswordQuery: "+resultStr);
            }
            ps.close();
            rs.close();
            return resultStr;
        }

    }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
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
                        connection = DriverManager.getConnection("jdbc:mysql://" + "47.106.91.12" + ":3306/" + "course_system", "root", "951970312");
                        Log.d("数据库连接成功",connection.toString());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Log.e("数据库连接失败", e.toString());
                    }
                }
            }).start();
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

