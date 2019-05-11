package vendingmachine.xr.com.coffeemachine.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;


public class Utils {

    public static void showToast(Context context, String content){
        Toast.makeText(context,content, Toast.LENGTH_SHORT).show();
    }
    public static boolean isEmpty(String s){
        boolean flag=false;
        if (s==null || "".equals(s)){
            flag=true;
        }
        return flag;
    }
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
    //添加日志
    public static void addText1(TextView textView, String content) {
        textView.append(content);
        textView.append("\n");
        int offset = textView.getLineCount() * textView.getLineHeight();
        if (offset > textView.getHeight()) {
            textView.scrollTo(0, offset - textView.getHeight());
        }
    }
    //清空日志
    public static void clearText1(TextView mTextView) {
        mTextView.setText("");

    }


    }


