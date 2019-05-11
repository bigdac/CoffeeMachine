package vendingmachine.xr.com.coffeemachine.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.xr.database.dao.daoImp.SaleGoodsDaoImp;

import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.application.MyApplication;
import vendingmachine.xr.com.coffeemachine.fragment.BuyFragment;
import vendingmachine.xr.com.coffeemachine.mqtt.MQService;
import vendingmachine.xr.com.coffeemachine.utils.AryChangeManager;
import vendingmachine.xr.com.coffeemachine.utils.SerialPortUtil;
import vendingmachine.xr.com.coffeemachine.utils.Utils;

import static vendingmachine.xr.com.coffeemachine.utils.Utils.addText1;
import static vendingmachine.xr.com.coffeemachine.utils.Utils.clearText1;

public class TestActivity extends AppCompatActivity {
    String machineType = "32";
    String machineAdress = "00";
    String command1 = "01";
    String command2 = "02";
    int x1 = AryChangeManager.stringToHex(machineType)[0];
    int x2 = AryChangeManager.stringToHex(machineAdress)[0];
    int c2 = AryChangeManager.stringToHex(command2)[0];
    int c1 = AryChangeManager.stringToHex(command1)[0];
    private static boolean isHexTransport = true;//当前为16进制发送
    private LocationClient mLocationClient;
    private LocationClientOption mOption;
    public static boolean running = false;
    private boolean isBound;
    Intent service;
    boolean isStart=true;
    static TextView editText;
    /*标识，故障*/
    TextView tv_test_bs,tv_top;
    static TextView tv_test_gz;
    /*咖啡测试,设备加料,设置功能,销售查询,设备信息*/
    LinearLayout li_test_gm,li_test_add,li_test_xs;
    RelativeLayout li_test_xbxx;
    Button bt_test_back,bt_test_out;
    SharedPreferences preferences;
    String szImei;
    MyApplication application;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());//百度地图
        RegisterBroadcast();
        setContentView(R.layout.activity_test);
        application= (MyApplication) getApplication();
        application.addActivity(this);
        tv_test_bs = findViewById(R.id.tv_test_bs);
        tv_test_gz = findViewById(R.id.tv_test_gz);
        li_test_gm = findViewById(R.id.li_test_gm);
        li_test_add=findViewById(R.id.li_test_add);
        li_test_xs = findViewById(R.id.li_test_xs);
        li_test_xbxx= findViewById(R.id.li_test_xbxx);
        bt_test_back = findViewById(R.id.bt_test_back);
        bt_test_out = findViewById(R.id.bt_test_out);
        tv_top = findViewById(R.id.tv_top);
        bt_test_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                application.removeAllActivity();
            }
        });
        bt_test_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                application.removeActivity(TestActivity.this);
                startActivity(new Intent(TestActivity.this,FirstActivity.class));
            }
        });
        String str =  "3200050037";
        SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        szImei = preferences.getString("szImei", "");
        tv_test_bs.setText(szImei);
        li_test_gm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this,CoffeeTestActivity.class));
            }
        });
        li_test_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this,AddGoodsActivity.class));
            }
        });
        li_test_xs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this,SaleActivity.class));
            }
        });
        LinearLayout li_test_jg = findViewById(R.id.li_test_jg);
        li_test_jg .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this,ChangePriceActivity.class));
            }
        });

        service = new Intent(this, MQService.class);
        isBound = this.bindService(service, connection, Context.BIND_AUTO_CREATE);
        myBRReceiver = new MyBRReceiver();
        IntentFilter itFilter = new IntentFilter("android.intent.action.ENG_MODE_SWITCH");
        registerReceiver(myBRReceiver, itFilter);
        initView();
        mLocationClient = new LocationClient(this);
        mLocationClient.setLocOption(getDefaultLocationClientOption());
        mLocationClient.registerLocationListener(mListener);
        mLocationClient.start();
        tv_top.setText("上位机版本号：302_V"+getVerName(this)+"_2019_1_10");

    }


    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }
    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            if("01".equals( msg.obj)){
                tv_test_gz.setText("落杯异常");
                isWell=false;
            }
            if("02".equals( msg.obj)){
                tv_test_gz.setText("平台移动异常");
                isWell=false;
            }
            if("03".equals( msg.obj)){
                tv_test_gz.setText("加热模块异常");
                isWell=false;
            }
            if("04".equals( msg.obj)){
                tv_test_gz.setText("取杯异常");
                isWell=false;
            }
            if("05".equals( msg.obj)){
                tv_test_gz.setText("出胶囊异常");
                isWell=false;
            }
            if("06".equals( msg.obj)){
                tv_test_gz.setText("皮带检测异常");
                isWell=false;
            }
            if ("result01".equals(msg.obj)){
                tv_test_gz.setText("正在出货");
                isWell=false;
            }
            if ("result02".equals(msg.obj)){
                tv_test_gz.setText("出货成功");
                isWell=true;
            }
            if ("nogoods03".equals(msg.obj)){
                tv_test_gz.setText("缺杯缺水");
                isWell=false;
            }
            if ("nogoods01".equals(msg.obj)){
                tv_test_gz.setText("缺杯子");
                isWell=false;
            }
            if ("nogoods02".equals(msg.obj)){
                tv_test_gz.setText("缺水");
                isWell=false;
            }
            if ("nogoods04".equals(msg.obj)){
                tv_test_gz.setText("缺糖");
                isWell=false;
            }
            if ("nogoods05".equals(msg.obj)){
                tv_test_gz.setText("缺糖缺杯");
                isWell=false;
            }
            if ("nogoods06".equals(msg.obj)){
                tv_test_gz.setText("缺糖缺水");
                isWell=false;
            }
            if ("nogoods07".equals(msg.obj)){
                tv_test_gz.setText("缺水缺糖缺杯");
                isWell=false;
            }
            if ("nogoods00".equals(msg.obj)){
                tv_test_gz.setText("（无）");
                isWell=true;
            }

        }


    };


    /**
     * 绑定service
     */
    MQService mqService;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder = (MQService.LocalBinder) service;
            mqService = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void initView() {

//        locationResult = (TextView) findViewById(R.id.textView1);
        Button bt_qh = (Button)findViewById(R.id.bt_qh);
        bt_qh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToast(TestActivity.this,"地址位于"+address);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        running=true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        running=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myBRReceiver!=null){
            unregisterReceiver(myBRReceiver);
        }
        if (mReceiver!=null){
            unregisterReceiver(mReceiver);
        }
        running=false;
        if (isBound && connection != null) {
            unbindService(connection);
        }
    }



    MyBRReceiver myBRReceiver;
    public class MyBRReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals("android.intent.action.ENG_MODE_SWITCH")){

                boolean msg = intent.getIntExtra("state", 1) == 1 ? true : false;
                if (msg) {
                    Toast.makeText(context, "维护模式 open",
                            Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "维护模式 close",
                            Toast.LENGTH_SHORT).show();
                    application.removeActivity(TestActivity.this);
                    startActivity(new Intent(TestActivity.this,FirstActivity.class));
                }
            }
        }

    }
    public void getState(){

        String i2 = AryChangeManager.dexToHex(x1 ^ x2 ^ c1 );
        String str = machineType + machineAdress + command1 + "00" +i2;
        Log.e("test", "onClick: " + str+"-----"+i2);
        if (isHexTransport) {

            SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));

        }
    }
    //校验码 =机器类型^机器地址^ 命令字 ^ 数据包长 ^ 数据包
    //          32       00
    public void sendBuyMess1(){

        String i2 = AryChangeManager.dexToHex(x1 ^ x2 ^ c2 ^ 2 ^ 4 ^ 1 );
        String str = machineType + machineAdress + command2 + "02" + "04" + "01"  + i2;
        Log.e("test", "onClick: " + str+"---------"+i2);
        if (isHexTransport) {
            Toast.makeText(TestActivity.this, "购买1号", Toast.LENGTH_SHORT).show();
            SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));

        }
    }

    static String errorCode;
    static String nowAction;
    static String result;
    static String nogoods;
    static String drinks;
    static boolean isWell=true;
    static boolean hasGoods = true;
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
                    if (!"00".equals(errorCode)){
                        if ("01".equals(errorCode)){
                            Message message = new Message();
                            message.obj="01";
                            mHandler.sendMessage(message);
                        }
                        if ("02".equals(errorCode)){
                            Message message = new Message();
                            message.obj="02";
                            mHandler.sendMessage(message);
                        }
                        if ("03".equals(errorCode)){
                            Message message = new Message();
                            message.obj="03";
                            mHandler.sendMessage(message);
                        }
                        if ("04".equals(errorCode)){
                            Message message = new Message();
                            message.obj="04";
                            mHandler.sendMessage(message);
                        }
                        if ("05".equals(errorCode)){
                            Message message = new Message();
                            message.obj="05";
                            mHandler.sendMessage(message);
                        }
                        if ("06".equals(errorCode)){
                            Message message = new Message();
                            message.obj="06";
                            mHandler.sendMessage(message);
                        }
                    }

                    if (!"00".equals(result)) {
                        if ("01".equals(result)) {
                            Message message = new Message();
                            message.obj = "result01";
                            mHandler.sendMessage(message);
                        }
                        if ("02".equals(result)) {
                            Message message = new Message();
                            message.obj = "result02";
                            mHandler.sendMessage(message);
                        }
                    }
                    if (!"00".equals(nogoods)) {
                        if ("01".equals(nogoods)) {
                            Message message = new Message();
                            message.obj = "nogoods01";
                            mHandler.sendMessage(message);
                            hasGoods=false;
                        }
                        if ("02".equals(nogoods)) {
                            Message message = new Message();
                            message.obj = "nogoods02";
                            mHandler.sendMessage(message);
                            hasGoods=false;
                        }
                        if ("03".equals(nogoods)) {
                            Message message = new Message();
                            message.obj = "nogoods03";
                            mHandler.sendMessage(message);
                            hasGoods=false;
                        }
                        if ("04".equals(nogoods)) {
                            Message message = new Message();
                            message.obj = "nogoods04";
                            mHandler.sendMessage(message);
                            hasGoods=false;
                        }
                        if ("05".equals(nogoods)) {
                            Message message = new Message();
                            message.obj = "nogoods05";
                            mHandler.sendMessage(message);
                            hasGoods=false;
                        }
                        if ("06".equals(nogoods)) {
                            Message message = new Message();
                            message.obj = "nogoods06";
                            mHandler.sendMessage(message);
                            hasGoods=false;
                        }
                        if ("07".equals(nogoods)) {
                            Message message = new Message();
                            message.obj = "nogoods07";
                            mHandler.sendMessage(message);
                            hasGoods=false;
                        }
                    }else {
                        if (!hasGoods) {
                            hasGoods=true;
                            Message message = new Message();
                            message.obj = "nogoods00";
                            mHandler.sendMessage(message);
                        }
                    }
                    Log.e("mes", "refreshReceive: -->" + errorCode + "..." + nowAction + "..." + result+"..."+drinks+"..."+nogoods);
                }
                }
                if (data.contains("A5")) {

                }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     *
     * 定位作用
     */
    private SDKReceiver mReceiver;
    public class SDKReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            String tx = "";

            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {

                tx = "key 验证出错! 错误码 :" + intent.getIntExtra
                        (SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, 0)
                        +  " ; 请在 AndroidManifest.xml 文件中检查 key 设置";
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                tx ="key 验证成功! 功能可以正常使用";
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                tx = "网络出错";
            }
            if (tx.contains("错")){
                AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(context);
                normalDialog.setTitle("提示");
                normalDialog.setMessage(tx);
                normalDialog.setPositiveButton("确定", null);
                normalDialog.setNegativeButton("关闭", null);
                // 显示
                normalDialog.show();
            }else {
                Toast.makeText(context,tx,Toast.LENGTH_SHORT).show();
            }

        }
    }
    protected void RegisterBroadcast(){
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);
    }

    public LocationClientOption getDefaultLocationClientOption(){
        if(mOption == null){
            mOption = new LocationClientOption();
            /**
             * 默认高精度，设置定位模式
             * LocationMode.Hight_Accuracy 高精度定位模式：这种定位模式下，会同时使用网络定位和GPS定位，优先返回最高精度的定位结果
             * LocationMode.Battery_Saving 低功耗定位模式：这种定位模式下，不会使用GPS，只会使用网络定位（Wi-Fi和基站定位）
             * LocationMode.Device_Sensors 仅用设备定位模式：这种定位模式下，不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位
             */
            mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

            /**
             * 默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
             * 目前国内主要有以下三种坐标系：
             1. wgs84：目前广泛使用的GPS全球卫星定位系统使用的标准坐标系；
             2. gcj02：经过国测局加密的坐标；
             3. bd09：为百度坐标系，其中bd09ll表示百度经纬度坐标，bd09mc表示百度墨卡托米制坐标；
             * 海外地区定位结果默认、且只能是wgs84类型坐标
             */
            mOption.setCoorType("bd09ll");

            /**
             * 默认0，即仅定位一次；设置间隔需大于等于1000ms，表示周期性定位
             * 如果不在AndroidManifest.xml声明百度指定的Service，周期性请求无法正常工作
             * 这里需要注意的是：如果是室外gps定位，不用访问服务器，设置的间隔是1秒，那么就是1秒返回一次位置
             如果是WiFi基站定位，需要访问服务器，这个时候每次网络请求时间差异很大，设置的间隔是3秒，只能大概保证3秒左右会返回就一次位置，有时某次定位可能会5秒返回
             */
            mOption.setScanSpan(3000);

            /**
             * 默认false，设置是否需要地址信息
             * 返回省市区等地址信息，这个api用处很大，很多新闻类api会根据定位返回的市区信息推送用户所在市的新闻
             */
            mOption.setIsNeedAddress(true);

            /**
             * 默认是true，设置是否使用gps定位
             * 如果设置为false，即使mOption.setLocationMode(LocationMode.Hight_Accuracy)也不会gps定位
             */
            mOption.setOpenGps(true);

            /**
             * 默认false，设置是否需要位置语义化结果
             * 可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
             */
            mOption.setIsNeedLocationDescribe(true);//

            /**
             * 默认false,设置是否需要设备方向传感器的方向结果
             * 一般在室外gps定位，时返回的位置信息是带有方向的，但是有时候gps返回的位置也不带方向，这个时候可以获取设备方向传感器的方向
             * wifi基站定位的位置信息是不带方向的，如果需要可以获取设备方向传感器的方向
             */
            mOption.setNeedDeviceDirect(false);

            /**
             * 默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
             * 室外gps有效时，周期性1秒返回一次位置信息，其实就是设置了
             locationManager.requestLocationUpdates中的minTime参数为1000ms，1秒回调一个gps位置
             */
            mOption.setLocationNotify(false);

            /**
             * 默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
             * 如果你已经拿到了你要的位置信息，不需要再定位了，不杀死留着干嘛
             */
            mOption.setIgnoreKillProcess(true);

            /**
             * 默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
             * POI就是获取到的位置附近的一些商场、饭店、银行等信息
             */
            mOption.setIsNeedLocationPoiList(true);

            /**
             * 默认false，设置是否收集CRASH信息，默认收集
             */
            mOption.SetIgnoreCacheException(false);

        }
        return mOption;
    }


    /*****
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    double mLongitude;
    double mLatitude;
    String address;
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {

            StringBuffer sb = new StringBuffer(256);
            sb.append("Thread : " + Thread.currentThread().getName());
            sb.append("\nphone : " + System.currentTimeMillis());
            sb.append("\ntime : ");
            sb.append(location.getTime());
            sb.append("\nlocType : ");// 定位类型
            sb.append(location.getLocType());

            sb.append("\nlatitude : ");// 纬度
            sb.append(location.getLatitude());
            mLatitude = location.getLatitude();
            sb.append("\nlontitude : ");// 经度
            sb.append(location.getLongitude());
            mLongitude = location.getLongitude();
            sb.append("\nradius : ");// 半径
            sb.append(location.getRadius());
            sb.append("\nCountryCode : ");// 国家码
            sb.append(location.getCountryCode());
            sb.append("\nCountry : ");// 国家名称
            sb.append(location.getCountry());
            sb.append("\ncitycode : ");// 城市编码
            sb.append(location.getCityCode());
            sb.append("\ncity : ");// 城市
            sb.append(location.getCity());
            sb.append("\nDistrict : ");// 区
            sb.append(location.getDistrict());
            sb.append("\nStreet : ");// 街道
            sb.append(location.getStreet());
            sb.append("\naddr : ");// 地址信息
            sb.append(location.getAddrStr());
            address=location.getAddrStr();
            sb.append("\nDirection(not all devices have value): ");
            sb.append(location.getDirection());// 方向
            sb.append("\nlocationdescribe: ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            sb.append("\nPoi: ");// POI信息
            if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                for (int i = 0; i < location.getPoiList().size(); i++) {
                    Poi poi = (Poi) location.getPoiList().get(i);
                    sb.append(poi.getName() + ";");
                }
            }
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 速度 单位：km/h
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());// 卫星数目
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 海拔高度 单位：米
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                // 运营商信息
                if (location.hasAltitude()) {// *****如果有海拔高度*****
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                }
                sb.append("\noperationers : ");// 运营商信息
                sb.append(location.getOperators());
                sb.append("方向1：" + location.getDerect());
                sb.append("方向2：" + location.getDirection());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
//            logMsg(sb.toString());
            Log.e("sbtostring", "onReceiveLocation: "+sb.toString() );
        }

    };

}
