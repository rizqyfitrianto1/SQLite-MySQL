package com.example.belajarsqlite.Http;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.belajarsqlite.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpActivity2 extends AppCompatActivity {

    LinearLayout linearLayout;
    AnimationDrawable animationDrawable;

    Button button1, button2;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.http_main);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        animationDrawable = (AnimationDrawable) linearLayout.getBackground();

        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);

        context = getApplicationContext();
        button1 = (Button) findViewById(R.id.btn1);
        button2 = (Button) findViewById(R.id.btn2);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSync();
            }
        });
        
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAsync();
            }
        });
    }

    private void requestSync() {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.readTimeout(5, TimeUnit.SECONDS);
        builder.writeTimeout(5, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();
        Request request = new Request.Builder()
                .url("https://jsonplaceholder.typicode.com/todos/1")
                .build();
        try{
            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            Toast.makeText(this, responseString, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Toast.makeText(this, "OkHTTP requestSync", Toast.LENGTH_SHORT).show();
    }

    private void requestAsync() {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.readTimeout(5, TimeUnit.SECONDS);
        builder.writeTimeout(5, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();
        Request request = new Request.Builder()
//                .url("https://jsonplaceholder.typicode.com/todos/1")
                .url("https://t-hisyam.net/demo/whyri/vote/list_vote.php")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("debuging", "Request Failed."+e.getMessage());
                responseAsync("Request Failed."+e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    if (response.isSuccessful()){
                        String responseString = response.body().string();
                        Log.d("debuging", responseString);
                        responseAsync(responseString);
                    }else {
                        Log.d("debuging", "Error "+ response);
                        responseAsync("Error "+ response);
                    }
                }catch (Exception e){
                    Log.d("debuging", "Exception caught : ", e);
                    responseAsync("Error "+ e.getMessage());
                }
            }
        });
        Toast.makeText(this, "OkHTTP requestAsync", Toast.LENGTH_SHORT).show();
    }

    private void responseAsync(final String responseStr) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), responseStr, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()){
            animationDrawable.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()){
            animationDrawable.start();
        }
    }
}
