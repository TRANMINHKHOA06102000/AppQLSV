package com.example.quanlysinhvien;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.ManagerFactoryParameters;

public class SinhVienAdapter extends BaseAdapter {

    private Activity context;
    private List<SinhVien> list;

    public SinhVienAdapter(Activity context, List<SinhVien> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();//trả về số dòng mà adapter vẽ muốn vẽ 5 dòng thì return 5
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.sinhvien_row,null);
        ImageView imgHinh=(ImageView) row.findViewById(R.id.imageHinh);
        TextView txtTen=(TextView) row.findViewById(R.id.textviewTen);
        TextView txtSdt=(TextView) row.findViewById(R.id.textviewSdt);
        TextView txtId=(TextView) row.findViewById(R.id.textId);
        ImageView imgSua=(ImageView) row.findViewById(R.id.imageviewSua);
        ImageView imgxoa=(ImageView) row.findViewById(R.id.imageviewXoa);
        CheckBox cbXoa= (CheckBox) row.findViewById(R.id.checkbox);
        cbXoa.setTag(i);
        final SinhVien sinhVien=list.get(i);
        txtId.setText(sinhVien.id+"");
        txtTen.setText(sinhVien.ten);
        txtSdt.setText(sinhVien.sdt);

        Bitmap bmhinhDaiDien= BitmapFactory.decodeByteArray(sinhVien.anh,0,sinhVien.anh.length);
        imgHinh.setImageBitmap(bmhinhDaiDien);
        if(MainActivity.isActionMode)
        {
            cbXoa.setVisibility(row.VISIBLE);

        }
        else
        {
            cbXoa.setVisibility(row.GONE);
        }
        cbXoa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int position=(int) compoundButton.getTag();
                if(MainActivity.UserSelection.contains(list.get(i)))
                {
                    MainActivity.UserSelection.remove(list.get(i));
                }
                else {
                    MainActivity.UserSelection.add(list.get(i));
                }
                MainActivity.actionModes.setTitle(MainActivity.UserSelection.size()+" Items selected..");
            }
        });
        imgSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,UpdateActivity.class);
                intent.putExtra("Id", sinhVien.id);
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.anim_enter,R.anim.anim_exit);
            }
        });
        imgxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác Nhận Xóa");
                builder.setMessage("Bạn có chắc chắc muốn xóa");
               builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                        delete(sinhVien.id);
                   }
               });
               builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {

                   }
               });
               AlertDialog dialog=builder.create();
               dialog.show();
            }
        });
        Animation animation= AnimationUtils.loadAnimation(context,R.anim.scale_list);
        row.startAnimation(animation);
        return row;
    }
    public void Remove(List<SinhVien> items)
    {
        for(SinhVien item : items)
        {
            list.remove(item);
        }
        notifyDataSetChanged();
    }
    private void delete(int idSinhVien) {
        SQLiteDatabase database=Database.initDatabase(context,"AppQLSinhVien.sqlite");
        database.delete("SinhVien","Id = ?",new String[]{ idSinhVien +""});
        list.clear();
        Cursor cursor=database.rawQuery("SELECT * FROM SinhVien",null);
        while(cursor.moveToNext())
        {
            int id=cursor.getInt(0);
            String ten=cursor.getString(1);
            String sdt=cursor.getString(2);
            byte[] anh=cursor.getBlob(3);

            list.add(new SinhVien(id,ten,sdt,anh));
        }
        notifyDataSetChanged();
    }
}
