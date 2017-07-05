package com.example.ync.mytimer;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        playBtn = (ImageButton) findViewById(R.id.playBtn);
        pauseBtn = (ImageButton) findViewById(R.id.pauseBtn);
        clockDraw = (ClockDraw) findViewById(R.id.clockDraw);
        workClockTimer = new AllCountDownTimer();
        workClockTimer.setView(clockDraw);
        workClockTimer.setMillisUntilFinished(2000, "work"); //default 2secs

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

    class RemindDialog{
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        protected void allDialog(){
            //AlertDialog-Button, set Listener
            if (workClockTimer.getType() == "work") {
                builder.setMessage(R.string.break_remind);
                builder.setPositiveButton(R.string.dialog_positive_break, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        workClockTimer.setMillisUntilFinished(30000, "break"); //test 30 sec
                        workClockTimer.callCountDownTimer(R.id.playBtn);
                    }
                }).setNegativeButton(R.string.dialog_negative, null);
            } else {
                builder.setMessage(R.string.work_remind);
                builder.setPositiveButton(R.string.dialog_positive_work, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        workClockTimer.setMillisUntilFinished(10000, "break"); //test 130 sec
                        workClockTimer.callCountDownTimer(R.id.playBtn);
                    }
                }).setNegativeButton(R.string.dialog_negative, null);
            }

            builder.show();
        }
    }

    void setNotifyTimeUp(){
        // 取得系統的通知服務
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 建立通知
        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this);
        mNotifyBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mNotifyBuilder.setContentTitle("50-10-Timer");
        mNotifyBuilder.setContentText("Time's up!");
        mNotifyBuilder.build();

        //發送通知
        mNotificationManager.notify(5010, mNotifyBuilder.build());

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        // 取得PendingIntent(requestCode, flags)
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        // ONE_SHOT：PendingIntent只使用一次；
        // CANCEL_CURRENT：PendingIntent執行前會先結束掉之前的；
        // NO_CREATE：沿用先前的PendingIntent，不建立新的PendingIntent；
        // UPDATE_CURRENT：更新先前PendingIntent所帶的額外資料，並繼續沿用
        mNotifyBuilder.setContentIntent(resultPendingIntent);
    }


    class AllCountDownTimer {

        long millisUntilFinished;
        ClockDraw workClock;
        //AllDialog dialogs;
        String type;

        /*FragmentActivity activity;
        AllCountDownTimer(FragmentActivity activity){
            this.activity = activity;
        }*/

        void setView(ClockDraw v) {
            this.workClock = v;
        }

        public void setMillisUntilFinished(long millisUntilFinished, String type) {
            this.millisUntilFinished = millisUntilFinished;
            this.type = type;
            this.workClock.setCountMinuteShow(String.valueOf(millisUntilFinished/1000));
        }

        public String getType() {
            return type;
        }

        class setCountDownTimer extends CountDownTimer {

            setCountDownTimer(long millisInFuture, long countDownInterval) {
                super(millisInFuture, countDownInterval);
            }

            @Override
            public void onTick(long millisUntilFinished) {
                AllCountDownTimer.this.millisUntilFinished = millisUntilFinished;
                if(millisUntilFinished < 10000){
                    workClock.setCountMinuteShow(""+millisUntilFinished/1000);
                }else if(millisUntilFinished >= 10000){
                    workClock.setCountMinuteShow(String.valueOf(millisUntilFinished/1000));
                }
            }

            @Override
            public void onFinish() {
                //setIsFinished(true);
                workClock.setCountMinuteShow("00");
                RemindDialog rd = new RemindDialog();
                rd.allDialog();
                setNotifyTimeUp();

            }
        }

        setCountDownTimer countDownTimer;

        public void callCountDownTimer(int resId){

            switch (resId){
                case R.id.playBtn:
                    countDownTimer = new setCountDownTimer(millisUntilFinished, 100);
                    countDownTimer.start();
                    break;
                case R.id.pauseBtn:
                    countDownTimer.cancel();
                    break;
            }
        }
    }


}
