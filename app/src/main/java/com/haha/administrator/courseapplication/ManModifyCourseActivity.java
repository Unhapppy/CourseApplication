package com.haha.administrator.courseapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.haha.administrator.courseapplication.jdbc.DButil;
import com.haha.administrator.courseapplication.model.Course;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class ManModifyCourseActivity extends AppCompatActivity {

    private String requestStr = "";
    private int requestType = 1;

    private EditText idEdit;
    private EditText nameEdit;
    private EditText periodEdit;
    private EditText creditEdit;
    private EditText numberEdit;
    private EditText beEdit;
    private EditText timeEdit;
    private EditText teaIdEdit;

    private Button submitBtn;
    private Button deleteBtn;

    private String idStr = "";
    private String nameStr = "";
    private String periodStr = "";
    private String creditStr = "";
    private String numberStr = "";
    private String beStr = "";
    private String timeStr = "";
    private String teaIdStr = "";

    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_modify_course);

        ActionBar actionBar = getSupportActionBar();
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);

        course = new Course();

        idEdit = (EditText) findViewById(R.id.man_modify_cou_id_edit);
        nameEdit = (EditText) findViewById(R.id.man_modify_cou_name_edit);
        periodEdit = (EditText) findViewById(R.id.man_modify_cou_period_edit);
        creditEdit = (EditText) findViewById(R.id.man_modify_cou_credit_edit);
        numberEdit = (EditText) findViewById(R.id.man_modify_cou_number_edit);
        beEdit = (EditText) findViewById(R.id.man_modify_cou_be_edit);
        timeEdit = (EditText) findViewById(R.id.man_modify_cou_time_edit);
        teaIdEdit = (EditText) findViewById(R.id.man_modify_cou_tea_edit);

        submitBtn = (Button) findViewById(R.id.man_modify_cou_submit_btn);
        deleteBtn = (Button) findViewById(R.id.man_modify_cou_delete_btn);

        Intent intent = getIntent();
        requestStr = intent.getStringExtra("requestStr");
        requestType = intent.getIntExtra("requestType", 1);
        Log.d("收到", "onCreate: " + requestStr + " " + requestType);
        final List<String> resultList = new ArrayList<>();
        if (requestType == 1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        resultList.addAll(DButil.QueryMoreString("cou_Id", "course", "cou_id", requestStr));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        } else if (requestType == 2) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        resultList.addAll(DButil.QueryMoreString("cou_name", "course", "cou_name", requestStr));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (String i : resultList) {
            Log.d("list", "onCreate: " + i);
        }

        //View dialogView = View.inflate(ManModifyStuActivity.this, R.layout.dialog_list, null);//填充ListView布局
        //ListView listView = (ListView) dialogView.findViewById(R.id.dialog_listview);//初始化ListView控件
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ManModifyCourseActivity.this, android.R.layout.simple_list_item_1, resultList);
        //listView.setAdapter(adapter);//ListView设置适配器

        AlertDialog listDialog = new AlertDialog.Builder(this)
                .setTitle("选择修改对象")//.setView(listView)//在这里把写好的这个listview的布局加载dialog中
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String request = resultList.get(which);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    course = DButil.queryOneCourse(request);
                                    Log.d("查询线程", "run: " + course.toString());
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        try {
                            Thread.sleep(1200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        idEdit.setText(course.getIdStr());
                        idEdit.setEnabled(false);
                        nameEdit.setText(course.getNameStr());
                        periodEdit.setText(course.getPeriodStr());
                        creditEdit.setText(course.getCreditStr());
                        numberEdit.setText(course.getNumInt() + "");
                        beEdit.setText(course.getBeStr());
                        timeEdit.setText(course.getTimeInt() + "");
                        teaIdEdit.setText(course.getIdStr());

                    }
                }).create();
        listDialog.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idStr = idEdit.getText().toString();
                nameStr = nameEdit.getText().toString();
                periodStr = periodEdit.getText().toString();
                creditStr = creditEdit.getText().toString();
                numberStr = numberEdit.getText().toString();
                beStr = beEdit.getText().toString();
                timeStr = timeEdit.getText().toString();
                teaIdStr = teaIdEdit.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DButil.modifyCou(idStr, nameStr, periodStr, creditStr, numberStr, beStr, timeStr, teaIdStr);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Toast.makeText(ManModifyCourseActivity.this, "修改成功", Toast.LENGTH_LONG).show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idStr = idEdit.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DButil.deleteOneRecord("course", "cou_id", idStr);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                idEdit.setEnabled(false);
                nameEdit.setEnabled(false);
                periodEdit.setEnabled(false);
                creditEdit.setEnabled(false);
                numberEdit.setEnabled(false);
                beEdit.setEnabled(false);
                timeEdit.setEnabled(false);
                teaIdEdit.setEnabled(false);
                submitBtn.setClickable(false);
                deleteBtn.setClickable(false);
                Toast.makeText(ManModifyCourseActivity.this, "删除成功", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 软键盘控制，重写监听事件，待整合
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 软键盘处理结束*************************************************
     */
}
