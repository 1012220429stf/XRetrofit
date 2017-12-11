# 引用项目

### 在application中的build.gradle中根节点添加地址


```
allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
```

### 在module中build.gradle添加

	dependencies {
	       compile 'com.github.JiangHaiYang01:XRetrofit:x.y.z'//引入最新版本即可
	}


  
 
 # 使用配置
 
 ### 初始化
 
 ```
 //参数2：debug  true 是debug模式
  XRetrofitApp.init(this,true);
```
 或者
 
 ```
XRetrofitApp.init(this);
```

>推荐放在Application 中初始化
 
 ### API
 

##### 设置参数


可以在Application中 使用一下方法进行配置  
```
 XRetrofitApp.setConnectTimeout(10);//设置连接超时时间 默认10
 XRetrofitApp.setReadTimeout(10);//设置读取超时时间 默认10
 XRetrofitApp.setWriteTimeout(10);//设置写的超时时间 默认10
 XRetrofitApp.setLogTag("XRetrofit");//设置debug模式下的LogTag 默认 XRetrofit
```




##### 是否显示Dialog （ 默认是显示的）

```

  XRetrofit.create()
                .build(baseUrl)
                .isShowDialog(false)
                .doPost()
                .........

```


自定义dialog
```
 ProgressDialog progressDialog = new ProgressDialog(this);
 progressDialog.setMessage("自定义的dialog");
 XRetrofit.create()
           .setDialog(progressDialog)
           .build(baseUrl)
           .......

```

##### GIT 请求


 ```

String baseUrl = "http://apis.juhe.cn/";
       
public void btn_get(View view) {
      String url = baseUrl + "mobile/get?phone=18856901111&key=5778e9d9cf089fc3b093b162036fc0e1";
      XRetrofit.create()
               .build(baseUrl)
               .isShowDialog(false)
               .doGet(MainActivity.this, PhoneBean.class, url, new OnRetrofit.OnQueryMapListener<PhoneBean>() {
                       @Override
                       public void onMap(Map<String, String> map) {
                            //这里的map 是当需要自己手动去拼接URL 时候使用  
                            //如下例子所示
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



 
 //==============================================分割线========================

String baseUrl = "http://apis.juhe.cn/";
       
public void btn_get(View view) {
        XRetrofit.create()
               .build(baseUrl)
               .isShowDialog(false)
               .doGet(MainActivity.this, PhoneBean.class, baseUrl, new OnRetrofit.OnQueryMapListener<PhoneBean>() {
                       @Override
                       public void onMap(Map<String, String> map) {
                            map.put("phone","18856901111");
                            map.put("key","5778e9d9cf089fc3b093b162036fc0e1");
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
```


##### POST请求
 

```

    public void btn_post(View view) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("自定义的dialog");
        XRetrofit.create()
                .setDialog(progressDialog)
                .build(baseUrl)
                .doPost(MainActivity.this, PhoneBean.class, "mobile/get", new OnRetrofit.OnQueryMapListener<PhoneBean>() {
                    @Override
                    public void onMap(Map<String, String> map) {
                        map.put("phone", "18856901111");
                        map.put("key", "5778e9d9cf089fc3b093b162036fc0e1");
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


```

> 入参说明

- 第一个参数 : 当前的Activity
- 第二个参数 : Bean对象
- 第二个参数 : post请求的后缀  比如我这里的 url 地址是 http://apis.juhe.cn/mobile/get/  所以需要加上后缀'mobile/get'  注意  不能以/ 结尾 否则胡出错

##### 下载


下载单个文件（小文件）

```

 // 下载文件  自定义文件名
    public void btn_downFileFromName(View view) {
        XRetrofit.create()
                .build(baseUrl)
                .doDownLoad(imgUrl,
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
                        });

    }

    // 下载文件 文件名 根据下载时候服务端的命名
    public void btn_downFileFromUrl(View view) {
        XRetrofit.create()
                .build(baseUrl)
                .doDownLoad(imgUrl, "1234", new OnRetrofit.OnDownLoadListener() {
                    @Override
                    public void onSuccess(int terms) {
                        Log.e("TAG", "TERMS---->" + terms);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("e----->" + e.getMessage());
                    }
                });
    }
```




下载大文件 断点下载

```

    //下载大文件 自己命名名字
    public void btnDownBig(View view) {
        XRetrofit.create()
                .build(baseUrl)
                .doDownLoadBig(imgUrl,
                        "1234",
                        "大文件.jpg",
                        new OnRetrofit.OnDownLoadListener() {
                            @Override
                            public void onSuccess(int terms) {
                                Log.e("TAG", "TERMS---->" + terms);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e("e----->" + e.getMessage());
                            }
                        });
    }




    //下载大文件 服务端返回
    public void btnDownBig(View view) {
        XRetrofit.create()
                .build(baseUrl)
                .doDownLoadBig(imgUrl,
                        "1234",
                        new OnRetrofit.OnDownLoadListener() {
                            @Override
                            public void onSuccess(int terms) {
                                Log.e("TAG", "TERMS---->" + terms);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e("e----->" + e.getMessage());
                            }
                        });
    }

```

暂停下载


```
 XRetrofit.create()
                .stopDown(url);
```

这里的url  就是你的下载地址   被我用来当做KEY  要是有人问我为啥   不为啥，懒  哈哈。。。

如果需要继续下载  调用上面的任意一个方法 doDownLoadBig 执行 就可以继续下载了



##### 添加请求头

```
 HashMap<String, String> map = new HashMap<>();
        map.put("ip", "192.168.1.108");
        XRetrofit.create()
                .addHeard(map)
                .build(baseUrl)
                ...
```


##### 上传

```
  public void upLoad(View view) {
         XRetrofit.create()
                 .build(baseUrl)
                 .doUpLoad(PhoneBean.class, "http://192.168.1.121/log/file/upload", new OnRetrofit.OnUpLoadListener<PhoneBean>() {
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


```










































