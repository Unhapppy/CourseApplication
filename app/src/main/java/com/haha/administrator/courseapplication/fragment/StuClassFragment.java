package com.haha.administrator.courseapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.haha.administrator.courseapplication.R;
import com.haha.administrator.courseapplication.ShowCourseActivity;
import com.haha.administrator.courseapplication.StuSelectCouActivity;
import com.haha.administrator.courseapplication.adapter.CourseAdapter;
import com.haha.administrator.courseapplication.jdbc.DButil;
import com.haha.administrator.courseapplication.model.Course;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StuClassFragment extends Fragment {

    private Button selectBtn;
    private String id = "";

    List<Course> courseList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stu_class_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        id = getActivity().getIntent().getStringExtra("id");
        final String finalId = id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    courseList.addAll(DButil.querySelectedCourse(finalId));
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
        CourseAdapter courseAdapter = new CourseAdapter(getActivity(), R.layout.course_item, courseList);
        ListView listView = (ListView) getActivity().findViewById(R.id.stu_class_inner_list_view);
        listView.setAdapter(courseAdapter);
        selectBtn = (Button) getActivity().findViewById(R.id.stu_class_inner_select_btn);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean[] isSelectOpen = {false};
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            isSelectOpen[0] =DButil.CanSelect();
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
                if(isSelectOpen[0]) {
                    Intent intent = new Intent(getActivity(), StuSelectCouActivity.class);
                    intent.putExtra("stuId", id);
                    startActivity(intent);
                }else {
                    Toast.makeText(getActivity(),"选课未开始",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
