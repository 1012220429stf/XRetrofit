import android.app.Application;

import com.allens.lib_retrofit.XRetrofitApp;

/**
 * Created by allens on 2017/12/11.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        XRetrofitApp.init(this,true);
        XRetrofitApp.setConnectTimeout(10);
        XRetrofitApp.setReadTimeout(10);
        XRetrofitApp.setWriteTimeout(10);
        XRetrofitApp.setLogTag("LOGTAG");
    }
}
