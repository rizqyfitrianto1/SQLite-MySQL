package com.example.belajarsqlite.Regristrasi;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.belajarsqlite.AppController;
import com.example.belajarsqlite.R;
import com.example.belajarsqlite.ServerLokal.serverReg;
import com.example.belajarsqlite.model.DataPart;
import com.example.belajarsqlite.util.VolleyMultiPartRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.isfaaghyth.rak.Rak;

public class RegistrasiMahasiswa extends AppCompatActivity {

    Uri fileUri;
    int PICK_IMAGE_REQUEST = 1;

    Button daftar,ubahFoto;
    Intent intent;
    EditText edtNama, edtNIM;
    ImageView imageView;
    Bitmap bitmap, decoded;

    int bitmap_size = 60;
    public final int REQUEST_CAMERA = 0;
    public final int SELECT_FILE = 1;
    int max_resolution_image = 800;

    ProgressDialog progressDialog;
    private String url = serverReg.URL_reg;
    private static  final String TAG = RegistrasiMahasiswa.class.getSimpleName();
    String tag_json_obj = "json_obj_req";
    ConnectivityManager conMgr;

    LinearLayout linearLayout;
    AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regis);

        Rak.initialize(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        ubahFoto = (Button) findViewById(R.id.btnFoto);
        ubahFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tampilGaleri();
            }
        });

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        {
            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()){

            }else {
                Toast.makeText(getApplicationContext(), " Silahkan Cek Lagi Koneksi Anda", Toast.LENGTH_SHORT).show();
            }
        }

        imageView = (ImageView) findViewById(R.id.imgFoto);
        edtNama = (EditText) findViewById(R.id.edtNama);
        edtNIM = (EditText) findViewById(R.id.edtNim);
        daftar = (Button) findViewById(R.id.btnDaftar);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = edtNama.getText().toString();
                String nim = edtNIM.getText().toString();

                if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()){
                    Regis(nama, nim);
                }else {
                    Toast.makeText(getApplicationContext(), "cek internet anda", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Animation Background
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        animationDrawable = (AnimationDrawable) linearLayout.getBackground();

        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);

//        imageView = (ImageView)findViewById(R.id.imgFoto);
//        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
//        imageView.startAnimation(myanim);
//        Thread timer = new Thread(){
//            public void run(){
//                try{
//                    sleep(7000);
//                }catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//            }
//        };
//        timer.start();

    }

    private void tampilGaleri() {
        imageView.setImageResource(0);
        final CharSequence[] items = {"Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrasiMahasiswa.this);
        builder.setTitle("Add Photo!");
        builder.setIcon(R.drawable.ic_camera);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Choose from Library")){
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
                }else if (items[item].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult", "requestCode " + requestCode + ", resultCode " + resultCode);

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == REQUEST_CAMERA){
                try {
                    Log.e("CAMERA", fileUri.getPath());
                    bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                }catch (Exception e){
                    Log.e("TAG", e.getMessage());
                }
            }else if (requestCode == SELECT_FILE && data != null && data.getData() != null){
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(RegistrasiMahasiswa.this.getContentResolver(), data.getData());
                    setToImageView(getResizedBitmap(bitmap, max_resolution_image));
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void setToImageView(Bitmap bmp){
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar
        imageView.setImageBitmap(decoded);
    }

    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    private static File getOutputMediaFile(){
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DeKa");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                Log.e("Monitoring", "Oops! Failed create Monitoring Directory");
                return null;
            }
        }

        //create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_DeKa_" + timeStamp + ".jpg");

        return mediaFile;
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);

        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void Regis(final String nama, final String nim) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Registrasi sedang diproses .....");
        showDialog();

        VolleyMultiPartRequest req = new VolleyMultiPartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Log.d("TAG", response.toString());
                try {
                    Rak.entry("nama", nama);
                    Rak.entry("nim", nim);

                    edtNama.setText("");
                    edtNIM.setText("");

                    Log.d("TAG", response.toString());
//                    startActivity(new Intent(RegistrasiMahasiswa.this, AdminActivity.class));
//                    finish();
                    Toast.makeText(getApplicationContext(), "Registrasi telah berhasil !!!", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e("haha", e.getMessage());
                    Log.d("TAG", response.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("nim", nim);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("images", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(req, tag_json_obj);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog(){
        if (progressDialog.isShowing())
            progressDialog.dismiss();
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
