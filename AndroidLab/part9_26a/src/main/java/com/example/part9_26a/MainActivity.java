package com.example.part9_26a;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<ChatMessage> list;
    MyAdapter ap;

    ListView listView;
    ImageView sendBtn;
    EditText msgEdit;

    boolean flagConnection = true;
    boolean isConnected = false;
    boolean flagRead = true;

    Handler writeHandler;

    Socket socket;
    BufferedInputStream bin;
    BufferedOutputStream bout;

    SocketThread st;
    ReadThread rt;
    WriteThread wt;

    String serverIp = "~~~";
    int serverPort = 7070;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.lab1_list);
        sendBtn = findViewById(R.id.lab1_send_btn);
        msgEdit = findViewById(R.id.lab1_send_text);

        sendBtn.setOnClickListener(this);

        list = new ArrayList<ChatMessage>();
        ap = new MyAdapter(this, R.layout.chat_item, list);
        listView.setAdapter(ap);

        sendBtn.setEnabled(false);
        msgEdit.setEnabled(false);


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        // FCM 서버로부터 얻은 키값 - 이 값을 서버 DB에 저장한다.
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.d("TAG", "* * * " + token);
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // firebase 테스트를 위한 아래 주석처리
//        st = new SocketThread();
//        st.start();
    }

    @Override
    protected void onStop() {
        super.onStop();

        flagConnection = false;
        isConnected = false;
/*
        if (socket != null) {
            try {
                flagRead = false;
                writeHandler.getLooper().quit();
                bout.close();
                bin.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void addMessage(String who, String msg) {
        ChatMessage vo = new ChatMessage();
        vo.who = who;
        vo.msg = msg;
        list.add(vo);
        ap.notifyDataSetChanged();
        listView.setSelection(list.size() - 1);
    }

    Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what == 10) {
                showToast("connection ok~~");
                sendBtn.setEnabled(true);
                msgEdit.setEnabled(true);
            } else if (msg.what == 20) {
                showToast("connection fail~~");
                sendBtn.setEnabled(false);
                msgEdit.setEnabled(false);
            } else if (msg.what == 100) {
                addMessage("you", (String) msg.obj);
            } else if (msg.what == 200) {
                addMessage("me", (String) msg.obj);
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (!msgEdit.getText().toString().trim().equals("")) {
            Message msg = new Message();
            msg.obj = msgEdit.getText().toString();
            writeHandler.sendMessage(msg);
        }
    }

    class SocketThread extends Thread {
        public void run() {
            while (flagConnection) {
                if (!isConnected) {
                    socket = new Socket();
                    SocketAddress remoteAddr = new InetSocketAddress(serverIp, serverPort);
                    try {
                        socket.connect(remoteAddr, 10000);
                        bout = new BufferedOutputStream(socket.getOutputStream());
                        bin = new BufferedInputStream(socket.getInputStream());
                        if (rt != null) {
                            flagRead = false;
                        }
                        if (wt != null) {
                            writeHandler.getLooper().quit();
                        }
                        wt = new WriteThread();
                        wt.start();
                        rt = new ReadThread();
                        rt.start();

                        isConnected = true;

                        Message msg = new Message();
                        msg.what = 10;
                        mainHandler.sendMessage(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                        SystemClock.sleep(10000);
                    }
                } else {
                    SystemClock.sleep(10000);
                }
            }
        }
    }

    class WriteThread extends Thread {
        @Override
        public void run() {
            Looper.prepare();
            writeHandler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    try {
                        bout.write(((String) msg.obj).getBytes());
                        bout.flush();

                        Message message = new Message();
                        message.what = 200;
                        message.obj = msg.obj;
                        mainHandler.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                        isConnected = false;
                        writeHandler.getLooper().quit();
                        try {
                            flagRead = false;
                        } catch (Exception e1) {
                        }
                    }
                }
            };
            Looper.loop();
        }
    }


    class ReadThread extends Thread {
        @Override
        public void run() {
            byte[] buffer = null;
            while (flagRead) {
                String message = null;
                try {
                    int size = bin.read(buffer);
                    if (size > 0) {
                        message = new String(buffer, 0, size, "utf-8");
                        if (message != null && !message.equals("")) {
                            Message msg = new Message();
                            msg.what = 100;
                            msg.obj = message;
                            mainHandler.sendMessage(msg);
                        }
                    } else {
                        flagRead = false;
                        isConnected = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    flagRead = false;
                    isConnected = false;
                }
            }
            Message msg = new Message();
            msg.what = 20;
            mainHandler.sendMessage(msg);
        }
    }
}

class ChatMessage {
    String who;
    String msg;
}

class MyAdapter extends ArrayAdapter<ChatMessage> {

    ArrayList<ChatMessage> list;
    int resId;
    Context context;


    public MyAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ChatMessage> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resId = resource;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(resId, null);

        TextView msgView = (TextView) convertView.findViewById(R.id.lab1_item_msg);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) msgView
                .getLayoutParams();
        ChatMessage msg = list.get(position);
        if (msg.who.equals("me")) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            msgView.setTextColor(Color.WHITE);
            msgView.setBackgroundResource(R.drawable.chat_right);
        } else if (msg.who.equals("you")) {
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            msgView.setBackgroundResource(R.drawable.chat_left);
        }
        msgView.setText(msg.msg);

        return convertView;
    }
}