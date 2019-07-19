package vendingmachine.xr.com.coffeemachine.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.application.MyApplication;

public class StartupPageActivity extends AppCompatActivity {
    public  static String HIDE_STATUSBAR_CMD ="su -c service call activity 42 s16 com.android.systemui";
    MyApplication application;
    CountDownTimer countDownTimer;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        secondHide();
        setContentView(R.layout.activity_startup);
        application= (MyApplication) getApplication();
        application.addActivity(this);
        Log.e("DDDDDDDDSSSSS111", "onCreate: -->"+countDownTimer );
        if (countDownTimer==null) {
            countDownTimer = new CountDownTimer(7000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.e("DDDDSSSXXXXX", "onTick: -->" + millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    application.removeActivity(StartupPageActivity.this);
                    startActivity(new Intent(StartupPageActivity.this, FirstActivity.class));
                }
            }.start();
            Log.e("DDDDDDDDSSSSS222", "onCreate: -->"+countDownTimer );
        }
//            try {
//                String proID = "79";
//                if (android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//                    proID ="42";
//                Process proc =Runtime.getRuntime().exec(HIDE_STATUSBAR_CMD);
//                proc.waitFor();
//            } catch ( Exception e) {
//                e.printStackTrace();
//                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//            }



    }



    public void secondHide() {
        int flags = getWindow().getDecorView().getSystemUiVisibility();
        getWindow().getDecorView().setSystemUiVisibility(flags | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer!=null)
            countDownTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer!=null)
            countDownTimer.cancel();
    }
}
