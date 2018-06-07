package com.haha.administrator.courseapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.haha.administrator.courseapplication.R;
import com.haha.administrator.courseapplication.TeaShowGradeActivity;
import com.haha.administrator.courseapplication.TeaStuInfoActivity;
import com.haha.administrator.courseapplication.adapter.CourseAdapter;
import com.haha.administrator.courseapplication.jdbc.DButil;
import com.haha.administrator.courseapplication.model.Course;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeaLogFragment extends Fragment {

    private String teaId="";

    private List<Course> courseList=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tea_log_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        teaId= getActivity().getIntent().getStringExtra("id");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    courseList.addAll(DButil.queryTeaCoures(teaId));
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

        CourseAdapter courseAdapter=new CourseAdapter(getActivity(),R.layout.course_item,courseList);
        ListView simpleListView=(ListView)getActivity().findViewById(R.id.inner_tea_log_list_view);
        simpleListView.setAdapter(courseAdapter);
        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course=courseList.get(position);
                Intent intent=new Intent(getActivity(), TeaStuInfoActivity.class);
                intent.putExtra("id",course.getIdStr());
                intent.putExtra("name",course.getNameStr());
                startActivity(intent);
            }
        });

        ListView showListView=(ListView)getActivity().findViewById(R.id.inner_tea_log_show_list_view);
        showListView.setAdapter(courseAdapter);
        showListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course=courseList.get(position);
                //
                Intent intent=new Intent(getActivity(), TeaShowGradeActivity.class);
                intent.putExtra("id",course.getIdStr());
                intent.putExtra("name",course.getNameStr());
                startActivity(intent);
            }
        });
    }
}
