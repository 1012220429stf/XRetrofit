package com.allens.lib_retrofit;


import android.app.Activity;
import android.app.Dialog;

import com.allens.lib_retrofit.impl.ApiService;
import com.allens.lib_retrofit.impl.OnRetrofit;
import com.allens.lib_retrofit.observer.MyObserver;
import com.allens.lib_retrofit.util.DownLoadUtil;
import com.allens.lib_retrofit.util.HttpManager;
import com.allens.lib_retrofit.util.UpLoadUtil;
import com.allens.lib_retrofit.util.XDialogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

/**
 * Created by allens on 2017/11/29.
 */

public class XRetrofit {
    private static XRetrofit mInstance;
    private ApiService apiService;

    private XRetrofit() {
    }

    public static XRetrofit create() {
        if (mInstance == null) {
            synchronized (XRetrofit.class) {
                mInstance = new XRetrofit();
            }
        }
        return mInstance;
    }


    public XRetrofit build(String BaseUrl) {
        XDialogUtil.create().setShow(true); // 默认显示Dialog
        apiService = HttpManager.create().build(BaseUrl).getService(ApiService.class);
        return this;
    }


    //添加请求头
    public XRetrofit addHeard(Map<String, String> heardMap) {
        HttpManager.create().addHeard(heardMap);
        return this;
    }

    public XRetrofit isShowDialog(boolean isShow) {
        XDialogUtil.create().setShow(isShow);
        return this;
    }


    //显示自定义dialog
    public XRetrofit setDialog(Dialog dialog) {
        XDialogUtil.create().setShow(true);
        XDialogUtil.create().setDialog(dialog);
        return this;
    }

    /**
     * @param activity
     * @param tClass
     * @param Url      当map  中没有参数的时候 需要上传url 如http://apis.juhe.cn/
     *                 反之 http://apis.juhe.cn/mobile/get?phone=18856907654&key=5778e9d9cf089fc3b093b162036fc0e1
     * @param listener
     * @Author create on 2017/12/8 下午4:14 by Allens
     * @Description
     * @Return void
     */
    public <T> void doGet(Activity activity, final Class<T> tClass, String Url, final OnRetrofit.OnQueryMapListener<T> listener) {
        HashMap<String, String> map = new HashMap<>();
        listener.onMap(map);
        if (map.size() > 0) {
            String param = HttpManager.create().prepareParam(map);
            if (param.trim().length() >= 1) {
                Url += "?" + param;
            }
        }
        apiService
                .getGetData(Url)
                .subscribeOn(Schedulers.io())//在子线程取数据
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//在主线程显示ui
                .subscribe(MyObserver.create().getObserver(activity, tClass, listener));
    }


    /**
     * @param activity
     * @param tClass
     * @param UrlPath  如 bother/call/can
     * @param listener
     * @Author create on 2017/12/8 下午4:37 by Allens
     * @Description
     * @Return void
     */
    public <T> void doPost(Activity activity, final Class<T> tClass, String UrlPath, final OnRetrofit.OnQueryMapListener<T> listener) {
        HashMap<String, String> map = new HashMap<>();
        listener.onMap(map);
        apiService
                .getPostData(UrlPath, map)
                .subscribeOn(Schedulers.io())//在子线程取数据
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//在主线程显示ui
                .subscribe(MyObserver.create().getObserver(activity, tClass, listener));
    }


    /**
     * @param url          下载地址
     * @param downLoadPath 下载路径  1234
     * @param FileName     下载的文件名  1234.jpg
     * @param listener
     * @Author create on 2017/12/8 下午5:38 by Allens
     * @Description
     * @Return void
     */
    public void doDownLoad(String url, String downLoadPath, String FileName, OnRetrofit.OnDownLoadListener listener) {
        apiService.downloadFile(url)
                .subscribeOn(Schedulers.io())//在子线程取数据
                .unsubscribeOn(Schedulers.io())
                .subscribe(MyObserver.create().createDownLoadObserver(downLoadPath, FileName, listener));
    }


    /**
     * @param url
     * @param downLoadPath
     * @param listener
     * @Author create on 2017/12/8 下午6:05 by Allens
     * @Description 文件名 是下载时候的文件名
     * @Return void
     */
    public void doDownLoad(String url, String downLoadPath, OnRetrofit.OnDownLoadListener listener) {
        apiService.downloadFile(url)
                .subscribeOn(Schedulers.io())//在子线程取数据
                .unsubscribeOn(Schedulers.io())
                .subscribe(MyObserver.create().createDownLoadObserver(downLoadPath, url, listener));
    }


    /**
     * @param url          下载地址
     * @param downLoadPath 下载路径  1234
     * @param FileName     下载的文件名  1234.jpg
     * @param listener
     * @Author create on 2017/12/8 下午6:37 by Allens
     * @Description 下载大文件
     * @Return void
     */
    public void doDownLoadBig(String url, String downLoadPath, String FileName, OnRetrofit.OnDownLoadListener listener) {
        String newFilePath = DownLoadUtil.create().createFile(downLoadPath) + FileName;
        DownLoadUtil.create().downLoadBig(apiService, url, newFilePath, listener);
    }

    /**
     * @param url          下载地址
     * @param downLoadPath 下载路径  1234
     * @param listener
     * @Author create on 2017/12/11 上午9:40 by Allens
     * @Description
     * @Return void
     */
    public void doDownLoadBig(String url, String downLoadPath, OnRetrofit.OnDownLoadListener listener) {
        String newFilePath = DownLoadUtil.create().createFile(downLoadPath) + DownLoadUtil.create().getFileName(url);
        DownLoadUtil.create().downLoadBig(apiService, url, newFilePath, listener);
    }

    /**
     * @param urlKey
     * @Author create on 2017/12/11 上午9:42 by Allens
     * @Description
     * @Return void
     */
    public void stopDown(String urlKey) {
        DownLoadUtil.create().stopDown(urlKey);
    }


    public <T> void doUpLoad(Class<T> tClass, String url, OnRetrofit.OnUpLoadListener<T> listener) {
        HashMap<String, String> FormDataPartMap = new HashMap<>();
        ArrayList<File> fileArrayList = new ArrayList<>();
        listener.onFormDataPartMap(FormDataPartMap);
        listener.onFileList(fileArrayList);
        MultipartBody multipartBody = UpLoadUtil.create().createMultipartBody(FormDataPartMap, fileArrayList);
        apiService.upLoad(url, multipartBody)
                .subscribeOn(Schedulers.io())//在子线程取数据
                .unsubscribeOn(Schedulers.io())
                .subscribe(MyObserver.create().createUpLoad(tClass, listener));
    }


}
