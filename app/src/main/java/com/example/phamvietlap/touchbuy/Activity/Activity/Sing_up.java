package com.example.phamvietlap.touchbuy.Activity.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Sing_up extends AppCompatActivity {
    EditText    edtEmail, edtPassword, edtPassAgain;
    Button      btnDangKy, btnNhapLai;
    String      user,pass,repass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        AnhXa();

        btnNhapLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                re_count();
            }
        });
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getText();
                if (user.equals("") || pass.equals("") || repass.equals("")){
                    Toast.makeText(Sing_up.this,"Email or password is null",Toast.LENGTH_LONG).show();
                }
                else
                    if(!pass.equals(repass)){
                        Toast.makeText(Sing_up.this,"Password is not match ",Toast.LENGTH_LONG).show();
                    }
                else
                    if (pass.length()<6)
                        Toast.makeText(Sing_up.this,"Password least is 6 character!",Toast.LENGTH_LONG).show();
                else
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            new Resgi().execute(Config.Link_singup);
                        }
                    });
            }
        });
    }

    private class Resgi extends AsyncTask<String,String,String>{
         ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(Sing_up.this,"Please wait",null);
        }

        @Override
        protected String doInBackground(String... params) {
            return postData(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            if (s.equals("successfully")){
                Toast.makeText(Sing_up.this, "Register succeed!", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
                Toast.makeText(Sing_up.this, "Email alredy exist!", Toast.LENGTH_LONG).show();
            super.onPostExecute(s);
        }
    }

    private void getText(){
        user=edtEmail.getText().toString().trim();
        pass=edtPassword.getText().toString().trim();
        repass=edtPassAgain.getText().toString().trim();
    }
    private void re_count(){
        edtEmail.setText("");
        edtPassword.setText("");
        edtPassAgain.setText("");
    }

    private void AnhXa(){
        edtEmail     = (EditText) findViewById(R.id.edtUserRegis);
        edtPassAgain = (EditText) findViewById(R.id.edtPassAgainRegis);
        edtPassword  = (EditText) findViewById(R.id.edtPassRegis);
        btnDangKy    = (Button) findViewById(R.id.btnDangKy);
        btnNhapLai   = (Button) findViewById(R.id.btnClearRegis);
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
                    .appendQueryParameter("email",edtEmail.getText().toString())
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
