package com.haha.administrator.courseapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.haha.administrator.courseapplication.adapter.CourseAdapter;
import com.haha.administrator.courseapplication.jdbc.DButil;
import com.haha.administrator.courseapplication.model.Course;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShowCourseActivity extends AppCompatActivity {

    List<Course> courseList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_course);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    courseList.addAll(DButil.queryAllCoures());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final CourseAdapter courseAdapter=new CourseAdapter(ShowCourseActivity.this,R.layout.course_item,courseList);
        ListView listView=(ListView)findViewById(R.id.course_list_view);
        listView.setAdapter(courseAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course=courseList.get(position);
                Intent intent=new Intent(ShowCourseActivity.this,ManModifyCourseActivity.class);
                intent.putExtra("requestStr",course.getIdStr());
                intent.putExtra("requestType",1);
                startActivity(intent);
            }
        });
    }
}
