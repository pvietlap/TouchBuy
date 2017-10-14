package com.example.phamvietlap.touchbuy.Activity.HomePage;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.phamvietlap.touchbuy.Activity.Activity.Detail_SanPham;
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
import java.util.Collections;
import java.util.Random;

import static android.view.View.VISIBLE;

/**
 * Created by phamvietlap on 04/10/2017.
 */

public class Page_home extends Fragment {
    AlphaAnimation  alphaAnimation;
    View            view;
    ImageButton     btnReloadpage1;
    TextView        txtInternet;
    GridView        gridView;
    ArrayList<SanPham>  mangsanpham;
    SanPhamAdapter      adapter=null;
    ProgressBar         load;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_page_home,container,false);
        anhxa();

        final Animation animation= AnimationUtils.loadAnimation(getActivity(),R.anim.zoom_in);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable = load.getIndeterminateDrawable().mutate();
            drawable.setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorAccent), PorterDuff.Mode.SRC_IN);
            load.setIndeterminateDrawable(drawable);
        }
        isInternetOn();
        btnReloadpage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                getActivity().finish();
                getActivity().overridePendingTransition(0,0);
                startActivity(getActivity().getIntent());
                getActivity().overridePendingTransition(0,0);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.startAnimation(animation);
                Intent intent= new Intent(getActivity(), Detail_SanPham.class);
                intent.putExtra("ID",mangsanpham.get(position).MaSanPham);
                getActivity().overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(intent);
            }
        });

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GetPageHome().execute(Config.Link_pageHome);

            }
        });
        return view;
    }
    private void anhxa(){
        alphaAnimation = new AlphaAnimation(1F,0.5F);
        btnReloadpage1=(ImageButton)view.findViewById(R.id.btnReloadPage);
        txtInternet=(TextView)view.findViewById(R.id.txtInternetPage1);
        gridView=(GridView)view.findViewById(R.id.gridViewSanPhamPage1);
        mangsanpham=new ArrayList<>();
        load=(ProgressBar)view.findViewById(R.id.progresssBar);
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
            btnReloadpage1.setVisibility(VISIBLE);
            txtInternet.setVisibility(VISIBLE);
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

    private class GetPageHome extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            load.setVisibility(view.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return GetSanPham(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            load.setVisibility(View.INVISIBLE);
            super.onPostExecute(s);

            try {
                JSONArray array = new JSONArray(s);
                for (int i=0; i<array.length(); i++){
                    JSONObject object = array.getJSONObject(i);
                    long seed = System.nanoTime();
                    mangsanpham.add(new SanPham(object.getString("Hinh"), object.getInt("Ma"), object.getString("Ten"), object.getInt("Gia"), object.getString("ChiTiet")));
                    Collections.shuffle(mangsanpham, new Random(seed));
                }
                adapter = new SanPhamAdapter(getActivity(), R.layout.dong_sanpham,mangsanpham);
                gridView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
