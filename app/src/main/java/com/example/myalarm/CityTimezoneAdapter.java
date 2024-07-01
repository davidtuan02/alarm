package com.example.myalarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myalarm.Model.CityZone_Model;

import java.util.ArrayList;

public class CityTimezoneAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CityZone_Model> cityZoneList;

    public CityTimezoneAdapter(Context context, ArrayList<CityZone_Model> cityZoneList) {
        this.context = context;
        this.cityZoneList = cityZoneList;
    }

    @Override
    public int getCount() {
        return cityZoneList.size();
    }

    @Override
    public Object getItem(int position) {
        return cityZoneList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_city, parent, false);
        }

        CityZone_Model cityZone = (CityZone_Model) getItem(position);
        String cityName = cityZone.getTenThanhPho() + ", " + cityZone.getQuocGia();


        TextView cityNameTextView = convertView.findViewById(R.id.tvCityName);
        cityNameTextView.setText(cityName);


        return convertView;
    }
}
