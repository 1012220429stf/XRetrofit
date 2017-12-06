package com.allens.lib_retrofit;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.allens.lib_retrofit.impl.ApiService;
import com.allens.lib_retrofit.impl.OnRetrofit;
import com.allens.lib_retrofit.util.DownLoadUtil;
import com.allens.lib_retrofit.util.UpLoadUtil;
import com.allens.lib_retrofit.util.WaitDialogUtil;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by allens on 2017/11/29.
 */

public class XRetrofit {
    private static XRetrofit mInstance;
    private int connectTimeout = 10;
    private int readTimeout = 10;
    private int writeTimeout = 10;
    private Retrofit retrofit;
    private Activity activity;
    private Boolean isShowDialog;
    private Map<String, String> heardMap;


    public static XRetrofit create() {
        if (mInstance == null) {
            synchronized (XRetrofit.class) {
                mInstance = new XRetrofit();
            }
        }
        return mInstance;
    }

    private XRetrofit() {
    }

    public XRetrofit build(String baseUrl) {
        isShowDialog = false;//默认不显示
        Retrofit.Builder builder = new Retrofit.Builder();
        retrofit = builder
                .client(createOKHttpClient(heardMap))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .baseUrl(baseUrl)
                .build();
        return this;
    }

    private OkHttpClient createOKHttpClient(final Map<String, String> heardMap) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (heardMap != null)
            builder
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            Request.Builder builder1 = request.newBuilder();
                            Request build = null;
                            for (Map.Entry<String, String> entry : heardMap.entrySet()) {
                                build = builder1.addHeader(entry.getKey(), entry.getValue()).build();
                            }
                            return chain.proceed(build);
                        }
                    })
                    .retryOnConnectionFailure(true);//连接失败后是否重新连接
        return builder
                .addInterceptor(createInterceptor()) //添加拦截
                .readTimeout(readTimeout, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)//设置连接超时时间
                .build();
    }

    //创建拦截器
    private HttpLoggingInterceptor createInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(final String message) {
                Log.e("RetrofitLog", "retrofitBack -> " + message); //打印retrofit日志
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }


    /**
     * @ User :  allens
     * @ 创建日期 :  2017/11/29 下午6:29
     * @模块作用 ： api
     * <p>
     * ====================================================================================================================================
     * ====================================================================================================================================
     */


    //添加请求头
    public XRetrofit addHeard(Map<String, String> heardMap) {
        this.heardMap = heardMap;
        return this;
    }

    //显示自定义dialog
    public XRetrofit setDialog(Activity activity, Dialog dialog) {
        this.activity = activity;
        WaitDialogUtil.create().setDialog(dialog);
        isShowDialog = true;
        return this;
    }

    //显示自定义dialog
    public XRetrofit setDialog(Fragment fragment, Dialog dialog) {
        this.activity = fragment.getActivity();
        WaitDialogUtil.create().setDialog(dialog);
        isShowDialog = true;
        return this;
    }


    public XRetrofit isShowDialog(Activity activity, boolean isShow) {
        this.activity = activity;
        isShowDialog = isShow;
        return this;
    }

    public XRetrofit isShowDialog(Fragment fragment, boolean isShow) {
        this.activity = fragment.getActivity();
        isShowDialog = isShow;
        return this;
    }

    //Get 请求后面带参数
    public <T> void doGet(final Class<T> tClass, String url, final OnRetrofit.OnQueryMapListener<T> listener) {
        if (isShowDialog) {
            if (activity != null)
                WaitDialogUtil.create().show(activity);
        }
        HashMap<String, String> map = new HashMap<>();
        listener.onMap(map);
        String param = prepareParam(map);
        if (param.trim().length() >= 1) {
            url += "?" + param;
        }
        Log.e("RetrofitLog", "urlResult---->" + url);
        ApiService apiService = getService(ApiService.class);
        apiService.getGetData(url)
                .subscribeOn(Schedulers.io())//在子线程取数据
                .observeOn(AndroidSchedulers.mainThread())//在主线程显示ui
                .subscribe(MyObserver.create().createQueryMapObserver(tClass, listener));
    }


    //Get 请求 后面不带参数
    public <T> void doGet(final Class<T> tClass, String url, final OnRetrofit.OnGetListener<T> listener) {
        if (isShowDialog) {
            if (activity != null)
                WaitDialogUtil.create().show(activity);
        }
        ApiService apiService = getService(ApiService.class);
        apiService.getGetData(url)
                .subscribeOn(Schedulers.io())//在子线程取数据
                .observeOn(AndroidSchedulers.mainThread())//在主线程显示ui
                .subscribe(MyObserver.create().createGetObserver(tClass, listener));
    }

    /**
     * @param UrlPath baseUrl 后面更上的url地址
     */
    public <T> void doPost(final Class<T> tClass, String UrlPath, final OnRetrofit.OnQueryMapListener<T> listener) {
        if (isShowDialog) {
            if (activity != null)
                WaitDialogUtil.create().show(activity);
        }
        HashMap<String, String> map = new HashMap<>();
        listener.onMap(map);
        ApiService apiService = getService(ApiService.class);
        apiService.getPostData(UrlPath, map)
                .subscribeOn(Schedulers.io())//在子线程取数据
                .observeOn(AndroidSchedulers.mainThread())//在主线程显示ui
                .subscribe(MyObserver.create().createQueryMapObserver(tClass, listener));
    }

    //文件名 自己定义
    public void doDownLoad(Context context, String url, String downLoadPath, String FileName, OnRetrofit.OnDownLoadListener listener) {
        Boolean isAlready = DownLoadUtil.create().isAlreadyDownLoadFromFileName(downLoadPath, FileName);
        if (isAlready) {
            listener.hasDown(DownLoadUtil.create().createFile(downLoadPath) + FileName);
        } else {
            ApiService apiService = getService(ApiService.class);
            apiService.downloadFile(url)
                    .subscribeOn(Schedulers.io())//在子线程取数据
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(MyObserver.create().createDownLoadObserver(context, downLoadPath, FileName, listener));
        }
    }

    //文件名 是下载时候的文件名
    public void doDownLoad(Context context, String url, String downLoadPath, OnRetrofit.OnDownLoadListener listener) {
        Boolean isAlready = DownLoadUtil.create().isAlreadyDownLoadFromUrl(downLoadPath, url);
        if (isAlready) {
            listener.hasDown(DownLoadUtil.create().createFile(downLoadPath) + DownLoadUtil.create().getFileName(url));
        } else {
            ApiService apiService = getService(ApiService.class);
            apiService.downloadFile(url)
                    .subscribeOn(Schedulers.io())//在子线程取数据
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(MyObserver.create().createDownLoadObserver(context, downLoadPath, listener, url));
        }
    }

    //下载大文件
    public void doDownLoadBig(Context context, String url, String downLoadPath, String FileName, OnRetrofit.OnDownLoadListener listener) {
        ApiService apiService = getService(ApiService.class);
        String newFilePath = DownLoadUtil.create().createFile(downLoadPath) + FileName;
        DownLoadUtil.create().downLoadBig(apiService, context, url, newFilePath, listener);
    }

    //下载大文件
    public void doDownLoadBig(Context context, String url, String downLoadPath, OnRetrofit.OnDownLoadListener listener) {
        ApiService apiService = getService(ApiService.class);
        String newFilePath = DownLoadUtil.create().createFile(downLoadPath) + DownLoadUtil.create().getFileName(url);
        DownLoadUtil.create().downLoadBig(apiService, context, url, newFilePath, listener);
    }

    public void stopDown(String urlKey) {
        DownLoadUtil.create().stopDown(urlKey);
    }


    public void doDownLoad(Context context, List<String> urlList, String downLoadPath, OnRetrofit.OnDownLoadListener listener) {
        for (String url : urlList) {
            doDownLoad(context, url, downLoadPath, listener);
        }
    }


    public <T> void upLoad(Class<T> tClass, String url, OnRetrofit.OnUpLoadListener<T> listener) {
        HashMap<String, String> FormDataPartMap = new HashMap<>();
        ArrayList<File> fileArrayList = new ArrayList<>();
        listener.onFormDataPartMap(FormDataPartMap);
        listener.onFileList(fileArrayList);
        ApiService apiService = getService(ApiService.class);
        MultipartBody multipartBody = UpLoadUtil.create()
                .createMultipartBody(FormDataPartMap, fileArrayList);
        apiService.upLoad(url, multipartBody)
                .subscribeOn(Schedulers.io())//在子线程取数据
                .unsubscribeOn(Schedulers.io())
                .subscribe(MyObserver.create().createUpLoad(tClass, listener));
    }

    /**
     * @param urlMap key url
     *               value name
     */
    public void doDownLoad(Context context, Map<String, String> urlMap, String downLoadPath, OnRetrofit.OnDownLoadListener listener) {
        for (Map.Entry<String, String> entry : urlMap.entrySet()) {
            doDownLoad(context, entry.getKey(), downLoadPath, entry.getValue(), listener);
        }
    }


    public XRetrofit seReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }


    public XRetrofit setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public XRetrofit setConnectTimeout(int timeDefault) {
        this.connectTimeout = timeDefault;
        return this;
    }


    private <T> T getService(Class<T> tClass) {
        return retrofit.create(tClass);
    }

    private static String prepareParam(Map<String, String> paramMap) {
        StringBuilder sb = new StringBuilder();
        if (paramMap.isEmpty()) {
            return "";
        } else {
            for (String key : paramMap.keySet()) {
                String value = paramMap.get(key);
                if (sb.length() < 1) {
                    sb.append(key).append("=").append(value);
                } else {
                    sb.append("&").append(key).append("=").append(value);
                }
            }
            return sb.toString();
        }
    }

}
