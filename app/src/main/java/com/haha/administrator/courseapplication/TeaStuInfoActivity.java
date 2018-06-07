package com.haha.administrator.courseapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haha.administrator.courseapplication.adapter.SelectCourseAdapter;
import com.haha.administrator.courseapplication.adapter.StudentAdapter;
import com.haha.administrator.courseapplication.jdbc.DButil;
import com.haha.administrator.courseapplication.model.Course;
import com.haha.administrator.courseapplication.model.Student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeaStuInfoActivity extends AppCompatActivity {

    private ListView listView;
    private TextView couNameText;
    private String couId="";
    private String couName="";
    private List<Student> studentList=new ArrayList<>();
    StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tea_stu_info);

        ActionBar actionBar = getSupportActionBar();
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);

        listView=(ListView)findViewById(R.id.tea_stu_list_view);
        couNameText=(TextView)findViewById(R.id.tea_stu_cou_name_text);

        Intent intent=getIntent();
        couId=intent.getStringExtra("id");
        couName=intent.getStringExtra("name");

        couNameText.setText(couName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    studentList.addAll(DButil.querySelectedStudent(couId));
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
        //SelectCourseAdapter selectCourseAdapter=new SelectCourseAdapter(StuSelectCouActivity.this,R.layout.course_select_item,courseList);
        //ListView listView=(ListView)findViewById(R.id.stu_select_cou_list_view);
        //listView.setAdapter(selectCourseAdapter);
        studentAdapter=new StudentAdapter(TeaStuInfoActivity.this,R.layout.stu_grade_item,studentList);
        listView.setAdapter(studentAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student student=studentList.get(position);
                showSearchDialog(student.getIdStr(),couId);
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //studentAdapter.setCurrentItem(position);
                //studentAdapter.notifyDataSetChanged();
            }
        });

    }

    public void showSearchDialog(final String stuId, final String couId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TeaStuInfoActivity.this);
        builder.setTitle("查询");
        View view = getLayoutInflater().inflate(R.layout.dialog_add_grade, null);
        final EditText gradeEdit = (EditText) view.findViewById(R.id.dialog_add_grade_edit);
        builder.setView(view);//设置dialog_search_stu为对话提示框
        builder.setCancelable(false);//设置为不可取消
        //设置正面按钮，并做事件处理
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String gradeStr = gradeEdit.getText().toString().trim();
                //studentAdapter.setAddSucess(true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DButil.teaGradeInsert(stuId,couId,gradeStr);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                Toast.makeText(TeaStuInfoActivity.this,"录入成功",Toast.LENGTH_LONG).show();
                //Log.d("成功1", "showSearchDialog: "+isSucess[0]);
            }
        });
        //设置反面按钮，并做事件处理
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();//显示Dialog对话框
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
