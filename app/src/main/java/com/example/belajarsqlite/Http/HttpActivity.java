package com.example.belajarsqlite.Http;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.belajarsqlite.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpActivity extends AppCompatActivity {
    TextView textView;
    Request request;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_http);

        textView = (TextView) findViewById(R.id.txtTes);

        OkHttpClient client = new OkHttpClient();

        request = new Request.Builder()
                .url("http://t-hisyam.net/demo/whyri/vote/list_vote.php")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                textView.setText("Gagal : "+e.toString());
            }

            @Override
            public void onResponse(Call call,  final Response response) throws IOException {
                HttpActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String json = response.body().string();
                            textView.setText(json);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
