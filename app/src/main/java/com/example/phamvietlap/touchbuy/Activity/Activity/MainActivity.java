package com.example.phamvietlap.touchbuy.Activity.Activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.example.phamvietlap.touchbuy.Activity.Adapter.Viewpager;
import com.example.phamvietlap.touchbuy.Activity.HomePage.Page_cart;
import com.example.phamvietlap.touchbuy.Activity.HomePage.Page_home;
import com.example.phamvietlap.touchbuy.Activity.HomePage.Page_new;
import com.example.phamvietlap.touchbuy.Activity.HomePage.Page_news;
import com.example.phamvietlap.touchbuy.Activity.Model.Sqlite;
import com.example.phamvietlap.touchbuy.R;

public class MainActivity extends AppCompatActivity  {
    DrawerLayout    drawerLayout;
    NavigationView  navigationView;
    Toolbar         toolbar;
    TabLayout       tabLayout;
    ViewPager       viewPager;
    public static Sqlite          db;
    public static Sqlite          numcart;
    private int[] tabIcons = {
            R.drawable.home
            ,R.drawable.spmoi
            ,R.drawable.carts
            ,R.drawable.news
    };
    private String[]  txtname={"HOME","NEW","CART","NEWS"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhxa();
        setNavigationView();
        setTabLayout();
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

    }
    private void Create_Cart(){
        db.TruyVanDuLieu("CREATE TABLE IF NOT EXISTS Cart(id INTEGER PRIMARY KEY AUTOINCREMENT,Email VARCHAR,Ma_sp INTEGER,So_luong INTEGER,Gia_sp INTEGER,Ten_sp VARCHAR,Hinh_sp VARCHAR)");
    }

    @Override
    protected void onStart() {
        Create_Cart();
        super.onStart();
    }

    @Override
    protected void onResume() {
        Create_Cart();
        loadpage();
        super.onResume();
    }

    private void setTabLayout(){
        Viewpager adapter_viewpager = new Viewpager(getSupportFragmentManager());
        adapter_viewpager.addFragment(new Page_home(),"HOME");
        adapter_viewpager.addFragment(new Page_new(),"NEW");
        adapter_viewpager.addFragment(new Page_cart(),"CART");
        adapter_viewpager.addFragment(new Page_news(),"NEWS");
        viewPager.setAdapter(adapter_viewpager);
        tabLayout.setupWithViewPager(viewPager);
        for (int i=0;i<4;i++) tabLayout.getTabAt(i).setText(txtname[i]);
        for (int i=0;i<4;i++) tabLayout.getTabAt(i).setIcon(tabIcons[i]);
    }
    private void setNavigationView(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TouchBuy");

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        navigationView.setItemIconTintList(null);
        toolbar.setNavigationIcon(R.drawable.ic_list_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_noibat:
                        Intent intent= new Intent(MainActivity.this,Noibat.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_gift:
                        intent= new Intent(MainActivity.this,Khuyen_mai.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_phone:
                        intent= new Intent(MainActivity.this,Danh_muc_san_pham.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_phukien:
                        intent= new Intent(MainActivity.this,Phu_kien.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_canhan:
                        intent = new Intent(MainActivity.this,Login.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_dienthoai:
                        String phone = "+982027974";
                        intent = new Intent(Intent.ACTION_DIAL,Uri.fromParts("tel", phone, null));
                        startActivity(intent);
                        break;
                    case R.id.nav_maps:

                        break;
                    case R.id.nav_baoloi:

                        break;
                    case R.id.nav_baohanh:

                        break;
                    case R.id.nav_exit:
                        AlertDialog.Builder arlertBuilder=new AlertDialog.Builder(MainActivity.this);
                        arlertBuilder.setTitle("Xác nhận");
                        arlertBuilder.setIcon(R.mipmap.ic_launcher_exit);
                        arlertBuilder.setMessage("Bạn có muốn tắt ứng dung?").setCancelable(false)
                                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent=new Intent(MainActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("EXIT",true);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog=arlertBuilder.create();
                        alertDialog.show();
                          break;
                }

                return false;
            }
        });

    }
    private void loadpage(){
        Bundle extras = getIntent().getExtras();
        int position = 0;
        if(extras!=null)
            position = extras.getInt("Page");
            viewPager.setCurrentItem(position);

    }
    private void anhxa(){
        drawerLayout=(DrawerLayout)findViewById(R.id.MyDrawerlayout);
        navigationView=(NavigationView)findViewById(R.id.myNavi_view);
        toolbar=(Toolbar)findViewById(R.id.nav_toolbar);
        viewPager = (ViewPager)findViewById(R.id.viewPage);
        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        db =new Sqlite(this,"Quan_ly_cart",null,1);
    }


}
