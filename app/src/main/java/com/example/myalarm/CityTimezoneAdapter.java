package com.example.myalarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myalarm.Model.CityZone_Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CityTimezoneAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Object> items; // Danh sách các mục, bao gồm cả tiêu đề và thành phố
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_CITY = 1;

    public CityTimezoneAdapter(Context context, ArrayList<CityZone_Model> cityZoneList) {
        this.context = context;
        items = new ArrayList<>();

        // Sắp xếp danh sách thành phố theo tên thành phố và quốc gia
        Collections.sort(cityZoneList, new Comparator<CityZone_Model>() {
            @Override
            public int compare(CityZone_Model city1, CityZone_Model city2) {
                return city1.getTenThanhPho().compareToIgnoreCase(city2.getTenThanhPho());
            }
        });

        // Thêm các tiêu đề và thành phố vào danh sách items
        String currentLetter = "";
        for (CityZone_Model city : cityZoneList) {
            String cityName = city.getTenThanhPho() + ", " + city.getQuocGia();
            String firstLetter = cityName.substring(0, 1).toUpperCase();

            // Thêm tiêu đề nếu chưa có
            if (!currentLetter.equals(firstLetter)) {
                currentLetter = firstLetter;
                items.add(currentLetter);
            }

            // Thêm thành phố vào danh sách
            items.add(city);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof String ? TYPE_TITLE : TYPE_CITY;
    }

    @Override
    public int getViewTypeCount() {
        return 2; // Số loại view: tiêu đề và thành phố
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        int viewType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (viewType) {
                case TYPE_TITLE:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_header_city, parent, false);
                    holder.titleTextView = convertView.findViewById(R.id.section_header);
                    break;
                case TYPE_CITY:
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_city, parent, false);
                    holder.cityNameTextView = convertView.findViewById(R.id.tvCityName);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Hiển thị dữ liệu tương ứng với loại view
        switch (viewType) {
            case TYPE_TITLE:
                String title = (String) getItem(position);
                holder.titleTextView.setText(title);
                break;
            case TYPE_CITY:
                CityZone_Model cityZone = (CityZone_Model) getItem(position);
                String cityName = cityZone.getTenThanhPho() + ", " + cityZone.getQuocGia();
                holder.cityNameTextView.setText(cityName);
                break;
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView cityNameTextView;
    }
}
