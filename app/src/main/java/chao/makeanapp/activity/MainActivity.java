package chao.makeanapp.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import chao.makeanapp.R;
import chao.makeanapp.bean.UpdateAppBean;
import chao.makeanapp.httputils.OkHttpUtils;
import chao.makeanapp.utils.DialogUtils;
import chao.makeanapp.utils.PackageUtils;

/**
 * Created by Chao on 2017/7/25.
 */

public class MainActivity extends AppCompatActivity {
    String path="https://guaju.github.io/versioninfo.json";
    private String currentVersion;
    private String version;
    private UpdateAppBean.DataBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkUpdate();




    }

    private void checkUpdate() {
        //创建builder对象
        Request.Builder builder = new Request.Builder();
        Request request = builder.get()
                .url(path)
                .build();
        //得到call对象
        Call call = OkHttpUtils.getInstance().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
//                int code = response.code();
//                if (200==code){
//                    Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
//                }
                if (response.isSuccessful()){
                    String json = response.body().string();
                    parseJson(json);
                }


            }
        });

    }

    private void parseJson(String json) {

        if (TextUtils.isEmpty(json)){
            return;
        }
        Gson gson=new Gson();
        UpdateAppBean updateBean = gson.fromJson(json, UpdateAppBean.class);
        if ("200".equals(updateBean.getStatus())){
            data = updateBean.getData();
            version = data.getVersion();
            try {
                currentVersion = PackageUtils.getCurrentVersion(MainActivity.this);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(version)){
                if (!version.equals(currentVersion)){
                    //需要下载app
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogUtils.showUpdateDialog(MainActivity.this,"版本更新", data.getInfo(), data.getAppurl());
                        }
                    });
//                     DownLoader.downLoadAndInstallApk(MainActivity.this,data.getAppurl());
                }
            }

        }

    }
}
