package com.example.myalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {
    private List<AlarmClockRecord> alarmList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private boolean isEditMode = false;


    public AlarmAdapter(Context context, List<AlarmClockRecord> alarmList) {
        this.context = context;
        this.alarmList = alarmList;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_baothuc, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        AlarmClockRecord alarm = alarmList.get(position);

        String formattedHour = String.format("%02d", Integer.parseInt(alarm.hour));
        String formattedMinute = String.format("%02d", Integer.parseInt(alarm.minute));

        // Hiển thị dữ liệu của bản ghi lên ViewHolder
        holder.hourTextView.setText(formattedHour);
        holder.minuteTextView.setText(formattedMinute);
        holder.labelTextView.setText(alarm.label);
        holder.alarmSwitch.setChecked(alarm.isEnable.equals("true"));

        // Thiết lập OnClickListener cho mỗi item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để mở SuaBaoThucActivity
                Intent intent = new Intent(context, SuaBaoThuc_Activity.class);
                // Đặt ID của bản ghi là một Extra trong Intent
                intent.putExtra("ALARM_ID", Integer.valueOf(alarm.id));
                // Khởi chạy Activity mới
                context.startActivity(intent);
            }
        });

        // Xử lý sự kiện khi người dùng thay đổi trạng thái của Switch
        holder.alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Cập nhật trạng thái vào cơ sở dữ liệu
                if(isChecked) {
                    alarm.isEnable = "true";
                }
                else {
                    alarm.isEnable = "false";
                }
                SQL dbHelper = new SQL(context);
                dbHelper.updateRecord(alarm);

                if (isChecked) {
                    // Bật chế độ báo thức cho item này
                    setAlarm(alarm);
                } else {
                    // Tắt chế độ báo thức cho item này
                    cancelAlarm(alarm);
                }
            }
        });

//        if (isEditMode) {
//            holder.alarmSwitch.setVisibility(View.GONE);
//            holder.btnDel.setVisibility(View.VISIBLE);
//        } else {
//            holder.alarmSwitch.setVisibility(View.VISIBLE);
//            holder.btnDel.setVisibility(View.GONE);
//        }
//
//        holder.btnDel.setOnClickListener(v -> {
//            // Handle delete action
////            cityTimeList.remove(position);
////            notifyItemRemoved(position);
////            notifyItemRangeChanged(position, cityTimeList.size());
//        });
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        notifyDataSetChanged();
    }
    private void setAlarm(AlarmClockRecord alarm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("ALARM_ID", Integer.parseInt(alarm.id)); // Gửi ID báo thức

        // Kiểm tra tính hợp lệ của giờ và phút
        int hour = Integer.parseInt(alarm.hour);
        int minute = Integer.parseInt(alarm.minute);

        // Đảm bảo rằng hour và minute không phải null và trong khoảng hợp lệ
        if (hour < 0 || hour >= 24 || minute < 0 || minute >= 60) {
            Log.e("AlarmAdapter", "Invalid hour or minute: " + hour + ":" + minute);
            return;
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(alarm.id), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Nếu thời gian được đặt đã qua, thiết lập báo thức cho ngày tiếp theo
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }



    private void cancelAlarm(AlarmClockRecord alarm) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(alarm.id), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    private long getTimeInMillis(int hour, int minute) {
        // Lấy thời gian hiện tại
        long currentTimeMillis = System.currentTimeMillis();
        long timeInMillis = (hour * 3600000 + minute * 60000) - currentTimeMillis;
        return System.currentTimeMillis() + timeInMillis;
    }
    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(AlarmClockRecord alarm);
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView hourTextView;
        TextView minuteTextView;
        TextView labelTextView;
        Switch alarmSwitch;

        Button btnDel;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            hourTextView = itemView.findViewById(R.id.hourTextView);
            minuteTextView = itemView.findViewById(R.id.minuteTextView);
            labelTextView = itemView.findViewById(R.id.labelTextVie); // Kiểm tra lại ID ở đây
            alarmSwitch = itemView.findViewById(R.id.switch1);
            btnDel = itemView.findViewById(R.id.btnDelete);

            // Kiểm tra xem các TextView có null không
            if (hourTextView == null || minuteTextView == null || labelTextView == null) {
                Log.e("ViewHolder", "One or more TextViews are null");
            }
        }
    }


    public void updateData(List<AlarmClockRecord> records) {
        this.alarmList = records;
        notifyDataSetChanged();
    }
}
