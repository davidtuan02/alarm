package com.example.myalarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Timer;
import java.util.TimerTask;

public class CountdownActivity extends AppCompatActivity {
    private TextView textViewCountdown;
    private Button btnPause;
    private Button btnCancel;
    private Timer timer;
    private int hour;
    private int minute;
    private int second;
    private boolean isPaused = false;
    private TimerTask timerTask;
    private ImageButton addRing;
    private Uri ringtoneUri;
    private Ringtone ringtone;
    private NotificationManager notificationManager;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_countdown);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            textViewCountdown = findViewById(R.id.textViewCountdown);
            btnPause = findViewById(R.id.btnPause);
            btnCancel = findViewById(R.id.btnCancel);
            addRing = findViewById(R.id.add_ring);

            Intent intent = getIntent();
            hour = intent.getIntExtra("hour", 0);
            minute = intent.getIntExtra("minute", 0);
            second = intent.getIntExtra("second", 0);

            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            second--;
                            if (second < 0) {
                                minute--;
                                second = 59;
                            }
                            if (minute < 0) {
                                hour--;
                                minute = 59;
                            }
                            if (hour < 0) {
                                // timer finished
                                timer.cancel();
                                playAlarmSound(); // play alarm sound
                            } else {
                                textViewCountdown.setText(String.format("%02d:%02d:%02d", hour, minute, second));
                            }
                        }

                        private void playAlarmSound() {
                        }
                    });
                }
            };
            timer.scheduleAtFixedRate(timerTask, 1000, 1000); // 1 second interval


            btnPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isPaused) {
                        isPaused = true;
                        btnPause.setText("Tiếp tục");
                        timerTask.cancel(); // cancel the timer task
                    } else {
                        isPaused = false;
                        btnPause.setText("Tạm dừng");
                        timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        second--;
                                        if (second < 0) {
                                            minute--;
                                            second = 59;
                                        }
                                        if (minute < 0) {
                                            hour--;
                                            minute = 59;
                                        }
                                        if (hour < 0) {
                                            // timer finished
                                            timer.cancel();
                                            textViewCountdown.setText("Time's up!");
                                            playAlarmSound(); // play alarm sound
                                        } else {
                                            textViewCountdown.setText(String.format("%02d:%02d:%02d", hour, minute, second));
                                        }
                                    }

                                    private void playAlarmSound() {
                                    }
                                });
                            }
                        };
                        timer.scheduleAtFixedRate(timerTask, 0, 1000); // resume timer
                    }
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timer.cancel();
                    finish(); // go back to SettingActivity
                }
            });


            return insets;
        });
    }

    private void playAlarmSound() {
        // Play the default ringtone
        mediaPlayer = MediaPlayer.create(CountdownActivity.this, R.raw.sena);
        if (mediaPlayer!= null) {
            mediaPlayer.start();
        }

        // Show a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

    }
}