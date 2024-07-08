package com.example.myalarm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class AmBao_BaoThuc_Activity extends AppCompatActivity {

    private String selectedRingtone = "";
    private InputStream selectedSound;
    MediaPlayer mediaPlayer;
    ImageView ivBack;
    TextView tv_Huy, tvBai1, tvBai2, tvBai3, selectFile;
    CheckBox checkBoxBai1, checkBoxBai2, checkBoxBai3;

    private static final int PICK_FILE_REQUEST = 2;
    private String selectedFilePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_am_bao_bao_thuc);

        ivBack = findViewById(R.id.iv_back);
        tv_Huy = findViewById(R.id.tv_Huy);
        tvBai1 = findViewById(R.id.tv1);
        tvBai2 = findViewById(R.id.tv2);
        tvBai3 = findViewById(R.id.tv3);
        checkBoxBai1 = findViewById(R.id.Cb_bai1);
        checkBoxBai2 = findViewById(R.id.Cb_bai2);
        checkBoxBai3 = findViewById(R.id.Cb_bai3);
        selectFile = findViewById(R.id.tvSelect);

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

        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilePicker();
            }
        });

        checkBoxBai1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    checkBoxBai2.setChecked(false);
//                    checkBoxBai3.setChecked(false);
//                    checkBoxBai4.setChecked(false);
//                    checkBoxBai5.setChecked(false);
//                }
//                else {
//                    checkBoxBai2.setChecked(true);
//                }
            }
        });

        checkBoxBai2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    checkBoxBai1.setChecked(false);
//                    checkBoxBai3.setChecked(false);
//                    checkBoxBai4.setChecked(false);
//                    checkBoxBai5.setChecked(false);
//                }
//                else {
//                    checkBoxBai1.setChecked(true);
//                }
            }
        });

        checkBoxBai3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    checkBoxBai1.setChecked(false);
