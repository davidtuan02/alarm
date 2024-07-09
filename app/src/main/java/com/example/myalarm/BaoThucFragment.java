package com.example.myalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class BaoThucFragment extends Fragment {
    private static final String TAG = "BaoThucFragment";

    private TextView tvAddAlarm, tvEdit, tvBack;
    private SQL dbHelper;
    private AlarmAdapter alarmAdapter;
    private AlarmManager alarmManager;
    private boolean isEditMode = false;

    private AlarmAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new SQL(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baothuc, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<AlarmClockRecord> records = dbHelper.getRecords();
        alarmAdapter = new AlarmAdapter(getContext(), records);
        recyclerView.setAdapter(alarmAdapter);

        alarmAdapter.setOnItemClickListener(new AlarmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AlarmClockRecord alarm) {
                Intent intent = new Intent(getContext(), SuaBaoThuc_Activity.class);
                intent.putExtra("ALARM_ID", alarm.id);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvAddAlarm = view.findViewById(R.id.tv_AddAlarm);
        tvEdit = view.findViewById(R.id.tv_SuaBaoThuc);
        tvBack = view.findViewById(R.id.tv_back);

        tvAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), Add_Alarm_Activity.class);
                startActivity(intent);
            }
        });

//        tvEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvBack.setVisibility(View.VISIBLE);
//                tvEdit.setVisibility(View.GONE);
////                isEditMode = !isEditMode;
////                adapter.setEditMode(isEditMode);
//            }
//        });
//
//        tvBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvBack.setVisibility(View.GONE);
//                tvEdit.setVisibility(View.VISIBLE);
////                isEditMode = !isEditMode;
////                adapter.setEditMode(isEditMode);
//            }
//        });

        List<AlarmClockRecord> records = dbHelper.getRecords();
        for (AlarmClockRecord record : records) {
            if ("true".equals(record.isEnable)) {
                setAlarm(record);
            } else {
                cancelAlarm(record);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        List<AlarmClockRecord> records = dbHelper.getRecords();
        alarmAdapter.updateData(records);

        for (AlarmClockRecord record : records) {
            if (Boolean.parseBoolean(record.isEnable)) {
                setAlarm(record);
            } else {
                cancelAlarm(record);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    private void setAlarm(AlarmClockRecord alarm) {
        alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireContext(), AlarmReceiver.class);

        int hour = Integer.parseInt(alarm.hour);
        int minute = Integer.parseInt(alarm.minute);

        if (hour < 0 || hour >= 24 || minute < 0 || minute >= 60) {
            Log.e(TAG, "Invalid hour or minute: " + hour + ":" + minute);
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        int alarmId;
        try {
            alarmId = Integer.parseInt(alarm.id);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Invalid alarm ID: " + alarm.id, e);
            return;
        }

        intent.putExtra("hour", hour);
        intent.putExtra("minute", minute);
        intent.putExtra("alarmId", alarmId);
        intent.putExtra("isSnooze", alarm.isSnooze.equals("true"));
//        Toast.makeText(getContext(), "" + alarm.isSnooze, Toast.LENGTH_SHORT).show();

        // Sử dụng FLAG_UPDATE_CURRENT để cập nhật PendingIntent hiện có hoặc tạo mới nếu chưa tồn tại
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }



    private void cancelAlarm(AlarmClockRecord alarm) {
        AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireContext(), AlarmReceiver.class);

        int alarmId;
        try {
            alarmId = Integer.parseInt(alarm.id);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Invalid alarm ID: " + alarm.id, e);
            return;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireContext(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);

        Log.i(TAG, "Alarm with ID " + alarmId + " cancelled.");
    }


}


