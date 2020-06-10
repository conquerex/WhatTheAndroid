package com.example.part7_20a;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;

public class PlayService extends Service implements MediaPlayer.OnCompletionListener {

    MediaPlayer player;
    String filePath;

    // Activity에서 실행시키는 Receiver
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mode = intent.getStringExtra("mode");
            if (mode != null) {
                if (mode.equals("start")) {
                    try {
                        if (player != null && player.isPlaying()) {
                            player.stop();
                            player.release();
                            player = null;
                        }
                        player = new MediaPlayer();
                        player.setDataSource(filePath);
                        player.prepare();
                        player.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent aIntent = new Intent("com.example.PLAY_TO_ACTIVITY");
                    aIntent.putExtra("mode", "start");
                    aIntent.putExtra("duration", player.getDuration());
                    sendBroadcast(aIntent);
                } else if (mode.equals("stop")) {
                    if (player != null && player.isPlaying()) {
                        player.stop();
                        player.release();
                        player = null;
                    }
                }
            }
        }
    };

    public PlayService() {
        // 해당없음
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(receiver, new IntentFilter("com.example.PLAY_TO_SERVICE"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // startService()를 사용했기에 onStartCommand를 오버라이드해서 사용
        filePath = intent.getStringExtra("filePath");

        // 이미 Service가 이전에 구동되어 음악이 플레이되고 있는 상황
        if (player != null) {
            Intent aIntent = new Intent("com.example.PLAY_TO_ACTIVITY");
            aIntent.putExtra("mode", "restart");
            aIntent.putExtra("duration", player.getDuration());
            aIntent.putExtra("current", player.getCurrentPosition());
            sendBroadcast(aIntent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        // 음악 play가 끝나면 activity에 알려준다
        Intent intent = new Intent("com.example.PLAY_TO_ACTIVITY");
        intent.putExtra("mode", "stop");
        sendBroadcast(intent);

        // Service 자신을 종료시킨다.
        stopSelf();
    }
}
