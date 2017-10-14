package com.example.phamvietlap.touchbuy.Activity.Adapter;

import android.content.Context;
import android.graphics.Paint;
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
import java.util.ArrayList;

/**
 * Created by phamvietlap on 14/10/2017.
 */

public class KhuyenMai_Adapter extends BaseAdapter{
    Context context;
    int     layout;
    ArrayList<SanPham> mangsp;
    public KhuyenMai_Adapter(Context context,int layout,ArrayList<SanPham> mangsp){
        this.context=context;
        this.layout=layout;
        this.mangsp=mangsp;
    }
    @Override
    public int getCount() {
        return mangsp.size();
    }

    @Override
    public Object getItem(int position) {
        return mangsp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class viewholder{
        TextView txtten,txtgiacu,txtgiamoi;
        ImageView imghinh;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View rowview=convertView;
        viewholder holder= new viewholder();
        if(rowview==null){
            rowview= inflater.inflate(R.layout.dong_khuyen_mai,null);
            holder.txtten=(TextView)rowview.findViewById(R.id.txtTenSanPhamKM);
            holder.txtgiacu = (TextView) rowview.findViewById(R.id.txtGiaCuKM);
            holder.txtgiamoi=(TextView)rowview.findViewById(R.id.txtGiaSanPhamKM) ;
            holder.imghinh=(ImageView)rowview.findViewById(R.id.imgHinhSanPhamKM);
            rowview.setTag(holder);
        }
        else
            holder= (viewholder) rowview.getTag();
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        holder.txtten.setText(mangsp.get(position).TenSanPham);
        holder.txtgiacu.setText(decimalFormat.format(mangsp.get(position).GiaSanPham*1.15)+"VND");
        holder.txtgiacu.setPaintFlags(holder.txtgiacu.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txtgiamoi.setText(decimalFormat.format(mangsp.get(position).GiaSanPham)+"VND");
        Picasso.with(context).load(mangsp.get(position).HinhSanPham).placeholder(R.drawable.image).into(holder.imghinh);
        return rowview;
    }
}
