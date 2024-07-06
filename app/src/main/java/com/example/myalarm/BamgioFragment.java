package com.example.myalarm;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class BamgioFragment extends Fragment {

    private Button btnStart, btnMainAction;
    private ListView lapListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> laps;
    private TextView tvTimer;
    private int labCount = 0;

    private Handler handler = new Handler();
    private long startTime, elapsedTime = 0L;
    private boolean isRunning = false;

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            int millis = (int) (elapsedTime % 1000 / 10); // Lấy hàng trăm của giây
            int seconds = (int) (elapsedTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            tvTimer.setText(String.format("%02d:%02d:%02d", minutes, seconds, millis));

            handler.postDelayed(this, 10); // Cập nhật mỗi 10 mili giây
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bamgio, container, false);

        tvTimer = view.findViewById(R.id.tvTimer);
        btnStart = view.findViewById(R.id.btnStart);
        btnMainAction = view.findViewById(R.id.btnMainAction);
        lapListView = view.findViewById(R.id.lapListView);

        laps = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, laps);
        lapListView.setAdapter(adapter);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    startTimer();
                } else {
                    stopTimer();
                }
            }
        });

        btnMainAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnMainAction.getText().toString().equals("Lab")) {
                    recordLapTime();
                } else {
                    resetTimer();
                }
            }
        });

        return view;
    }

    private void startTimer() {
        startTime = System.currentTimeMillis() - elapsedTime;
        handler.postDelayed(timerRunnable, 0);
        isRunning = true;
        btnStart.setText("Stop");
        btnStart.setBackgroundResource(R.drawable.button_stop_background); // Thay đổi màu nền
        btnStart.setTextColor(getResources().getColor(R.color.red)); // Thay đổi màu chữ thành đỏ
        btnMainAction.setText("Lab");
        btnMainAction.setEnabled(true);
        btnMainAction.setAlpha(1.0f); // Đặt độ mờ thành 100%
    }

    private void stopTimer() {
        handler.removeCallbacks(timerRunnable);
        isRunning = false;
        btnStart.setText("Start");
        btnStart.setBackgroundResource(R.drawable.button_start_background); // Thay đổi màu nền
        btnStart.setTextColor(getResources().getColor(R.color.green)); // Thay đổi màu chữ thành xanh
        btnMainAction.setText("Reset");
    }

    private void resetTimer() {
        elapsedTime = 0L;
        tvTimer.setText("00:00:00");
        laps.clear();
        labCount = 0; // Reset lab count
        adapter.notifyDataSetChanged();
        btnStart.setText("Start");
        btnStart.setBackgroundResource(R.drawable.button_start_background); // Đặt lại màu nền
        btnStart.setTextColor(getResources().getColor(R.color.green)); // Đặt lại màu chữ thành xanh
        btnMainAction.setText("Lab");
        btnMainAction.setEnabled(false);
        btnMainAction.setAlpha(0.5f); // Đặt độ mờ thành 50%
    }

    private void recordLapTime() {
        labCount++;
        String currentLapTime = tvTimer.getText().toString();
        laps.add("Lab " + labCount + ": " + currentLapTime);
        adapter.notifyDataSetChanged();
    }
}
