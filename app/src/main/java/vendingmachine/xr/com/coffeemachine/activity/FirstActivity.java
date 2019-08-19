package vendingmachine.xr.com.coffeemachine.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.xr.database.dao.daoImp.ADDaoImp;
import org.json.JSONObject;
import java.util.List;
import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.application.MyApplication;
import vendingmachine.xr.com.coffeemachine.fragment.BuyFragment;
import vendingmachine.xr.com.coffeemachine.fragment.MediaplayFragment;
import vendingmachine.xr.com.coffeemachine.fragment.XqFragment;
import vendingmachine.xr.com.coffeemachine.mqtt.MQService;
import vendingmachine.xr.com.coffeemachine.pojo.AD;


public class FirstActivity extends AppCompatActivity  {
    MessageReceiver receiver;
    static BuyFragment buyFragment;
    MediaplayFragment videoFragment;
    XqFragment xqFragment;
    ADDaoImp adDaoImp;
    private boolean isBound;
    Intent service;
    public static boolean running = false;
    MyApplication application;
    AD ad;
    public CountDownTimer countDownTimer;
    private long advertisingTime = 5 * 1000;//定时跳转广告时间
    FrameLayout frameLayout;
    FrameLayout frameLayout1;
//    private MyOnClick myPersonOnClick;
    SharedPreferences preferences;
    Button bt_first_qh ;
    int isPicture = 1;
    String szImei;
    int mHeight = 0 ;

    private boolean mLayoutComplete = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        secondHide();

        setContentView(R.layout.activity_first);
        hideBottomUIMenu();
        preferences = getSharedPreferences("my",MODE_PRIVATE);
        adDaoImp = new ADDaoImp(getApplicationContext());
        application= (MyApplication) getApplication();
        application.addActivity(this);
        ad = new AD();
        AD ad1 = new AD();
        List<AD> ads = adDaoImp.findAllad();
        if (ads.size() > 0) {
            ad1 = ads.get(0);
            String s1 = ad1.getAdVideo();
            String s2 = ad1.getAdPictire();
            if (s1 != null) {
                videourl = s1;
            }
            if (s2 != null) {
                picurl = s2;
            }
        }
        frameLayout = findViewById(R.id.right_fragment);
        frameLayout1 = findViewById(R.id.left_fragment);
        frameLayout1.setVisibility(View.VISIBLE);
        bt_first_qh = findViewById(R.id.bt_first_qh);
        bt_first_qh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPicture==1){
                    advideo();
                    isPicture=2;
                }else {
                    isPicture=1;
                    adpic();
                }

            }
        });
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        IntentFilter intentFilter = new IntentFilter("FirstActivity");
        receiver = new MessageReceiver();
        registerReceiver(receiver, intentFilter);
        videoFragment = new MediaplayFragment();
        if (buyFragment == null) {
            buyFragment = new BuyFragment();
        }
        xqFragment = new XqFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.left_fragment, xqFragment, "leftListFragment");//添加leftListFragment并为其设置tag leftListFragment
        transaction.add(R.id.right_fragment, buyFragment, "rightListFragment");////添加rightListFragment并为其设置tag rightListFragment
        transaction.commit();

//        if (ad1.getAdPictire() != null) {
//            adpic();
//        } else if (ad1.getAdVideo() != null) {
//            advideo();
//        }

        service = new Intent(this, MQService.class);
        startService(service);
        isBound = this.bindService(service, connection, Context.BIND_AUTO_CREATE);
        myBRReceiver = new MyBRReceiver();
        IntentFilter itFilter = new IntentFilter("android.intent.action.ENG_MODE_SWITCH");
        registerReceiver(myBRReceiver, itFilter);
//        hidebottomuimenu();
    }


    public void secondHide() {
        int flags = getWindow().getDecorView().getSystemUiVisibility();
        getWindow().getDecorView().setSystemUiVisibility(flags | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);



    }



    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    public void stopBuyGoods (){
        frameLayout1.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
    }

