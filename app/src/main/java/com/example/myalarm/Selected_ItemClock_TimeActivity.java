package com.example.myalarm;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myalarm.Model.CityZone_Model;

import java.util.ArrayList;

public class Selected_ItemClock_TimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item_clock_time);

        ListView listViewCities = findViewById(R.id.listViewCities);

        // Create a list of CityZone_Model items
        ArrayList<CityZone_Model> cityZoneList = new ArrayList<>();
        cityZoneList.add(new CityZone_Model(1, "VietNam", "Ho Chi Minh", "Asia/Ho_Chi_Minh"));
        cityZoneList.add(new CityZone_Model(2, "Howland Island", "Baker Island", "Pacific/Baker"));
        cityZoneList.add(new CityZone_Model(3, "Samoa thuộc Mỹ", "Pago Pago", "Pacific/Pago_Pago"));
        cityZoneList.add(new CityZone_Model(4, "Hoa Kỳ - Hawaii", "Honolulu", "Pacific/Honolulu"));
        cityZoneList.add(new CityZone_Model(5, "Hoa Kỳ - Alaska", "Anchorage", "America/Anchorage"));
        cityZoneList.add(new CityZone_Model(6, "Hoa Kỳ", "Los Angeles", "America/Los_Angeles"));
        cityZoneList.add(new CityZone_Model(7, "Canada", "Vancouver", "America/Vancouver"));
        cityZoneList.add(new CityZone_Model(8, "Hoa Kỳ", "Denver", "America/Denver"));
        cityZoneList.add(new CityZone_Model(9, "Hoa Kỳ - Arizona", "Phoenix", "America/Phoenix"));
        cityZoneList.add(new CityZone_Model(10, "Mexico", "Mexico City", "America/Mexico_City"));
        cityZoneList.add(new CityZone_Model(11, "Hoa Kỳ", "Chicago", "America/Chicago"));
        cityZoneList.add(new CityZone_Model(12, "Canada", "Winnipeg", "America/Winnipeg"));
        cityZoneList.add(new CityZone_Model(13, "Hoa Kỳ", "New York", "America/New_York"));
        cityZoneList.add(new CityZone_Model(14, "Canada", "Toronto", "America/Toronto"));
        cityZoneList.add(new CityZone_Model(15, "Peru", "Lima", "America/Lima"));
        cityZoneList.add(new CityZone_Model(16, "Colombia", "Bogota", "America/Bogota"));
        cityZoneList.add(new CityZone_Model(17, "Venezuela", "Caracas", "America/Caracas"));
        cityZoneList.add(new CityZone_Model(18, "Chile", "Santiago", "America/Santiago"));
        cityZoneList.add(new CityZone_Model(19, "Bolivia", "La Paz", "America/La_Paz"));
        cityZoneList.add(new CityZone_Model(20, "Argentina", "Buenos Aires", "America/Argentina/Buenos_Aires"));
        cityZoneList.add(new CityZone_Model(21, "Uruguay", "Montevideo", "America/Montevideo"));
        cityZoneList.add(new CityZone_Model(22, "Brazil", "São Paulo", "America/Sao_Paulo"));
        cityZoneList.add(new CityZone_Model(23, "Bồ Đào Nha", "Azores", "Atlantic/Azores"));
        cityZoneList.add(new CityZone_Model(24, "Cabo Verde", "Cape Verde", "Atlantic/Cape_Verde"));
        cityZoneList.add(new CityZone_Model(25, "Anh", "London", "Europe/London"));
        cityZoneList.add(new CityZone_Model(26, "Bồ Đào Nha", "Lisbon", "Europe/Lisbon"));
        cityZoneList.add(new CityZone_Model(27, "Ireland", "Dublin", "Europe/Dublin"));
        cityZoneList.add(new CityZone_Model(28, "Iceland", "Reykjavik", "Atlantic/Reykjavik"));
        cityZoneList.add(new CityZone_Model(29, "Pháp", "Paris", "Europe/Paris"));
        cityZoneList.add(new CityZone_Model(30, "Đức", "Berlin", "Europe/Berlin"));
        cityZoneList.add(new CityZone_Model(31, "Tây Ban Nha", "Madrid", "Europe/Madrid"));
        cityZoneList.add(new CityZone_Model(32, "Ý", "Rome", "Europe/Rome"));
        cityZoneList.add(new CityZone_Model(33, "Hy Lạp", "Athens", "Europe/Athens"));
        cityZoneList.add(new CityZone_Model(34, "Ai Cập", "Cairo", "Africa/Cairo"));
        cityZoneList.add(new CityZone_Model(35, "Israel", "Jerusalem", "Asia/Jerusalem"));
        cityZoneList.add(new CityZone_Model(36, "Romania", "Bucharest", "Europe/Bucharest"));
        cityZoneList.add(new CityZone_Model(37, "Nga", "Moscow", "Europe/Moscow"));
        cityZoneList.add(new CityZone_Model(38, "Ả Rập Saudi", "Riyadh", "Asia/Riyadh"));
        cityZoneList.add(new CityZone_Model(39, "Kenya", "Nairobi", "Africa/Nairobi"));
        cityZoneList.add(new CityZone_Model(40, "UAE", "Dubai", "Asia/Dubai"));
        cityZoneList.add(new CityZone_Model(41, "Azerbaijan", "Baku", "Asia/Baku"));
        cityZoneList.add(new CityZone_Model(42, "Pakistan", "Islamabad", "Asia/Karachi"));
        cityZoneList.add(new CityZone_Model(43, "Uzbekistan", "Tashkent", "Asia/Tashkent"));
        cityZoneList.add(new CityZone_Model(44, "Bangladesh", "Dhaka", "Asia/Dhaka"));
        cityZoneList.add(new CityZone_Model(45, "Kazakhstan", "Almaty", "Asia/Almaty"));
        cityZoneList.add(new CityZone_Model(46, "Thái Lan", "Bangkok", "Asia/Bangkok"));
        cityZoneList.add(new CityZone_Model(47, "Indonesia", "Jakarta", "Asia/Jakarta"));
        cityZoneList.add(new CityZone_Model(48, "Trung Quốc", "Beijing", "Asia/Shanghai"));
        cityZoneList.add(new CityZone_Model(49, "Singapore", "Singapore", "Asia/Singapore"));
        cityZoneList.add(new CityZone_Model(50, "Malaysia", "Kuala Lumpur", "Asia/Kuala_Lumpur"));
        cityZoneList.add(new CityZone_Model(51, "Nhật Bản", "Tokyo", "Asia/Tokyo"));
        cityZoneList.add(new CityZone_Model(52, "Hàn Quốc", "Seoul", "Asia/Seoul"));
        cityZoneList.add(new CityZone_Model(53, "Úc", "Sydney", "Australia/Sydney"));
        cityZoneList.add(new CityZone_Model(54, "Papua New Guinea", "Port Moresby", "Pacific/Port_Moresby"));
        cityZoneList.add(new CityZone_Model(55, "New Caledonia", "Noumea", "Pacific/Noumea"));
        cityZoneList.add(new CityZone_Model(56, "Solomon Islands", "Honiara", "Pacific/Honiara"));
        cityZoneList.add(new CityZone_Model(57, "Fiji", "Suva", "Pacific/Fiji"));
        cityZoneList.add(new CityZone_Model(58, "Marshall Islands", "Majuro", "Pacific/Majuro"));
        cityZoneList.add(new CityZone_Model(59, "Tonga", "Nuku'alofa", "Pacific/Tongatapu"));
        cityZoneList.add(new CityZone_Model(60, "Samoa", "Apia", "Pacific/Apia"));
        cityZoneList.add(new CityZone_Model(61, "Kiribati", "Kiritimati", "Pacific/Kiritimati"));

        // Create the adapter and set it to the ListView
        CityTimezoneAdapter adapter = new CityTimezoneAdapter(this, cityZoneList);
        listViewCities.setAdapter(adapter);
    }
}
