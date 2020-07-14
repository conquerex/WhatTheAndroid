package com.example.part10_29a;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpRequester {

    HttpTask http;

    // 외부에서 HTTP 요청 필요시 호출
    public void request(String url, HashMap<String, String> param, HttpCallback callback) {
        http = new HttpTask(url, param, callback);
        http.execute();
    }

    // 외부에서 HTTP 요청 취소시 호출
    public void cancel() {
        if (http != null) http.cancel(true);
    }

    // AsyncTask 클래스
    private class HttpTask extends AsyncTask<Void, Void, String> {

        String url;
        HashMap<String, String> param;
        HttpCallback callback;

        public HttpTask(String url, HashMap<String, String> param, HttpCallback callback) {
            this.url = url;
            this.param = param;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String response = "";
            String postData = "";
            PrintWriter pw = null;
            BufferedReader in = null;

            try {
                URL text = new URL(url);
                HttpURLConnection http = (HttpURLConnection) text.openConnection();
//                http.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
//                http.setRequestProperty("Content-type", "application/json");
                http.setConnectTimeout(10000);
                http.setReadTimeout(10000);
                http.setRequestMethod("GET");
                http.setDoInput(true);
                http.setDoOutput(true);

                // 서버에 전송하기 위한 데이터를 웹의 Query 문자열 형식으로 변형
                if (param != null && param.size() > 0) {
                    Iterator<Map.Entry<String, String>> entries = param.entrySet().iterator();
                    int index = 0;
                    while (entries.hasNext()) {
                        if (index != 0) {
                            postData = postData + "&";
                        }
                        Map.Entry<String, String> mapEntry = (Map.Entry<String, String>) entries.next();
                        postData = postData + mapEntry.getKey() + "=" + URLEncoder.encode(mapEntry.getValue(), "UTF-8");
                        index++;
                    }
                    // 데이터 서버 전송
                    pw = new PrintWriter(new OutputStreamWriter(http.getOutputStream(), "UTF-8"));
                    pw.write(postData);
                    Log.d("HttpRequester", "* * * postData : " + postData);
                    pw.flush();
                }
                // 서버로부터 데이터 수신
                in = new BufferedReader(new InputStreamReader(http.getInputStream(), "UTF-8"));
                StringBuffer sb = new StringBuffer();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    Log.d("HttpRequester", "* * * inputLine : " + inputLine);
                    sb.append(inputLine);
                }
                response = sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (pw != null) pw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (in != null) in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            this.callback.onResult(s);
        }
    }
}
