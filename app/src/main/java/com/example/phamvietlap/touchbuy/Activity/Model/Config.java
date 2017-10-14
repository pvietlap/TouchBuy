package com.example.phamvietlap.touchbuy.Activity.Model;

/**
 * Created by phamvietlap on 09/10/2017.
 */

public class Config {

    public static  final String  s="http://touchbuy123.atspace.cc";
  //  public static  final String  s="http://192.168.56.1/phone";
    public static final String Link_login= s+"/login.php";
    public static final String Link_pageHome=s+"/getsanphampage1.php";
    public static final String Link_pagenew=s+"/getspmoi.php";

    public static final String Link_profile=s+"/getcustomer.php?Mail=";
    public static final String Link_product=s+"/getdetailsp.php?Id=";
    public static final String Link_Thanhtoan1=s+"/dathang.php";
    public static final String Link_Thanhtoan2=s+"/chitietdonhang.php";
    public static final String Link_getId=s+"/getid.php?email=";
    public static final String Link_getspnoibat=s+"/getspnoibat.php";
    public static final String Link_getspkhuyenmai=s+"/getspkhuyenmai.php";
    public static final String Link_getdanhmuc=s+"/getphanloai.php";
    public static final String Link_getphone=s+"/getiphone.php?MaLoai=";
    public static final String Link_getphukien=s+"/getphukien.php";
    public static final String Link_getnews="https://vnexpress.net/rss/so-hoa.rss";
    public static final String Link_num_cart=s+"/getnumcart.php?Id=";

    public static final String SHARED_PREF_NAME = "myloginapp";
    public static final String Link_singup="public static final String Link_singup";
    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "email";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    public static final String ID_SHARRED_PREF="ID";
}
