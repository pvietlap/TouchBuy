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
 * Created by phamvietlap on 09/10/2017.
 */

public class NoiBatAdapter extends BaseAdapter {
    Context context;
    int     layout;
    List<SanPham>   arrSanPham;
    public NoiBatAdapter(Context context,int layout, List<SanPham> arrSanPham){
        this.context=context;
        this.layout=layout;
        this.arrSanPham=arrSanPham;
    }
    @Override
    public int getCount() {
        return arrSanPham.size();
    }

    @Override
    public Object getItem(int position) {
        return arrSanPham.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class Viewholder{
        TextView    txtten,txtgia;
        ImageView   imghinh;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View    rowview=convertView;
        Viewholder  viewholder=new Viewholder();
        if(rowview==null){
            rowview=inflater.inflate(R.layout.sanpham_new,null);
            viewholder.txtten=(TextView)rowview.findViewById(R.id.txt_tensanpham_new);
            viewholder.txtgia=(TextView)rowview.findViewById(R.id.txt_giasanpham_new);
            viewholder.imghinh=(ImageView)rowview.findViewById(R.id.img_hinhsanpham_new);
            rowview.setTag(viewholder);
        }else
            viewholder= (Viewholder) rowview.getTag();
        DecimalFormat   decimalFormat=new DecimalFormat("###,###,###");
        viewholder.txtten.setText(arrSanPham.get(position).TenSanPham);
        viewholder.txtgia.setText(decimalFormat.format(arrSanPham.get(position).GiaSanPham)+"VND");
        Picasso.with(context).load(arrSanPham.get(position).HinhSanPham).placeholder(R.drawable.image).into(viewholder.imghinh);
        return rowview;
    }
}
