package com.example.lee.imagelist;

import static android.provider.BaseColumns._ID; //資料庫都會有唯一的id
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
/**
 * Created by Lee on 2015/3/21.
 */
public class DBHelper extends SQLiteOpenHelper {
    //宣告公用常數(final)
    public static final String TABLE_NAME = "task";//表格名稱
    public static final String URI = "uri";
    //public static final String TEL ="tel";
    public static final String FILENAME ="filename";

    private final static String DATABASE_NAME = "list.db"; //資料庫名稱
    private final static int DATABASE_VERSION  = 1; //資料庫版本
    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    //建立table,有 NAME,TEL,EMAIL三個欄位
    public void onCreate(SQLiteDatabase db){
        final String INIT_TABLE = "CREATE TABLE " + TABLE_NAME + "("+ _ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+ URI + " CHAR, "+ FILENAME +" CHAR);";
        db.execSQL(INIT_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        final String DROP_TABLE = "DROP TABLE IF EXISTS" + TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
