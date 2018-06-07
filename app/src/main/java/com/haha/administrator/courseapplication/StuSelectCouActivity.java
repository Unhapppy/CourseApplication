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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.haha.administrator.courseapplication.adapter.SelectCourseAdapter;
import com.haha.administrator.courseapplication.jdbc.DButil;
import com.haha.administrator.courseapplication.model.Course;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StuSelectCouActivity extends AppCompatActivity {

    List<Course> courseList=new ArrayList<>();
    String stuId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_select_cou);
        ActionBar actionBar = getSupportActionBar();
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);
        Intent intent=getIntent();
        stuId=intent.getStringExtra("stuId");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    courseList.addAll(DButil.queryAllCoures());
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

        SelectCourseAdapter selectCourseAdapter=new SelectCourseAdapter(StuSelectCouActivity.this,R.layout.course_select_item,courseList);
        ListView listView=(ListView)findViewById(R.id.stu_select_cou_list_view);
        listView.setAdapter(selectCourseAdapter);

        //点击选项选课
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course=courseList.get(position);
                try {
                    showSearchDialog(course);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void showSearchDialog(Course course) throws InterruptedException {
        AlertDialog.Builder builder = new AlertDialog.Builder(StuSelectCouActivity.this);
        builder.setTitle("查询");
        //通过布局填充器获dialog_search_stu
        View view = getLayoutInflater().inflate(R.layout.dialog_select_confirm,null);
        TextView selectedNumText=(TextView)view.findViewById(R.id.dialog_selected_num_text);
        TextView canSelectText=(TextView)view.findViewById(R.id.dialog_can_select_text);
        String idStr=course.getIdStr();
        int num=course.getNumInt();
        final int time=course.getTimeInt();
        final int[] selectedNum = {0};
        final String paraCouId=idStr;
        boolean isfull=false;
        final boolean[] isRepeated = {false};
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    selectedNum[0] =DButil.querySeletedNumber(paraCouId);
                    //Log.e("传入", "run: "+paraCouId+time );
                    isRepeated[0] =DButil.isTimeRepeated(stuId,time);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(800);
        selectedNumText.setText(selectedNum[0]+"");

        if(selectedNum[0]>=num){
            canSelectText.setText("容量已满，不可选");
            isfull=true;
        }else{
            canSelectText.setText("可选,确认选课?");
            isfull=false;
        }
        builder.setView(view);//设置dialog_search_stu为对话提示框
        builder.setCancelable(false);//设置为不可取消
        //设置正面按钮，并做事件处理
        final boolean finalIsfull = isfull;
        Log.e("标志位", "showSearchDialog: "+isfull +isRepeated[0]);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(finalIsfull){
                    Toast.makeText(StuSelectCouActivity.this,"当前课程容量已满！",Toast.LENGTH_LONG).show();
                }else {
                    if(isRepeated[0]){
                        Toast.makeText(StuSelectCouActivity.this,"重复选择或时间冲突！",Toast.LENGTH_LONG).show();
                    }
                    else{
                        //添加选课
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //Log.e("插入", "run: "+stuId+" "+paraCouId );
                                    DButil.stuCouInsert(stuId,paraCouId);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        Toast.makeText(StuSelectCouActivity.this,"选课成功！",Toast.LENGTH_LONG).show();
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
