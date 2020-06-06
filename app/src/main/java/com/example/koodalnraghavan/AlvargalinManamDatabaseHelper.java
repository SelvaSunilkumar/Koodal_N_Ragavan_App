package com.example.koodalnraghavan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlvargalinManamDatabaseHelper extends SQLiteOpenHelper {

    public AlvargalinManamDatabaseHelper(Context context)
    {
        super(context,"AudioAlwarDatabase.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE AudioAl (name TEXT,url TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS AudioAl");
    }

    public boolean insertAudio(String audioName,String url)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name",audioName);
        values.put("url",url);

        long result = database.insert("AudioAl",null,values);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor fetchSongList()
    {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM AudioAl",null);
        return data;
    }
}
