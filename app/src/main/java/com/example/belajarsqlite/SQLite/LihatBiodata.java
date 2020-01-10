package com.example.belajarsqlite.SQLite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.belajarsqlite.R;

public class LihatBiodata extends AppCompatActivity {

    protected Cursor cursor;
    DataHelper dataHelper;
    Button button;
    TextView textView1, textView2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lihat_biodata);

        dataHelper = new DataHelper(this);
        textView1 = (TextView) findViewById(R.id.txtNPM);
        textView2 = (TextView) findViewById(R.id.txtNama);
        button = (Button) findViewById(R.id.btnBack);

        SQLiteDatabase database = dataHelper.getReadableDatabase();
        cursor = database.rawQuery("SELECT * FROM biodata WHERE nama = '" + getIntent().getStringExtra("nama") + "'", null);
        cursor.moveToFirst();
        if(cursor.getCount()>0){
            cursor.moveToPosition(0);
            textView1.setText(cursor.getString(0).toString());
            textView2.setText(cursor.getString(1).toString());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }
}
