package com.example.phamvietlap.touchbuy.Activity.Model;

/**
 * Created by phamvietlap on 07/10/2017.
 */

public class SanPham {
    public int          MaSanPham;
    public String       TenSanPham;
    public int          GiaSanPham;
    public String       ChiTiet;
    public String       HinhSanPham;

    public SanPham(){
    }
    public SanPham(String hinhSanPham,int maSanPham, String tenSanPham, int giaSanPham, String chiTiet) {
        MaSanPham = maSanPham;
        TenSanPham = tenSanPham;
        GiaSanPham = giaSanPham;
        ChiTiet = chiTiet;
        HinhSanPham = hinhSanPham;
    }
}
