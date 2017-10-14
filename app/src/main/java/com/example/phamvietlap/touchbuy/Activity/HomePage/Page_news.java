package com.example.phamvietlap.touchbuy.Activity.HomePage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.phamvietlap.touchbuy.Activity.Activity.Read_news;
import com.example.phamvietlap.touchbuy.Activity.Model.Config;
import com.example.phamvietlap.touchbuy.Activity.Model.XMLDOMParser;
import com.example.phamvietlap.touchbuy.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by phamvietlap on 04/10/2017.
 */

public class Page_news extends Fragment {
    View view;
    Toolbar toolbar;
    ListView    listView;
    ArrayAdapter adapter;
    ArrayList<String>   Title,Link;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_page_news,container,false);
        Anhxa();
        toolbar();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Get_link().execute(Config.Link_getnews);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), Read_news.class);
                intent.putExtra("link",Link.get(position));
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Get_link().execute(Config.Link_getnews);
            }
        });
        super.onResume();
    }

    private class Get_link extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            return GetSanPham(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            XMLDOMParser parser = new XMLDOMParser();
            if(s!=null && s.trim().length()>0){
                Document document=parser.getDocument(s);
                NodeList nodeList= document.getElementsByTagName("item");
                String title="";
                for(int i=0;i<nodeList.getLength();i++){
                    Element element= (Element) nodeList.item(i);
                    title=parser.getValue(element,"title")+"\n";
                    Title.add(title);
                    Link.add(parser.getValue(element,"link")+"\n");
                }
                    adapter= new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,Title);
                    listView.setAdapter(adapter);
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
        toolbar = (Toolbar) view.findViewById(R.id.nav_toolbar_detail);
    }
    private void Anhxa(){
        listView=(ListView)view.findViewById(R.id.lvTinTuc);
        Title   = new ArrayList<String>();
        Link    = new ArrayList<String>();
    }
}
