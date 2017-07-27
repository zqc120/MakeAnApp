package chao.makeanapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import chao.makeanapp.R;

/**
 * Created by Chao on 2017/7/25.
 */

public class SplashActivity extends Activity {
    private TextView skip;
    int count = 0;
    int time = 0;
    private static final int STARTCOUNT = 10;
    Timer timer;
    private Thread thread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        //countDownTimer.start();
        //useTimer(3000);
        //useHandler(3000);
        useWhile(3000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    private void useWhile(final int time) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (count < time) {
                    SystemClock.sleep(1000);
                    count += 1000;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count != 4000) {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        }
                    }
                });
            }
        });
        thread.start();
    }

    private void initView() {
        skip = (TextView) findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 4000;
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        });
    }

    void useTimer(final int time) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (count < time) {
                            count += 1000;
                        } else {
                            //停止
                            timer.cancel();
                            timer.purge();
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        }
                    }
                }, 0, 1000);
            }
        }).start();
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case STARTCOUNT:
                    mHandler.sendEmptyMessageDelayed(STARTCOUNT, 1000);
                    count += 1000;
                    if (count >= time) {
                        mHandler.removeMessages(STARTCOUNT);
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void useHandler(int time) {
        this.time = time;
        mHandler.sendEmptyMessage(STARTCOUNT);
    }

    CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
    };

}
