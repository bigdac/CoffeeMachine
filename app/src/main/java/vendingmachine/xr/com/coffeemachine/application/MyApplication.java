package vendingmachine.xr.com.coffeemachine.application;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.util.ArrayList;
import java.util.List;

import vendingmachine.xr.com.coffeemachine.utils.CrashHandler;

/**
 * Created by Norton on 2017/12/2.
 *
 */

public class    MyApplication extends Application {
    public static int sendByte = 0;//发送出去的字节数
    public static int receiveByte = 0;//接收到的字节数
    private static Context mContext;
    private List<Activity> activities;
    private List<Fragment> fragments;

    @Override
    public void onCreate() {
        super.onCreate();
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
        mContext = getApplicationContext();
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "= 5b7409e1");
        activities=new ArrayList<>();
        fragments=new ArrayList<>();

    }
    public static Context getContext(){
        return mContext;

    }

    public void addActivity(Activity activity){
        if (!activities.contains(activity)){
            activities.add(activity);
        }
    }
    public void addFragment(Fragment fragment){
        if (!fragments.contains(fragment)){
            fragments.add(fragment);
        }
    }

    public List<Fragment> getFragments() {
        return fragments;
    }
    public void removeFragment(Fragment fragment){
        if (fragments.contains(fragment)){
            fragments.remove(fragment);
        }
    }
    public void removeAllFragment(){
        fragments.clear();
    }

    public void removeActivity(Activity activity){
        if (activities.contains(activity)){
            activities.remove(activity);
            activity.finish();
        }
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void removeAllActivity(){
        for (Activity activity:activities){
            activity.finish();
        }
    }
}
