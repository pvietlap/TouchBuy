package com.example.phamvietlap.touchbuy.Activity.HomePage;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.phamvietlap.touchbuy.Activity.Activity.Detail_SanPham;
import com.example.phamvietlap.touchbuy.Activity.Adapter.NoiBatAdapter;
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
import java.util.Collections;
import java.util.Random;

import static android.view.View.VISIBLE;

/**
 * Created by phamvietlap on 04/10/2017.
 */

public class Page_new extends Fragment {
    View            view;
    ProgressBar     progressBarload;
    AlphaAnimation  animation;
    ImageButton     btnimgload;
    TextView        txtinternet;
    GridView        gridViewSanPham;
    ArrayList<SanPham>  mangSanPham;
    NoiBatAdapter adapter =null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_page_new,container,false);
        anhxa();
        isInternetOn();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GetPageNew().execute(Config.Link_pagenew);
            }
        });
        gridViewSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent  intent= new Intent(getActivity(), Detail_SanPham.class);
                intent.putExtra("ID",mangSanPham.get(position).MaSanPham);
                startActivity(intent);

            }
        });

        return view;
    }
    private void anhxa(){
        animation =  new AlphaAnimation(1F,0.5F);
        btnimgload=(ImageButton)view.findViewById(R.id.btnimgreload_new);
        txtinternet=(TextView)view.findViewById(R.id.txtinternet_new);
        gridViewSanPham=(GridView)view.findViewById(R.id.gridViewSanPhamPageNew);
        progressBarload=(ProgressBar)view.findViewById(R.id.progresssBar_new);
        mangSanPham =new ArrayList<>();

    }
    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            btnimgload.setVisibility(VISIBLE);
            txtinternet.setVisibility(VISIBLE);
            return false;
        }
        return false;
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
    public class GetPageNew extends AsyncTask<String,Integer,String>{

        @Override
        protected void onPreExecute() {
            progressBarload.setVisibility(view.INVISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return GetSanPham(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray   array= new JSONArray(s);
                for(int i=0;i<array.length();i++){
                    JSONObject  object= array.getJSONObject(i);
                    mangSanPham.add(new SanPham(object.getString("Hinh"), object.getInt("Ma"), object.getString("Ten"), object.getInt("Gia"), object.getString("ChiTiet")));
                }
                Long seed = System.nanoTime();
                Collections.shuffle(mangSanPham,new Random(seed));

                adapter = new NoiBatAdapter(getContext(),R.layout.sanpham_new,mangSanPham);
                gridViewSanPham.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
