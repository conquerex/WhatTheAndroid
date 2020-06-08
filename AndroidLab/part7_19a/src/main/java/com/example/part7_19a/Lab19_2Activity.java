package com.example.part7_19a;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Lab19_2Activity extends AppCompatActivity implements View.OnClickListener {

    Button basicBtn;
    Button bigPictureBtn;
    Button bigTextBtn;
    Button inboxBtn;
    Button progressBtn;
    Button headsupBtn;
    Button messageBtn;

    NotificationManager manager;
    NotificationCompat.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab19_2);

        basicBtn = findViewById(R.id.lab2_basic);
        bigPictureBtn = findViewById(R.id.lab2_bigpicture);
        bigTextBtn = findViewById(R.id.lab2_bigtext);
        inboxBtn = findViewById(R.id.lab2_inbox);
        progressBtn = findViewById(R.id.lab2_progress);
        headsupBtn = findViewById(R.id.lab2_headsup);
        messageBtn = findViewById(R.id.lab2_message);

        basicBtn.setOnClickListener(this);
        bigPictureBtn.setOnClickListener(this);
        bigTextBtn.setOnClickListener(this);
        inboxBtn.setOnClickListener(this);
        progressBtn.setOnClickListener(this);
        headsupBtn.setOnClickListener(this);
        messageBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "one-channel";
            String channelName = "My Channel One";
            String channelDescription = "My Channel One Description";

            NotificationChannel channel = null;
            if (view == headsupBtn) {
                channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            } else {
                channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            }
            channel.setDescription(channelDescription);
            manager.createNotificationChannel(channel);

            builder = new NotificationCompat.Builder(this, channelId);
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        builder.setSmallIcon(android.R.drawable.ic_notification_overlay);
        builder.setContentTitle("Content Title");
        builder.setContentText("Content Message");
        builder.setAutoCancel(true);

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        PendingIntent pIntent1 = PendingIntent.getBroadcast(this, 0, new Intent(this, NotiReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_share, "ACTION1", pIntent1).build());

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.noti_large);
        builder.setLargeIcon(largeIcon);

        if (view == ) {

        }
    }
}
