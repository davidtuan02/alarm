package com.example.myalarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myalarm.Model.CityZone_Model;

import java.util.ArrayList;
import java.util.List;

public class SQL_Timezone extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "timezone.db";
    private static final int DATABASE_VERSION = 1;

    // Tên bảng và các cột
    private static final String TABLE_NAME = "city";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CITY_NAME = "tenThanhPho";
    private static final String COLUMN_COUNTRY = "quocGia";
    private static final String COLUMN_ZONE = "zone";

    // Câu lệnh tạo bảng
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CITY_NAME + " TEXT NOT NULL, " +
                    COLUMN_COUNTRY + " TEXT NOT NULL, " +
                    COLUMN_ZONE + " TEXT NOT NULL);";

    public SQL_Timezone(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Phương thức để chèn dữ liệu
    public void insertData(String tenThanhPho, String quocGia, String zone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CITY_NAME, tenThanhPho);
        values.put(COLUMN_COUNTRY, quocGia);
        values.put(COLUMN_ZONE, zone);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Phương thức để đọc dữ liệu
    public List<CityZone_Model> getAllData() {
        List<CityZone_Model> cityList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_ID,
                COLUMN_CITY_NAME,
                COLUMN_COUNTRY,
                COLUMN_ZONE
        };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String tenThanhPho = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITY_NAME));
                String quocGia = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY));
                String zone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ZONE));

                CityZone_Model city = new CityZone_Model(id, tenThanhPho, quocGia, zone);
                cityList.add(city);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cityList;
    }
}
