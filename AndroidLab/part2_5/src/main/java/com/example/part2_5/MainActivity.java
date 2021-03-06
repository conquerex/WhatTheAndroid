package com.example.part2_5;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
/**
 * Created by kkang
 * 깡샘의 안드로이드 프로그래밍 - 루비페이퍼
 * 위의 교제에 담겨져 있는 코드로 설명 및 활용 방법은 교제를 확인해 주세요.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button vibrationBtn;
    Button systemBeepBtn;
    Button customBeepBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vibrationBtn=findViewById(R.id.btn_vibration);
        systemBeepBtn=findViewById(R.id.btn_system_beep);
        customBeepBtn=findViewById(R.id.btn_custom_sound);

        vibrationBtn.setOnClickListener(this);
        systemBeepBtn.setOnClickListener(this);
        customBeepBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==vibrationBtn){
            Vibrator vibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(1000);
        }else if(v==systemBeepBtn){
            Uri notification=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone=RingtoneManager.getRingtone(getApplicationContext(), notification);
            ringtone.play();
        }else if(v==customBeepBtn){
            MediaPlayer player=MediaPlayer.create(this, R.raw.fallbackring);
            player.start();
        }
    }
}
