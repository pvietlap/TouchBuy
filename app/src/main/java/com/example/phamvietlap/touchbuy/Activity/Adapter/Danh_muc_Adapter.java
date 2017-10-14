package com.example.phamvietlap.touchbuy.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phamvietlap.touchbuy.Activity.Model.DanhMuc;
import com.example.phamvietlap.touchbuy.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by phamvietlap on 14/10/2017.
 */

public class Danh_muc_Adapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<DanhMuc> mangdanhmuc;
    public Danh_muc_Adapter(Context context,int layout,ArrayList<DanhMuc> mangdanhmuc){
        this.context=context;
        this.layout=layout;
        this.mangdanhmuc=mangdanhmuc;
    }
    @Override
    public int getCount() {
        return mangdanhmuc.size();
    }

    @Override
    public Object getItem(int position) {
        return mangdanhmuc.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class viewholder{
        TextView txtten;
        ImageView imghinh;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowview=convertView;
        viewholder holder= new viewholder();
        if(rowview==null){
            rowview= inflater.inflate(R.layout.dong_danh_muc_sp,null);
            holder.txtten=(TextView)rowview.findViewById(R.id.txtTenDanhMuc);
            holder.imghinh=(ImageView)rowview.findViewById(R.id.imgHinhDanhMuc);
            rowview.setTag(holder);
        }
        else
            holder= (viewholder) rowview.getTag();
        holder.txtten.setText(mangdanhmuc.get(position).TenLoai);
        Picasso.with(context).load(mangdanhmuc.get(position).HinhAnh).placeholder(R.drawable.image).into(holder.imghinh);
        return rowview;
    }
}
