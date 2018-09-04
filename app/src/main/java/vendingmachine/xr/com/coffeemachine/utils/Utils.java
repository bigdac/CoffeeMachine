package vendingmachine.xr.com.coffeemachine.utils;

import android.content.Context;
import android.widget.Toast;


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

}


