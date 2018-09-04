package vendingmachine.xr.com.coffeemachine.application;

import android.app.Application;
import android.content.Context;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import vendingmachine.xr.com.coffeemachine.utils.CrashHandler;

/**
 * Created by Norton on 2017/12/2.
 *
 */

public class MyApplication extends Application {
    public static int sendByte = 0;//发送出去的字节数
    public static int receiveByte = 0;//接收到的字节数
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
        mContext = getApplicationContext();
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "= 5b7409e1");


    }
    public static Context getContext(){
        return mContext;

    }
}
