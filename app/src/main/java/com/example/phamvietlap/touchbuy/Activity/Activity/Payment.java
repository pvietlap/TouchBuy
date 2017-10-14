package com.example.phamvietlap.touchbuy.Activity.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phamvietlap.touchbuy.Activity.Adapter.Donhang_Adapter;
import com.example.phamvietlap.touchbuy.Activity.Model.Config;
import com.example.phamvietlap.touchbuy.Activity.Model.GioHang;
import com.example.phamvietlap.touchbuy.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Payment extends AppCompatActivity {
    Toolbar             toolbar;
    public static TextView            txtTongTien;
    ListView            lvDonHang;
    int                 id = 0;
    Donhang_Adapter     adapter;
    Button              btnThanhToan;
    String              Tensp,Hinhsp,Email;
    int                 Giasp,Soluong;
    String              ID,ID_DonHang;
    public static int                 tongtien=0;
    ArrayList<GioHang>  mangGioHang;
    String              gia_sp,sl_sp,tien,id_sp;
    DecimalFormat decimalFormat= new DecimalFormat("###,###,###");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        AnhXa();
        toolbar();

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Thanhtoan().execute(Config.Link_Thanhtoan1);
                    }
                });
                MainActivity.db.TruyVanDuLieu("DELETE FROM Cart WHERE Email='"+Email+"'");
                Intent intent=new Intent(Payment.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        getCart();
        tongtien=0;
        if(!checkLogin()) {
            getSanpham_no_user();
            ID="0";
        }
        else
            getCart();
        for(int i=0;i<mangGioHang.size();i++)
            tongtien+=mangGioHang.get(i).GiaSp*mangGioHang.get(i).SoLuong;
        txtTongTien.setText(decimalFormat.format(tongtien)+"VNĐ");
        showList();
        super.onResume();
    }

    private void showList(){
        adapter = new Donhang_Adapter(getApplicationContext(),R.layout.dong_sanpham,mangGioHang);
        lvDonHang.setAdapter(adapter);
    }
    private void getSanpham_no_user(){
        Bundle bundle=getIntent().getExtras();
        id=bundle.getInt("ID");
        Tensp=bundle.getString("tensp");
        Giasp= Integer.parseInt(bundle.getString("giasp"));
        Soluong= Integer.parseInt(bundle.getString("soluong"));
        Hinhsp=bundle.getString("Hinhanh");
        tongtien=Soluong*Giasp;
        txtTongTien.setText(decimalFormat.format(tongtien)+"VNĐ");
        mangGioHang.add(new GioHang(id,Tensp,Soluong,Giasp,Hinhsp));
    }
    private boolean checkLogin(){
        SharedPreferences sharedPreferences=getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        Email=sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"");
        ID=sharedPreferences.getString(Config.ID_SHARRED_PREF,"");
        if(sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"").equals(""))
            return false;
        else return true;
    }

    private void getCart() {
        Cursor get = MainActivity.db.LayDuLieu("SELECT * FROM Cart WHERE Email='" + Email + "'");
        while (get.moveToNext()) {
            mangGioHang.add(new GioHang(get.getInt(2), get.getString(5), get.getInt(3), get.getInt(4), get.getString(6)));
        }
    }
    private void toolbar() {
        toolbar = (Toolbar) findViewById(R.id.nav_toolbar_detail);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private class Thanhtoan extends AsyncTask<String,String,String>{
        Dialog  dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=ProgressDialog.show(Payment.this,"Please wait","Loading");
        }

        @Override
        protected String doInBackground(String... params) {
            return postData(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            boolean handler= new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                  dialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Book success",Toast.LENGTH_SHORT).show();
                }
            },3000);
            ID_DonHang=s;

            for(int i=0;i<mangGioHang.size();i++){
                id_sp= String.valueOf(mangGioHang.get(i).MaSp);
                gia_sp= String.valueOf(mangGioHang.get(i).GiaSp);
                sl_sp= String.valueOf(mangGioHang.get(i).SoLuong);
                tien= String.valueOf(mangGioHang.get(i).GiaSp*mangGioHang.get(i).SoLuong);
             //   Toast.makeText(getApplicationContext(),tien+"",Toast.LENGTH_LONG).show();
               doit(ID_DonHang,id_sp,gia_sp,sl_sp,tien);
            }
            super.onPostExecute(s);
        }
    }
    public void doit(final String a, final String b, final String c, final String d, final String e) {
         class post_chitiet extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... params) {
                return post_san_pham(params[0],a,b,c,d,e);
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new post_chitiet().execute(Config.Link_Thanhtoan2);
            }
        });
    }


    private String  post_san_pham(String link,String id_donhang,String id_sp,String gia_sp,String sl_sp,String tien){
        HttpURLConnection connect;
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Error!";
        }
        try {
            // cấu hình HttpURLConnection
            connect = (HttpURLConnection)url.openConnection();
            connect.setReadTimeout(10000);
            connect.setConnectTimeout(15000);
            connect.setRequestMethod("POST");
            // Gán tham số vào URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("id_donhang",id_donhang)
                    .appendQueryParameter("id_sp",id_sp)
                    .appendQueryParameter("gia_sp",gia_sp)
                    .appendQueryParameter("sl_sp",sl_sp)
                    .appendQueryParameter("tien",tien);
            String query = builder.build().getEncodedQuery();

            // Mở kết nối gửi dữ liệu
            OutputStream os = connect.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            connect.connect();
        } catch (IOException e1) {
            e1.printStackTrace();
            return "Error!";
        }
        try {
            int response_code = connect.getResponseCode();

            // kiểm tra kết nối ok
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Đọc nội dung trả về
                InputStream input = connect.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            }else{
                return "Error!";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error!";
        } finally {
            connect.disconnect();
        }
    }
    private String  postData(String link){
        HttpURLConnection connect;
        URL url = null;
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "Error!";
        }
        try {
            // cấu hình HttpURLConnection
            connect = (HttpURLConnection)url.openConnection();
            connect.setReadTimeout(10000);
            connect.setConnectTimeout(15000);
            connect.setRequestMethod("POST");
            // Gán tham số vào URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("makh",ID)
                    .appendQueryParameter("tongtien", String.valueOf(tongtien));
            String query = builder.build().getEncodedQuery();
            // Mở kết nối gửi dữ liệu
            OutputStream os = connect.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            connect.connect();
        } catch (IOException e1) {
            e1.printStackTrace();
            return "Error!";
        }
        try {
            int response_code = connect.getResponseCode();

            // kiểm tra kết nối ok
            if (response_code == HttpURLConnection.HTTP_OK) {
                // Đọc nội dung trả về
                InputStream input = connect.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            }else{
                return "Error!";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error!";
        } finally {
            connect.disconnect();
        }
    }
    private void AnhXa(){

        btnThanhToan = (Button) findViewById(R.id.btnThanhToanDH);
        txtTongTien = (TextView) findViewById(R.id.txtTongTien);
        lvDonHang = (ListView) findViewById(R.id.lvDonHang);
        mangGioHang=new ArrayList<>();
    }
}
