package com.haha.administrator.courseapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.haha.administrator.courseapplication.R;
import com.haha.administrator.courseapplication.model.Course;


import java.util.List;

public class CourseAdapter extends ArrayAdapter<Course> {

    private int resourceId;

    public CourseAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Course> objects) {
        super(context, textViewResourceId, objects);
        resourceId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Course course=getItem(position);//获取当前的course实例
        View view;
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        }else{
            view =convertView;
        }
        TextView idText=(TextView)view.findViewById(R.id.course_id_item);
        TextView nameText=(TextView)view.findViewById(R.id.course_name_item);
        idText.setText(course.getIdStr());
        nameText.setText(course.getNameStr());

        return view;
    }
}
