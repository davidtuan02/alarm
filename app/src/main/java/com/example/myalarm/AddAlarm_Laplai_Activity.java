package com.example.myalarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myalarm.R;

public class AddAlarm_Laplai_Activity extends AppCompatActivity {

    private Toolbar toolbarLaplai;
    private CheckBox checkBoxt2, checkBoxtCn, checkBoxt3, checkBoxt4, checkBoxt5, checkBoxt6, checkBoxt7;
    private ImageView iv_Back;
    private TextView tv_Huy, tvT2, tvT3, tvT4, tvT5, tvT6, tvT7, tvCN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm_laplai);

        // Ánh xạ các view
        anhXa();

        // Nhận giá trị repeat từ Intent nếu có từ SuaBaoThuc_Activity hoặc AddAlarm_Activity
        Intent intent = getIntent();
        if (intent != null) {
            String repeat = intent.getStringExtra("repeat");
            if (repeat != null) {
                setCheckboxStateFromRepeat(repeat);
            } else {
                // Khôi phục trạng thái của các CheckBox nếu không nhận được Intent
                restoreCheckboxState();
            }
        } else {
            // Khôi phục trạng thái của các CheckBox nếu không nhận được Intent
            restoreCheckboxState();
        }

        // Thiết lập toolbar
        toolbarLaplai = findViewById(R.id.ToolbarAddAlarm_laplai);
        setSupportActionBar(toolbarLaplai);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Ẩn tiêu đề mặc định
        }

        iv_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResultAndFinish();
            }
        });

        tv_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResultAndFinish();
            }
        });

        setupTextViewClick(R.id.tv_Moit2, R.id.Cb_t2);
        setupTextViewClick(R.id.tv_Moit3, R.id.Cb_t3);
        setupTextViewClick(R.id.tv_Moit4, R.id.Cb_t4);
        setupTextViewClick(R.id.tv_Moit5, R.id.Cb_t5);
        setupTextViewClick(R.id.tv_Moit6, R.id.Cb_t6);
        setupTextViewClick(R.id.tv_Moit7, R.id.Cb_t7);
        setupTextViewClick(R.id.tv_Moicn, R.id.Cb_cn);
    }

    private void setupTextViewClick(int textViewId, int checkBoxId) {
        TextView textView = findViewById(textViewId);
        CheckBox checkBox = findViewById(checkBoxId);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(!checkBox.isChecked());
            }
        });
    }

    private void anhXa() {
        checkBoxt2 = findViewById(R.id.Cb_t2);
        checkBoxt3 = findViewById(R.id.Cb_t3);
        checkBoxt4 = findViewById(R.id.Cb_t4);
        checkBoxt5 = findViewById(R.id.Cb_t5);
        checkBoxt6 = findViewById(R.id.Cb_t6);
        checkBoxt7 = findViewById(R.id.Cb_t7);
        checkBoxtCn = findViewById(R.id.Cb_cn);
        iv_Back = findViewById(R.id.iv_back);
        tv_Huy = findViewById(R.id.tv_Huy);
        tvT2 = findViewById(R.id.tv_Moit2);
        tvT3 = findViewById(R.id.tv_Moit3);
        tvT4 = findViewById(R.id.tv_Moit4);
        tvT5 = findViewById(R.id.tv_Moit5);
        tvT6 = findViewById(R.id.tv_Moit6);
        tvT7 = findViewById(R.id.tv_Moit7);
        tvCN = findViewById(R.id.tv_Moicn);
    }

    private String generateRepeatString() {
        int count = checkNgayDuocChon();
        String luachon = "Không";

        if (count == 7) {
            luachon = "Mỗi ngày";
        } else if (count > 0) {
            luachon = "Mỗi ";
            if (checkBoxt2.isChecked()) luachon += "T2 ";
            if (checkBoxt3.isChecked()) luachon += "T3 ";
            if (checkBoxt4.isChecked()) luachon += "T4 ";
            if (checkBoxt5.isChecked()) luachon += "T5 ";
            if (checkBoxt6.isChecked()) luachon += "T6 ";
            if (checkBoxt7.isChecked()) luachon += "T7 ";
            if (checkBoxtCn.isChecked()) luachon += "CN";
        }

        return luachon.trim();
    }

    private int checkNgayDuocChon() {
        int count = 0;
        if (checkBoxt2.isChecked()) count++;
        if (checkBoxt3.isChecked()) count++;
        if (checkBoxt4.isChecked()) count++;
        if (checkBoxt5.isChecked()) count++;
        if (checkBoxt6.isChecked()) count++;
        if (checkBoxt7.isChecked()) count++;
        if (checkBoxtCn.isChecked()) count++;
        return count;
    }

    private void saveCheckboxState() {
        SharedPreferences sharedPreferences = getSharedPreferences("checkbox_state", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("t2", checkBoxt2.isChecked());
        editor.putBoolean("t3", checkBoxt3.isChecked());
        editor.putBoolean("t4", checkBoxt4.isChecked());
        editor.putBoolean("t5", checkBoxt5.isChecked());
        editor.putBoolean("t6", checkBoxt6.isChecked());
        editor.putBoolean("t7", checkBoxt7.isChecked());
        editor.putBoolean("cn", checkBoxtCn.isChecked());
        editor.apply();
    }

    private void restoreCheckboxState() {
        SharedPreferences sharedPreferences = getSharedPreferences("checkbox_state", MODE_PRIVATE);
        checkBoxt2.setChecked(sharedPreferences.getBoolean("t2", false));
        checkBoxt3.setChecked(sharedPreferences.getBoolean("t3", false));
        checkBoxt4.setChecked(sharedPreferences.getBoolean("t4", false));
        checkBoxt5.setChecked(sharedPreferences.getBoolean("t5", false));
        checkBoxt6.setChecked(sharedPreferences.getBoolean("t6", false));
        checkBoxt7.setChecked(sharedPreferences.getBoolean("t7", false));
        checkBoxtCn.setChecked(sharedPreferences.getBoolean("cn", false));
    }

    private void setCheckboxStateFromRepeat(String repeat) {
        if (repeat.equals("Mỗi ngày")) {
            checkBoxt2.setChecked(true);
            checkBoxt3.setChecked(true);
            checkBoxt4.setChecked(true);
            checkBoxt5.setChecked(true);
            checkBoxt6.setChecked(true);
            checkBoxt7.setChecked(true);
            checkBoxtCn.setChecked(true);
        } else {
            checkBoxt2.setChecked(repeat.contains("T2"));
            checkBoxt3.setChecked(repeat.contains("T3"));
            checkBoxt4.setChecked(repeat.contains("T4"));
            checkBoxt5.setChecked(repeat.contains("T5"));
            checkBoxt6.setChecked(repeat.contains("T6"));
            checkBoxt7.setChecked(repeat.contains("T7"));
            checkBoxtCn.setChecked(repeat.contains("CN"));
        }
    }

    private void returnResultAndFinish() {
        String luachon = generateRepeatString();

        // Tạo một Intent để chứa dữ liệu trả về
        Intent resultIntent = new Intent();
        // Thêm dữ liệu vào Intent. Sử dụng khóa thống nhất "repeat"
        resultIntent.putExtra("repeat", luachon);
        // Thiết lập kết quả trả về với RESULT_OK và Intent chứa dữ liệu
        setResult(Activity.RESULT_OK, resultIntent);
        // Kết thúc Activity hiện tại và quay lại Activity trước đó
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveCheckboxState();
    }
}
