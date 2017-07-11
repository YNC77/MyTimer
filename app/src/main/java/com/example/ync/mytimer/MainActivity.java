package com.example.ync.mytimer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton playBtn;
    ImageButton pauseBtn;
    ClockDraw clockDraw;
    AllCountDownTimer workClockTimer;
    int notifyId = 5010; // 通知的識別號碼


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LayoutInflater inflater = LayoutInflater.from(getBaseContext());

        playBtn = (ImageButton) findViewById(R.id.playBtn);
        pauseBtn = (ImageButton) findViewById(R.id.pauseBtn);
        clockDraw = (ClockDraw) findViewById(R.id.clockDraw);
        workClockTimer = new AllCountDownTimer(this);
        workClockTimer.setView(clockDraw);
        workClockTimer.setMillisUntilFinished(20000, "work"); //default 20secs

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workClockTimer.callCountDownTimer(R.id.playBtn);
                playBtn.setVisibility(View.GONE);
                pauseBtn.setVisibility(View.VISIBLE);
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workClockTimer.callCountDownTimer(R.id.pauseBtn);
                playBtn.setVisibility(View.VISIBLE);
                pauseBtn.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    void allDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            //AlertDialog-Button, set Listener
            if (workClockTimer.getType() == "work") {
                builder.setMessage(R.string.break_remind);
                builder.setPositiveButton(R.string.dialog_positive_break, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        workClockTimer.setMillisUntilFinished(30000, "break"); //test 30 sec
                        workClockTimer.callCountDownTimer(R.id.playBtn);
                        playBtn.setVisibility(View.GONE);
                        pauseBtn.setVisibility(View.VISIBLE);
                        clearNotification();
                    }
                }).setNegativeButton(R.string.dialog_negative, null);
            } else {
                builder.setMessage(R.string.work_remind);
                builder.setPositiveButton(R.string.dialog_positive_work, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        workClockTimer.setMillisUntilFinished(10000, "work"); //test 10 sec
                        workClockTimer.callCountDownTimer(R.id.playBtn);
                        playBtn.setVisibility(View.GONE);
                        pauseBtn.setVisibility(View.VISIBLE);
                        clearNotification();
                    }
                }).setNegativeButton(R.string.dialog_negative, null);
            }

            builder.show();
    }


    void setNotifyTimeUp(){
        // 取得系統的通知服務
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 建立通知
        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this);
        mNotifyBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mNotifyBuilder.setContentTitle("50-10-Timer");
        mNotifyBuilder.setContentText("Time's up!");
        mNotifyBuilder.setAutoCancel(true);// 點擊通知後是否要自動移除掉通知
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); // 通知音效的URI，在這裡使用系統內建的通知音效
        mNotifyBuilder.setSound(soundUri);
        mNotifyBuilder.setPriority(Notification.PRIORITY_HIGH);

        final Intent intent = getIntent(); // 目前Activity的Intent
        final int flags = PendingIntent.FLAG_CANCEL_CURRENT;
        // ONE_SHOT：PendingIntent只使用一次；
        // CANCEL_CURRENT：PendingIntent執行前會先結束掉之前的；
        // NO_CREATE：沿用先前的PendingIntent，不建立新的PendingIntent；
        // UPDATE_CURRENT：更新先前PendingIntent所帶的額外資料，並繼續沿用
        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 5010, intent, flags); // 取得PendingIntent

        mNotifyBuilder.setContentIntent(pendingIntent);

        //發送通知
        mNotificationManager.notify(5010, mNotifyBuilder.build());

    }

    private void clearNotification(){
        // 启动后删除之前我们定义的通知
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(5010);

    }


}
