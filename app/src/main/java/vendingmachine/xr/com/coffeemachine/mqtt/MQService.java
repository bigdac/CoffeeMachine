package vendingmachine.xr.com.coffeemachine.mqtt;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.fragment.BuyFragment;
import vendingmachine.xr.com.coffeemachine.pojo.Order;

public class MQService extends Service {

    private String TAG = "MQService";
    private String host = "tcp://47.98.131.11:1883";
    private String userName = "admin";
    private String passWord = "Xr7891122";
    String macAddress = "hrrj7895ccf7f6c9fa4";
    public static boolean running = false;
    /**
     * 测试的macAddrsss
     */


    private Context mContext = this;



    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private MqttClient client;
    private MqttConnectOptions options;
    String clientId;
    private LocalBinder binder = new LocalBinder();
//    private

    /**
     * 服务启动之后就初始化MQTT,连接MQTT
     */
    @Override
    public void onCreate() {
        super.onCreate();
        clientId = UUID.getUUID(this);
        init();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {

        public MQService getService() {
            Log.i(TAG, "Binder");
            return MQService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("sss","-->onStart");
        connect();
        return super.onStartCommand(intent, flags, startId);

    }

    /**
     * 销毁服务，则断开MQTT,释放资源
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            Log.i(TAG, "onDestroy");
            scheduler.shutdown();
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存

            client = new MqttClient(host, clientId,
                    new MemoryPersistence());
            //MQTT的连接设置
            options = new MqttConnectOptions();
            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            //设置连接的用户名
            options.setUserName(userName);
            //设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(15);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
//            options.setKeepAliveInterval(20);


            //设置回调
            client.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable cause) {
                    //连接丢失后，一般在这里面进行重连
                    System.out.println("connectionLost----------");
                    startReconnect();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    //publish后会执行到这里
                    System.out.println("deliveryComplete---------"
                            + token.isComplete());
                }

                @Override
                public void messageArrived(String topicName, MqttMessage message) {
                    try {
                        Log.e("mess", "messageArrived: 1111" );
                        new LoadAsyncTask().execute(topicName, message.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            if (client.isConnected()) {
                client.disconnect();
            }
            new ConAsync().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接MQTT
     */
    class ConAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (!client.isConnected()) {
                    client.connect(options);
                }
                List<String> topicNames = getTopicNames();
                if (client.isConnected() && !topicNames.isEmpty()) {
                    for (String topicName : topicNames) {
                        if (!TextUtils.isEmpty(topicName)) {
                            client.subscribe(topicName, 1);
                            Log.i("client", "-->" + topicName);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     * 加载MQTT返回的消息
     */
    class LoadAsyncTask extends AsyncTask<String, Void, Object> {

        @Override
        protected Object doInBackground(String... strings) {
            String topicName = strings[0];/**收到的主题*/
            String message = strings[1];
            Log.i("topicName", "-->:" + topicName);

            try {
                JSONObject messageJsonObject=null;
                if (!TextUtils.isEmpty(message) && message.startsWith("{") && message.endsWith("}")) {
                    if ("coffee/1234/deal/qrcode".equals(topicName)){
                        messageJsonObject = new JSONObject(message);
                        Order order = new Order();
                        String orderNo="";
                        String aliCode="";
                        String wxCode="";
                        int result;
                        if (messageJsonObject.has("orderNo")){
                            orderNo=messageJsonObject.getString("orderNo");
                            order.setOrderNo(orderNo);
                        }
                        if (messageJsonObject.has("aliCode")){
                            aliCode=messageJsonObject.getString("aliCode");
                            order.setAliCode(aliCode);
                        }

                        if (messageJsonObject.has("wxCode")){
                            wxCode = messageJsonObject.getString("wxCode");
                            order.setWxCode(wxCode);
                        }
                        if (messageJsonObject.has("result")){
                            result  = messageJsonObject.getInt("result");
                            order.setResult(result);
                        }
                        if (BuyFragment.running){
                            Intent mqttIntent = new Intent("BuyFragment");
                            mqttIntent.putExtra("message", order);
                            sendBroadcast(mqttIntent);
                        }
                    }else if ("coffee/1234/deal/payresult".equals(topicName)) {
                        JSONObject jsonObjectpay=new JSONObject(message);
                        String orderNo="";
                        int result=-1;
                        if(jsonObjectpay.has("orderNo")){
                            orderNo=jsonObjectpay.getString("orderNo");
                        }
                        if (jsonObjectpay.has("result")){
                            result=jsonObjectpay.getInt("result");
                        }
                        if (BuyFragment.running){
                            Intent mqttIntent = new Intent("BuyFragment");
                            mqttIntent.putExtra("payresult",result);
                            Log.e("mess", "doInBackground: -->" );
                            sendBroadcast(mqttIntent);
                        }
                    }else if ("coffee/1234/commodity/edit".equals(topicName)) {

                        if (BuyFragment.running){
                            Intent mqttIntent = new Intent("BuyFragment");
                            mqttIntent.putExtra("mes_new",message);
                            Log.e("mess", "doInBackground: -->" );
                            sendBroadcast(mqttIntent);
                        }
                    }else if ("coffee/1234/device/edit".equals(topicName)) {

                        if (BuyFragment.running){
                            Intent mqttIntent = new Intent("FirstActivity");
                            mqttIntent.putExtra("mes_url",message);
                            Log.e("mess", "doInBackground: -->" );
                            sendBroadcast(mqttIntent);
                        }
                    }else if ("coffee/1234/commodity/delete".equals(topicName)) {

                        if (BuyFragment.running){
                            Intent mqttIntent = new Intent("BuyFragment");
                            mqttIntent.putExtra("mes_delgoods",message);
                            Log.e("mess", "doInBackground: -->" );
                            sendBroadcast(mqttIntent);
                        }
                    }



                }
            }catch (Exception e){
                e.printStackTrace();
            }

           return null;

        }

    }


    /**
     * 获得订阅MQTT的所有主题
     */
    String userId;

    public List<String> getTopicNames() {
        List<String> topics=new ArrayList<>();
        topics.add("coffee/1234/device/survey");//服务器添加设备后，向设备获取杯子数量，胶囊数量，设备状态
        topics.add("coffee/1234/commodity/new");//机器中放入新的商品时，向服务器发送商品的编号、名称、数量、价格
        topics.add("coffee/1234/commodity/edit");//服务器修改商品数据时，向设备发送修改数据
        topics.add("coffee/1234/deal/create");//设备上发起交易时，向服务器发送交易信息
        //服务器收到交易数据，生成支付宝、微信的付款二维码并发送给设备
        topics.add("coffee/1234/deal/qrcode");
        //服务器发送二维码后，开始监视付款状态，待付款成功，或付款因超时等原因失败时，向设备发送结果
        topics.add("coffee/1234/deal/payresult");
        topics.add("coffee/1234/deal/doresult");//设备向服务器传送出货结果
        topics.add("coffee/1234/device/control");//控制设备开关状态
        topics.add("coffee/1234/device/switch");//设备开关时向服务器更新状态
        topics.add("coffee/1234/device/die");//设备故障等情况时，作为遗嘱告知服务器
        topics.add("coffee/1234/device/edit");//设备添加广告
        topics.add("coffee/1234/commodity/delete");//删除货物

        topics.add(macAddress);
        return topics;
    }

    /**
     * 重新连接MQTT
     */
    private void startReconnect() {

        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                if (!client.isConnected()) {
                    connect();
                }
            }
        }, 0 * 1000, 1 * 1000, TimeUnit.MILLISECONDS);
    }



    /**
     * 发送MQTT主题
     */
    public boolean publish(String topicName, int qos, String payload) {
        boolean flag = false;
        if (client != null && client.isConnected()) {
            try {
                MqttMessage message = new MqttMessage(payload.getBytes("utf-8"));
                qos = 1;
                //设置保留消息
                if (topicName.contains("friend")) {
                    message.setRetained(true);
                }
                if (topicName.contains("clockuniversal")) {
                    message.setRetained(true);
                }
                message.setQos(qos);
                client.publish(topicName, message);
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 订阅MQTT主题
     */
    public boolean subscribe(String topicName, int qos) {
        boolean flag = false;
        if (client != null && client.isConnected()) {
            try {
                client.subscribe(topicName, qos);
                flag = true;
            } catch (MqttException e) {
            }
        }
        return flag;
    }



    //取消订阅
    public void unsubscribe(String topicName) {
        if (client != null && client.isConnected()) {
            try {
                client.unsubscribe(topicName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }









}
