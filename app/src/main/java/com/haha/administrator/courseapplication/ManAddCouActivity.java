package com.haha.administrator.courseapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.haha.administrator.courseapplication.LoginActivity;
import com.haha.administrator.courseapplication.MysqlService;
import com.haha.administrator.courseapplication.R;
import com.haha.administrator.courseapplication.jdbc.DButil;

import java.sql.SQLException;

public class ManAddCouActivity extends AppCompatActivity {

    private EditText couIdEdit;
    private EditText couNameEdit;
    private EditText couPeriodEdit;
    private EditText couCreditEdit;
    private EditText couNumEdit;
    private EditText couBeEdit;
    private EditText couTimeEdit;
    private EditText couTeaEdit;

    private String couIdStr = "";
    private String couNameStr = "";
    private String couPeriodStr = "";
    private String couCreditStr = "";
    private String couNumStr = "";
    private String couBeStr = "";
    private String couTimeStr = "";
    private String couTeaStr = "";

    private Button submitBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_add_cou);

        ActionBar actionBar = getSupportActionBar();
        // 显示返回按钮
        actionBar.setDisplayHomeAsUpEnabled(true);
        // 去掉logo图标
        actionBar.setDisplayShowHomeEnabled(false);

        couIdEdit = (EditText) findViewById(R.id.man_edit_cou_id);
        couNameEdit = (EditText) findViewById(R.id.man_edit_cou_name);
        couPeriodEdit = (EditText) findViewById(R.id.man_edit_cou_period);
        couCreditEdit = (EditText) findViewById(R.id.man_edit_cou_credit);
        couNumEdit = (EditText) findViewById(R.id.man_edit_cou_number);
        couBeEdit = (EditText) findViewById(R.id.man_edit_cou_be);
        couTimeEdit = (EditText) findViewById(R.id.man_edit_cou_time);
        couTeaEdit = (EditText) findViewById(R.id.man_edit_cou_tea);
        submitBtn = (Button) findViewById(R.id.man_edit_cou_submit_btn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couIdStr = couIdEdit.getText().toString();
                couNameStr = couNameEdit.getText().toString();
                couPeriodStr = couPeriodEdit.getText().toString();
                couCreditStr = couCreditEdit.getText().toString();
                couNumStr = couNumEdit.getText().toString();
                couBeStr = couBeEdit.getText().toString();
                couTimeStr = couTimeEdit.getText().toString();
                couTeaStr = couTeaEdit.getText().toString();
                couTeaStr = couTeaEdit.getText().toString();

                final String queryIdStr = couIdStr;
                final boolean[] isRepeated = {false};
                final int[] resultInt = {0};
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String checkId = DButil.QueryOneString("cou_id", "course", "cou_id", queryIdStr);
                            Log.e("checkId", "run: " + checkId);
                            if (queryIdStr.equals(checkId)) {
                                isRepeated[0] = true;
                                Log.e("query", "run: " + isRepeated[0]);
                            } else {
                                isRepeated[0] = false;
                                resultInt[0] = DButil.CourseInsert(couIdStr, couNameStr, couPeriodStr, couCreditStr,
                                        couNumStr, couBeStr, couTimeStr, couTeaStr);
                            }
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
                Log.e("isrepe", "onClick: "+isRepeated[0]);
                if (isRepeated[0]==false) {
                    Toast.makeText(ManAddCouActivity.this, "插入" + resultInt[0] + "成功", Toast.LENGTH_LONG).show();
                }else {
                    couIdEdit.setError("课程编号已存在");
                    couIdEdit.requestFocus();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unbindService(serviceConnection);
    }
}
