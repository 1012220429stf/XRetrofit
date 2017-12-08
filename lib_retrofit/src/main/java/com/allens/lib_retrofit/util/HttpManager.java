package com.allens.lib_retrofit.util;

import com.allens.lib_retrofit.XRetrofitApp;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by allens on 2017/12/8.
 */

public class HttpManager {

    private static HttpManager mInstance;
    private Retrofit retrofit;
    private Map<String, String> headMap;

    private HttpManager() {
    }

    public static HttpManager create() {
        if (mInstance == null) {
            synchronized (HttpManager.class) {
                mInstance = new HttpManager();
            }
        }
        return mInstance;
    }


    public HttpManager build(String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        retrofit = builder
                .client(createOKHttpClient())
//                .addConverterFactory(GsonConverterFactory.create()) //Gson解析器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .baseUrl(baseUrl)
                .build();
        return this;
    }

    private OkHttpClient createOKHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (XRetrofitApp.isDebug())
            builder.addInterceptor(InterceptorUtil.LogInterceptor());//添加拦截
        if (headMap != null && headMap.size() > 0)
            builder.addInterceptor(InterceptorUtil.HeaderInterceptor(headMap));

        return builder
                .readTimeout(XRetrofitApp.getReadTimeout(), TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(XRetrofitApp.getWriteTimeout(), TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(XRetrofitApp.getConnectTimeout(), TimeUnit.SECONDS)//设置连接超时时间
//                .retryOnConnectionFailure(true)//连接失败后是否重新连接
                .build();
    }

    //拼接URL

    public String prepareParam(Map<String, String> paramMap) {
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


    //获取接口类
    public <T> T getService(Class<T> tClass) {
        return retrofit.create(tClass);
    }

    public void addHeard(Map<String, String> heardMap) {
        this.headMap = heardMap;
    }
}
