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
import android.widget.TextView;
import android.widget.Toast;

import com.haha.administrator.courseapplication.ManAddStuActivity;
import com.haha.administrator.courseapplication.ManAddTeaActivity;
import com.haha.administrator.courseapplication.ManModifyStuActivity;
import com.haha.administrator.courseapplication.R;
import com.haha.administrator.courseapplication.ManAddCouActivity;
import com.haha.administrator.courseapplication.jdbc.DButil;

import org.w3c.dom.Text;

import java.sql.SQLException;

public class ManManageFragment extends Fragment {

    private Button addStuBtn;
    private Button addTeaBtn;
    private Button addCouBtn;
    private Button modifyStuBtn;
    private Button modifyTeaBtn;
    private Button modifyCouBtn;
    private Button beginSelectBtn;
    private Button endSelectBtn;
    private TextView selectStateText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.man_manage_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        addStuBtn = (Button) getActivity().findViewById(R.id.man_manage_inner_user_stu_btn);
        addTeaBtn = (Button) getActivity().findViewById(R.id.man_manage_inner_user_tea_btn);
        addCouBtn = (Button) getActivity().findViewById(R.id.man_manage_inner_cou_add_btn);
        modifyStuBtn = (Button) getActivity().findViewById(R.id.man_manage_inner_stu_modify_btn);
        modifyTeaBtn = (Button) getActivity().findViewById(R.id.man_manage_inner_tea_modify_btn);
        modifyCouBtn = (Button) getActivity().findViewById(R.id.man_manage_inner_cou_modify_btn);
        beginSelectBtn = (Button) getActivity().findViewById(R.id.man_manage_inner_select_begin_btn);
        endSelectBtn = (Button) getActivity().findViewById(R.id.man_manage_inner_select_end_btn);

        selectStateText = (TextView) getActivity().findViewById(R.id.man_manage_inner_select_state_text);

        addStuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManAddStuActivity.class);
                startActivity(intent);
            }
        });

        addTeaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManAddTeaActivity.class);
                startActivity(intent);
            }
        });

        addCouBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ManAddCouActivity.class);
                startActivity(intent);
            }
        });

        modifyStuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("点击", "onClick: ");
                showSearchDialog();
                Log.e("对话框", "onClick: " );
                //Intent intent = new Intent(getActivity(), ManModifyStuActivity.class);
                //intent.putExtra("requestStr",requestStr);
                //intent.putExtra("requestType",requestType);
                //startActivity(intent);
            }
        });

        modifyTeaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        modifyCouBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        beginSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DButil.SelectControl("1");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                selectStateText.setText("选课开始");
            }
        });

        endSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DButil.SelectControl("0");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                selectStateText.setText("选课未开始");
            }
        });
    }

    public void showSearchDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("查询");
        //通过布局填充器获dialog_search_stu
        View view = getLayoutInflater().inflate(R.layout.dialog_search_stu,null);
        //获取两个文本编辑框（密码这里不做登陆实现，仅演示）
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
                    if (idStr.isEmpty()) {
                        intent.putExtra("requestStr",nameStr);
                        intent.putExtra("requestType",2);
                    }
                    if (nameStr.isEmpty()) {
                        intent.putExtra("requestStr",idStr);
                        intent.putExtra("requestType",1);
                    }
                    if (!idStr.isEmpty() && !nameStr.isEmpty()) {
                        intent.putExtra("requestStr",idStr);
                        intent.putExtra("requestType",1);
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
