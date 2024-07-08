package com.example.myalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myalarm.R;

public class NhanBaoThuc_Activity extends AppCompatActivity {

    private static final String TAG = "NhanBaoThuc_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhan_bao_thuc);
        ImageView ivBack = findViewById(R.id.iv_back);
        TextView tv_Huy = findViewById(R.id.tv_Huy);
        EditText edt_Nhan = findViewById(R.id.edt_Nhan);

        // Nhận dữ liệu nhãn từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            String currentLabel = intent.getStringExtra("current_label");
            if (currentLabel != null && !currentLabel.isEmpty()) {
                // Đặt giá trị của EditText là giá trị nhãn hiện tại
                edt_Nhan.setText(currentLabel);
            }
        }

        edt_Nhan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (edt_Nhan.getCompoundDrawables()[DRAWABLE_RIGHT] != null &&
                            event.getRawX() >= (edt_Nhan.getRight() - edt_Nhan.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        edt_Nhan.setText("");
                        Intent intent = new Intent();
                        intent.putExtra("label", "");
                        setResult(RESULT_OK, intent);
                        return true;
                    }
                }
                return false;
            }
        });

        tv_Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFinish();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFinish();
            }
        });
    }

    private void handleFinish() {
        // Lấy nhãn từ EditText
        EditText edt_Nhan = findViewById(R.id.edt_Nhan);
        String nhan = edt_Nhan.getText().toString().trim();

        // Tạo Intent để trả về dữ liệu cho màn hình thêm báo thức
        Intent intent = new Intent();
        intent.putExtra("label", nhan);

        // Đặt kết quả là RESULT_OK và truyền Intent
        setResult(RESULT_OK, intent);
        finish();
    }

//    @Override
//    public void onBackPressed() {
//        handleFinish();
//    }
}