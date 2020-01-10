package com.example.belajarsqlite.SQLite;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.belajarsqlite.MainActivity;
import com.example.belajarsqlite.R;

public class SQLiteMain extends AppCompatActivity {
    ListView listView;
    DataHelper dataHelper;
    EditText editText1, editText2;
    public static SQLiteMain ma;
    Button button;
    protected Cursor cursor;
    String[] daftar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.biodata);

        button = (Button) findViewById(R.id.btnSubmit);

        dataHelper = new DataHelper(this);
        editText1 = (EditText) findViewById(R.id.edtNPM);
        editText2 = (EditText) findViewById(R.id.edtNama);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = dataHelper.getWritableDatabase();
                database.execSQL("insert into biodata(npm, nama) values ('" +
                        editText1.getText().toString() + "','" +
                        editText2.getText().toString() + "')");
                Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                editText1.setText("");
                editText2.setText("");
                SQLiteMain.ma.RefreshList();
            }
        });

        ma = this;
        dataHelper = new DataHelper(this);
        RefreshList();
    }

    public  void RefreshList(){
        SQLiteDatabase database = dataHelper.getReadableDatabase();
        cursor = database.rawQuery("SELECT * FROM biodata", null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc = 0; cc < cursor.getCount(); cc++){
            cursor.moveToPosition(cc);
            daftar[cc] = cursor.getString(1).toString();
        }
        listView = (ListView) findViewById(R.id.list_item);
        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, daftar));
        listView.setSelected(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String selection = daftar[arg2];
                final CharSequence[] dialogitem = {"Lihat Biodata"};

                AlertDialog.Builder builder = new AlertDialog.Builder(SQLiteMain.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item){
                            case 0:
                                Intent i = new Intent(getApplicationContext(), LihatBiodata.class);
                                i.putExtra("nama", selection);
                                startActivity(i);
                                RefreshList();
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        ((ArrayAdapter) listView.getAdapter()).notifyDataSetInvalidated();
    }
}
