package com.allens.lib_retrofit;

import android.content.Context;
import android.os.Handler;

import com.allens.lib_retrofit.impl.OnRetrofit;
import com.allens.lib_retrofit.util.DownLoadUtil;
import com.allens.lib_retrofit.util.WaitDialogUtil;
import com.google.gson.Gson;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

/**
 * Created by allens on 2017/11/29.
 */

public class MyObserver {

    protected static MyObserver create() {
        return new MyObserver();
    }


    protected Observer<? super ResponseBody> createDownLoadObserver(final Context context,
                                                                    final String downLoadPath,
                                                                    final OnRetrofit.OnDownLoadListener listener,
                                                                    final String url) {
        return new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                WaitDialogUtil.create().hide();
                String filePath = DownLoadUtil.create().createFile(downLoadPath);
                String newFilePath = filePath + DownLoadUtil.create().getFileName(url);
                DownLoadUtil.create().downLoad(responseBody, context, newFilePath, listener);
            }

            @Override
            public void onError(Throwable e) {
                WaitDialogUtil.create().hide();
                DownLoadUtil.create().handlerFailed(e, listener);
            }

            @Override
            public void onComplete() {
                WaitDialogUtil.create().hide();
            }
        };
    }


    protected <T> Observer<ResponseBody> createGetObserver(final Class<T> tClass, final OnRetrofit.OnGetListener<T> listener) {
        return new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                WaitDialogUtil.create().hide();
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
                WaitDialogUtil.create().hide();
                listener.onError(e);
            }

            @Override
            public void onComplete() {
                WaitDialogUtil.create().hide();
            }
        };
    }

    protected <T> io.reactivex.Observer<ResponseBody> createQueryMapObserver(final Class<T> tClass,
                                                                             final OnRetrofit.OnQueryMapListener<T> listener) {
        return new io.reactivex.Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                WaitDialogUtil.create().hide();
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
                WaitDialogUtil.create().hide();
                listener.onError(e);
            }

            @Override
            public void onComplete() {
                WaitDialogUtil.create().hide();
            }
        };
    }


    protected Observer<ResponseBody> createDownLoadObserver(final Context context,
                                                            final String downLoadPath, final String fileName,
                                                            final OnRetrofit.OnDownLoadListener listener) {
        return new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                String filePath = DownLoadUtil.create().createFile(downLoadPath);
                String newFilePath = filePath + fileName;
                DownLoadUtil.create().downLoad(responseBody, context, newFilePath, listener);
            }

            @Override
            public void onError(Throwable e) {
                DownLoadUtil.create().handlerFailed(e, listener);
                WaitDialogUtil.create().hide();
            }

            @Override
            public void onComplete() {
                WaitDialogUtil.create().hide();
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
                WaitDialogUtil.create().hide();
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
                WaitDialogUtil.create().hide();
                listener.onError(e);
            }

            @Override
            public void onComplete() {
                WaitDialogUtil.create().hide();
            }
        };
    }
}
