package com.example.myalarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myalarm.Model.CityZone_Model;

import java.util.List;

public class InternationalTimeCancelAdapter extends RecyclerView.Adapter<InternationalTimeCancelAdapter.ViewHolder> {
    private Context context;
    private List<CityZone_Model> cityTimeList;
    private boolean isEditMode = false;

    public InternationalTimeCancelAdapter(Context context, List<CityZone_Model> cityTimeList) {
        this.context = context;
        this.cityTimeList = cityTimeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gio_quocte_sua, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CityZone_Model cityTime = cityTimeList.get(position);
        String[] arr = cityTime.getZone().split("/");
        if (arr.length > 1) {
            holder.tvCity.setText(arr[1]);
        }
        holder.tvDate.setText("Today");  // Giả sử bạn muốn đặt ngày hoặc thông tin khác vào tvDate

        holder.itemView.setOnClickListener(v -> {
            String message = "City: " + cityTime.getTenThanhPho() +
                    ", Country: " + cityTime.getQuocGia() +
                    ", Timezone: " + cityTime.getZone() +
                    ", Current Time: " + cityTime.getTime();
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        });



        holder.btnDelete.setOnClickListener(v -> {
            // Handle delete action
            cityTimeList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cityTimeList.size());
        });

        holder.btnOnDelete.setOnClickListener(v -> {
            holder.btnDelete.setVisibility( View.VISIBLE );
        });
    }

    @Override
    public int getItemCount() {
        return cityTimeList.size();
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity, tvDate;
        LinearLayout btnDelete;
        ImageView btnOnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnOnDelete = itemView.findViewById(R.id.btnOnDelete);
        }
    }
}
