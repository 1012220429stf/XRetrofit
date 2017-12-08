package com.allens.lib_retrofit;


import android.app.Activity;

import com.allens.lib_retrofit.impl.ApiService;
import com.allens.lib_retrofit.impl.OnRetrofit;
import com.allens.lib_retrofit.observer.MyObserver;
import com.allens.lib_retrofit.util.HttpManager;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by allens on 2017/11/29.
 */

public class XRetrofit {
    private static XRetrofit mInstance;

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


    //添加请求头
    public XRetrofit addHeard(Map<String, String> heardMap) {
        HttpManager.create().addHeard(heardMap);
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
        HttpManager.create()
                .build("http://apis.juhe.cn/")
                .getService(ApiService.class)
                .getGetData(Url)
                .subscribeOn(Schedulers.io())//在子线程取数据
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//在主线程显示ui
                .subscribe(MyObserver.create().getObserver(activity, tClass, listener));
    }


    /**
     * @param activity
     * @param tClass
     * @param baseUrl  如 http://apis.juhe.cn/
     * @param UrlPath  如 bother/call/can
     * @param listener
     * @Author create on 2017/12/8 下午4:37 by Allens
     * @Description
     * @Return void
     */
    public <T> void doPost(Activity activity, final Class<T> tClass, String baseUrl, String UrlPath, final OnRetrofit.OnQueryMapListener<T> listener) {
        HashMap<String, String> map = new HashMap<>();
        listener.onMap(map);
        HttpManager.create()
                .build(baseUrl)
                .getService(ApiService.class)
                .getPostData(UrlPath, map)
                .subscribeOn(Schedulers.io())//在子线程取数据
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())//在主线程显示ui
                .subscribe(MyObserver.create().getObserver(activity, tClass, listener));
    }


//
//    //显示自定义dialog
//    public XRetrofit setDialog(Activity activity, Dialog dialog) {
//        this.activity = activity;
//        WaitDialogUtil.create().setDialog(dialog);
//        isShowDialog = true;
//        return this;
//    }
//
//    //显示自定义dialog
//    public XRetrofit setDialog(Fragment fragment, Dialog dialog) {
//        this.activity = fragment.getActivity();
//        WaitDialogUtil.create().setDialog(dialog);
//        isShowDialog = true;
//        return this;
//    }
//
//
//    public XRetrofit isShowDialog(Activity activity, boolean isShow) {
//        this.activity = activity;
//        isShowDialog = isShow;
//        return this;
//    }
//
//    public XRetrofit isShowDialog(Fragment fragment, boolean isShow) {
//        this.activity = fragment.getActivity();
//        isShowDialog = isShow;
//        return this;
//    }
//


//
//    //文件名 自己定义
//    public void doDownLoad(Context context, String url, String downLoadPath, String FileName, OnRetrofit.OnDownLoadListener listener) {
//        Boolean isAlready = DownLoadUtil.create().isAlreadyDownLoadFromFileName(downLoadPath, FileName);
//        if (isAlready) {
//            listener.hasDown(DownLoadUtil.create().createFile(downLoadPath) + FileName);
//        } else {
//            ApiService apiService = getService(ApiService.class);
//            apiService.downloadFile(url)
//                    .subscribeOn(Schedulers.io())//在子线程取数据
//                    .unsubscribeOn(Schedulers.io())
//                    .subscribe(MyObserver.create().createDownLoadObserver(context, downLoadPath, FileName, listener));
//        }
//    }
//
//    //文件名 是下载时候的文件名
//    public void doDownLoad(Context context, String url, String downLoadPath, OnRetrofit.OnDownLoadListener listener) {
//        Boolean isAlready = DownLoadUtil.create().isAlreadyDownLoadFromUrl(downLoadPath, url);
//        if (isAlready) {
//            listener.hasDown(DownLoadUtil.create().createFile(downLoadPath) + DownLoadUtil.create().getFileName(url));
//        } else {
//            ApiService apiService = getService(ApiService.class);
//            apiService.downloadFile(url)
//                    .subscribeOn(Schedulers.io())//在子线程取数据
//                    .unsubscribeOn(Schedulers.io())
//                    .subscribe(MyObserver.create().createDownLoadObserver(context, downLoadPath, listener, url));
//        }
//    }
//
//    //下载大文件
//    public void doDownLoadBig(Context context, String url, String downLoadPath, String FileName, OnRetrofit.OnDownLoadListener listener) {
//        ApiService apiService = getService(ApiService.class);
//        String newFilePath = DownLoadUtil.create().createFile(downLoadPath) + FileName;
//        DownLoadUtil.create().downLoadBig(apiService, context, url, newFilePath, listener);
//    }
//
//    //下载大文件
//    public void doDownLoadBig(Context context, String url, String downLoadPath, OnRetrofit.OnDownLoadListener listener) {
//        ApiService apiService = getService(ApiService.class);
//        String newFilePath = DownLoadUtil.create().createFile(downLoadPath) + DownLoadUtil.create().getFileName(url);
//        DownLoadUtil.create().downLoadBig(apiService, context, url, newFilePath, listener);
//    }
//
//    public void stopDown(String urlKey) {
//        DownLoadUtil.create().stopDown(urlKey);
//    }
//
//
//    public void doDownLoad(Context context, List<String> urlList, String downLoadPath, OnRetrofit.OnDownLoadListener listener) {
//        for (String url : urlList) {
//            doDownLoad(context, url, downLoadPath, listener);
//        }
//    }
//
//
//    public <T> void upLoad(Class<T> tClass, String url, OnRetrofit.OnUpLoadListener<T> listener) {
//        HashMap<String, String> FormDataPartMap = new HashMap<>();
//        ArrayList<File> fileArrayList = new ArrayList<>();
//        listener.onFormDataPartMap(FormDataPartMap);
//        listener.onFileList(fileArrayList);
//        ApiService apiService = getService(ApiService.class);
//        MultipartBody multipartBody = UpLoadUtil.create()
//                .createMultipartBody(FormDataPartMap, fileArrayList);
//        apiService.upLoad(url, multipartBody)
//                .subscribeOn(Schedulers.io())//在子线程取数据
//                .unsubscribeOn(Schedulers.io())
//                .subscribe(MyObserver.create().createUpLoad(tClass, listener));
//    }
//
//    /**
//     * @param urlMap key url
//     *               value name
//     */
//    public void doDownLoad(Context context, Map<String, String> urlMap, String downLoadPath, OnRetrofit.OnDownLoadListener listener) {
//        for (Map.Entry<String, String> entry : urlMap.entrySet()) {
//            doDownLoad(context, entry.getKey(), downLoadPath, entry.getValue(), listener);
//        }
//    }
//
//

//


}
