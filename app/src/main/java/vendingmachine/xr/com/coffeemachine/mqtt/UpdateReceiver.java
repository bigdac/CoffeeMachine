package vendingmachine.xr.com.coffeemachine.mqtt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import vendingmachine.xr.com.coffeemachine.activity.FirstActivity;
import vendingmachine.xr.com.coffeemachine.activity.StartupPageActivity;

public class UpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        try {
            if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")){

                    Toast.makeText(context, "升级了一个安装包，重新启动此程序", Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(context, StartupPageActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent2);
                // 说明系统中不存在这个activity

            }
            //接收安装广播
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
                String packageName = intent.getDataString();
                System.out.println("安装了:" +packageName + "包名的程序");
            }
            //接收卸载广播
            if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
                String packageName = intent.getDataString();
                System.out.println("卸载了:"  + packageName + "包名的程序");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
