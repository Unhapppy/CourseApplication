package com.haha.administrator.courseapplication;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.haha.administrator.courseapplication.adapter.StudentAdapter;
import com.haha.administrator.courseapplication.adapter.StudentGradeAdapter;
import com.haha.administrator.courseapplication.jdbc.DButil;
import com.haha.administrator.courseapplication.model.Student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeaShowGradeActivity extends AppCompatActivity {

    private ListView listView;
    private TextView couNameText;
    private String couId="";
    private String couName="";
    private List<Student> studentList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_show_grade);

        ActionBar actionBar = getSupportActionBar();
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);

        listView=(ListView)findViewById(R.id.tea_show_grade_list_view);
        couNameText=(TextView)findViewById(R.id.tea_show_grade_cou_name_text);

        Intent intent=getIntent();
        couId=intent.getStringExtra("id");
        couName=intent.getStringExtra("name");

        couNameText.setText(couName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    studentList.addAll(DButil.querySelectedStudent(couId));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        StudentGradeAdapter studentGradeAdapter=new StudentGradeAdapter(TeaShowGradeActivity.this,R.layout.stu_grade_show_item,studentList);
        listView.setAdapter(studentGradeAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
