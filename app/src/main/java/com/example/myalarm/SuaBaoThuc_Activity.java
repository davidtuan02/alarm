package com.example.myalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class SuaBaoThuc_Activity extends AppCompatActivity {

    private NumberPicker numberPickerHour, numberPickerMinute;
    private TextView tvLabel, tvLapLai, tvAmBao;
    private Switch switchRepeat;
    private String isEnable;
    private int alarmId;
    private SQL dbHelper;

    private static final int REQUEST_EDIT_LABEL = 1;
    private static final int REQUEST_EDIT_REPEAT = 2;
    private static final int REQUEST_EDIT_SOUND = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_bao_thuc);

        dbHelper = new SQL(this);
        alarmId = getIntent().getIntExtra("ALARM_ID", -1);

        numberPickerHour = findViewById(R.id.numberPickerHour);
        numberPickerMinute = findViewById(R.id.numberPickerMinute);
        tvLabel = findViewById(R.id.tv_Label);
        tvLapLai = findViewById(R.id.tv_Laplai);
        tvAmBao = findViewById(R.id.tv_Ambao);
        switchRepeat = findViewById(R.id.switch1);

        numberPickerHour.setMinValue(0);
        numberPickerHour.setMaxValue(23);
        numberPickerMinute.setMinValue(0);
        numberPickerMinute.setMaxValue(59);

        if (alarmId != -1) {
            AlarmClockRecord alarm = dbHelper.getAlarmById(String.valueOf(alarmId));
            if (alarm != null) {
                updateUI(alarm);
            } else {
                Toast.makeText(this, "Không tìm thấy báo thức", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Không nhận được ID của báo thức", Toast.LENGTH_SHORT).show();
            finish();
        }

        tvLabel.setOnClickListener(v -> {
            Intent intent = new Intent(SuaBaoThuc_Activity.this, NhanBaoThuc_Activity.class);
            intent.putExtra("current_label", tvLabel.getText().toString());
            startActivityForResult(intent, REQUEST_EDIT_LABEL);
        });

        tvLapLai.setOnClickListener(v -> {
            Intent intent = new Intent(SuaBaoThuc_Activity.this, AddAlarm_Laplai_Activity.class);
            intent.putExtra("repeat", dbHelper.getAlarmById(String.valueOf(alarmId)).days);
            startActivityForResult(intent, REQUEST_EDIT_REPEAT);
        });

        tvAmBao.setOnClickListener(v -> {
            Intent intent = new Intent(SuaBaoThuc_Activity.this, AmBao_BaoThuc_Activity.class);
            intent.putExtra("selectedRingtone", tvAmBao.getText().toString());
            startActivityForResult(intent, REQUEST_EDIT_SOUND);
        });

        switchRepeat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Cập nhật trạng thái lặp lại khi switch thay đổi
        });

        TextView tvHuy = findViewById(R.id.tv_Huy);
        tvHuy.setOnClickListener(v -> finish());

        TextView tvLuu = findViewById(R.id.tv_Luu);
        tvLuu.setOnClickListener(v -> saveAlarm());

        Button btnDelete = findViewById(R.id.btn_Del);
        btnDelete.setOnClickListener(v -> deleteAlarm());
    }

    private void updateUI(AlarmClockRecord alarm) {
        isEnable = alarm.isEnable;
        numberPickerHour.setValue(Integer.parseInt(alarm.hour));
        numberPickerMinute.setValue(Integer.parseInt(alarm.minute));
        tvLabel.setText(alarm.label);
        switchRepeat.setChecked(alarm.isSnooze.equals("true"));
        updateRepeatTextView(alarm.days);

        // Xác định âm báo dựa trên byte array của âm thanh từ cơ sở dữ liệu
        byte[] senaTone = convertInputStreamToByteArray(getResources().openRawResource(R.raw.sena));
        byte[] quandoiTone = convertInputStreamToByteArray(getResources().openRawResource(R.raw.quandoi));

        boolean isSenaTone = Arrays.equals(alarm.tone, senaTone);
        boolean isQuandoiTone = Arrays.equals(alarm.tone, quandoiTone);

        if (isSenaTone) {
            tvAmBao.setText("Thức dậy cho tao");
        } else if (isQuandoiTone) {
            tvAmBao.setText("Quân đội");
        } else {
            tvAmBao.setText("Nhạc hay");
        }
    }

    private byte[] convertInputStreamToByteArray(InputStream inputStream) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        try {
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer.toByteArray();
    }

    private void saveAlarm() {
        String hour = String.valueOf(numberPickerHour.getValue());
        String minute = String.valueOf(numberPickerMinute.getValue());
        String label = tvLabel.getText().toString();
        String days = tvLapLai.getText().toString();
        String sound = tvAmBao.getText().toString();
        String repeat = switchRepeat.isChecked() ? "true" : "false";

        byte[] soundBytes = null;
        if (sound.equals("Quân đội") || sound.equals("Nhạc hay")) {
            soundBytes = convertInputStreamToByteArray(getResources().openRawResource(R.raw.quandoi));
        } else if (sound.equals("Thức dậy cho tao")) {
            soundBytes = convertInputStreamToByteArray(getResources().openRawResource(R.raw.sena));
        }

        AlarmClockRecord updatedAlarm = new AlarmClockRecord(String.valueOf(alarmId), label, hour, minute, days, "", soundBytes, repeat, isEnable);
        int result = dbHelper.updateRecord(updatedAlarm);

        if (result > 0) {
            finish();
        } else {
            Toast.makeText(this, "Lỗi khi cập nhật báo thức", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteAlarm() {
        boolean deleted = dbHelper.deleteRecord(String.valueOf(alarmId));
        if (deleted) {
            finish();
        } else {
            Toast.makeText(this, "Không thể xóa báo thức", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_EDIT_LABEL:
                    String newLabel = data.getStringExtra("label");
                    if (newLabel != null) {
                        if (newLabel.equals("")) {
                            newLabel = "Báo thức";
                        }
                        tvLabel.setText(newLabel);
                    }
                    break;
                case REQUEST_EDIT_REPEAT:
                    String newDays = data.getStringExtra("repeat");
                    if (newDays != null) {
                        updateRepeatTextView(newDays);
                    }
                    break;
                case REQUEST_EDIT_SOUND:
                    String newSound = data.getStringExtra("selectedRingtone");
                    if (newSound != null) {
                        tvAmBao.setText(newSound);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void updateRepeatTextView(String repeat) {
        switch (repeat) {
            case "1111111":
                tvLapLai.setText("Mỗi ngày");
                break;
            case "0000000":
                tvLapLai.setText("Không");
                break;
            default:
                // Xử lý các trường hợp còn lại
                StringBuilder repeatText = new StringBuilder();
                if (repeat.charAt(0) == '1') {
                    repeatText.append("T2 ");
                }
                if (repeat.charAt(1) == '1') {
                    repeatText.append("T3 ");
                }
                if (repeat.charAt(2) == '1') {
                    repeatText.append("T4 ");
                }
                if (repeat.charAt(3) == '1') {
                    repeatText.append("T5 ");
                }
                if (repeat.charAt(4) == '1') {
                    repeatText.append("T6 ");
                }
                if (repeat.charAt(5) == '1') {
                    repeatText.append("T7 ");
                }
                if (repeat.charAt(6) == '1') {
                    repeatText.append("CN");
                }
                tvLapLai.setText(repeatText.toString().trim());
                break;
        }
    }
}
