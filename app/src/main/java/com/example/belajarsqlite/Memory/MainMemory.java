package com.example.belajarsqlite.Memory;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.belajarsqlite.R;

import java.io.File;
import java.io.FileOutputStream;

public class MainMemory extends AppCompatActivity {

    LinearLayout linearLayout;
    AnimationDrawable animationDrawable;

    Button buttonIN,buttonEX;
    EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_memori);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        animationDrawable = (AnimationDrawable) linearLayout.getBackground();

        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);

        buttonIN = (Button) findViewById(R.id.btn1);
        buttonEX = (Button) findViewById(R.id.btn2);
        editText = (EditText) findViewById(R.id.edt1);

        buttonIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setdata = editText.getText().toString();
                FileOutputStream fileOutputStream;

                try {
                    fileOutputStream = openFileOutput("Data akoeh ini", Context.MODE_PRIVATE);
                    fileOutputStream.write(setdata.getBytes());

                    fileOutputStream.close();

                    Toast.makeText(getApplicationContext(), "Data simpan di internal", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        buttonEX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)){
                    //mengecek lokasi direktori penyimpanan
                    File dirExternal = Environment.getExternalStorageDirectory();

                    //membuat direktori baru
                    File createDir = new File(dirExternal.getAbsolutePath()+"/ContohDirektori");

                    //jika direktori "ContohDirektori tidak ada, maka akan dibuatkan
                    if (!createDir.exists()){
                        createDir.mkdir();
                        //membuat file baru
                        File file = new File(createDir, "ContohFileEx.txt");

                        String setData = editText.getText().toString();
                        //membuat dan menulis file/data di internal storage
                        FileOutputStream fileOutputStream;
                        try {
                            //membuat berkas baru dengan mode private
                            fileOutputStream = new FileOutputStream(file);
                            fileOutputStream.write(setData.getBytes());
                            fileOutputStream.close  ();

                            Toast.makeText(getApplicationContext(), "Data Disimpan di External", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Penyimpanan External Tidak Tersedia", Toast.LENGTH_SHORT).show();
                }
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
