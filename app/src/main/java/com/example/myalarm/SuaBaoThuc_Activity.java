package com.example.myalarm;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class SuaBaoThuc_Activity extends AppCompatActivity {

    private NumberPicker numberPickerHour, numberPickerMinute;
    private TextView tvLabel, tvLapLai, tvAmBao;
    private Switch switchRepeat;
    private String isEnable;
    private int alarmId;
    private SQL dbHelper;

    private String newFilePath;
    private String newSound;
    private byte[] selectedSound = null;

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

        tvLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuaBaoThuc_Activity.this, NhanBaoThuc_Activity.class);
                intent.putExtra("current_label", tvLabel.getText().toString());
                startActivityForResult(intent, REQUEST_EDIT_LABEL);
            }
        });

        tvLapLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuaBaoThuc_Activity.this, AddAlarm_Laplai_Activity.class);
                intent.putExtra("repeat", tvLapLai.getText().toString());
                startActivityForResult(intent, REQUEST_EDIT_REPEAT);
            }
        });

        tvAmBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuaBaoThuc_Activity.this, AmBao_BaoThuc_Activity.class);
                intent.putExtra("selectedRingtone", tvAmBao.getText().toString());
                startActivityForResult(intent, REQUEST_EDIT_SOUND);
            }
        });

        switchRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            }
        });

        TextView tvHuy = findViewById(R.id.tv_Huy);
        tvHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tvLuu = findViewById(R.id.tv_Luu);
        tvLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAlarm();
            }
        });

        Button btnDelete = findViewById(R.id.btn_Del);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlarm();
            }
        });
    }

    private void updateUI(AlarmClockRecord alarm) {
        isEnable = alarm.isEnable;
        selectedSound = alarm.tone;
        numberPickerHour.setValue(Integer.parseInt(alarm.hour));
        numberPickerMinute.setValue(Integer.parseInt(alarm.minute));
        tvLabel.setText(alarm.label);
        tvLapLai.setText(alarm.days);
        if(alarm.isSnooze.equals("true")) {
            switchRepeat.setChecked(true);
        }
        else if(alarm.isSnooze.equals("false")) {
            switchRepeat.setChecked(false);
        }

        byte[] quandoiTone = null;
        byte[] gagayTone = null;
        byte[] killthisloveTone = null;

        // Đọc file MP3 từ res/raw
        InputStream inputStreamGaGay = getResources().openRawResource(R.raw.gagay);
        gagayTone = convertInputStreamToByteArray(inputStreamGaGay);

        InputStream inputStreamQuandoi = getResources().openRawResource(R.raw.quandoi);
        quandoiTone = convertInputStreamToByteArray(inputStreamQuandoi);

        InputStream inputStreamKTL = getResources().openRawResource(R.raw.killthislove);
        killthisloveTone = convertInputStreamToByteArray(inputStreamKTL);

        // Đóng InputStream sau khi sử dụng
        try {
            inputStreamGaGay.close();
            inputStreamQuandoi.close();
            inputStreamKTL.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Kiểm tra xem alarm.tone có bằng senaTone hay quandoiTone không
        boolean isQuandoiTone = Arrays.equals(alarm.tone, quandoiTone);
        boolean isGaGayTone = Arrays.equals(alarm.tone, gagayTone);
        boolean isKillthisloveTone = Arrays.equals(alarm.tone, killthisloveTone);

        if (isQuandoiTone) {
            tvAmBao.setText("Quân đội");
        }
        else if (isGaGayTone) {
            tvAmBao.setText("Tiếng gà gáy");
        }
        else if (isKillthisloveTone) {
            tvAmBao.setText("Kill this love");
        }
        else {
            tvAmBao.setText("LỖI NÀY!");
        }

    }


    // Hàm chuyển đổi byte[] thành chuỗi
    private String convertByteArrayToString(byte[] byteArray) {
        try {
            return new String(byteArray, "UTF-8"); // Sử dụng mã hóa phù hợp
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
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
//        String sound = tvAmBao.getText().toString();
        String repeat;
        if(switchRepeat.isChecked()) {
            repeat = "true";
        }
        else {
            repeat = "false";
        }

        AlarmClockRecord updatedAlarm = new AlarmClockRecord(String.valueOf(alarmId), label, hour, minute, days, "", selectedSound, repeat, isEnable);
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
                        if(newLabel.equals("")) {
                            newLabel = "Báo thức";
                        }
                        tvLabel.setText(newLabel);
                    }
                    break;
                case REQUEST_EDIT_REPEAT:
                    String newDays = data.getStringExtra("repeat");
                    if (newDays != null) {
                        tvLapLai.setText(newDays);
                    }
                    break;
                case REQUEST_EDIT_SOUND:
                    String newSound = data.getStringExtra("selectedRingtone");
                    String newFilePath = data.getStringExtra("selectedFilePath");

                    if (newFilePath != null && !newFilePath.isEmpty()) {
                        // Xử lý newFilePath tại đây
                        tvAmBao.setText("Custom Sound");

                        try {
                            // Tạo đối tượng FileInputStream từ đường dẫn filePath
                            FileInputStream inputStream = new FileInputStream(newFilePath);

                            // Chuyển đổi inputStream thành mảng byte
                            byte[] soundBytes = convertInputStreamToByteArray(inputStream);

                            // Lưu soundBytes vào selectedSound
                            selectedSound = soundBytes;

                            // Đóng inputStream nếu không sử dụng nữa để giải phóng tài nguyên
                            inputStream.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if (newSound != null && !newSound.isEmpty()) {
                        // Xử lý newSound tại đây
                        tvAmBao.setText(newSound);

                        if (newSound.equals("Quân đội")) {
                            InputStream inputStream = getResources().openRawResource(R.raw.quandoi);
                            byte[] soundBytes = convertInputStreamToByteArray(inputStream);
                            selectedSound = soundBytes;
                        }
//
                        else if (newSound.equals("Tiếng gà gáy")) {
                            InputStream inputStream = getResources().openRawResource(R.raw.gagay);
                            byte[] soundBytes = convertInputStreamToByteArray(inputStream);
                            selectedSound = soundBytes;
                        }
                        else if (newSound.equals("Kill this love")) {
                            InputStream inputStream = getResources().openRawResource(R.raw.killthislove);
                            byte[] soundBytes = convertInputStreamToByteArray(inputStream);
                            selectedSound = soundBytes;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
