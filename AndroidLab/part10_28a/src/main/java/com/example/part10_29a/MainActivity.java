package com.example.part10_29a;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView titleView;
    TextView dateView;
    TextView contentView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleView = findViewById(R.id.lab1_title);
        dateView = findViewById(R.id.lab1_date);
        contentView = findViewById(R.id.lab1_content);
        imageView = findViewById(R.id.lab1_image);


        HashMap<String, String> map = new HashMap<>();
//        map.put("page", "2");

        HttpRequester httpRequester = new HttpRequester();
        httpRequester.request("https://reqres.in/api/users?page=2", map, httpCallback);
    }

    HttpCallback httpCallback = new HttpCallback() {
        @Override
        public void onResult(String result) {
            try {
                JSONObject root = new JSONObject(result);
                Log.d("part10_28a", "* * * " + root.toString());
                titleView.setText(root.getString("id"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    HttpImageCallback imageCallback = new HttpImageCallback() {
        @Override
        public void onResult(Bitmap d) {
            imageView.setImageBitmap(d);
        }
    };
}
