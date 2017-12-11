package com.allens.lib_retrofit.observer;

import android.app.Activity;
import android.util.Log;

import com.allens.lib_retrofit.impl.OnRetrofit;
import com.allens.lib_retrofit.util.DownLoadUtil;
import com.allens.lib_retrofit.util.XDialogUtil;
import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by allens on 2017/11/29.
 */

public class MyObserver {

    public static MyObserver create() {
        return new MyObserver();
    }

    public Observer<ResponseBody> createDownLoadObserver(
            final String downLoadPath,
            final String fileNameOrUrl,
            final OnRetrofit.OnDownLoadListener listener) {
        return new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                String newFilePath = null;
                String filePath = DownLoadUtil.create().createFile(downLoadPath);
                if (fileNameOrUrl.contains("https://")
                        || fileNameOrUrl.contains("http://")
                        || fileNameOrUrl.contains("ftp://")
                        || fileNameOrUrl.contains("rtsp://")
                        || fileNameOrUrl.contains("mms://")
                        ) {
                    newFilePath = filePath + DownLoadUtil.create().getFileName(fileNameOrUrl);
                } else {
                    newFilePath = filePath + fileNameOrUrl;
                }
                DownLoadUtil.create().downLoad(responseBody, newFilePath, listener);
            }

            @Override
            public void onError(Throwable e) {
                DownLoadUtil.create().handlerFailed(e, listener);
            }

            @Override
            public void onComplete() {
            }
        };
    }


    public <T> Observer<? super ResponseBody> getObserver(final Activity activity, final Class<T> tClass, final OnRetrofit.OnQueryMapListener<T> listener) {
        return new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {
                XDialogUtil.create().show(activity);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    Gson gson = new Gson();
                    T t = gson.fromJson(responseBody.string(), tClass);
                    listener.onSuccess(t);
                } catch (IOException e) {
                    listener.onError(e);
                }
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e);
            }

            @Override
            public void onComplete() {
                XDialogUtil.create().hide();
            }
        };
    }


    public <T> Observer<? super ResponseBody> createUpLoad(final Class<T> tClass, final OnRetrofit.OnUpLoadListener<T> listener) {

        return new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    Gson gson = new Gson();
                    T t = gson.fromJson(responseBody.string(), tClass);
                    listener.onSuccess(t);
                } catch (IOException e) {
                    listener.onError(e);
                }
            }

            @Override
            public void onError(Throwable e) {
                listener.onError(e);
            }

            @Override
            public void onComplete() {
            }
        };
    }
}
