package com.allens.lib_retrofit.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by allens on 2017/11/29.
 */

public interface OnRetrofit {

    interface OnQueryMapListener<T> {

        void onMap(Map<String, String> map);

        void onSuccess(T t);

        void onError(Throwable e);
    }


    interface OnDownLoadListener {

        void onSuccess(int terms);

        void onError(Throwable e);

//        void hasDown(String path);
    }

    interface OnUpLoadListener<T> {

        void onFormDataPartMap(Map<String, String> map);

        void onFileList(List<File> fileList);

        void onSuccess(T t);

        void onError(Throwable e);
    }
}
