package com.example.part9_25a;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpRequester {
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
                http.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
                http.setConnectTimeout(10000);
                http.setReadTimeout(10000);
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);

                if (param != null && param.size() > 0) {
                    Iterator<Map.Entry<String, String>> entries = param.entrySet().iterator();
                    int index = 0;
                    while (entries.hasNext()) {
                        if (index != 0) {
                            postData = postData + "&";
                        }
                        Map.Entry<String, String> mapEntry = (Map.Entry<String, String>) entries.next();
                        postData = postData+mapEntry.getKey() + "=" + URLEncoder.encode(mapEntry.getValue(), "UTF-8");
                        index++;
                    }
                    pw = new PrintWriter(new OutputStreamWriter(http.getOutputStream(), "UTF-8"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
