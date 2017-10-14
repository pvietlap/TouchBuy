package com.example.phamvietlap.touchbuy.Activity.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.phamvietlap.touchbuy.Activity.Adapter.Danh_muc_Adapter;
import com.example.phamvietlap.touchbuy.Activity.Model.Config;
import com.example.phamvietlap.touchbuy.Activity.Model.DanhMuc;
import com.example.phamvietlap.touchbuy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Danh_muc_san_pham extends AppCompatActivity {
    Toolbar toolbar;
    GridView gridViewDanhmuc;
    ArrayList<DanhMuc> mangDanhmuc;
    Danh_muc_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_muc_san_pham);
        AnhXa();
        toolbar();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Get_danh_muc().execute(Config.Link_getdanhmuc);
            }
        });
        gridViewDanhmuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(Danh_muc_san_pham.this,Dien_thoai_danh_muc.class);
                intent.putExtra("Id_danh_muc",mangDanhmuc.get(position).MaLoai);
                startActivity(intent);
            }
        });
    }

    private class Get_danh_muc extends AsyncTask<String,String,String>{

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
                    mangDanhmuc.add(new DanhMuc( object.getInt("Ma"),object.getString("Ten"),object.getString("Hinh") ));
                }
                adapter= new Danh_muc_Adapter(Danh_muc_san_pham.this,R.layout.dong_danh_muc_sp,mangDanhmuc);
                gridViewDanhmuc.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }
    private void AnhXa(){
        gridViewDanhmuc = (GridView) findViewById(R.id.gridViewDanhMucSp);
        mangDanhmuc = new ArrayList<DanhMuc>();
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

}
