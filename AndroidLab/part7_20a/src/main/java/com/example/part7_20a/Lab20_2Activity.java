package com.example.part7_20a;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.part7_20a_aidl.IPlayService;

public class Lab20_2Activity extends AppCompatActivity implements View.OnClickListener {

    public static final int MEDIA_STATUS_STOP = 0;
    public static final int MEDIA_STATUS_RUNNING = 1;
    public static final int MEDIA_STATUS_COMPLETED = 2;

    ImageView playBtn;
    ImageView stopBtn;
    ProgressBar progressBar;
    TextView titleView;

    String filePath;
    boolean runThread;

    IPlayService pService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab20_2);

        playBtn = findViewById(R.id.lab2_play);
        stopBtn = findViewById(R.id.lab2_stop);
        progressBar = findViewById(R.id.lab2_progress);
        titleView = findViewById(R.id.lab2_title);

        playBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);

        titleView.setText("music.mp3");
        stopBtn.setEnabled(false);
        playBtn.setEnabled(false);

        filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/music.mp3";

        Intent intent = new Intent("com.example.part7_20a.ACTION_PLAY");
        intent.setPackage("com.example.part7_20a_aidl");
        intent.putExtra("filePath", filePath);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            pService = IPlayService.Stub.asInterface(iBinder);
            playBtn.setEnabled(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            pService = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        runThread = false;
    }

    @Override
    public void onClick(View view) {
        if (view == playBtn) {
            if (pService != null) {
                try {
                    pService.start();
                    progressBar.setMax(pService.getMaxDuration());
                    runThread = true;
                    ProgressThread thread = new ProgressThread();
                    thread.start();
                    playBtn.setEnabled(false);
                    stopBtn.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (view == stopBtn) {
            if (pService != null) {
                try {
                    pService.stop();
                    runThread = false;
                    progressBar.setProgress(0);
                    playBtn.setEnabled(true);
                    stopBtn.setEnabled(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ProgressThread extends Thread {
        @Override
        public void run() {
            while (runThread) {
                progressBar.incrementProgressBy(1000);
                SystemClock.sleep(1000);
                if (progressBar.getProgress() == progressBar.getMax()) {
                    runThread = false;
                }
            }
        }
    }
}