package com.haha.administrator.courseapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.haha.administrator.courseapplication.jdbc.DButil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ManModifyStuActivity extends AppCompatActivity {

    private String requestStr="";
    private int requestType=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_modify_stu);

        Intent intent=getIntent();
        requestStr=intent.getStringExtra("requestStr");
        requestType=intent.getIntExtra("requestType",1);
        Log.d("收到", "onCreate: "+requestStr+" "+requestType);
        List<String> resultList=new ArrayList<>();
        if(requestType==1){
            try {
                resultList.addAll( DButil.QueryMoreString("stu_Id","student","stu_id",requestStr));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(requestType==2){
            try {
                resultList.addAll(DButil.QueryMoreString("stu_name","student","stu_name",requestStr));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        for(String i:resultList){
            Log.d("list", "onCreate: "+i);
        }
    }

}
