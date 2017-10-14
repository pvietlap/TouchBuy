package com.example.phamvietlap.touchbuy.Activity.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phamvietlap.touchbuy.Activity.Model.Config;
import com.example.phamvietlap.touchbuy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.net.URLConnection;

public class Edit_Profile extends AppCompatActivity {
    EditText txtEmail, txtHoTen, txtSDT, txtDiaChi;
    TextView     txtSoLuong;
    Button      btnhoanthanh;
    public static String      email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);
        AnhXa();
        SharedPreferences sharedPreferences=getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        email=sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getprofile().execute(Config.Link_profile+getEmail());
            }
        });
        btnhoanthanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PUsh_data().execute("http://192.168.56.1/phone/updatecustomer.php");
                finish();
               // Toast.makeText(getApplicationContext(),email+txtDiaChi.getText().toString()+txtSDT.getText().toString()+txtHoTen.getText().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }
    private String getEmail(){
        SharedPreferences sharedPreferences=getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"");
    }
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences=getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        email=sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"");
    }
    private class getprofile extends AsyncTask<String,String,String> {

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
    private class PUsh_data extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            return postData(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
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
                    .appendQueryParameter("email",email)
                    .appendQueryParameter("name",txtHoTen.getText().toString())
                    .appendQueryParameter("phone",txtSDT.getText().toString())
                    .appendQueryParameter("diachi",txtDiaChi.getText().toString());
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
    private void AnhXa(){
        txtEmail    = (EditText) findViewById(R.id.edtEmailKhachHang);
        txtDiaChi   = (EditText) findViewById(R.id.edtDiaChiKH);
        txtSDT      = (EditText) findViewById(R.id.edtSDTKhachHang);
        txtHoTen    = (EditText) findViewById(R.id.edtHoTenKH);
        txtSoLuong  = (TextView) findViewById(R.id.txtSoLuongSP);
        btnhoanthanh=(Button)findViewById(R.id.btnHoanThanh);
    }
}
