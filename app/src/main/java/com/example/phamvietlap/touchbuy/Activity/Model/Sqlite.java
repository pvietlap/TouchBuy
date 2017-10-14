package com.example.phamvietlap.touchbuy.Activity.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by phamvietlap on 11/10/2017.
 */

public class Sqlite extends SQLiteOpenHelper {
    public Sqlite(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
    }
    public void TruyVanDuLieu(String sql){
        SQLiteDatabase database=getWritableDatabase();
        database.execSQL(sql);
    }
    public Cursor LayDuLieu(String sql){
        SQLiteDatabase database=getWritableDatabase();
        return database.rawQuery(sql,null);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
