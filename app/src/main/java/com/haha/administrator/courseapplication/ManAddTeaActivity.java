package com.haha.administrator.courseapplication;

import android.content.Context;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.haha.administrator.courseapplication.jdbc.DButil;

import java.sql.SQLException;
import java.text.ParseException;

public class ManAddTeaActivity extends AppCompatActivity {

    private EditText idEdit;
    private EditText nameEdit;
    private EditText genderEdit;
    private EditText birthEdit;
    private EditText passwordEdit;
    private EditText depIdEdit;
    private EditText powerEdit;

    private Button submitBtn;

    private String idStr = "";
    private String nameStr = "";
    private String genderStr = "";
    private String birthStr = "";
    private String passwordStr = "";
    private String depIdStr = "";
    private String powerStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_add_tea);
        ActionBar actionBar = getSupportActionBar();
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);
        idEdit = (EditText) findViewById(R.id.man_edit_tea_id);
        nameEdit = (EditText) findViewById(R.id.man_edit_tea_name);
        genderEdit = (EditText) findViewById(R.id.man_edit_tea_gender);
        birthEdit = (EditText) findViewById(R.id.man_edit_tea_birth);
        passwordEdit = (EditText) findViewById(R.id.man_edit_tea_password);
        depIdEdit = (EditText) findViewById(R.id.man_edit_tea_dep_id);
        powerEdit = (EditText) findViewById(R.id.man_edit_tea_power);
        submitBtn = (Button) findViewById(R.id.man_edit_tea_submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idStr = idEdit.getText().toString();
                nameStr = nameEdit.getText().toString();
                genderStr = genderEdit.getText().toString();
                birthStr = birthEdit.getText().toString();
                passwordStr = passwordEdit.getText().toString();
                depIdStr = depIdEdit.getText().toString();
                powerStr = powerEdit.getText().toString();

                final String queryIdStr = idStr;
                final boolean[] isRepeated = {false};
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String checkId = DButil.QueryOneString("tea_id", "teacher", "tea_id", queryIdStr);
                            Log.e("checkId", "run: " + checkId);
                            if (queryIdStr.equals(checkId)) {
                                isRepeated[0] = true;
                                Log.e("query", "run: " + isRepeated[0]);
                            } else {
                                isRepeated[0] = false;
                                DButil.TeacherInsert(idStr, nameStr, genderStr, birthStr, passwordStr, depIdStr, powerStr);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                try {
                    Thread.sleep(1200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("isrepe", "onClick: " + isRepeated[0]);
                if (isRepeated[0] == false) {
                    Toast.makeText(ManAddTeaActivity.this, "插入成功", Toast.LENGTH_LONG).show();
                } else {
                    idEdit.setError("工号已存在");
                    idEdit.requestFocus();
                }

            }
        });
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
