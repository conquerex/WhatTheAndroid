package com.example.part9_25a;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Lab25_2Activity extends AppCompatActivity {

    TextView titleView;
    TextView dateView;
    TextView contentView;
    NetworkImageView imageView;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab25_2);

        titleView = findViewById(R.id.lab2_title);
        dateView = findViewById(R.id.lab2_date);
        contentView = findViewById(R.id.lab2_content);
        imageView = findViewById(R.id.lab2_image);

        queue = Volley.newRequestQueue(this);
        // 문자열 Request 정보를 담은 StringRequest 생성
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, "https://reqres.in/api/users?page=2", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // 서버 응답 후 사후 처리
                        // JSONObject에서 데이터 획득
                        try {
                            Log.d("Lab25_2Activity", "* * * " + response.toString());
                            titleView.setText(response.getString("per_page"));
                            String imageFile = response.getString("file");
                            if (imageFile != null && !imageFile.equals("")) {
                                ImageLoader imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
                                    @Override
                                    public Bitmap getBitmap(String url) {
                                        return null;
                                    }

                                    @Override
                                    public void putBitmap(String url, Bitmap bitmap) {

                                    }
                                });
                                imageView.setImageUrl("" + imageFile, imageLoader);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //
            }
        });

        queue.add(jsonRequest);
    }
}