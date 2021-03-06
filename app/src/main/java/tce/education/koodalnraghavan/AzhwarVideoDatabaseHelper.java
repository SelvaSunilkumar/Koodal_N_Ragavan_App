package tce.education.koodalnraghavan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AzhwarVideoDatabaseHelper extends SQLiteOpenHelper {

    public AzhwarVideoDatabaseHelper(Context context) {
        super(context,"AzhwarVideoDatabase.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE VideoAl (name TEXT,url TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS VideoAl");
    }

    public boolean insertValue(String videoName,String url)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name",videoName);
        values.put("url",url);
        long result = database.insert("VideoAl",null,values);

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor fetchVideoList()
    {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM VideoAl",null);
        return data;
    }
}
