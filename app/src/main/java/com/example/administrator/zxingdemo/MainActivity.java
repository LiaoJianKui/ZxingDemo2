package com.example.administrator.zxingdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int REQUEST_CODE = 1001;
  private Button btnScan;
    private EditText etInput;
    private Button btnCreate;
    private Button btnCreateWithLogo;
    private ImageView ivImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        requestPermission();
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    1002);
        }

    }

    private void initViews() {
        btnScan= (Button) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        etInput= (EditText) findViewById(R.id.etInput);
        btnCreate= (Button) findViewById(R.id.btnCreate);
        btnCreateWithLogo= (Button) findViewById(R.id.btnCreateWithLogo);
        ivImage= (ImageView) findViewById(R.id.ivImage);
        btnCreate.setOnClickListener(this);
        btnCreateWithLogo.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if(requestCode==REQUEST_CODE){
            //处理扫描结果（在界面上显示）
                if(data!=null){
                    Bundle bundle=data.getExtras();
                    if(bundle==null){
                        return;
                    }
                    if(bundle.getInt(CodeUtils.RESULT_TYPE)==CodeUtils.RESULT_SUCCESS){
                            String result=bundle.getString(CodeUtils.RESULT_STRING);
                        //用默认浏览器打开扫描得到的地址
                        Intent intent =new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url= Uri.parse(result.toString());
                        intent.setData(content_url);
                        startActivity(intent);
                  }else if(bundle.getInt(CodeUtils.RESULT_TYPE)==CodeUtils.RESULT_FAILED){
                        Toast.makeText(this, "解析二维码失败", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnCreate:
                String textContent=etInput.getText().toString();
                if(TextUtils.isEmpty(textContent)){
                    Toast.makeText(this, "您的输入为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                etInput.setText("");
                Bitmap image=CodeUtils.createImage(textContent,400,400,null);
                ivImage.setImageBitmap(image);
                break;
            case R.id.btnCreateWithLogo:
                String textContent2=etInput.getText().toString();
                if(TextUtils.isEmpty(textContent2)){
                    Toast.makeText(this, "您的输入为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                etInput.setText("");
                Bitmap image2=CodeUtils.createImage(textContent2,400,400, BitmapFactory.decodeResource(getResources(),R.drawable.logo));
                ivImage.setImageBitmap(image2);
                break;
            default:
        }
    }
}
