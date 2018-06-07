package com.haha.administrator.courseapplication.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.haha.administrator.courseapplication.ManModifyCourseActivity;
import com.haha.administrator.courseapplication.ManModifyStuActivity;
import com.haha.administrator.courseapplication.ManModifyTeaActivity;
import com.haha.administrator.courseapplication.R;
import com.haha.administrator.courseapplication.ShowCourseActivity;
import com.haha.administrator.courseapplication.jdbc.DButil;
import com.haha.administrator.courseapplication.model.Manager;
import com.haha.administrator.courseapplication.model.Student;

import java.lang.reflect.Type;
import java.sql.SQLException;

public class ManInfoFragment extends Fragment {

    private TextView personIdText;
    private TextView personNameText;
    private TextView personGenderText;
    private TextView personBirthText;
    private TextView personDSText;//院系
    private Button personBtn;
    private Button userStuBtn;
    private Button userTeaBtn;
    private Button courseSpeBtn;
    private Button courseAllBtn;
    private EditText selectIdEdit;
    private EditText selectNameEdit;
    private Button selectBtn;

    private Manager manager;

    public String manId = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.man_info_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        manId = getActivity().getIntent().getStringExtra("id");
        manager = new Manager();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    manager = DButil.queryManPerson(manId);
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
        personIdText.setText(manager.getIdStr());
        personNameText.setText(manager.getNameStr());
        personGenderText.setText(manager.getGenderStr());
        personBirthText.setText(manager.getBirthDate().toString());
        personDSText.setText("-----");

        personBtn = (Button) getActivity().findViewById(R.id.info_inner_person_btn);

        userStuBtn = (Button) getActivity().findViewById(R.id.man_info_inner_user_stu_btn);
        userTeaBtn = (Button) getActivity().findViewById(R.id.man_info_inner_user_tea_btn);

        courseSpeBtn = (Button) getActivity().findViewById(R.id.man_info_inner_course_spe_btn);
        courseAllBtn = (Button) getActivity().findViewById(R.id.man_info_inner_course_all_btn);

        selectIdEdit = (EditText) getActivity().findViewById(R.id.man_info_inner_select_id_edit);
        selectNameEdit = (EditText) getActivity().findViewById(R.id.man_info_inner_select_name_edit);
        selectBtn = (Button) getActivity().findViewById(R.id.man_info_inner_select_btn);

        personBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 补全
            }
        });

        userStuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog(1);
            }
        });

        userTeaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog(2);
            }
        });

        courseSpeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog(3);
            }
        });

        courseAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ShowCourseActivity.class);
                startActivity(intent);
            }
        });

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void showSearchDialog(final int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("查询");
        //通过布局填充器获dialog_search_stu
        //在另一个fragment里也用了，可能会冲突*************************************************************
        View view = getLayoutInflater().inflate(R.layout.dialog_search_stu, null);
        final EditText idEdit = (EditText) view.findViewById(R.id.dialog_search_id);
        final EditText nameEdit = (EditText) view.findViewById(R.id.dialog_search_name);
        builder.setView(view);//设置dialog_search_stu为对话提示框
        builder.setCancelable(false);//设置为不可取消
        //设置正面按钮，并做事件处理
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String idStr = idEdit.getText().toString().trim();
                String nameStr = nameEdit.getText().toString().trim();
                if (idStr.isEmpty() && nameStr.isEmpty()) {
                    idEdit.setError("不能为空");
                } else {
                    Intent intent = new Intent(getActivity(), ManModifyStuActivity.class);
                    if (type == 1) {
                        intent = new Intent(getActivity(), ManModifyStuActivity.class);
                    } else if (type == 2) {
                        intent = new Intent(getActivity(), ManModifyTeaActivity.class);
                    } else if (type == 3) {
                        intent = new Intent(getActivity(), ManModifyCourseActivity.class);
                    }
                    if (idStr.isEmpty()) {
                        intent.putExtra("requestStr", nameStr);
                        intent.putExtra("requestType", 2);
                    }
                    if (nameStr.isEmpty()) {
                        intent.putExtra("requestStr", idStr);
                        intent.putExtra("requestType", 1);
                    }
                    if (!idStr.isEmpty() && !nameStr.isEmpty()) {
                        intent.putExtra("requestStr", idStr);
                        intent.putExtra("requestType", 1);
                    }
                    //intent.putExtra("requestStr",requestStr);
                    //intent.putExtra("requestType",requestType);
                    startActivity(intent);
                }
            }
        });
        //设置反面按钮，并做事件处理
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("对话框取消", "onClick: ");
            }
        });
        builder.show();//显示Dialog对话框
    }

}
