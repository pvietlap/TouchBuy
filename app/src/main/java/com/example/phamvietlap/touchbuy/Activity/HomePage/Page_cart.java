package com.example.phamvietlap.touchbuy.Activity.HomePage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.phamvietlap.touchbuy.Activity.Activity.MainActivity;
import com.example.phamvietlap.touchbuy.Activity.Activity.Payment;
import com.example.phamvietlap.touchbuy.Activity.Adapter.Cart_Adapter;
import com.example.phamvietlap.touchbuy.Activity.Model.Cart;
import com.example.phamvietlap.touchbuy.Activity.Model.Config;
import com.example.phamvietlap.touchbuy.R;

import java.util.ArrayList;

/**
 * Created by phamvietlap on 04/10/2017.
 */

public class Page_cart extends Fragment {
    View view;
    ListView        listview;
    ArrayList<Cart> mangCart;
    Button          btnthanhtoan;
    String          Email;
    Cart_Adapter    adapter;
    int             count=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_page_cart,container,false);
        Anhxa();

        return view;
    }

    @Override
    public void onResume() {
        mangCart.clear();
        if (!Check_Login())
            Toast.makeText(getActivity(),"Bạn cần đăng nhập",Toast.LENGTH_LONG).show();
        else {
            getCart();
            adapter = new Cart_Adapter(getActivity(), R.layout.dong_cart, mangCart);
            listview.setAdapter(adapter);
        }
        btnthanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Payment.class);
                getActivity().startActivity(intent);
            }
        });
        super.onResume();
    }

    private boolean Check_Login(){
        SharedPreferences get= getActivity().getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        Email=get.getString(Config.EMAIL_SHARED_PREF,"");

        if (get.getString(Config.EMAIL_SHARED_PREF,"").equals(""))
            return false;
        else return true;

    }
    private void getCart(){
        Cursor get = MainActivity.db.LayDuLieu("SELECT * FROM Cart WHERE Email='"+Email+"'");
        while (get.moveToNext()){
            mangCart.add(new Cart(get.getInt(2),get.getString(5),get.getInt(3)));
        }

    }
    private void Anhxa(){
        listview=(ListView)view.findViewById(R.id.lvGioHang);
        mangCart=new ArrayList<>();
        btnthanhtoan=(Button)view.findViewById(R.id.btnThanhToan);
    }

}
