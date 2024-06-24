package com.example.myalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class AmBao_BaoThuc_Activity extends AppCompatActivity {

    private String selectedRingtone = "";
    MediaPlayer mediaPlayer;
    ImageView ivBack;
    TextView tv_Huy, tvBai1, tvBai2;
    CheckBox checkBoxBai1;
    CheckBox checkBoxBai2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_am_bao_bao_thuc);

        ivBack = findViewById(R.id.iv_back);
        tv_Huy = findViewById(R.id.tv_Huy);
        tvBai1 = findViewById(R.id.tv1);
        tvBai2 = findViewById(R.id.tv2);
        checkBoxBai1 = findViewById(R.id.Cb_bai1);
        checkBoxBai2 = findViewById(R.id.Cb_bai2);

        Intent intent = getIntent();
        if (intent != null) {
            String tone = intent.getStringExtra("selectedRingtone");
            if (tone != null) {
                setCheckboxStateFromTone(tone);
            } else {
                // Khôi phục trạng thái của các CheckBox nếu không nhận được Intent
                restoreCheckboxState();
            }
        } else {
            // Khôi phục trạng thái của các CheckBox nếu không nhận được Intent
            restoreCheckboxState();
        }

        checkBoxBai1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checkBoxBai2.setChecked(false);
                }
                else {
                    checkBoxBai2.setChecked(true);
                }
            }
        });

        checkBoxBai2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    checkBoxBai1.setChecked(false);
                }
                else {
                    checkBoxBai1.setChecked(true);
                }
            }
        });


        // Lắng nghe sự kiện nhấn nút quay lại
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResultAndFinish(); // Gọi phương thức kết thúc và trả về kết quả
            }
        });

        tv_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResultAndFinish(); // Gọi phương thức kết thúc và trả về kết quả
            }
        });

        setupRingtoneSelection();
    }

    private void setupRingtoneSelection() {
        TextView tvRingtone1 = findViewById(R.id.tv1);
        TextView tvRingtone2 = findViewById(R.id.tv2);

        View.OnClickListener ringtoneClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView selectedTextView = (TextView) v;
                selectedRingtone = selectedTextView.getText().toString();

                if (v.getId() == R.id.tv1) {
                    checkBoxBai1.setChecked(true);
                    checkBoxBai2.setChecked(false);
                } else if (v.getId() == R.id.tv2) {
                    checkBoxBai1.setChecked(false);
                    checkBoxBai2.setChecked(true);
                }

                // Kiểm tra nếu đã có bài hát đang được phát
                if (mediaPlayer != null) {
                    mediaPlayer.stop(); // Dừng bài hát đang được phát
                    mediaPlayer.release(); // Giải phóng tài nguyên của bài hát
                }

                // Phát bài hát mới được chọn
                if (selectedRingtone.equals("Thức dậy cho tao")) {
                    mediaPlayer = MediaPlayer.create(AmBao_BaoThuc_Activity.this, R.raw.sena);
                    Intent intent = new Intent();
                    intent.putExtra("selectedRingtone", selectedRingtone);
                    setResult(Activity.RESULT_OK, intent);
                } else if (selectedRingtone.equals("Quân đội")) {
                    mediaPlayer = MediaPlayer.create(AmBao_BaoThuc_Activity.this, R.raw.quandoi);
                    Intent intent = new Intent();
                    intent.putExtra("selectedRingtone", selectedRingtone);
                    setResult(Activity.RESULT_OK, intent);
                }

                // Kiểm tra và bắt đầu phát bài hát mới nếu có
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
            }
        };

        tvRingtone1.setOnClickListener(ringtoneClickListener);
        tvRingtone2.setOnClickListener(ringtoneClickListener);
    }

    private String generateToneString() {
        String checkedTone = "Quân đội";
        if(checkBoxBai1.isChecked()) {
            checkedTone = "Thức dậy cho tao";
        }
        if(checkBoxBai2.isChecked()) {
            checkedTone = "Quân đội";
        }

        return checkedTone;
    }


    private void saveCheckboxState() {
        SharedPreferences sharedPreferences = getSharedPreferences("checkbox_state", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Thức dậy cho tao", checkBoxBai1.isChecked());
        editor.putBoolean("Quân đội", checkBoxBai2.isChecked());
        editor.apply();
    }

    private void restoreCheckboxState() {
        SharedPreferences sharedPreferences = getSharedPreferences("checkbox_state", MODE_PRIVATE);
        checkBoxBai1.setChecked(sharedPreferences.getBoolean("Thức dậy cho tao", false));
        checkBoxBai2.setChecked(sharedPreferences.getBoolean("Quân đội", true));
    }

    private void setCheckboxStateFromTone(String tone) {
        if(tone.equals("Nhạc hay")) {
            checkBoxBai2.setChecked(true);
            checkBoxBai1.setChecked(false);
        }
        else {
            checkBoxBai1.setChecked(tone.contains("Thức dậy cho tao"));
            checkBoxBai2.setChecked(tone.contains("Quân đội"));
        }
    }

//
    private void returnResultAndFinish() {
        String checkedTone = generateToneString();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selectedRingtone", checkedTone);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Giải phóng tài nguyên khi kết thúc ứng dụng
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        saveCheckboxState();
    }
}