//    //定义回调接口
//    public interface MyOnClick{
//        void myListener(int what);
//    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                //有按下动作时取消定时
//                if (countDownTimer != null){
//                    countDownTimer.cancel();
//                }
                Log.e("zzzzzsfdsdfsdff", "dispatchTouchEvent: -->"+ev.getAction());
                break;
            case MotionEvent.ACTION_UP:
                //抬起时启动定时
//                if (isPlayAd) {
//                    frameLayout.setVisibility(View.VISIBLE);
//                    frameLayout1.setVisibility(View.GONE);
//                    if (null != myPersonOnClick) {
//                        myPersonOnClick.myListener(0);
//                    }
//                    startAD();
//                }


                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void hideADpage(){
        frameLayout1.setVisibility(View.GONE);
    }

    /**
     * 跳zhuanAD
     */
//    public boolean isPlayAd;
//    public void startAD() {
//        isPlayAd=false;
//        if (countDownTimer == null) {
//            countDownTimer = new CountDownTimer(advertisingTime, 1000) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//                    Log.e("count", "onTick: --?"+millisUntilFinished/1000 );
//                }
//
//                @Override
//                public void onFinish() {
//                    //定时完成后的操作
//                    //跳转到广告页面
//                    frameLayout1.setVisibility(View.VISIBLE);
//                    frameLayout.setVisibility(View.GONE);
////传递点击事件 以及activity向fragment传值
//                    if(null!=myPersonOnClick){
//                        myPersonOnClick.myListener(1);
//                        isPlayAd=true;
//                    }
//                }
//            };
//            countDownTimer.start();
//        } else {
//            countDownTimer.start();
//        }
//    }
//    public void stopAD() {
//
//        if (countDownTimer != null) {
//            countDownTimer.cancel();
//        }
//    }
//
//    public void setMyPersonOnClick(MyOnClick myOnClick1){
//        this.myPersonOnClick=myOnClick1;
//    }
//

    @Override
    protected void onStart() {
        super.onStart();
        running = true;
//        startAD();
    }







    /**
     * 绑定service
     */
    MQService mqService;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder = (MQService.LocalBinder) service;
            mqService = binder.getService();
//            szImei = preferences.getString("szImei","");
//            boolean success = mqService.publish("coffee/"+szImei+"/device/die",1,"die");
//            if (!success){
//                boolean success2 = mqService.publish("coffee/"+szImei+"/device/die",1,"die");
//            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    MyBRReceiver myBRReceiver;



    /*复写跟换bnner图片*/
//    @Override
//    public void setValue(int value) {
//
//    }

    public class MyBRReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.ENG_MODE_SWITCH")) {

                boolean msg = intent.getIntExtra("state", 1) == 1 ? true : false;
                if (msg) {
                    Toast.makeText(context, "维护模式 open",
                            Toast.LENGTH_SHORT).show();
                    application.removeActivity(FirstActivity.this);
                    startActivity(new Intent(FirstActivity.this, TestActivity.class));

                } else {
                    Toast.makeText(context, "维护模式 close",
                            Toast.LENGTH_SHORT).show();

                }
            }
        }

    }


    public void advideo() {
        FragmentManager fragmentManager1 = getSupportFragmentManager();
        FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
        MediaplayFragment videoFragment = new MediaplayFragment();
        transaction1.replace(R.id.left_fragment, videoFragment, "leftListFragment2");//添加leftListFragment并为其设置tag leftListFragment
        transaction1.commit();
    }

    public void adpic() {
        FragmentManager fragmentManager1 = getSupportFragmentManager();
        FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
        XqFragment xqFragment = new XqFragment();
        transaction1.replace(R.id.left_fragment, xqFragment, "leftListFragment");//添加leftListFragment并为其设置tag leftListFragment
        transaction1.commit();
    }

    /**
     * 下部的购买列表
     ***/
    public void myClick(int pos) {
        buyFragment.setAAA(pos);
    }

    /**
     * 设置购买的数量
     **/
    public void myClick2(int pos, int val) {
        buyFragment.setVal(pos, val);
    }

    /**
     * 显示详情界面
     **/
    public void myClick1() {
//        if (buyFragment.getCanBuy()){
            frameLayout1.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.VISIBLE);
            buyFragment.setView();
