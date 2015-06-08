package com.example.lee.imagelist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lee on 2015/4/11.
 */
public class Activity01 extends Activity{
    public Uri PicUri;
    File file;
    TextView title;
    String text;
    MainActivity main;
    ScreenCapture capture;
    int position;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskview);

        title = (TextView)findViewById(R.id.textView);
        Intent intent = this.getIntent();
        //取得傳遞過來的資料
        text = intent.getStringExtra("content");
       // String name = text.substring(text.lastIndexOf("/"));
        //name = name.replaceFirst("/","");
        title.setText(text);



        //title.setText("Title");
    }
    public void onBackPressed(){

        capture.shoot(Activity01.this,text);
        PicUri = Uri.parse("file://" + capture.filename);
        setPic();

    }

    protected void setPic() {
        file = new File(capture.filename);
        PicUri = Uri.parse("file://" + capture.filename);
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
        main.add(PicUri.toString(), capture.filename.toString());

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

    /* 取得縮圖
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

}
