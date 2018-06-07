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
import com.haha.administrator.courseapplication.model.Teacher;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ManModifyTeaActivity extends AppCompatActivity {

    private String requestStr = "";
    private int requestType = 1;

    private EditText idEdit;
    private EditText nameEdit;
    private EditText genderEdit;
    private EditText birthEdit;
    private EditText passwordEdit;
    private EditText speIdEdit;

    private Button submitBtn;
    private Button deleteBtn;

    private String idStr = "";
    private String nameStr = "";
    private String genderStr = "";
    private String birthStr = "";
    private String passwordStr = "";
    private String sprIdStr = "";
    private Teacher teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_modify_tea);

        ActionBar actionBar = getSupportActionBar();
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);

        teacher=new Teacher();

        idEdit = (EditText) findViewById(R.id.man_modify_tea_id);
        nameEdit = (EditText) findViewById(R.id.man_modify_tea_name);
        genderEdit = (EditText) findViewById(R.id.man_modify_tea_gender);
        birthEdit = (EditText) findViewById(R.id.man_modify_tea_birth);
        passwordEdit = (EditText) findViewById(R.id.man_modify_tea_password);
        speIdEdit = (EditText) findViewById(R.id.man_modify_tea_dep_id);
        submitBtn = (Button) findViewById(R.id.man_modify_tea_submit_btn);
        deleteBtn = (Button) findViewById(R.id.man_modify_tea_delete_btn);

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
                        resultList.addAll(DButil.QueryMoreString("tea_Id", "teacher", "tea_id", requestStr));
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
                        resultList.addAll(DButil.QueryMoreString("tea_name", "teacher", "tea_name", requestStr));
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ManModifyTeaActivity.this, android.R.layout.simple_list_item_1, resultList);
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
                                    teacher = DButil.queryOneTeacher(request);
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
                        idEdit.setText(teacher.getIdStr());
                        idEdit.setEnabled(false);
                        nameEdit.setText(teacher.getNameStr());
                        genderEdit.setText(teacher.getGenderStr());
                        birthEdit.setText(teacher.getBirthDate().toString());
                        passwordEdit.setText(teacher.getPasswordStr());
                        speIdEdit.setText(teacher.getDepIdStr());
                    }
                }).create();
        listDialog.show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idStr = idEdit.getText().toString();
                nameStr = nameEdit.getText().toString();
                genderStr = genderEdit.getText().toString();
                birthStr = birthEdit.getText().toString();
                passwordStr = passwordEdit.getText().toString();
                sprIdStr = speIdEdit.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            DButil.modifyTea(idStr, nameStr, genderStr, birthStr, passwordStr, sprIdStr,teacher.getPower());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Toast.makeText(ManModifyTeaActivity.this,"修改成功",Toast.LENGTH_LONG).show();
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
                            DButil.deleteOneRecord("Teacher","tea_id",idStr);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                idEdit.setEnabled(false);
                nameEdit.setEnabled(false);
                genderEdit.setEnabled(false);
                birthEdit.setEnabled(false);
                passwordEdit.setEnabled(false);
                speIdEdit.setEnabled(false);
                submitBtn.setClickable(false);
                deleteBtn.setClickable(false);
                Toast.makeText(ManModifyTeaActivity.this,"删除成功",Toast.LENGTH_LONG).show();
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
