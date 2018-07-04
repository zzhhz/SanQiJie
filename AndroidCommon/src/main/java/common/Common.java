package common;

import android.app.Application;
import android.content.Context;


import common.Utils.BroadcastUtils;

/**
 * Created by Beidouht on 2017/7/6.
 */

public class Common extends Application {

    protected static Application application;
    public static boolean sAppDebug = true;
    public static boolean sGlobalCatchError = true;
    public static boolean sReportCatchError = false;
    public static Context sApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        sApplicationContext = application.getApplicationContext();
        init();
    }

    public static Application getApplication() {
        return application;
    }

    public static void init(){
        BroadcastUtils.init(sApplicationContext);
    }
}
