package com.example.phamvietlap.touchbuy.Activity.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.phamvietlap.touchbuy.Activity.Adapter.Noi_bat_Adapter;
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

public class Noibat extends AppCompatActivity {
    Toolbar toolbar;
    AlphaAnimation alphaAnimation;
    GridView gridViewSanPham;
    ArrayList<SanPham> mangSanPham;
    Noi_bat_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noibat);
        Anhxa();
        toolbar();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new get_san_pham().execute(Config.Link_getspnoibat);
            }
        });
        gridViewSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(getApplicationContext(), Detail_SanPham.class);
                intent.putExtra("ID",mangSanPham.get(position).MaSanPham);
                startActivity(intent);

            }
        });
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
        alphaAnimation= new AlphaAnimation(1F,0.5F);
        gridViewSanPham = (GridView) findViewById(R.id.gridViewSanPhamNB);
        mangSanPham = new ArrayList<>();
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
    public class get_san_pham extends AsyncTask<String,String,String>{
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
                adapter= new Noi_bat_Adapter(Noibat.this,R.layout.dong_noi_bat,mangSanPham);
                gridViewSanPham.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }
}
