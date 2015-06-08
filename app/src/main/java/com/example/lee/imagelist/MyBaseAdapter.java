package com.example.lee.imagelist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lee on 2015/3/26.
 */
public class MyBaseAdapter extends BaseAdapter {

    private MainActivity mainActivity;
    private LayoutInflater myInflater;
    private ArrayList<HashMap<String, String>> list = null;
    private ViewTag viewTag;
    private String fileName;
    public MyBaseAdapter(MainActivity context, ArrayList<HashMap<String, String>> list) {
        //取得 MainActivity 實體
        this.mainActivity = context;
        this.myInflater = LayoutInflater.from(context);
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            // 取得listItem容器 view
            convertView = myInflater.inflate(R.layout.mylistview, null);

            // 建構listItem內容view
            viewTag = new ViewTag(
                    (ImageView) convertView.findViewById(R.id.imageView1),
                    (Button) convertView.findViewById(R.id.button1),
                    (TextView) convertView.findViewById(R.id.textView1)
            );

            // 設置容器內容
            convertView.setTag(viewTag);

        } else {
            viewTag = (ViewTag) convertView.getTag();
        }

        //取得縮圖檔名，設定 ImageView
        fileName = list.get(position).get("filename");
        Bitmap bm = BitmapFactory.decodeFile(fileName);
        viewTag.iv1.setImageBitmap(bm);
        //
        String name = fileName.substring(fileName.lastIndexOf("/"));
        name = name.replaceFirst("/","");
        viewTag.txv1.setText(name);
        //
        //設定按鈕監聽事件及傳入 MainActivity 實體
       // viewTag.btn1.setOnClickListener(new ItemButton_Click(this.mainActivity, position));

        return convertView;
    }

    public class ViewTag {
        ImageView iv1;
        TextView btn1;
        TextView txv1;

        public ViewTag(ImageView imageView1, Button button1,TextView textview) {
            this.iv1 = imageView1;
            this.btn1 = button1;
            this.txv1 = textview;
        }
    }

    //自訂按鈕監聽事件
    /*class ItemButton_Click implements View.OnClickListener {
        private int position;
        private MainActivity mainActivity;

        ItemButton_Click(MainActivity context, int pos) {
            this.mainActivity = context;
            position = pos;

        }

        public void onClick(View v) {
            String path = this.mainActivity.list.get(position).get("filename");
            this.mainActivity.removeItem(position,path);

            /*boolean success = this.mainActivity.list.get(position).get("filename").delete();
            if(success){
                Toast.makeText(mainActivity,"Remove successed!",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(mainActivity,"Failed to remove!!",Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(this.mainActivity,"position is"+position,Toast.LENGTH_SHORT).show();
        }
    }*/

}
