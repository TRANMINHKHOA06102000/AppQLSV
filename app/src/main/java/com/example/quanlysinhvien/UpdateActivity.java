package com.example.quanlysinhvien;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UpdateActivity extends AppCompatActivity {
    final  String DATABASE_NAME="AppQLSinhVien.sqlite";
    final int REQUEST_TAKE_PHOTO=123;
    final int REQUEST_CHOOSE_PHOTO=321;
    int id = -1;
    Button btnChonHinh,btnChhupHinh,btnLuu,btnHuy;
    EditText edtTen,edtSdt;
    ImageView imghinhdaidien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Anhxa();
        addEvents();
        initUI();
    }
    private void Anhxa()
    {
        btnChhupHinh=(Button) findViewById(R.id.buttonChupHinh);
        btnChonHinh=(Button) findViewById(R.id.buttonChonHinh);
        btnLuu=(Button) findViewById(R.id.buttonLuu);
        btnHuy=(Button) findViewById(R.id.buttonHuy);
        edtTen=(EditText) findViewById(R.id.edittextTen);
        edtSdt=(EditText) findViewById(R.id.edittextSdt);
        imghinhdaidien=(ImageView) findViewById(R.id.imageHinhDaiDien);
    }
    private  void addEvents()
    {
        btnChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });
        btnChhupHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
                Toast.makeText(UpdateActivity.this,"Cập nhật thành công",Toast.LENGTH_LONG).show();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }
    private  void initUI()
    {
        Intent intent=getIntent();
        int id=intent.getIntExtra("Id",-1);
        SQLiteDatabase database =Database.initDatabase(this,DATABASE_NAME);
        Cursor cursor=database.rawQuery("SELECT * FROM SinhVien WHERE Id=?",new String[]{id + ""});
        cursor.moveToFirst();
        String ten=cursor.getString(1);
        String sdt=cursor.getString(2);
        byte[] anh=cursor.getBlob(3);
        Bitmap bitmap= BitmapFactory.decodeByteArray(anh,0,anh.length);
        imghinhdaidien.setImageBitmap(bitmap);
        edtTen.setText(ten);
        edtSdt.setText(sdt);
    }

    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }
    private void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                try {
                    Uri imageUri=data.getData();
                    InputStream is=getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap=BitmapFactory.decodeStream(is);
                    imghinhdaidien.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap= (Bitmap) data.getExtras().get("data");
                imghinhdaidien.setImageBitmap(bitmap);
            }
        }
    }
    private void update()
    {
        String ten = edtTen.getText().toString();
        String sdt = edtSdt.getText().toString();
        byte[] anh = getByteArrayFromImageView(imghinhdaidien);

        ContentValues contentValues = new ContentValues();
        contentValues.put("Ten", ten);
        contentValues.put("SDT", sdt);
        contentValues.put("Anh", anh);

        Intent intent=getIntent();
        int id=intent.getIntExtra("ID",-1);
        SQLiteDatabase database= Database.initDatabase(this,"AppQLSinhVien.sqlite");
        database.update("SinhVien",contentValues,"Id=?", new String[] {id+ ""});
        Intent myintent = new Intent(this, MainActivity.class);
        startActivity(myintent);
    }
    private void cancel(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
    }
    private byte[] getByteArrayFromImageView(ImageView imgv){

        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}