package com.example.lee.imagelist;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Scroller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.lee.imagelist.SlideCutListView.RemoveDirection;
import com.example.lee.imagelist.SlideCutListView.RemoveListener;
import com.example.lee.imagelist.ScreenCapture;

import static android.provider.BaseColumns._ID;
import static com.example.lee.imagelist.DBHelper.URI;
import static com.example.lee.imagelist.DBHelper.FILENAME;
import static com.example.lee.imagelist.DBHelper.TABLE_NAME;


public class MainActivity extends ActionBarActivity implements RemoveListener {
    final static int KEY_CHAIN = 333;
    final static int KEY_HOLE = 333;
    protected ArrayList<HashMap<String, String>> list;
    private ScreenCapture capture = new ScreenCapture();
    public Uri PicUri;
    private SlideCutListView slideCutListView ;
    String address;
    //ListView listView;
    File file;
    String content;
    //SQLiteDatabase mDb;
    DBHelper dbHelper = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<HashMap<String, String>>();
        dbHelper = new DBHelper(this);
        dbHelper.close();
            Toast.makeText(this,capture.filename,Toast.LENGTH_SHORT).show();
            //Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show()
            show();
            BindData();


        slideCutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                //int position = pos;
                //Toast.makeText(MainActivity.this,"click"+pos,Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent();
                intent.setClass(MainActivity.this, Activity01.class);//切換頁面
                content = list.get(pos).get("filename");
                intent.putExtra("content", content);
                //intent.putExtra("pos", position); //插入一個字串訊息
                startActivity(intent);*/
                Toast.makeText(MainActivity.this,"Click"+pos,Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 綁定列表資料
     */
    protected void BindData() {
        //listView = (ListView) findViewById(R.id.listView1);
        slideCutListView = (SlideCutListView) findViewById(R.id.slideCutListView);
        slideCutListView.setRemoveListener(this);
        slideCutListView.setAdapter(new MyBaseAdapter(this, list));
        //listView.setAdapter(new MyBaseAdapter(this, list));
    }

    /**
     * 新增圖片
     */
    public void onClick(View view) {
        /*Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        Intent destIntent = Intent.createChooser(intent, "選擇檔案");
        //啟動此函數會呼叫onActivityResult() 方法
        startActivityForResult(destIntent, 0);*/
      /*  capture.shoot(this,"test1");
        PicUri = Uri.parse("file://" + capture.filename);
        setPic();
        show();
        BindData();*/
    //Dialog
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        final View v = inflater.inflate(R.layout.dialog_signin, null);

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("請輸入你的名字")
                .setView(v)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText editText = (EditText) (v.findViewById(R.id.username));
                        //Intent intent = new Intent();
                        //intent.setClass(MainActivity.this, Activity01.class);//切換頁面
                        content = editText.getText().toString();
                        //intent.putExtra("content", content);
                        //intent.putExtra("pos", position); //插入一個字串訊息
                        capture.shoot(MainActivity.this,content);
                        PicUri = Uri.parse("file://" + capture.filename);
                        setPic();
                        show();
                        BindData();
                        //startActivity(intent);
                    }

                })
                .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){

                    }
                })
                .show();
        /*Intent intent = new Intent(MainActivity.this,CreateTask.class);
        //intent.setClass(MainActivity.this,CreateTask.class);
        //startActivity(intent);
        //MainActivity.this.finish();
        startActivityForResult(intent,KEY_CHAIN);
        */
    }
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == KEY_CHAIN){
            if(resultCode == KEY_HOLE){
                PicUri = Uri.parse(data.getExtras().getString("URI"));
                Toast.makeText(this,capture.filename,Toast.LENGTH_SHORT).show();
                setPic();
            }
        }

    }

    protected void setPic() {
        file = new File(capture.filename);

        //File file = new File("/storage/emulated/0/", "test.png");
        try {
            //Bitmap bitmap = getBitmap(PicUri);
            Bitmap bitmap = getBitmap(PicUri);
            OutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {

            System.out.println(e.toString());

        }
        add(PicUri.toString(), capture.filename.toString());

        //Toast.makeText(this,"inserted"+PicUri,Toast.LENGTH_SHORT).show();
        //dao.insert("task",PicUri.toString(),file.toString());
        // 將檔名資料加入列表
        /*HashMap<String, String> item = new HashMap<String, String>();
        item.put("uri", PicUri.toString());
        item.put("filename", file.toString());
        list.add(item);*/
        //BindData();
       // show();

    }

    //新增資料
    protected void add(String uri, String filename) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(URI, uri);
        values.put(FILENAME, filename);
        //values.put(EMAIL,emailText.getText().toString());
        db.insert(TABLE_NAME, null, values);
    }

    //顯示資料
    public void show() {
        list.clear();
        Cursor cursor = getCursor();
        //StringBuilder resultData = new StringBuilder("RESULT: \n");
        int columnsSize = cursor.getColumnCount();
        while (cursor.moveToNext()) {

            HashMap<String, String> map = new HashMap<String, String>();
            for (int i = 0; i < columnsSize; i++) {
                // map.put("id", c.getString(0));
                   /* map.put("username", c.getString(1));
                    map.put("birthday", c.getString(2));
                    map.put("image", c.getString(3));*/
                map.put("uri", cursor.getString(1));
                map.put("filename", cursor.getString(2));
            }
            //listData.add(map);
            list.add(map);
        }

    }
    public void delete(int ID){
        ID = ID + 1;
        String[] whereArgs = new String[] { String.valueOf(ID) };
        //String id = String.valueOf(ID);
        try{
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, "_id=?", whereArgs);
        show();
        }catch (Exception e){
            Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
            Log.i("MainActivity",e.toString());
        }
    }

    public Cursor getCursor() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {_ID, URI, FILENAME};
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
        startManagingCursor(cursor);
        return cursor;
    }


    /** 選擇圖片後回呼函式 */


    /**
     * 取得縮圖
     */
    public Bitmap getBitmap(Uri uri) {
        try {
            InputStream in = getContentResolver().openInputStream(uri);

            // 第一次 decode,只取得圖片長寬,還未載入記憶體
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, opts);
            in.close();

            // 取得動態計算縮圖長寬的 SampleSize (2的平方最佳)
            int sampleSize = computeSampleSize(opts, -1, 512 * 512);

            // 第二次 decode,指定取樣數後,產生縮圖
            in = getContentResolver().openInputStream(uri);
            opts = new BitmapFactory.Options();
            opts.inSampleSize = sampleSize;

            Bitmap bmp = BitmapFactory.decodeStream(in, null, opts);
            in.close();

            return bmp;
        } catch (Exception err) {
            return null;
        }
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {

        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;

        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {

        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }


    /**
     * 移除項目
     */
   /* public void removeItem(int position, String path) {
        list.remove(position);
        //Toast.makeText(MainActivity.this,"Click "+position,Toast.LENGTH_SHORT).show();
        delete(position);
        /*file = new File(path);
        boolean success = file.delete();
        if (success) {
            Toast.makeText(this, "Remove successed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to remove!!", Toast.LENGTH_SHORT).show();
        }

        BindData();
    }*/
    public void removeItem(RemoveDirection direction, int position) {
        //adapter.remove(adapter.getItem(position));
        list.remove(position);
        switch (direction) {
            case RIGHT:
                Toast.makeText(this, "向右删除  "+ position, Toast.LENGTH_SHORT).show();
                break;
            case LEFT:
                Toast.makeText(this, "向左删除  "+ position, Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        BindData();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}