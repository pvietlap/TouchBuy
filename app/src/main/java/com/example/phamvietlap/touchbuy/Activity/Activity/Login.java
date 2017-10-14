package com.example.phamvietlap.touchbuy.Activity.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phamvietlap.touchbuy.Activity.Model.Config;
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

public class Login extends AppCompatActivity {
    Toolbar     toolbar;
    TextView    txtsing_up;
    EditText    edtEmail, edtPassword;
    Button      btnDangNhap;
     String     user=null,pass=null;
    Boolean     Login=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Anhxa();

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Xulychuoi();
                if(user.equals("") || pass.equals(""))
                    Toast.makeText(Login.this,"Username or Password null",Toast.LENGTH_LONG).show();
                else
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Login_Acc().execute(Config.Link_login);
                    }
                });
            }
        });
        txtsing_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent=new Intent(Login.this,Sing_up.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (!sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"").equals("")) {
            Intent intent = new Intent(Login.this, Profile.class);
            finish();
            startActivity(intent);
        }
        super.onResume();
    }
    private class Login_Acc extends AsyncTask<String,String,String>{
        private Dialog  loadding;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadding= ProgressDialog.show(Login.this,"Please wait","Loading....");
        }

        @Override
        protected String doInBackground(String... params) {
            return postData(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            loadding.dismiss();
            if (s.equals("success")) {
                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                editor.putString(Config.EMAIL_SHARED_PREF, user);
                editor.commit();
                Intent  intent=new Intent(Login.this,Profile.class);
                finish();
                startActivity(intent);
            }
            else
                Toast.makeText(Login.this, "Invalid User Name or Password", Toast.LENGTH_LONG).show();
            super.onPostExecute(s);
        }
    }


    private void    Anhxa(){
        edtEmail    = (EditText)findViewById(R.id.edtEmailLogin);
        edtPassword = (EditText)findViewById(R.id.edtPassLogin);
        btnDangNhap = (Button)findViewById(R.id.btnDangNhap);
        txtsing_up  = (TextView)findViewById(R.id.txtChuaCoTk);
    }
    private void    Xulychuoi(){
        user=edtEmail.getText().toString().trim();
        pass=edtPassword.getText().toString().trim();
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
                    .appendQueryParameter("username",edtEmail.getText().toString())
                    .appendQueryParameter("password",edtPassword.getText().toString());
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


}
