package com.example.phamvietlap.touchbuy.Activity.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phamvietlap.touchbuy.Activity.Activity.MainActivity;
import com.example.phamvietlap.touchbuy.Activity.Activity.Payment;
import com.example.phamvietlap.touchbuy.Activity.Model.Config;
import com.example.phamvietlap.touchbuy.Activity.Model.GioHang;
import com.example.phamvietlap.touchbuy.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by phamvietlap on 11/10/2017.
 */

public class Donhang_Adapter extends BaseAdapter {
    int            tongtien=Payment.tongtien;
    Context         context;
    int             layout;
    ArrayList<GioHang>   MangHang;
    public Donhang_Adapter(Context context,int layout,ArrayList<GioHang> MangHang){
        this.context=context;
        this.layout=layout;
        this.MangHang=MangHang;
    }
    @Override
    public int getCount() {
        return MangHang.size();
    }

    @Override
    public Object getItem(int position) {
        return MangHang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder{
        TextView    txtten,txtgia,txtsoluong;
        ImageView   imageView;
        Button      btndelete;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View    rowview=convertView;
        LayoutInflater  layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        ViewHolder  viewHolder= new ViewHolder();
        if(rowview==null){
            rowview = layoutInflater.inflate(R.layout.dong_don_hang,null);
            viewHolder.txtten=(TextView)rowview.findViewById(R.id.txtTenSanPhamDH);
            viewHolder.txtgia=(TextView)rowview.findViewById(R.id.txtGiaSanPhamDH);
            viewHolder.txtsoluong=(TextView)rowview.findViewById(R.id.txtSoLuongDH);
            viewHolder.imageView=(ImageView)rowview.findViewById(R.id.imgHinhDonhang);
            viewHolder.btndelete=(Button)rowview.findViewById(R.id.btnDeleteDH);
            rowview.setTag(viewHolder);
        }
        else  viewHolder= (ViewHolder) rowview.getTag();
        final DecimalFormat decimalFormat= new DecimalFormat("###,###,###");
        viewHolder.txtten.setText(MangHang.get(position).Tensp);
        viewHolder.txtsoluong.setText("Số lượng: "+MangHang.get(position).SoLuong);
        viewHolder.txtgia.setText("Giá: "+decimalFormat.format(MangHang.get(position).GiaSp)+" VNĐ");
        Picasso.with(context).load(MangHang.get(position).HinhSp).placeholder(R.drawable.image).into(viewHolder.imageView);
        viewHolder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.db.TruyVanDuLieu("DELETE FROM Cart WHERE Ma_sp='"+MangHang.get(position).MaSp+"' AND Email='"+getEmail()+"'");
                notifyDataSetChanged();
                int tien=tongtien-MangHang.get(position).SoLuong*MangHang.get(position).GiaSp;
                Payment.tongtien=tien;
                Payment.txtTongTien.setText(decimalFormat.format(tien)+" VNĐ");
                MangHang.remove(position);
                Toast.makeText(context,"Bạn đã xóa thành công",Toast.LENGTH_LONG).show();
            }
        });

        return rowview;
    }
    private String getEmail(){
        SharedPreferences sharedPreferences= context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"");
    }
}
