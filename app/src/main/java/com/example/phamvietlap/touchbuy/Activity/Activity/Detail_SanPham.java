package com.example.phamvietlap.touchbuy.Activity.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phamvietlap.touchbuy.Activity.Model.Cart;
import com.example.phamvietlap.touchbuy.Activity.Model.Config;
import com.example.phamvietlap.touchbuy.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Detail_SanPham extends AppCompatActivity {

    Toolbar         toolbar;
    Button          btnMuaNgay;
    ImageButton     btnThemVaoGioHang;
    TextView        txtTenSanPham,txtGiaSanPham,txtChiTietSP,txtdown,txtup,txtSoLuong;
    ImageView       imgHinhChiTiet;
    ProgressBar     progressBar;
    int             Id=0;
    int             Sl=0;
    String          email,imgsp=null,tensp=null,giasp=null;
    ArrayList<Cart> Mang_Cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__san_pham);
        Anhxa();
        toolbar();
        Id=getID();
        click_up_down();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getDetail().execute(Config.Link_product+Id);
            }
        });
        btnMuaNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Sl!=0) {
                    if (!checkLogin()) {
                        Intent intent = new Intent(Detail_SanPham.this, Payment.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("ID", Id);
                        bundle.putString("tensp", tensp);
                        bundle.putString("giasp", giasp);
                        bundle.putString("soluong", txtSoLuong.getText().toString());
                        bundle.putString("Hinhanh", imgsp);
                        intent.putExtras(bundle);

                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(Detail_SanPham.this, Payment.class);
                        startActivity(intent);
                    }
                }
                else
                    Toast.makeText(getApplicationContext(),"Vui lòng chọn số lượng",Toast.LENGTH_LONG).show();
            }
        });
        btnThemVaoGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkLogin()){
                    if (Sl==0)
                        Toast.makeText(getApplicationContext(),"Bạn chưa chọn số lượng hàng",Toast.LENGTH_LONG).show();
                    else
                    {
                        final Cursor get = MainActivity.db.LayDuLieu("SELECT * FROM Cart WHERE Ma_sp='"+Id+"' AND Email='"+getEmail()+"'");
                        if (get.getCount()==0){
                                MainActivity.db.TruyVanDuLieu("INSERT INTO Cart VALUES(null,'"+email+"','"+Id+"','"+Sl+"','"+giasp+"','"+tensp+"','"+imgsp+"')");
                            Toast.makeText(getApplicationContext(),"Thêm vào giỏ hàng thành công.",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            AlertDialog.Builder alertDialog= new AlertDialog.Builder(Detail_SanPham.this);
                            alertDialog.setIcon(R.drawable.mark);
                            alertDialog.setTitle("Sản phẩm đã có trong giỏ hàng");
                            alertDialog.setMessage("Bạn có thể thêm số lượng hoặc cập nhập lại số lượng");
                            alertDialog.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    while (get.moveToNext())
                                         Sl=Sl+get.getInt(3);
                                    MainActivity.db.TruyVanDuLieu("UPDATE Cart SET So_luong ='"+Sl+"' WHERE Ma_sp = '"+Id+"' AND Email='"+getEmail()+"'");

                                    Toast.makeText(getApplicationContext(),"Thêm vào giỏ hàng thành công.",Toast.LENGTH_LONG).show();
                                }
                            });
                            alertDialog.setNeutralButton("Hủy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.setNegativeButton("Cập nhập", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MainActivity.db.TruyVanDuLieu("UPDATE Cart SET So_luong ='"+Sl+"' WHERE Ma_sp = '"+Id+"' AND Email='"+getEmail()+"'");
                                    Toast.makeText(getApplicationContext(),"Cập nhập giỏ hàng thành công.",Toast.LENGTH_LONG).show();
                                }
                            });
                            alertDialog.show();
                        }
                    }
                }
                else
                    Toast.makeText(getApplicationContext(),"Bạn cần đăng nhập trước khi khi mua hàng",Toast.LENGTH_LONG).show();
            }
        });


    }


    private void click_up_down(){
        txtup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sl++;
                txtSoLuong.setText(Sl+"");
            }
        });
        txtdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Sl>0) Sl--;
                txtSoLuong.setText(Sl+"");
            }
        });
    }
    private class getDetail extends AsyncTask<String,Integer,String>{
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return GetSanPham(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject object=new JSONObject(s);
                JSONArray   array=  object.getJSONArray("result");
                JSONObject  ob=array.getJSONObject(0);
                txtTenSanPham.setText(ob.getString("Ten"));
                DecimalFormat   decimalformat=new DecimalFormat("###,###,###");
                txtGiaSanPham.append(" "+decimalformat.format(ob.getInt("Gia"))+" VND");
                Picasso.with(getApplicationContext()).load(ob.getString("HinhAnh")).placeholder(R.drawable.image).into(imgHinhChiTiet);
                imgsp=ob.getString("HinhAnh");
                txtChiTietSP.setText(ob.getString("ChiTiet"));
                tensp=ob.getString("Ten");
                giasp= String.valueOf(ob.getInt("Gia"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }
    private boolean checkLogin(){
        SharedPreferences sharedPreferences=getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        email=sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"");
       if(sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"").equals(""))
           return false;
        else
            return true;
    }
    private int getID(){
        Intent  intent=getIntent();
        return intent.getIntExtra("ID",0);
    }
    private void toolbar() {
        toolbar = (Toolbar) findViewById(R.id.nav_toolbar_detail);
        toolbar.inflateMenu(R.menu.cart);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle("TouchBuy");
        toolbar.findViewById(R.id.menu_cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent   intent = new Intent(Detail_SanPham.this,MainActivity.class);
                intent.putExtra("Page",2);
                startActivity(intent);
            }
        });

    }

    private String getEmail(){
        SharedPreferences sharedPreferences=getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"");
    }

    private void Anhxa(){
        progressBar=(ProgressBar)findViewById(R.id.progresssBar_detail);
        btnMuaNgay=(Button)findViewById(R.id.btnMua);
        btnThemVaoGioHang=(ImageButton)findViewById(R.id.btnThem);
        txtTenSanPham=(TextView)findViewById(R.id.ten_sanpham_detail);
        txtGiaSanPham=(TextView)findViewById(R.id.txt_giasanpham_detail);
        txtChiTietSP=(TextView)findViewById(R.id.txtChiTietSP);
        txtdown=(TextView)findViewById(R.id.txtdown);
        txtup=(TextView)findViewById(R.id.txtup);
        txtSoLuong=(TextView)findViewById(R.id.txtsoluong);
        imgHinhChiTiet  =(ImageView)findViewById(R.id.img_hinhsanpham_detail);
        Mang_Cart=new ArrayList<>();
    }
    private static String GetSanPham(String theUrl)    {
        StringBuilder content = new StringBuilder();

        try
        {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }

}
