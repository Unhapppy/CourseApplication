package com.haha.administrator.courseapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.haha.administrator.courseapplication.R;
import com.haha.administrator.courseapplication.model.Course;
import com.haha.administrator.courseapplication.model.Student;

import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {
    private int resourceId;
    public StudentAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Student> objects) {
        super(context, textViewResourceId, objects);
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Student student=getItem(position);//获取当前的course实例
        View view;
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        }else{
            view =convertView;
        }
        TextView idText=(TextView)view.findViewById(R.id.stu_grade_id_text);
        TextView nameText=(TextView)view.findViewById(R.id.stu_grade_name_text);
        TextView speNameText=(TextView)view.findViewById(R.id.stu_grade_spe_text);
        idText.setText(student.getIdStr());
        nameText.setText(student.getNameStr());
        speNameText.setText(student.getSpeName());


        return view;
    }

}
