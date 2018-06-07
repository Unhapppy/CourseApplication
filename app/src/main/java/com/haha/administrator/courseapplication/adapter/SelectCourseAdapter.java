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

public class SelectCourseAdapter extends ArrayAdapter<Course>{
    private int resourceId;

    public SelectCourseAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Course> objects) {
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
        TextView idText=(TextView)view.findViewById(R.id.select_cou_id_text);
        TextView nameText=(TextView)view.findViewById(R.id.select_cou_name_text);
        TextView periodText=(TextView)view.findViewById(R.id.select_cou_period_text);
        TextView creditText=(TextView)view.findViewById(R.id.select_cou_credit_text);
        TextView beText=(TextView)view.findViewById(R.id.select_cou_be_text);
        TextView timeText=(TextView)view.findViewById(R.id.select_cou_time_text);
        TextView teaText=(TextView)view.findViewById(R.id.select_cou_tea_text);
        TextView numText=(TextView)view.findViewById(R.id.select_cou_num_text);

        //上课时间存储为一个十位数，十位表示周，个位表示节数
        int time=course.getTimeInt();
        int week=time%10;
        int pos=time-10*week;
        String timeStr="周"+week+"第"+(2*pos-1)+","+(2*pos)+"节";

        idText.setText(course.getIdStr());
        nameText.setText(course.getNameStr());
        periodText.setText("学时"+course.getPeriodStr());
        creditText.setText("学分"+course.getCreditStr());
        beText.setText(course.getBeStr()+"周");
        timeText.setText(timeStr);
        teaText.setText(course.getTeaNameStr());
        //注意已选人数并没有被装载
        numText.setText("容量"+course.getNumInt());
        return view;
    }

}
