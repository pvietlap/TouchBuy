package com.example.phamvietlap.touchbuy.Activity.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.phamvietlap.touchbuy.Activity.Adapter.SanPhamAdapter;
import com.example.phamvietlap.touchbuy.Activity.Model.Config;
import com.example.phamvietlap.touchbuy.Activity.Model.SanPham;
import com.example.phamvietlap.touchbuy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Dien_thoai_danh_muc extends AppCompatActivity {
    int id_danh_muc;
    Toolbar toolbar;
    GridView gridViewSanPham;
    ArrayList<SanPham> mangSanPham;
    SanPhamAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai_danh_muc);
        Intent intent=getIntent();
        id_danh_muc=intent.getIntExtra("Id_danh_muc",0);
        Anhxa();
        toolbar();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Get_phone().execute(Config.Link_getphone+id_danh_muc);
            }
        });
        gridViewSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent  intent= new Intent(getApplicationContext(), Detail_SanPham.class);
                intent.putExtra("ID",mangSanPham.get(position).MaSanPham);
                startActivity(intent);

            }
        });
    }
    private class Get_phone extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            return GetSanPham(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray jsonArray= new JSONArray(s);
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    mangSanPham.add(new SanPham(object.getString("Hinh"), object.getInt("Ma"), object.getString("Ten"), object.getInt("Gia"), object.getString("ChiTiet")));
                }
                adapter= new SanPhamAdapter(Dien_thoai_danh_muc.this,R.layout.dong_sanpham,mangSanPham);
                gridViewSanPham.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
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
    private void Anhxa(){
        gridViewSanPham=(GridView)findViewById(R.id.gridDienthoaiphanloai);
        mangSanPham= new ArrayList<SanPham>();
    }
}
