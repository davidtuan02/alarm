package com.example.myalarm;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SQL extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "alarm.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tblAlarmClock";
    private Context context;
    private MediaPlayer mediaPlayer;

    public SQL(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table if not exists
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "id TEXT PRIMARY KEY, " +
                "label TEXT, " +
                "hour TEXT, " +
                "minute TEXT, " +
                "days TEXT, " +
                "weekly TEXT, " +
                "tone BLOB, " +
                "isSnooze TEXT, " +
                "isEnable TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }


    //--------------------------------------------------------------------------
    public void addRecord(String id, String label, String hour, String minute, String days, String weekly, byte[] tone, String isSnooze, String isEnable) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("label", label);
        cv.put("hour", hour);
        cv.put("minute", minute);
        cv.put("days", days);
        cv.put("weekly", weekly);
        cv.put("tone", tone);
        cv.put("isSnooze", isSnooze);
        cv.put("isEnable", isEnable);

        String msg;
        if (db.insert(TABLE_NAME, null, cv) == -1) {
            msg = "Fail to insert record!";
        } else {
            msg = "Inserted record successfully!";
        }
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        db.close();
    }


    //-------------------------------------------------------------------------
    public int updateRecord(AlarmClockRecord alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("hour", alarm.hour);
        cv.put("minute", alarm.minute);
        cv.put("label", alarm.label);
        cv.put("days", alarm.days);
        cv.put("weekly", alarm.weekly);
        cv.put("tone", alarm.tone);
        cv.put("isSnooze", alarm.isSnooze);
        cv.put("isEnable", alarm.isEnable);

        int update = db.update(TABLE_NAME, cv, "id = ?", new String[]{String.valueOf(alarm.id)});
        db.close();
        return update; // Return number of rows affected
    }




    //----------------------------------------------------------------------
    public boolean deleteRecord(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int del = db.delete(TABLE_NAME, "id = ?", new String[]{id});
        db.close();

        // Kiểm tra xem có bản ghi nào được xóa không và trả về kết quả
        return del > 0;
    }


    //------------------------------------------------------------------------
    public List<AlarmClockRecord> getRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        List<AlarmClockRecord> records = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(0);
                String label = cursor.getString(1);
                String hour = cursor.getString(2);
                String minute = cursor.getString(3);
                String days = cursor.getString(4);
                String weekly = cursor.getString(5);
                byte[] tone = cursor.getBlob(6);
                String isSnooze = cursor.getString(7);
                String isEnable = cursor.getString(8);

                AlarmClockRecord record = new AlarmClockRecord(id, label, hour, minute, days, weekly, tone, isSnooze, isEnable);
                records.add(record);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        if (records.isEmpty()) {
            Toast.makeText(context, "No records found.", Toast.LENGTH_LONG).show();
        } else {
            StringBuilder data = new StringBuilder();
            for (AlarmClockRecord record : records) {
                data.append("id: ").append(record.id)
                        .append(" label: ").append(record.label)
                        .append(" time: ").append(record.hour).append("h").append(record.minute)
                        .append(" days: ").append(record.days)
                        .append(" weekly: ").append(record.weekly)
                        .append(" isSnooze: ").append(record.isSnooze)
                        .append(" isEnable: ").append(record.isEnable)
                        .append("\n");
            }
//            Toast.makeText(context, data.toString(), Toast.LENGTH_LONG).show();
        }

        return records;
    }

    public AlarmClockRecord getAlarmById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        AlarmClockRecord alarm = null;

        Cursor cursor = db.query(TABLE_NAME, null, "id = ?", new String[]{id}, null, null, null);

        try {
            if (cursor.moveToFirst()) {
                alarm = new AlarmClockRecord();
                alarm.id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                alarm.label = cursor.getString(cursor.getColumnIndexOrThrow("label"));
                alarm.hour = cursor.getString(cursor.getColumnIndexOrThrow("hour"));
                alarm.minute = cursor.getString(cursor.getColumnIndexOrThrow("minute"));
                alarm.days = cursor.getString(cursor.getColumnIndexOrThrow("days"));
                alarm.weekly = cursor.getString(cursor.getColumnIndexOrThrow("weekly"));
                alarm.tone = cursor.getBlob(cursor.getColumnIndexOrThrow("tone"));
                alarm.isSnooze = cursor.getString(cursor.getColumnIndexOrThrow("isSnooze"));
                alarm.isEnable = cursor.getString(cursor.getColumnIndexOrThrow("isEnable"));

                // Hiển thị thông tin của alarm bằng Toast
                StringBuilder sb = new StringBuilder();
                sb.append("id: ").append(alarm.id)
                        .append("\nlabel: ").append(alarm.label)
                        .append("\ntime: ").append(alarm.hour).append("h").append(alarm.minute)
                        .append("\ndays: ").append(alarm.days)
                        .append("\nweekly: ").append(alarm.weekly)
                        .append("\nisSnooze: ").append(alarm.isSnooze)
                        .append("\nisEnable: ").append(alarm.isEnable);

//                Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
            } else {
                Log.e("SQL", "No record found with ID: " + id);
                Toast.makeText(context, "No record found with ID: " + id, Toast.LENGTH_SHORT).show();
            }
        } catch (IllegalArgumentException e) {
            Log.e("SQL", "Error retrieving column.", e);
        } finally {
            cursor.close();
            db.close();
        }

        return alarm;
    }



    //--------------------------------------------------------------------
    //Chuyển đổi tệp âm thanh thành mảng byte và lưu va db
    public void saveAudio(String id, String label, String hour, String minute, String days, String weekly, String isSnooze, String isEnable, InputStream inputStream) {
        SQLiteDatabase db = null;
        ByteArrayOutputStream bos = null;

        try {
            db = this.getWritableDatabase();
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, length);
            }
            byte[] audioBytes = bos.toByteArray();

            ContentValues cv = new ContentValues();
            cv.put("id", id);
            cv.put("label", label);
            cv.put("hour", hour);
            cv.put("minute", minute);
            cv.put("days", days);
            cv.put("weekly", weekly);
            cv.put("tone", audioBytes); // Lưu byte[] vào cột 'tone'
            cv.put("isSnooze", isSnooze);
            cv.put("isEnable", isEnable);

            long result = db.insert("tblAlarmClock", null, cv);
            if (result == -1) {
                Log.d("SQL", "Failed to insert record.");
            } else {
                Log.d("SQL", "Record inserted successfully.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (db != null) {
                db.close();
            }
        }
    }




    //-------------------------------------------------------------------
    //Đọc dữ liệu từ file âm thanh vào mảng byte.
    private byte[] readFileToByteArray(String filePath) {
        File file = new File(filePath);
        byte[] data = new byte[(int) file.length()];
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(data);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


    //--------------------------------------------------------------------
    //Get data âm thanh từ db dưới dạng mảng byte.
    public byte[] getAudioData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"tone"}, "id=?", new String[]{id}, null, null, null);
        byte[] audioData = null;
        if (cursor.moveToFirst()) {
            audioData = cursor.getBlob(0);
        }
        cursor.close();
        db.close();
        return audioData;
    }

    //-------------------------------------------------------------------

    //Phát dữ liệu âm thanh từ mảng byte
    public void playAudio(String id) {
        byte[] audioData = getAudioData(id);
        if (audioData != null) {
            try {
                // Tạo tệp tạm thời từ byte array
                File tempFile = File.createTempFile("audio", "temp", context.getCacheDir());
                FileOutputStream fos = new FileOutputStream(tempFile);
                fos.write(audioData);
                fos.close();

                // Sử dụng MediaPlayer để phát âm thanh từ tệp tạm thời
                MediaPlayer mediaPlayer = new MediaPlayer();
                FileInputStream fis = new FileInputStream(tempFile);
                mediaPlayer.setDataSource(fis.getFD());
                mediaPlayer.prepare();
                mediaPlayer.setLooping(true); // Phát âm thanh lặp lại
                mediaPlayer.start();

                // Lưu trữ tham chiếu đến MediaPlayer
                this.mediaPlayer = mediaPlayer;

                // Đóng FileInputStream sau khi MediaPlayer đã chuẩn bị
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Failed to play audio.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "No audio data found for the given ID.", Toast.LENGTH_LONG).show();
        }
    }


    public void stopAudio() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
