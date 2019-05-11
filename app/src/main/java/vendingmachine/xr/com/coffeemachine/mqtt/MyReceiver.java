package vendingmachine.xr.com.coffeemachine.mqtt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import vendingmachine.xr.com.coffeemachine.activity.StartupPageActivity;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            Intent i = new Intent(context, StartupPageActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

}
