# 引用


### Step 1. Add the JitPack repository to your build file


Add it in your root build.gradle at the end of repositories:

```
allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
```

### Step 2. Add the dependency

	dependencies {
	        compile 'com.github.JiangHaiYang01:XRetrofit:1.0.8'
	}




# 介绍

 封装RxJava + Retrofit2 网络请求框架
 
 
 
 
 # API 介绍
 
 
 
 ## 配置
 
 懒  直接放代码   
 
```
    public XRetrofit seReadTimeout(int readTimeout) {//设置读取超时时间
        this.readTimeout = readTimeout;
        return this;
    }


    public XRetrofit setWriteTimeout(int writeTimeout) {//设置写的超时时间
        this.writeTimeout = writeTimeout;
        return this;
    }

    public XRetrofit setConnectTimeout(int timeDefault) {//设置连接超时时间
        this.connectTimeout = timeDefault;
        return this;
    }
```
 
 
 ## GET请求
 
 #### 第一种 
 
 url 已经拼接好的 不需要用户自己拼接
 
 比如  http://apis.juhe.cn/mobile/get?phone=18856907654&key=5778e9d9cf089fc3b093b162036fc0e1
 
 ```

    public void btnYS(View view) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("自定义的dialog");

        String baseUrl = "http://apis.juhe.cn/"
        String url = baseUrl + "mobile/get?phone=18856907654&key=5778e9d9cf089fc3b093b162036fc0e1";
        XRetrofit.create(this)
                .build(baseUrl)
                .setDialog(progressDialog) // 使用自定义的dialog
              //  .hideDialog() 不显示等待的dialog 就使用这个方法
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
```

#### 第二种

后面的参数 需要移动端自己拼接的  



```
    public void btnQuery(View view) {
        String baseUrl = "http://apis.juhe.cn/"
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
```

> 注意

- baseUrl 必须以 ‘/’ 结束

- url  不能有 '/'


## Post


接口 doPost  

```

    public void btnPath(View view) {
        XRetrofit.create(this)
                .build(baseUrl)
                .hideDialog()
                .doPost(PhoneBean.class, "mobile/get", new OnRetrofit.OnQueryMapListener<PhoneBean>() {
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

```

> 说明

入参
- 第一个参数 : Bean对象
- 第二个参数 : post请求的后缀  比如我这里的 url 地址是 http://apis.juhe.cn/mobile/get/  所以需要加上后缀'mobile/get'  注意  不能以/ 结尾 否则胡出错

## 下载

下载有很多情况需要考虑 下面就一一介绍一下

#### 下载单个文件（小文件）

#####（1）第一种方式

这个就比方说下载一个图片啊  apk版本更新 什么的 

```
    public void btnPostFile(View view) {
  
        XRetrofit.create(this)
                .build(baseUrl)
                .doDownLoad("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3565619450,1776366346&fm=27&gp=0.jpg",
                        "1234",
                        "小文件.jpg",
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

```


> 说明


入参说明

这里使用的是第一个方式
- 第一个参数 url  下载地址
- 第二个参数 downLoadPath 保存文件的文件夹名字  也可以写成  ‘12/12/23’  不要以 '/' 结束
- 第三个参数 fileName 自定义的文件名称

返回接口

- onSuccess（int terms）
    - 现在的进度，当terms == 100 时候  下载完成  一般这里可以放个进度条什么的
- onError(Throwable e)
    - 失败回调
- hasDown(String path）
    - 如果文件已经下载了  就会在这里出现提示  返回已经下载文件的地址
    
（2）第二种方式

```
        XRetrofit.create(this)
                .build(baseUrl)
                .doDownLoad("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3565619450,1776366346&fm=27&gp=0.jpg",
                        "1234",
                        new OnRetrofit.OnDownLoadListener() {}
```

> 说明

这里少了一个 fileName 的入参  默认的名字 就是你下载的名字  比如我这里的名字就是  u=3565619450,1776366346&fm=27&gp=0.jpg

#### 下载多个文件

```
        List<String> urlList = Arrays.asList("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2019270811,1269730008&fm=27&gp=0.jpg",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2701408155,2184514200&fm=27&gp=0.jpg",
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3565619450,1776366346&fm=27&gp=0.jpg");

        XRetrofit.create(this)
                .build(baseUrl)
                .doDownLoad(urlList,
                        "1234",
                        new OnRetrofit.OnDownLoadListener() {}
                           

```


```
  HashMap<String, String> map = new HashMap<>();
        map.put("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2019270811,1269730008&fm=27&gp=0.jpg","1.jpg");
        map.put("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2019270811,1269730008&fm=27&gp=0.jpg","2.jpg");
        XRetrofit.create(this)
                .build(baseUrl)
                .doDownLoad(map,
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

                            @Override
                            public void hasDown(String path) {
                                Logger.e("path---->" + path);
                            }
                        });
```

#### 下载大文件 断点下载

```
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
```

```
    public void btnDownBig(View view) {
        XRetrofit.create(this)
                .build(baseUrl)
                .doDownLoadBig(url,
                        "1234", new OnRetrofit.OnDownLoadListener() {
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
```

暂停下载


```
 XRetrofit.create(this)
                .stopDown(url);
```

这里的url  就是你的下载地址   被我用来当做KEY  要是有人问我为啥   不为啥，懒  哈哈。。。

如果需要继续下载  调用上面的任意一个方法 doDownLoadBig 执行 就可以继续下载了



## 添加请求头

```
 HashMap<String, String> map = new HashMap<>();
        map.put("ip", "192.168.1.108");
        XRetrofit.create(this)
                .addHeard(map)
                .build(baseUrl)
```


## 上传

```
  public void upLoad(View view) {
        XRetrofit.create(this)
                .build(baseUrl)
                .upLoad(PhoneBean.class, "", new OnRetrofit.OnUpLoadListener<PhoneBean>() {
                    @Override
                    public void onFormDataPartMap(Map<String, String> map) {
                        map.put("fikename", "121212.pmg");
                    }

                    @Override
                    public void onFileList(List<File> fileList) {
                        fileList.add(new File("c:/xx/xx/121212/pmg"));
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










































