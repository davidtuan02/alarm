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

public class AddAlarm_Laplai_Activity extends AppCompatActivity {

    private Toolbar toolbarLaplai;
    private CheckBox checkBoxt2, checkBoxtCn, checkBoxt3, checkBoxt4, checkBoxt5, checkBoxt6, checkBoxt7;
    private ImageView iv_Back;
    private TextView tv_Huy, tvT2, tvT3, tvT4, tvT5, tvT6, tvT7, tvCN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm_laplai);

        // Initialize views
        mapUI();

        // Receive repeat value from intent (if coming from SuaBaoThuc_Activity or Add_Alarm_Activity)
        Intent intent = getIntent();
        if (intent != null) {
            String repeat = intent.getStringExtra("repeat");
            if (repeat != null) {
                setCheckboxStateFromRepeat(repeat);
            } else {
                restoreCheckboxState();
            }
        } else {
            // Restore checkbox state if no intent received
            restoreCheckboxState();
        }

        // Setup toolbar
        toolbarLaplai = findViewById(R.id.ToolbarAddAlarm_laplai);
        setSupportActionBar(toolbarLaplai);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide default title
        }

        // Back button click listener
        iv_Back.setOnClickListener(v -> returnResultAndFinish());

        // Cancel button click listener
        tv_Huy.setOnClickListener(v -> returnResultAndFinish());

        // Setup click listeners for each day TextView
        setupTextViewClick(R.id.tv_Moit2, checkBoxt2);
        setupTextViewClick(R.id.tv_Moit3, checkBoxt3);
        setupTextViewClick(R.id.tv_Moit4, checkBoxt4);
        setupTextViewClick(R.id.tv_Moit5, checkBoxt5);
        setupTextViewClick(R.id.tv_Moit6, checkBoxt6);
        setupTextViewClick(R.id.tv_Moit7, checkBoxt7);
        setupTextViewClick(R.id.tv_Moicn, checkBoxtCn);
    }

    // Method to handle TextView click and toggle CheckBox
    private void setupTextViewClick(int textViewId, CheckBox checkBox) {
        TextView textView = findViewById(textViewId);
        textView.setOnClickListener(v -> checkBox.setChecked(!checkBox.isChecked()));
    }

    // Map UI elements
    private void mapUI() {
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

    // Generate repeat string based on CheckBox states
    private String generateRepeatString() {
        StringBuilder repeat = new StringBuilder("0000000");

        if (checkBoxt2.isChecked()) repeat.setCharAt(0, '1');
        if (checkBoxt3.isChecked()) repeat.setCharAt(1, '1');
        if (checkBoxt4.isChecked()) repeat.setCharAt(2, '1');
        if (checkBoxt5.isChecked()) repeat.setCharAt(3, '1');
        if (checkBoxt6.isChecked()) repeat.setCharAt(4, '1');
        if (checkBoxt7.isChecked()) repeat.setCharAt(5, '1');
        if (checkBoxtCn.isChecked()) repeat.setCharAt(6, '1');

        return repeat.toString();
    }

    // Save CheckBox states in SharedPreferences
    private void saveCheckboxState() {
        SharedPreferences sharedPreferences = getSharedPreferences("checkbox_state_laplai", MODE_PRIVATE);
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

    // Restore CheckBox states from SharedPreferences
    private void restoreCheckboxState() {
        SharedPreferences sharedPreferences = getSharedPreferences("checkbox_state_laplai", MODE_PRIVATE);
        checkBoxt2.setChecked(sharedPreferences.getBoolean("t2", false));
        checkBoxt3.setChecked(sharedPreferences.getBoolean("t3", false));
        checkBoxt4.setChecked(sharedPreferences.getBoolean("t4", false));
        checkBoxt5.setChecked(sharedPreferences.getBoolean("t5", false));
        checkBoxt6.setChecked(sharedPreferences.getBoolean("t6", false));
        checkBoxt7.setChecked(sharedPreferences.getBoolean("t7", false));
        checkBoxtCn.setChecked(sharedPreferences.getBoolean("cn", false));
    }

    // Set CheckBox states based on repeat string
    private void setCheckboxStateFromRepeat(String repeat) {
        if ("Không".equals(repeat)) {
            checkBoxt2.setChecked(false);
            checkBoxt3.setChecked(false);
            checkBoxt4.setChecked(false);
            checkBoxt5.setChecked(false);
            checkBoxt6.setChecked(false);
            checkBoxt7.setChecked(false);
            checkBoxtCn.setChecked(false);
        } else if ("Mỗi ngày".equals(repeat)) {
            checkBoxt2.setChecked(true);
            checkBoxt3.setChecked(true);
            checkBoxt4.setChecked(true);
            checkBoxt5.setChecked(true);
            checkBoxt6.setChecked(true);
            checkBoxt7.setChecked(true);
            checkBoxtCn.setChecked(true);
        } else if (repeat.length() == 7) {
            checkBoxt2.setChecked(repeat.charAt(0) == '1');
            checkBoxt3.setChecked(repeat.charAt(1) == '1');
            checkBoxt4.setChecked(repeat.charAt(2) == '1');
            checkBoxt5.setChecked(repeat.charAt(3) == '1');
            checkBoxt6.setChecked(repeat.charAt(4) == '1');
            checkBoxt7.setChecked(repeat.charAt(5) == '1');
            checkBoxtCn.setChecked(repeat.charAt(6) == '1');
        }
    }

    // Return repeat string as result and finish activity
    private void returnResultAndFinish() {
        String repeat = generateRepeatString();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("repeat", repeat);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveCheckboxState(); // Save CheckBox state when activity is destroyed
    }
}
