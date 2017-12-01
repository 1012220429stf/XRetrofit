package com.allens.xretrofit;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.allens.lib_retrofit.impl.OnRetrofit;
import com.allens.lib_retrofit.XRetrofit;
import com.allens.lib_retrofit.util.DownLoadUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String baseUrl = "http://apis.juhe.cn/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }


    //get请求
    public void btnYS(View view) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("自定义的dialog");
        String url = baseUrl + "mobile/get?phone=18856907654&key=5778e9d9cf089fc3b093b162036fc0e1";
        XRetrofit.create(this)
                .build(baseUrl)
                .setDialog(progressDialog)
                .doGet(PhoneBean.class, url, new OnRetrofit.OnGetListener<PhoneBean>() {
                    @Override
                    public void onSuccess(PhoneBean phoneBean) {
                        Logger.e("phoneBean---->" + phoneBean.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("e---->" + e.getMessage());
                    }
                });
    }

    //get请求
    public void btnQuery(View view) {
        String url = baseUrl + "mobile/get";
        XRetrofit.create(MainActivity.this)
                .build(baseUrl)
                .doGet(PhoneBean.class, url, new OnRetrofit.OnQueryMapListener<PhoneBean>() {
                    @Override
                    public void onMap(Map<String, String> map) {
                        map.put("phone", "18856907654");
                        map.put("key", "5778e9d9cf089fc3b093b162036fc0e1");
                    }

                    @Override
                    public void onSuccess(PhoneBean phoneBean) {
                        Logger.e("phoneBean---->" + phoneBean.toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    //post 请求
    public void btnPath(View view) {
        HashMap<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json");
        map.put("Authorization", "jwt eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJVaWQiOiIxMjMxMjMxMjMiLCJBY2NvdW50IjoiYWRtaW4ifQ.rJmKVPGm781oMWUrU2HGnTSE7q4JQ63iQ8e5XpAzDi8");
        XRetrofit.create(this)
                .addHeard(map)
                .build("http://192.168.1.121/")
                .hideDialog()
                .doPost(PhoneBean.class, "bother/call/can", new OnRetrofit.OnQueryMapListener<PhoneBean>() {
                    @Override
                    public void onMap(Map<String, String> map) {
                        map.put("account", "admin");
                        map.put("password", "123");
                        map.put("deviceId", "3");
                    }

                    @Override
                    public void onSuccess(PhoneBean phoneBean) {
                        Logger.e("phoneBean---->" + phoneBean.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("e---->" + e.getMessage());
                    }
                });

    }


    // 下载文件
    public void btnPostFile(View view) {
        List<String> urlList = Arrays.asList("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2019270811,1269730008&fm=27&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2701408155,2184514200&fm=27&gp=0.jpg",
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3565619450,1776366346&fm=27&gp=0.jpg");

        HashMap<String, String> map = new HashMap<>();
        map.put("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2019270811,1269730008&fm=27&gp=0.jpg", "1.jpg");
        map.put("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2019270811,1269730008&fm=27&gp=0.jpg", "2.jpg");
        XRetrofit.create(this)
                .build(baseUrl)
                .doDownLoad("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2019270811,1269730008&fm=27&gp=0.jpg",
                        "1234",
                        "1.jpg",
                        new OnRetrofit.OnDownLoadListener() {
                            @Override
                            public void onSuccess(int terms) {
                                Log.e("TAG", "TERMS---->" + terms);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e("e----->" + e.getMessage());
                            }

                            @Override
                            public void hasDown(String path) {
                                Logger.e("path---->" + path);
                            }
                        });
    }

    private String url = "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3565619450,1776366346&fm=27&gp=0.jpg";

    private String imgUrl = "http://sw.bos.baidu.com/sw-search-sp/software/ca7bd0d4676bc/xmind-8-3.7.5-macosx.dmg";

    //下载大文件
    public void btnDownBig(View view) {
        XRetrofit.create(this)
                .build(baseUrl)
                .doDownLoadBig(url,
                        "1234", "大文件.jpg", new OnRetrofit.OnDownLoadListener() {
                            @Override
                            public void onSuccess(int terms) {
                                Log.e("TAG", "TERMS---->" + terms);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e("e----->" + e.getMessage());
                            }

                            @Override
                            public void hasDown(String path) {
                                Logger.e("path---->" + path);
                            }
                        });
    }

    // 暂定下载
    public void btnDownBigStop(View view) {
        XRetrofit.create(this)
                .stopDown(url);

    }

    //添加头部信息
    public void addHeard(View view) {
        HashMap<String, String> map = new HashMap<>();
        map.put("ip", "192.168.1.108");
        XRetrofit.create(this)
                .addHeard(map)
                .build(baseUrl)
                .doDownLoadBig(url,
                        "1234", "大文件.jpg", new OnRetrofit.OnDownLoadListener() {
                            @Override
                            public void onSuccess(int terms) {
                                Log.e("TAG", "TERMS---->" + terms);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e("e----->" + e.getMessage());
                            }

                            @Override
                            public void hasDown(String path) {
                                Logger.e("path---->" + path);
                            }
                        });
    }


    public void upLoad(View view) {
        XRetrofit.create(this)
                .build(baseUrl)
                .upLoad(PhoneBean.class, "http://192.168.1.121/log/file/upload", new OnRetrofit.OnUpLoadListener<PhoneBean>() {
                    @Override
                    public void onFormDataPartMap(Map<String, String> map) {
                        map.put("Data", "2017-11-30");
                        map.put("Site", "ZhuZou");
                        map.put("User", "test");
                        map.put("Name", "gps");
                    }

                    @Override
                    public void onFileList(List<File> fileList) {
                        String path = DownLoadUtil.create().createFile("1234") + "1.jpg";
                        Logger.e("filepath---====--<" + path);
                        File file = new File(path);
                        fileList.add(file);
                    }

                    @Override
                    public void onSuccess(PhoneBean phoneBean) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }


}

