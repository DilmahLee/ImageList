package com.example.lee.imagelist;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
/**
 * Created by Lee on 2015/4/12.
 */
public class CreateTask extends Activity {
    final static int KEY_CHAIN = 333;
    final static int KEY_HOLE = 333;
    private EditText TaskName;
    private Button create;
    private Button cancel;
    protected Uri PicUri;
    String filename;
    ScreenCapture capture = new ScreenCapture();
    MainActivity main = new MainActivity();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createview);

        TaskName = (EditText)findViewById(R.id.editText);
        create = (Button)findViewById(R.id.Createbutton);
        cancel = (Button)findViewById(R.id.CancelButton);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture.shoot(CreateTask.this,TaskName.getText().toString());
                //main.PicUri = Uri.parse("file://" + capture.filename);

                //main.show();
                //main.BindData();
                Intent intent = new Intent();
                intent.putExtra("URI","file://"+capture.filename);
                setResult(KEY_HOLE,intent);
                CreateTask.this.finish();




            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CreateTask.this,MainActivity.class);
                startActivity(intent);
                CreateTask.this.finish();
            }
        });


    }
}
