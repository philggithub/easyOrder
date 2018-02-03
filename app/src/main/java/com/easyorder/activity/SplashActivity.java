package com.easyorder.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.easyorder.easyorder.R;
import com.easyorder.utils.StreamUtil;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SplashActivity extends AppCompatActivity {

    private TextView tv_vision_name;
    private int mLocalversionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_vision_name = findViewById(R.id.tv_version_name);
        //初始化界面
        initUI();
        //初始化数据
        initData();
    }

    private void initData() {
        mLocalversionCode = getLocalversionCode();

        checkVersion();
    }

    private void checkVersion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url=new URL("http://192.168.91.105/update/update.json");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(2000);
                    conn.setReadTimeout(2000);
                    if(conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                        InputStream is = conn.getInputStream();
                        String json= StreamUtil.streamToString(is);
                        Log.i("json", "run: "+json);

                        JSONObject jsonObject = new JSONObject(json);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 获取本地APP版本号
     * @return  null返回异常
     */
    private int getLocalversionCode() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }

    }

    /**
     * 初始化UI
     */
    private void initUI() {
        tv_vision_name.setText(getString(R.string.version_name)+getLocalVersionName());
    }

    /**
     * 取地本地APP版本名称
     * @return  返回null为异常
     */
    private String getLocalVersionName() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
