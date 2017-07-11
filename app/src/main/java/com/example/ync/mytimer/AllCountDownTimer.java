package com.example.ync.mytimer;

import android.os.CountDownTimer;
import android.view.View;

/**
 * Created by YNC on 2017/7/5.
 */
class AllCountDownTimer {

    private MainActivity mainActivity;
    long millisUntilFinished;
    ClockDraw workClock;
    //AllDialog dialogs;
    String type;

    public AllCountDownTimer(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

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
        this.workClock.setCountMinuteShow(String.valueOf(millisUntilFinished / 1000));
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
            if (millisUntilFinished < 10000) {
                workClock.setCountMinuteShow("0" + millisUntilFinished / 1000);
            } else if (millisUntilFinished >= 10000) {
                workClock.setCountMinuteShow(String.valueOf(millisUntilFinished / 1000));
            }
        }

        @Override
        public void onFinish() {
            //setIsFinished(true);
            workClock.setCountMinuteShow("00");
            mainActivity.pauseBtn.setVisibility(View.GONE);
            mainActivity.playBtn.setVisibility(View.VISIBLE);
            mainActivity.playBtn.setEnabled(false);
            mainActivity.allDialog();
            mainActivity.setNotifyTimeUp();



        }
    }

    setCountDownTimer countDownTimer;

    public void callCountDownTimer(int resId) {

        switch (resId) {
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
