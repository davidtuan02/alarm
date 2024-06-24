package com.example.myalarm;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class Add_Alarm_Activity extends AppCompatActivity {

    private static final int REQUEST_CODE_EDIT_LABEL = 123;
    private SQL dbHelper;
    private NumberPicker numberPickerHour;
    private NumberPicker numberPickerMinute;
    private Switch switchLaplai;
    private TextView tv_Laplai;
    private TextView btn_nhan;
    private TextView btn_Ambao;
    private TextView tv_Luu, tv_Huy;

    // Biến để lưu trữ giờ và phút đã chọn
    private String selectedHour;
    private String selectedMinute;

    private String selectedDays = "";

    // Biến để lưu trữ trạng thái của công tắc lặp lại
    private boolean isRepeatEnabled;

    // Biến để lưu trữ tùy chọn nhãn và âm báo
    private String selectedLabel = "";
    private InputStream selectedSound = null;

    // Biến để lưu trữ trạng thái lặp lại
    private String repeatState = "Không";

    // ActivityResultLauncher cho màn hình sửa nhãn và âm báo
    private ActivityResultLauncher<Intent> mEditLabelLauncher;
    private ActivityResultLauncher<Intent> mEditSoundLauncher;
    private ActivityResultLauncher<Intent> mEditRepeatLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        dbHelper = new SQL(this);

        // Ánh xạ các thành phần giao diện
        anhXa();

        // Khởi tạo các NumberPicker và Switch
        initNumberPickersAndSwitch();

        selectedHour = Integer.toString(numberPickerHour.getValue());
        selectedMinute = Integer.toString(numberPickerMinute.getValue());

        // Khởi tạo ActivityResultLauncher cho màn hình sửa nhãn
        mEditLabelLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String editedLabel = data.getStringExtra("label");
                            if(editedLabel.equals("")) {
                                btn_nhan.setText("Báo thức");
                                selectedLabel = "Báo thức";
                            } else {
                                btn_nhan.setText(editedLabel);
                                selectedLabel = editedLabel;
                            }
                        }
                    }
                });

        // Khởi tạo ActivityResultLauncher cho màn hình chọn âm báo
        mEditSoundLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String selectedRingtone = data.getStringExtra("selectedRingtone");

                            if(selectedRingtone.equals("Thức dậy cho tao")) {
                                InputStream inputStream = getResources().openRawResource(R.raw.sena);
                                selectedSound = inputStream;
                                btn_Ambao.setText(selectedRingtone);
                            }
                            else if(selectedRingtone.equals("Quân đội")) {
                                InputStream inputStream = getResources().openRawResource(R.raw.quandoi);
                                selectedSound = inputStream;
                                btn_Ambao.setText(selectedRingtone);
                            }
                            else {
                                btn_Ambao.setText("Nhạc hay");
                            }
                        }
                    }
                });

        mEditRepeatLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String repeat = data.getStringExtra("repeat");
                            repeat = repeat.trim();
                            tv_Laplai.setText(repeat);
                            repeatState = repeat; // Lưu trạng thái lặp lại
                            selectedDays = repeat;
                        }
                    }
                });

        // Lắng nghe sự kiện khi người dùng thay đổi giá trị của NumberPicker
        numberPickerHour.setOnValueChangedListener((picker, oldVal, newVal) -> {
            String hour = Integer.toString(newVal);
            selectedHour = hour;
        });

        numberPickerMinute.setOnValueChangedListener((picker, oldVal, newVal) -> {
            String minute = Integer.toString(newVal);
            selectedMinute = minute;
        });

        // Lắng nghe sự kiện khi người dùng thay đổi trạng thái của Switch
        switchLaplai.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isRepeatEnabled = isChecked;
        });

        // Lắng nghe sự kiện khi người dùng click vào các tùy chọn nhãn và âm báo
        btn_nhan.setOnClickListener(v -> {
            // Lấy giá trị hiện tại của nhãn
            String currentLabel = btn_nhan.getText().toString();

            // Mở màn hình chọn nhãn và gửi giá trị nhãn hiện tại
            Intent intent = new Intent(Add_Alarm_Activity.this, NhanBaoThuc_Activity.class);
            intent.putExtra("current_label", currentLabel);
            mEditLabelLauncher.launch(intent);
        });

        tv_Laplai.setOnClickListener(v -> {
            // Mở màn hình chọn lặp lại và gửi trạng thái hiện tại
            Intent intent = new Intent(Add_Alarm_Activity.this, AddAlarm_Laplai_Activity.class);
            intent.putExtra("repeat", repeatState); // Gửi trạng thái lặp lại hiện tại
            mEditRepeatLauncher.launch(intent);
        });

        btn_Ambao.setOnClickListener(v -> {
            // Mở màn hình chọn âm báo
            Intent intent = new Intent(Add_Alarm_Activity.this, AmBao_BaoThuc_Activity.class);
            mEditSoundLauncher.launch(intent);
        });

        // Lắng nghe sự kiện khi người dùng click vào nút "Lưu"
        tv_Luu.setOnClickListener(v -> {
            if(selectedLabel == "") {
                selectedLabel = "Báo thức";
            }
            if(selectedDays == "") {
                selectedDays = "Mỗi ngày";
            }
            if(selectedSound == null) {
                selectedSound = getResources().openRawResource(R.raw.quandoi);
            }
            int random = (int) Math.floor(Math.random() * 100 + 1);
            String id = Integer.toString(random);
            dbHelper.saveAudio(id, selectedLabel, selectedHour, selectedMinute, selectedDays, "", isRepeatEnabled ? "true" : "false", "true", selectedSound);
            finish();
        });

        // Lắng nghe sự kiện khi người dùng click vào nút "Hủy"
        tv_Huy.setOnClickListener(v -> {
            finish();
        });
    }

    // Ánh xạ các thành phần giao diện
    private void anhXa() {
        numberPickerHour = findViewById(R.id.numberPickerHour);
        numberPickerMinute = findViewById(R.id.numberPickerMinute);
        switchLaplai = findViewById(R.id.switch1);
        tv_Laplai = findViewById(R.id.tv_Laplai);
        btn_nhan = findViewById(R.id.btn_nhan);
        btn_Ambao = findViewById(R.id.btnAmBao);
        tv_Luu = findViewById(R.id.tv_Luu);
        tv_Huy = findViewById(R.id.tv_Huy);
    }

    // Khởi tạo các NumberPicker và Switch
    private void initNumberPickersAndSwitch() {
        // Khởi tạo NumberPicker cho giờ
        numberPickerHour.setMinValue(0);
        numberPickerHour.setMaxValue(23);
        numberPickerHour.setValue(0); // Giá trị mặc định là 0
        selectedHour = "0"; // Khởi tạo giá trị đã chọn

        // Khởi tạo NumberPicker cho phút
        numberPickerMinute.setMinValue(0);
        numberPickerMinute.setMaxValue(59);
        numberPickerMinute.setValue(0); // Giá trị mặc định là 0
        selectedMinute = "0"; // Khởi tạo giá trị đã chọn

        // Khởi tạo Switch cho tùy chọn lặp lại
        switchLaplai.setChecked(false); // Mặc định là không được chọn
        isRepeatEnabled = false; // Khởi tạo trạng thái lặp lại
    }
}
