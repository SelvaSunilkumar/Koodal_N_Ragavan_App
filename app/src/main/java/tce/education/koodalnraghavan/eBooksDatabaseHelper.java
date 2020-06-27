package tce.education.koodalnraghavan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class eBooksDatabaseHelper extends SQLiteOpenHelper {

    public eBooksDatabaseHelper(Context context)
    {
        super(context,"DownloadsDatabase.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE EBooksData (bookname TEXT,url TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE EBooksData");
    }

    public boolean insertBook(String bookName,String url)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("bookname",bookName);
        values.put("url",url);

        long result = db.insert("EBooksData",null,values);

        if(result == -1)
        {
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor fetchBooks()
    {
        SQLiteDatabase ds = this.getWritableDatabase();
        Cursor data = ds.rawQuery("SELECT * FROM EBooksData",null);
        return data;
    }
}
