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
import android.widget.TextView;

import com.haha.administrator.courseapplication.R;
import com.haha.administrator.courseapplication.StuInfoPersonActivity;
import com.haha.administrator.courseapplication.adapter.GradeAdapter;
import com.haha.administrator.courseapplication.adapter.SelectCourseAdapter;
import com.haha.administrator.courseapplication.jdbc.DButil;
import com.haha.administrator.courseapplication.model.Course;
import com.haha.administrator.courseapplication.model.Student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StuInfoFragment extends Fragment {

    private TextView personIdText;
    private TextView personNameText;
    private TextView personGenderText;
    private TextView personBirthText;
    private TextView personDSText;//院系

    private ListView courseListView;
    private ListView gradeListView;

    private Button moreInfoBtn;

    private Student student;

    private String stuId="";

    List<Course> courseList=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stu_info_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        student=new Student();
        stuId= getActivity().getIntent().getStringExtra("id");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    student= DButil.queryOneStudent(stuId);
                    courseList.addAll(DButil.queryStuCoures(stuId));
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

        personIdText = (TextView) getActivity().findViewById(R.id.info_inner_person_number);
        personNameText = (TextView) getActivity().findViewById(R.id.info_inner_person_name);
        personGenderText = (TextView) getActivity().findViewById(R.id.info_inner_person_gender);
        personBirthText = (TextView) getActivity().findViewById(R.id.info_inner_person_birth);
        personDSText = (TextView) getActivity().findViewById(R.id.info_inner_person_dep_spe);
        personIdText.setText(student.getIdStr());
        personNameText.setText(student.getNameStr());
        personGenderText.setText(student.getGenderStr());
        personBirthText.setText(student.getBirthDate().toString());
        personDSText.setText(student.getSpeName());

        courseListView=(ListView)getActivity().findViewById(R.id.inner_stu_info_course_list_view);
        gradeListView=(ListView)getActivity().findViewById(R.id.inner_stu_info_grade_list_view);

        SelectCourseAdapter selectCourseAdapter=new SelectCourseAdapter(getActivity(),R.layout.course_select_item,courseList);
        courseListView.setAdapter(selectCourseAdapter);

        GradeAdapter gradeAdapter=new GradeAdapter(getActivity(),R.layout.stu_grade_list_item,courseList);
        gradeListView.setAdapter(gradeAdapter);

        moreInfoBtn=(Button)getActivity().findViewById(R.id.info_inner_person_btn);
        moreInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), StuInfoPersonActivity.class);
                startActivity(intent);
            }
        });
    }
}
