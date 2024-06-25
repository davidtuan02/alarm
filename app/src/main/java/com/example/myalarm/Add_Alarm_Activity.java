package com.example.myalarm;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class Add_Alarm_Activity extends AppCompatActivity {

    private SQL dbHelper;
    private NumberPicker numberPickerHour;
    private NumberPicker numberPickerMinute;
    private Switch switchLaplai;
    private TextView tv_Laplai;
    private TextView btn_nhan;
    private TextView btn_Ambao;
    private TextView tv_Luu, tv_Huy;

    // Variables to store selected hour and minute
    private String selectedHour;
    private String selectedMinute;

    // Variables to store selected days of repeat
    private String selectedDays = "0000000"; // Default: no repeat

    // Variables to store label and sound
    private String selectedLabel = "";
    private InputStream selectedSound = null;

    // ActivityResultLauncher for label editing screen
    private ActivityResultLauncher<Intent> mEditLabelLauncher;

    // ActivityResultLauncher for sound selection screen
    private ActivityResultLauncher<Intent> mEditSoundLauncher;

    // ActivityResultLauncher for repeat selection screen
    private ActivityResultLauncher<Intent> mEditRepeatLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        dbHelper = new SQL(this);

        // Map UI elements
        mapUI();

        // Initialize NumberPickers and Switch
        initNumberPickersAndSwitch();

        // Set default values for hour and minute pickers
        selectedHour = Integer.toString(numberPickerHour.getValue());
        selectedMinute = Integer.toString(numberPickerMinute.getValue());

        // Initialize ActivityResultLauncher for label editing
        mEditLabelLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String editedLabel = data.getStringExtra("label");
                            if (editedLabel.equals("")) {
                                btn_nhan.setText("Báo thức");
                                selectedLabel = "Báo thức";
                            } else {
                                btn_nhan.setText(editedLabel);
                                selectedLabel = editedLabel;
                            }
                        }
                    }
                });

        // Initialize ActivityResultLauncher for sound selection
        mEditSoundLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String selectedRingtone = data.getStringExtra("selectedRingtone");

                            if (selectedRingtone.equals("Thức dậy cho tao")) {
                                InputStream inputStream = getResources().openRawResource(R.raw.sena);
                                selectedSound = inputStream;
                                btn_Ambao.setText(selectedRingtone);
                            } else if (selectedRingtone.equals("Quân đội")) {
                                InputStream inputStream = getResources().openRawResource(R.raw.quandoi);
                                selectedSound = inputStream;
                                btn_Ambao.setText(selectedRingtone);
                            } else {
                                btn_Ambao.setText("Nhạc hay");
                            }
                        }
                    }
                });

        // Initialize ActivityResultLauncher for repeat selection
        mEditRepeatLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String repeat = data.getStringExtra("repeat");
                            repeat = repeat.trim();
                            updateRepeatTextView(repeat); // Update TextView for repeat display
                            selectedDays = repeat; // Save repeat days string
                        }
                    }
                });

        // Listen to hour and minute picker changes
        numberPickerHour.setOnValueChangedListener((picker, oldVal, newVal) -> {
            String hour = Integer.toString(newVal);
            selectedHour = hour;
        });

        numberPickerMinute.setOnValueChangedListener((picker, oldVal, newVal) -> {
            String minute = Integer.toString(newVal);
            selectedMinute = minute;
        });

        // Click listener for label selection
        btn_nhan.setOnClickListener(v -> {
            // Get current label value
            String currentLabel = btn_nhan.getText().toString();

            // Open label editing screen and pass current label value
            Intent intent = new Intent(Add_Alarm_Activity.this, NhanBaoThuc_Activity.class);
            intent.putExtra("current_label", currentLabel);
            mEditLabelLauncher.launch(intent);
        });

        // Click listener for repeat selection
        tv_Laplai.setOnClickListener(v -> {
            // Open repeat selection screen and pass current repeat state
            Intent intent = new Intent(Add_Alarm_Activity.this, AddAlarm_Laplai_Activity.class);
            intent.putExtra("repeat", selectedDays); // Pass current repeat state
            mEditRepeatLauncher.launch(intent);
        });

        // Click listener for sound selection
        btn_Ambao.setOnClickListener(v -> {
            // Open sound selection screen
            Intent intent = new Intent(Add_Alarm_Activity.this, AmBao_BaoThuc_Activity.class);
            mEditSoundLauncher.launch(intent);
        });

        // Click listener for "Lưu" button
        tv_Luu.setOnClickListener(v -> {
            if (selectedLabel.equals("")) {
                selectedLabel = "Báo thức";
            }
            updateRepeatTextView(selectedDays); // Update TextView for repeat display

            if (selectedSound == null) {
                selectedSound = getResources().openRawResource(R.raw.quandoi);
            }
            // Generate a random ID
            int random = (int) Math.floor(Math.random() * 100 + 1);
            String id = Integer.toString(random);

            // Save alarm details to database
            dbHelper.saveAudio(id, selectedLabel, selectedHour, selectedMinute, selectedDays, "", switchLaplai.isChecked() ? "true" : "false", "true", selectedSound);
            finish(); // Finish activity
        });

        // Click listener for "Hủy" button
        tv_Huy.setOnClickListener(v -> {
            finish(); // Finish activity
        });
    }

    // Map UI elements
    private void mapUI() {
        numberPickerHour = findViewById(R.id.numberPickerHour);
        numberPickerMinute = findViewById(R.id.numberPickerMinute);
        switchLaplai = findViewById(R.id.switch1);
        tv_Laplai = findViewById(R.id.tv_Laplai);
        btn_nhan = findViewById(R.id.btn_nhan);
        btn_Ambao = findViewById(R.id.btnAmBao);
        tv_Luu = findViewById(R.id.tv_Luu);
        tv_Huy = findViewById(R.id.tv_Huy);
    }

    // Initialize NumberPickers and Switch
    private void initNumberPickersAndSwitch() {
        // Initialize NumberPicker for hour
        numberPickerHour.setMinValue(0);
        numberPickerHour.setMaxValue(23);
        numberPickerHour.setValue(0); // Default value is 0
        selectedHour = "0"; // Initialize selected hour

        // Initialize NumberPicker for minute
        numberPickerMinute.setMinValue(0);
        numberPickerMinute.setMaxValue(59);
        numberPickerMinute.setValue(0); // Default value is 0
        selectedMinute = "0"; // Initialize selected minute

        // Initialize Switch for repeat option
        switchLaplai.setChecked(false); // Default is unchecked
    }

    // Update TextView for repeat display
    private void updateRepeatTextView(String repeat) {
        switch (repeat) {
            case "1111111":
                tv_Laplai.setText("Mỗi ngày");
                break;
            case "0000000":
                tv_Laplai.setText("Không");
                break;
            default:
                // Handle other cases
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
                tv_Laplai.setText(repeatText.toString().trim());
                break;
        }
    }
}
