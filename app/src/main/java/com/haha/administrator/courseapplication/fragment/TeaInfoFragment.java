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
import android.widget.TextView;

import com.haha.administrator.courseapplication.R;
import com.haha.administrator.courseapplication.StuSelectCouActivity;
import com.haha.administrator.courseapplication.TeaStuInfoActivity;
import com.haha.administrator.courseapplication.adapter.CourseAdapter;
import com.haha.administrator.courseapplication.adapter.SelectCourseAdapter;
import com.haha.administrator.courseapplication.jdbc.DButil;
import com.haha.administrator.courseapplication.model.Course;
import com.haha.administrator.courseapplication.model.Teacher;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeaInfoFragment extends Fragment{

    private TextView personIdText;
    private TextView personNameText;
    private TextView personGenderText;
    private TextView personBirthText;
    private TextView personDSText;//院系

    Teacher teacher;
    private String teaId="";
    List<Course> courseList=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tea_info_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        teacher=new Teacher();
        teaId= getActivity().getIntent().getStringExtra("id");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    teacher= DButil.queryOneTeacher(teaId);
                    courseList.addAll(DButil.queryTeaCoures(teaId));
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
        personIdText.setText(teacher.getIdStr());
        personNameText.setText(teacher.getNameStr());
        personGenderText.setText(teacher.getGenderStr());
        personBirthText.setText(teacher.getBirthDate().toString());
        personDSText.setText(teacher.getDepName());

        SelectCourseAdapter selectCourseAdapter=new SelectCourseAdapter(getActivity(),R.layout.course_select_item,courseList);
        ListView listView=(ListView)getActivity().findViewById(R.id.tea_cou_list_view);
        listView.setAdapter(selectCourseAdapter);

        CourseAdapter courseAdapter=new CourseAdapter(getActivity(),R.layout.course_item,courseList);
        ListView simpleListView=(ListView)getActivity().findViewById(R.id.tea_cou_simple_list_view);
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
    }

}
