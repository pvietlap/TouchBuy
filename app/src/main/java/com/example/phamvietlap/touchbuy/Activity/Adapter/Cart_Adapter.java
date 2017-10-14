package com.example.phamvietlap.touchbuy.Activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.phamvietlap.touchbuy.Activity.Model.Cart;
import com.example.phamvietlap.touchbuy.R;

import java.util.ArrayList;

/**
 * Created by phamvietlap on 12/10/2017.
 */

public class Cart_Adapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Cart> mangCart;
    public Cart_Adapter(Context context,int layout,ArrayList<Cart> mangCart){
        this.context=context;
        this.layout=layout;
        this.mangCart=mangCart;
    }
    @Override
    public int getCount() {
        return mangCart.size();
    }

    @Override
    public Object getItem(int position) {
        return mangCart.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class viewholder{
        TextView txtten,txtsl;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowview=convertView;
        viewholder holder=new viewholder();
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        if(rowview==null){
            rowview=layoutInflater.inflate(R.layout.dong_cart,null);
            holder.txtten=(TextView)rowview.findViewById(R.id.txtTenSanPhamGioHang);
            holder.txtsl=(TextView)rowview.findViewById(R.id.txtSoLuongGioHang);
            rowview.setTag(holder);
        }
        else
            holder= (viewholder) rowview.getTag();
            holder.txtten.setText(mangCart.get(position).TenSp);
            holder.txtsl.setText(mangCart.get(position).So_luong+"");
        return rowview;
    }
}
