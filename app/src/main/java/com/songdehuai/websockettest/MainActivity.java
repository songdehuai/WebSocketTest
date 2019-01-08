package com.songdehuai.websockettest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.koushikdutta.async.ByteBufferList;
import com.koushikdutta.async.DataEmitter;
import com.koushikdutta.async.callback.DataCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.WebSocket;

public class MainActivity extends AppCompatActivity {

    private Button loginBtn, sendBtn, connectBtn;
    private EditText nameEt, passEt, messageEt, logEt, ipEt, portEt;

    private String uri = "";
    private String port = "";
    private WebSocket mWebSocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        loginBtn = findViewById(R.id.login_btn);
        sendBtn = findViewById(R.id.send_btn);
        connectBtn = findViewById(R.id.connect_btn);
        nameEt = findViewById(R.id.name_et);
        passEt = findViewById(R.id.pass_et);
        messageEt = findViewById(R.id.message_et);
        logEt = findViewById(R.id.log_et);
        ipEt = findViewById(R.id.ip_et);
        portEt = findViewById(R.id.port_et);

        sendBtn.setEnabled(false);
        loginBtn.setEnabled(false);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSocket();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageStr = messageEt.getText().toString().trim();
                if (messageStr.isEmpty()) {
                    mWebSocket.send(messageStr);
                }
            }
        });
    }

    private void initSocket() {
        uri = ipEt.getText().toString();
        port = portEt.getText().toString();
        AsyncHttpRequest httpRequest = new AsyncHttpGet(uri);
        httpRequest.addHeader("", "");
        AsyncHttpClient.getDefaultInstance().websocket(httpRequest, "my-protocol", new AsyncHttpClient.WebSocketConnectCallback() {
            @Override
            public void onCompleted(Exception ex, WebSocket webSocket) {
                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }
                connectBtn.setEnabled(false);
                loginBtn.setEnabled(false);
                sendBtn.setEnabled(true);
                mWebSocket = webSocket;
                webSocket.setStringCallback(new WebSocket.StringCallback() {
                    public void onStringAvailable(String s) {
                        System.out.println("I got a string: " + s);
                        log(s);
                    }
                });
                webSocket.setDataCallback(new DataCallback() {
                    public void onDataAvailable(DataEmitter emitter, ByteBufferList byteBufferList) {
                        System.out.println("I got some bytes!");
                        // note that this data has been read
                        byteBufferList.recycle();
                    }
                });
            }
        });
    }

    private void login() {

    }

    private void log(String text) {
        if (logEt != null) {
            logEt.append(text + "\n");
        }
    }
}
