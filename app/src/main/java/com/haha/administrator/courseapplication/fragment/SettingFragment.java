package com.haha.administrator.courseapplication.fragment;

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
import android.widget.Toast;

import com.haha.administrator.courseapplication.LoginActivity;
import com.haha.administrator.courseapplication.ManModifyStuActivity;
import com.haha.administrator.courseapplication.R;
import com.haha.administrator.courseapplication.jdbc.DButil;

import java.sql.SQLException;
import java.util.zip.Inflater;

public class SettingFragment extends Fragment {

    private Button changePasswordBtn;
    private Button quitBtn;
    String password;
    String idType = "";
    String passType = "";
    String table = "";
    String id = "";
    int type = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.setting_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        changePasswordBtn = (Button) getActivity().findViewById(R.id.change_password_btn);
        quitBtn = (Button) getActivity().findViewById(R.id.quit_btn);

        Intent intent = getActivity().getIntent();
        id = intent.getStringExtra("id");
        password = intent.getStringExtra("password");
        type = intent.getIntExtra("type", 0);
        if (type == 1) {
            idType = "tea_id";
            passType = "tea_password";
            table = "teacher";
        } else if (type == 2) {
            idType = "stu_id";
            passType = "stu_password";
            table = "student";
        } else if (type == 3) {
            idType = "man_id";
            passType = "man_password";
            table = "manager";
        } else {
            Toast.makeText(getActivity(), "传参错误", Toast.LENGTH_LONG).show();
        }
        Log.d("检查", "onActivityCreated: "+id+password+idType+passType+table);

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog();
            }
        });
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

    }

    public void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("修改密码");
        //通过布局填充器获dialog_search_stu
        View view = getLayoutInflater().inflate(R.layout.dialog_change, null);
        //获取两个文本编辑框（密码这里不做登陆实现，仅演示）
        final EditText oldEdit = (EditText) view.findViewById(R.id.dialog_change_old_edit);
        final EditText newEdit = (EditText) view.findViewById(R.id.dialog_change_new_edit);
        builder.setView(view);//设置dialog_search_stu为对话提示框
        builder.setCancelable(false);//设置为不可取消
        //设置正面按钮，并做事件处理
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("正面按钮", "onClick: ");
                final String oldStr = oldEdit.getText().toString().trim();
                final String newStr = newEdit.getText().toString().trim();
                if (oldStr.isEmpty()) {
                    oldEdit.setError("不能为空");
                } else if (newStr.isEmpty()) {
                    newEdit.setError("不能为空");
                } else if (!oldStr.isEmpty() && !newStr.isEmpty()) {
                    if (oldStr.equals(password)) {
                        Log.d("成功", "onClick: ");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //DButil.comparePassword(id,password,idType,passType,table)
                                    DButil.updatePassword(id, newStr, idType, passType, table);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_LONG).show();
                    } else {
                        oldEdit.setError("密码错误");
                    }
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
