package com.example.quanlysinhvien;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final String DATABASE_NAME="AppQLSinhVien.sqlite";
    SQLiteDatabase database;
    ListView lvSinhVien;
    ArrayList<SinhVien> list;
    SinhVienAdapter adapter;
    ImageView imgThem;
    public static List<SinhVien> UserSelection=new ArrayList<>();//mảng chứa những những checkbox được chọn
    public static boolean isActionMode=false;
    public static ActionMode actionModes=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Anhxa();
        readData();

       imgThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
            }
        });
    }

    AbsListView.MultiChoiceModeListener modeListener=new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater inflater= actionMode.getMenuInflater();
            inflater.inflate(R.menu.menu_context,menu);
            isActionMode=true;
            actionModes=actionMode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId())
            {

                case R.id.menuThem:
                    Intent intent=new Intent(MainActivity.this,AddActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
                    return true;
                case R.id.menuXoa:
                    adapter.Remove(UserSelection);
                    actionMode.finish();
                    Toast.makeText(MainActivity.this,"Đã xóa "+UserSelection.size()+" item",Toast.LENGTH_LONG).show();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            isActionMode=false;
            actionModes=null;
        }
    };

    private  void Anhxa()
    {
        imgThem=(ImageView) findViewById(R.id.imageviewThem);
        lvSinhVien=(ListView) findViewById(R.id.listView);
        list=new ArrayList<>();
        lvSinhVien.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvSinhVien.setMultiChoiceModeListener(modeListener);
        adapter=new SinhVienAdapter(this,list);
        lvSinhVien.setAdapter(adapter);

    }
    private void readData()
    {
        database=Database.initDatabase(this,DATABASE_NAME);
        Cursor cursor=database.rawQuery("SELECT * FROM SinhVien",null);
        list.clear();
       for(int i=0;i<cursor.getCount();i++)
       {
           cursor.moveToPosition(i);
           int id=cursor.getInt(0);
           String ten=cursor.getString(1);
           String sdt=cursor.getString(2);
            byte[] anh=cursor.getBlob(3);
            list.add(new SinhVien(id,ten,sdt,anh));
       }
       adapter.notifyDataSetChanged();
    }
}