//                    checkBoxBai2.setChecked(false);
//                    checkBoxBai4.setChecked(false);
//                    checkBoxBai5.setChecked(false);
//                }
//                else {
//                    checkBoxBai1.setChecked(true);
//                }
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

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Chọn tất cả các loại tệp
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Chọn file"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedFileUri = data.getData();
            if (selectedFileUri != null) {
                selectedFilePath = selectedFileUri.toString(); // Lưu đường dẫn tệp đã chọn
                // Xử lý tiếp theo với đường dẫn tệp đã chọn
                // Ví dụ: hiển thị tên tệp, lưu đường dẫn vào SharedPreferences, ...
                selectFile.setText("Tự chọn");
                Toast.makeText(this, "Đã chọn file: " + selectedFilePath, Toast.LENGTH_SHORT).show();

                // Reset checkbox state
                checkBoxBai1.setChecked(false);
                checkBoxBai2.setChecked(false);
                checkBoxBai3.setChecked(false);
            }
        }
    }

    private void setupRingtoneSelection() {
        TextView tvRingtone1 = findViewById(R.id.tv1);
        TextView tvRingtone2 = findViewById(R.id.tv2);
        TextView tvRingtone3 = findViewById(R.id.tv3);

        View.OnClickListener ringtoneClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView selectedTextView = (TextView) v;
                selectedRingtone = selectedTextView.getText().toString();
                selectedFilePath = ""; // Đặt lại selectedFilePath

                if (v.getId() == R.id.tv1) {
                    checkBoxBai1.setChecked(true);
                    checkBoxBai2.setChecked(false);
                    checkBoxBai3.setChecked(false);
                }
                else if (v.getId() == R.id.tv2) {
                    checkBoxBai1.setChecked(false);
                    checkBoxBai2.setChecked(true);
                    checkBoxBai3.setChecked(false);
                }
                else if (v.getId() == R.id.tv3) {
                    checkBoxBai1.setChecked(false);
                    checkBoxBai2.setChecked(false);
                    checkBoxBai3.setChecked(true);
                }

                // Kiểm tra nếu đã có bài hát đang được phát
                if (mediaPlayer != null) {
                    mediaPlayer.stop(); // Dừng bài hát đang được phát
                    mediaPlayer.release(); // Giải phóng tài nguyên của bài hát
                }

                // Phát bài hát mới được chọn
                if (selectedRingtone.equals("Quân đội")) {
//                    Toast.makeText(AmBao_BaoThuc_Activity.this, "quan doi", Toast.LENGTH_SHORT).show();
                    mediaPlayer = MediaPlayer.create(AmBao_BaoThuc_Activity.this, R.raw.quandoi);
                }
                else if (selectedRingtone.equals("Tiếng gà gáy")) {
//                    Toast.makeText(AmBao_BaoThuc_Activity.this, "tieng ga gay", Toast.LENGTH_SHORT).show();
                    mediaPlayer = MediaPlayer.create(AmBao_BaoThuc_Activity.this, R.raw.gagay);
                }
                else if (selectedRingtone.equals("Kill this love")) {
//                    Toast.makeText(AmBao_BaoThuc_Activity.this, "kill this love", Toast.LENGTH_SHORT).show();
                    mediaPlayer = MediaPlayer.create(AmBao_BaoThuc_Activity.this, R.raw.killthislove);
                }

                // Kiểm tra và bắt đầu phát bài hát mới nếu có
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    Intent intent = new Intent();
                    intent.putExtra("selectedRingtone", selectedRingtone);
                    setResult(Activity.RESULT_OK, intent);
                }
            }
        };

        tvRingtone1.setOnClickListener(ringtoneClickListener);
        tvRingtone2.setOnClickListener(ringtoneClickListener);
        tvRingtone3.setOnClickListener(ringtoneClickListener);
    }


    private String generateToneString() {
        String checkedTone;
        if (checkBoxBai2.isChecked()) {
            checkedTone = "Quân đội";
        }
        else if (checkBoxBai1.isChecked()) {
            checkedTone = "Tiếng gà gáy";
        }
        else if (checkBoxBai3.isChecked()) {
            checkedTone = "Kill this love";
        }
        else {
            checkedTone = "Quân đội";
        }

        return checkedTone;
    }

    private void saveCheckboxState() {
        SharedPreferences sharedPreferences = getSharedPreferences("checkbox_state", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Quân đội", checkBoxBai2.isChecked());
        editor.putBoolean("Tiếng gà gáy", checkBoxBai1.isChecked());
        editor.putBoolean("Kill this love", checkBoxBai3.isChecked());
        editor.apply();
    }

    private void restoreCheckboxState() {
        SharedPreferences sharedPreferences = getSharedPreferences("checkbox_state", MODE_PRIVATE);
        checkBoxBai2.setChecked(sharedPreferences.getBoolean("Quân đội", true));
        checkBoxBai1.setChecked(sharedPreferences.getBoolean("Tiếng gà gáy", false));
        checkBoxBai3.setChecked(sharedPreferences.getBoolean("Kill this love", false));
    }


    private void setCheckboxStateFromTone(String tone) {
        if (tone.equals("Nhạc hay")) {
            checkBoxBai1.setChecked(false);
            checkBoxBai2.setChecked(true);
            checkBoxBai3.setChecked(false);
        } else {
            checkBoxBai2.setChecked(tone.contains("Quân đội"));
            checkBoxBai1.setChecked(tone.contains("Tiếng gà gáy"));
            checkBoxBai3.setChecked(tone.contains("Kill this love"));

        }
    }

    private void returnResultAndFinish() {
        Intent resultIntent = new Intent();
        if (!selectedFilePath.isEmpty()) {
            resultIntent.putExtra("selectedFilePath", selectedFilePath);
        } else {
            String checkedTone = generateToneString();
            resultIntent.putExtra("selectedRingtone", checkedTone);
        }
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