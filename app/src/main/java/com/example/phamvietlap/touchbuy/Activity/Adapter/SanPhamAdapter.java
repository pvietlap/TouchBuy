package com.example.phamvietlap.touchbuy.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phamvietlap.touchbuy.Activity.Model.SanPham;
import com.example.phamvietlap.touchbuy.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by phamvietlap on 07/10/2017.
 */

public class SanPhamAdapter extends BaseAdapter {
    Context context;
    int     layout;
    List<SanPham>   arraySanpham;
    public SanPhamAdapter(Context context,int layout,List<SanPham> arraySanpham){
        this.context=context;
        this.layout=layout;
        this.arraySanpham=arraySanpham;
    }

    @Override
    public int getCount() {
        return arraySanpham.size();
    }

    @Override
    public Object getItem(int position) {
        return arraySanpham.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        TextView    txtten,txtgia;
        ImageView       imgHinh;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View    rowview = convertView;
        ViewHolder  holder= new ViewHolder();
        if (rowview==null){
            rowview=layoutInflater.inflate(R.layout.dong_sanpham,null);
            holder.txtten=(TextView)rowview.findViewById(R.id.txt_tensanpham);
            holder.txtgia=(TextView)rowview.findViewById(R.id.txt_giasanpham);
            holder.imgHinh=(ImageView)rowview.findViewById(R.id.img_hinhsanpham);
            rowview.setTag(holder);
        }else{
            holder= (ViewHolder) rowview.getTag();
        }
        DecimalFormat   decimalFormat= new DecimalFormat("###,###,###");
        holder.txtten.setText(arraySanpham.get(position).TenSanPham);
        holder.txtgia.setText(decimalFormat.format(arraySanpham.get(position).GiaSanPham)+" VND");
        Picasso.with(context).load(arraySanpham.get(position).HinhSanPham).placeholder(R.drawable.image).into(holder.imgHinh);
        return rowview;
    }
}