//        }else {
//            Toast.makeText(this,"暂时无法购买，请稍候。。。",Toast.LENGTH_SHORT).show();
//        }


    }

    public void starvideo() {
        videoFragment.setStarplay();
    }

    public void stopvideo() {
        videoFragment.setStopplay();
    }

    /**
     * 接收到mqtt的广播
     */
    String videourl = "";
    String picurl = "";

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (running) {
                String mess = intent.getStringExtra("mes_url");
                String mes_updata = intent.getStringExtra("mes_updata");
                if (!TextUtils.isEmpty(mess)) {
                    try {
                        adDaoImp.deleteAll();
                        JSONObject jsonObject = new JSONObject(mess);

                        if (jsonObject.has("adVideo")) {
                            videourl = jsonObject.getString("adVideo");
                        }
                        if (jsonObject.has("adPictire")) {
                            picurl = jsonObject.getString("adPictire");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (!TextUtils.isEmpty(videourl)) {
                    ad.setAdVideo(videourl);
                    ad.setId(1L);
                    adDaoImp.insert(ad);
                    advideo();
                } else if (!TextUtils.isEmpty(picurl)) {
                    ad.setAdPictire(picurl);
                    ad.setId(1L);
                    adDaoImp.insert(ad);
                    adpic();
                }
                if (!TextUtils.isEmpty(mes_updata)){
                    startActivity(new Intent(FirstActivity.this,DownloadActivity.class));
                }

            }


        }
    }
    public  void  AppupData(){
        Intent intent = new Intent(this,DownloadActivity.class);
        startActivity(intent);
    }

    public String getPicurl() {
        Log.e("url", "getVideourl: -->" + picurl);
        return picurl;
    }

    public String getVideourl() {
        Log.e("url", "getVideourl: -->" + videourl);
        return videourl;

    }

    static String errorCode;
    static String nowAction;
    static String result;
    static String nogoods;
    static String drinks;
    static boolean hasGoods=true;
    static int i =1 ;
    static int j=1 ;
    public static void refreshReceive(String data) {

        Log.e("TTTTTTrrrr", "refreshReceive: " + data);
        try {
            if (data.contains("A1")) {
                if (data.length() > 10) {
                    String mes = data.substring(data.indexOf("A1"));
                    Log.e("mes", "refreshReceive: -->" + mes);
                    errorCode = mes.substring(4, 6);
                    nogoods = mes.substring(6,8);
                    drinks = mes.substring(8,14);
                    nowAction = mes.substring(14, 16);
                    result = mes.substring(16, 18);
                    Log.e("mes", "refreshReceive: -->" + errorCode + "..." + nowAction + "..." + result+"..."+drinks+"..."+nogoods);
                    if (!"00".equals(errorCode)) {
                        if ("01".equals(errorCode)) {
                            Message message = new Message();
                            message.obj = "01";
                            mHandler1.sendMessage(message);
                        }
                        if ("02".equals(errorCode)) {
                            Message message = new Message();
                            message.obj = "02";
                            mHandler1.sendMessage(message);
                        }
                        if ("03".equals(errorCode)) {
                            Message message = new Message();
                            message.obj = "03";
                            mHandler1.sendMessage(message);
                        }
                        if ("04".equals(errorCode)) {
                            Message message = new Message();
                            message.obj = "04";
                            mHandler1.sendMessage(message);
                        }
                        if ("05".equals(errorCode)) {
                            Message message = new Message();
                            message.obj = "05";
                            mHandler1.sendMessage(message);
                        }
                    }
                    if (!"00".equals(nowAction)){
                        if ("01".equals(nowAction)) {
                            Message message = new Message();
                            message.obj = "nowAction01";
                            mHandler1.sendMessage(message);
                            i=1;
                        }
                    }
                        if ( "00".equals(nowAction)){
                            if (i==1){
                            Message message = new Message();
                            message.obj = "nowAction00";
                            mHandler1.sendMessage(message);
                            i=0;
                        }
                    }
                    if (!"00".equals(nogoods)) {
                        if ("01".equals(nogoods)) {
                            Message message = new Message();
                            message.obj = "nogoods01";
                            mHandler1.sendMessage(message);
                            hasGoods=false;
                        }
                        if ("02".equals(nogoods)) {
                            Message message = new Message();
                            message.obj = "nogoods02";
                            mHandler1.sendMessage(message);
                            hasGoods=false;
                        }
                        if ("03".equals(nogoods)) {
                            Message message = new Message();
                            message.obj = "nogoods03";
                            mHandler1.sendMessage(message);
                            hasGoods=false;
                        }
                        if ("04".equals(nogoods)) {
                            Message message = new Message();
                            message.obj = "nogoods04";
                            mHandler1.sendMessage(message);
                            hasGoods=false;
                        }
                        if ("05".equals(nogoods)) {
                            Message message = new Message();
                            message.obj = "nogoods05";
                            mHandler1.sendMessage(message);
                            hasGoods=false;
                        }
                        if ("06".equals(nogoods)) {
                            Message message = new Message();
                            message.obj = "nogoods06";
                            mHandler1.sendMessage(message);
                            hasGoods=false;
                        }
                        if ("07".equals(nogoods)) {
                            Message message = new Message();
                            message.obj = "nogoods07";
                            mHandler1.sendMessage(message);
                            hasGoods=false;
                        }
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


//        if (backCode.equals("A2")) {
//           mqService.starThread();
//
//        }

    }
    @SuppressLint("HandlerLeak")
    private static Handler mHandler1 = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            if("01".equals( msg.obj)){
                buyFragment.SeterrorCode(1,"落杯异常");
            }
            if("02".equals( msg.obj)){
                buyFragment.SeterrorCode(1,"平台移动异常：（x，y，卡顿）");
            }
            if("03".equals( msg.obj)){
                buyFragment.SeterrorCode(1,"加热模块异常");
            }
            if("04".equals( msg.obj)){
                buyFragment.SeterrorCode(1,"取杯异常：（取杯未报）");
            }
            if("05".equals( msg.obj)){
                buyFragment.SeterrorCode(1,"出胶囊异常");
            }
            if("06".equals( msg.obj)){
                buyFragment.SeterrorCode(1,"皮带检测异常");
            }
            if ("nowAction01".equals(msg.obj)){
                buyFragment.SeterrorCode(3,"正在加热中，请稍候。。。");
            }
            if ("nowAction00".equals(msg.obj)){
                buyFragment.SeterrorCode(2,"正常销售中。。。");
            }
            if("nogoods03".equals( msg.obj)){
                buyFragment.SeterrorCode(1,"缺杯缺水");
            }
            if("nogoods01".equals( msg.obj)){
                buyFragment.SeterrorCode(1,"缺杯子");
            }
            if("nogoods02".equals( msg.obj)){
                buyFragment.SeterrorCode(1,"缺水");
            }
            if("nogoods04".equals( msg.obj)){
                buyFragment.SeterrorCode(1,"缺糖");
            }
            if("nogoods05".equals( msg.obj)){
                buyFragment.SeterrorCode(1,"缺糖缺杯");
            }
            if("nogoods06".equals( msg.obj)){
                buyFragment.SeterrorCode(1,"缺糖缺水");
            }

            if("nogoods07".equals( msg.obj)){
                buyFragment.SeterrorCode(1,"缺水缺糖缺杯");
            }

        }


    };
    public static String getDrinks() {
        return drinks;
    }

    public static String getNowAction() {
        return nowAction;
    }

    public static String getResult() {
        return result;
    }
    public static String setResult(String s) {
         result=s;
         return result;
    }

    @Override
    protected void onStop() {
        super.onStop();
        running = false;
        hasGoods=false;
        i=1;
        if (countDownTimer != null)
            countDownTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        running = false;
        hasGoods=false;
        i=1;
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (myBRReceiver != null) {
            unregisterReceiver(myBRReceiver);
        }
        if (isBound && connection != null) {
            unbindService(connection);
        }
        if (countDownTimer != null)
            countDownTimer.cancel();

    }
}
