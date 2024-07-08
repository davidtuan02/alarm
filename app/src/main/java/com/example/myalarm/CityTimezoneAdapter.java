package com.example.myalarm;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myalarm.Model.CityZone_Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class CityTimezoneAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Object> items; // Danh sách các mục, bao gồm cả tiêu đề và thành phố
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_CITY = 1;
    private OnItemClickListener onItemClickListener;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
//            case TYPE_CITY:
//                final CityZone_Model cityZone = (CityZone_Model) getItem(position);
//                String cityName = cityZone.getTenThanhPho() + ", " + cityZone.getQuocGia();
//                holder.cityNameTextView.setText(cityName);

            // Xử lý sự kiện khi click vào item
            case TYPE_CITY:
                final CityZone_Model cityZone = (CityZone_Model) getItem(position);
                String cityName = cityZone.getTenThanhPho() + ", " + cityZone.getQuocGia();
                holder.cityNameTextView.setText(cityName);

                // Xử lý sự kiện khi click vào item thành phố
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Gọi API để lấy thông tin thời gian dựa trên timezone của cityZone
                        String timezone = cityZone.getZone();
                        String apiUrl = "https://timeapi.io/api/Time/current/zone?timeZone=" + timezone;

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    URL url = new URL(apiUrl);
                                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                                    urlConnection.setRequestMethod("GET");
                                    urlConnection.connect();

                                    // Đọc dữ liệu trả về từ API
                                    InputStream inputStream = urlConnection.getInputStream();
                                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                                    StringBuilder response = new StringBuilder();
                                    String line;
                                    while ((line = bufferedReader.readLine()) != null) {
                                        response.append(line);
                                    }

                                    // Đóng kết nối
                                    bufferedReader.close();
                                    inputStream.close();
                                    urlConnection.disconnect();

                                    // Xử lý dữ liệu trả về
                                    final String responseData = response.toString();

                                    // Hiển thị Toast với thông tin thời gian lấy được từ API
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
//                                            Toast.makeText(context, "Current time: " + responseData, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    // Gửi dữ liệu về GioquocteFragment
                                    if (context instanceof Activity) {
                                        Intent resultIntent = new Intent();
                                        resultIntent.putExtra("currentTime", responseData); // Dữ liệu lấy được từ API
                                        ((Activity) context).setResult(Activity.RESULT_OK, resultIntent);
                                        ((Activity) context).finish(); // Kết thúc hoạt động và quay lại GioquocteFragment
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                    // Xử lý lỗi nếu có
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context, "Failed to get time information", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                });
                break;

        }

        return convertView;
    }

    private static class ViewHolder {
        TextView titleTextView;
        TextView cityNameTextView;
    }

    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(int position, CityZone_Model cityZone);
    }

    // Setter để thiết lập sự kiện click từ bên ngoài
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
