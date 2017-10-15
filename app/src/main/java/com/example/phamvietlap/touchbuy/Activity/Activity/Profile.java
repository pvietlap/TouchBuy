package com.example.phamvietlap.touchbuy.Activity.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.phamvietlap.touchbuy.Activity.Model.Config;
import com.example.phamvietlap.touchbuy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Profile extends AppCompatActivity {
    Toolbar toolbar;
    TextView    txtEmail, txtHoTen, txtSDT, txtDiaChi, txtSoLuong;
    public static String      email;
    String  ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setToolbar();
        AnhXa();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getprofile().execute(Config.Link_profile+getEmail());
                new getId().execute(Config.Link_getId+getEmail());
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getsoluong().execute(Config.Link_num_cart+ID);
            }
        });
    }

    @Override
    protected void onRestart() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getprofile().execute(Config.Link_profile+getEmail());
                new getId().execute(Config.Link_getId+getEmail());
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getsoluong().execute(Config.Link_num_cart+ID);
            }
        });
        super.onRestart();
    }

    private class getprofile extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            return GetProfile(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {

                JSONObject  object=new JSONObject(s);
                JSONArray   array=object.getJSONArray("result");
                JSONObject  ob=array.getJSONObject(0);
                String Ten=ob.getString("Ten");
                String sdt=ob.getString("Sdt");
                String diachi=ob.getString("Diachi");
                if(Ten.equals("null"))    txtHoTen.setText("");
                else txtHoTen.setText(Ten);
                if(sdt.equals("null")) txtSDT.setText("");
                else
                    txtSDT.setText(sdt);
                if(diachi.equals("null")) txtDiaChi.setText("");
                else txtDiaChi.setText(diachi);
                txtEmail.setText(email);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }
    private class getsoluong extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            return GetNumCart(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            txtSoLuong.setText(s);
        }
    }
    private String getEmail(){
        SharedPreferences sharedPreferences=getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        ID=sharedPreferences.getString(Config.ID_SHARRED_PREF,"");
        return sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"");
    }

    private void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.profine_toolbar_detail);
        toolbar.inflateMenu(R.menu.profile);
        toolbar.setTitle("TouchBuy");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.logout:
                        SharedPreferences sharedPreferences=getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Config.EMAIL_SHARED_PREF,"");
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF,false);
                        editor.commit();
                        Intent intent=new Intent(Profile.this,Login.class);
                        finish();
                        startActivity(intent);
                        break;
                    case R.id.edit_info:
                        intent= new Intent(Profile.this,Edit_Profile.class);
                        startActivity(intent);
                        break;
                    case R.id.back_home:
                        intent= new Intent(Profile.this,MainActivity.class);
                        startActivity(intent);
                }
                return false;
            }
        });
        toolbar.findViewById(R.id.menu_cart_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent   intent = new Intent(Profile.this,MainActivity.class);
                intent.putExtra("Page",2);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences=getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        email=sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getsoluong().execute(Config.Link_num_cart+ID);
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getprofile().execute(Config.Link_profile+getEmail());
                new getId().execute(Config.Link_getId+getEmail());
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getsoluong().execute(Config.Link_num_cart+ID);
            }
        });
    }
    private static String GetNumCart(String theUrl)    {
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
    private static String GetProfile(String theUrl)    {
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

    private class getId extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            return Get_In_Id(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Config.ID_SHARRED_PREF,s);
            editor.commit();
            super.onPostExecute(s);
        }
    }
    private static String Get_In_Id(String theUrl)    {
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
    private void AnhXa(){
        txtEmail    = (TextView) findViewById(R.id.txtEmailKhachHang);
        txtDiaChi   = (TextView) findViewById(R.id.txtDiaChiKH);
        txtSDT      = (TextView) findViewById(R.id.txtSDTKhachHang);
        txtHoTen    = (TextView) findViewById(R.id.txtHoTenKH);
        txtSoLuong  = (TextView) findViewById(R.id.txtSoLuongSP);
    }
}
