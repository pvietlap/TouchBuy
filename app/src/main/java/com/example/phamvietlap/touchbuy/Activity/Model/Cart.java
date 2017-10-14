package com.example.phamvietlap.touchbuy.Activity.Model;

/**
 * Created by phamvietlap on 11/10/2017.
 */

public class Cart {
    public int      Ma_sp;
    public String   TenSp;
    public int      So_luong;

    public Cart(int ma_sp, String tenSp, int so_luong) {
        Ma_sp = ma_sp;
        TenSp = tenSp;
        So_luong = so_luong;
    }
}
