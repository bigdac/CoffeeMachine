package vendingmachine.xr.com.coffeemachine.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.adapter.MyGridAdapter;
import vendingmachine.xr.com.coffeemachine.application.MyApplication;
import vendingmachine.xr.com.coffeemachine.constants.IConstant;
import vendingmachine.xr.com.coffeemachine.entity.Option;
import vendingmachine.xr.com.coffeemachine.mqtt.MQService;
import vendingmachine.xr.com.coffeemachine.mqtt.MQTTMessageReveiver;
import vendingmachine.xr.com.coffeemachine.utils.AryChangeManager;
import vendingmachine.xr.com.coffeemachine.utils.SerialPortUtil;
import vendingmachine.xr.com.coffeemachine.utils.ThreadPoolManager;

public class MainActivity extends Activity {
    private static boolean isSerialOpen = false;//串口状态
    private static Button btnControlSerial;//控制串口开关

    private GridView gridSetting;//串口数据设置区
    private MyGridAdapter adapter;
    private List<Option> options;
    private MQTTMessageReveiver myReceiver;//启动服务
    private  static TextView textReceiveContent;//串口数据接收区
    private EditText editTransportContent;//串口数据发送区
    private RadioGroup groupReceive;//接收缓冲区
    private RadioGroup groupTransport;//发送缓冲区
    private Button btnClearReceive;//清空接收区按钮
    private Button btnClearTransport;//清空发送区按钮
    private Button btnSend;//发送串口指令
    private Button btnSend1;//发送命令1
    private Button btnSend2;//发送命令1
    private Button btnSend3;//发送命令1
    private Button btnSend4;//发送命令1
    private Button btnSend5;//发送命令1
    private Button btnSend6;//发送命令1
    private CheckBox boxAutoSend;//自动发送
    private EditText editAutoTime;//自动发送串口数据的时间间隔
    private boolean isAutoSend = false;//是否自动发送
    private static TextView textSendByte;//发送出去的字节数
    private static TextView textReceiveByte;//接收到的字节数
    private Button btnClear;//清空自动接收的字节数

    private static String port;//串口号
    private static int baud;//波特率
    private static char check;//校验位
    private static int data;//数据位
    private static int stop;//停止位
    private RadioGroup groupReceiveModel;//接收方法的单选按钮组
    private RadioGroup groupTransportModel;//发送方法的单选按钮组
    public static boolean isHexReceive = true;//当前为16进制接收
    private static boolean isHexTransport = true;//当前为16进制发送
    String machineType = "32";
    String machineAdress = "00";
    String command1= "01";
    String command2= "02";
    String command3= "03";
    String command4= "04";
    String command5= "05";
    String command6= "06";
    String command7= "07";
    //String转化为16进制
    int x1= AryChangeManager.stringToHex(machineType)[0];
    int x2 = AryChangeManager.stringToHex(machineAdress)[0];
    int c1 =  AryChangeManager.stringToHex(command1)[0];
    int c2 =  AryChangeManager.stringToHex(command2)[0];
    int c3 =  AryChangeManager.stringToHex(command3)[0];
    int c4 =  AryChangeManager.stringToHex(command4)[0];
    int c5 =  AryChangeManager.stringToHex(command5)[0];
    //校验码10 转16进制
    String i =AryChangeManager.dexToHex(x1^x2^c1) ;
    String i3 = AryChangeManager.dexToHex(x1^x2^c3);
    String i4 = AryChangeManager.dexToHex(x1^x2^c4);
    String i5 = AryChangeManager.dexToHex(x1^x2^c5);
    Intent mqintent;
    MQService mqService;
    private  boolean mqBound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("mqttmessage2");
        myReceiver = new MQTTMessageReveiver();
        this.registerReceiver(myReceiver, filter);
        mqintent = new Intent(MainActivity.this, MQService.class);
        mqBound = bindService(mqintent, mqconnection, Context.BIND_AUTO_CREATE);
        /*清空接收区
         */
                btnClearReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textReceiveContent.setText("");
            }
        });

        /*清空发送区
         */
        btnClearTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTransportContent.setText("");
            }
        });
        /*发送命令123
         */
        btnSend1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String str = machineType+""+machineAdress+""+command1+"00"+i;
                Log.e("test", "onClick: "+str );
               if(isHexTransport){

                   Toast.makeText(MainActivity.this,"读取机器状态",Toast.LENGTH_SHORT).show();
                    SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str)  );

                }

            }
        });
        btnSend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int jy =  AryChangeManager.stringToHex("06")[0];
                int jy1 =  AryChangeManager.stringToHex("01")[0];
                int jy2 =  AryChangeManager.stringToHex("00")[0];
                int jy3 =  AryChangeManager.stringToHex("1E")[0];
                int jy4 =  AryChangeManager.stringToHex("14")[0];
                int jy5 =  AryChangeManager.stringToHex("01")[0];
                int jy6 =  AryChangeManager.stringToHex("2C")[0];
                String i2 = AryChangeManager.dexToHex(x1^x2^c2^jy^jy1^jy2^jy3^jy4^jy5^jy6);
                String str = machineType+machineAdress+command2+"06"+"01"+"00"+"1E"+"14"+"01"+"2C"+i2;
                Log.e("test", "onClick: "+str );
                if(isHexTransport){
                    Toast.makeText(MainActivity.this,"购买1号",Toast.LENGTH_SHORT).show();
                    SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));

                }
            }
        });
        btnSend3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = machineType+machineAdress+command3+"00"+i3;
                Log.e("test", "onClick: "+str );
                if(isHexTransport){
                    Toast.makeText(MainActivity.this,"清除运行结果",Toast.LENGTH_SHORT).show();
                    SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));

                }

            }
        });
        btnSend4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = machineType+machineAdress+command4+"00"+i4;
                Log.e("test", "onClick: "+str );
                if(isHexTransport){
                    Toast.makeText(MainActivity.this,"读取机器版本号",Toast.LENGTH_SHORT).show();
                    SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));

                }

            }
        });
        btnSend5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = machineType+machineAdress+command5+"00"+i5;
                Log.e("test", "onClick: "+str );
                if(isHexTransport){
                    Toast.makeText(MainActivity.this,"重启下位机",Toast.LENGTH_SHORT).show();
                    SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));

                }

            }
        });

        btnSend6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mqService!=null){
                    boolean success=mqService.publish("coffee/number/device/survey",2,"010101");
                    Log.i("succ","-->"+success);

                }


            }
        });
        /*发送串口指令
         */
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editTransportContent.getText().toString();
                Log.i("TAG",str+"=");
//                for(int i:AryChangeManager.stringToHex(str)){
//                    Log.i("TAG",""+i);
//                }
                if(isHexTransport){
                    SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));

                }else{
                    SerialPortUtil.sendTextSerialPort(str);
//                    SerialPortUtil.sendTextSerialPort("a");
                }
            }
        });

        /**
         *接收方式的选择改变监听
         */
        groupReceiveModel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i("TAG","接收方式改变");
                if(group.getCheckedRadioButtonId()==R.id.rb_receive_hex){
                    Log.i("TAG","16进制接收");
                    isHexReceive = true;
                }else{
                    Log.i("TAG","文本模式接收");
                    isHexReceive = false;
                }
            }
        });

        /**
         *发送方式的选择改变监听
         */
        groupTransportModel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i("TAG","发送方式改变");
                if(group.getCheckedRadioButtonId()==R.id.rb_transport_hex){
                    Log.i("TAG","16进制发送");
                    isHexTransport = true;
                }else{
                    Log.i("TAG","文本模式发送");
                    isHexTransport = false;
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.receiveByte = 0;
                MyApplication.sendByte = 0;
                refreshReceiveByte();
                refreshSendByte();
            }
        });


        final Handler handler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==100){
                    refreshSendByte();
                }
            }
        };
        ThreadPoolManager.getManager().getCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                while(true){
                    handler.sendEmptyMessage(100);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

            //打开串口
            openSerial();

    }

    boolean boundclock;
    ServiceConnection mqconnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MQService.LocalBinder binder = (MQService.LocalBinder) service;
            mqService = binder.getService();
            boundclock = true;

        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    /**
     *初始化操作
     */
    private void init() {
        initUI();
        initGridSetting();


    }

    private void initGridSetting() {
        options = new ArrayList<>();
        options.add(IConstant.SERIAL_PORT);
        options.add(IConstant.BAUD_RATE);
        options.add(IConstant.CHECK);
        options.add(IConstant.DATA);
        options.add(IConstant.STOP);
        adapter = new MyGridAdapter(MainActivity.this,options);
        gridSetting.setAdapter(adapter);
    }

    /**
     *初始化UI控件
     */
    private void initUI() {
        btnControlSerial = (Button) findViewById(R.id.btn_control_serial);

        gridSetting = (GridView) findViewById(R.id.gv_main_setting);

        textReceiveContent = (TextView) findViewById(R.id.tv_receive_content);
        editTransportContent = (EditText) findViewById(R.id.et_transport_content);
        groupReceive = (RadioGroup) findViewById(R.id.rg_receive_model);
        groupTransport = (RadioGroup) findViewById(R.id.rg_transport_model);
        btnClearReceive = (Button) findViewById(R.id.btn_receive_clear);
        btnClearTransport = (Button) findViewById(R.id.btn_transport_clear);
        btnSend = (Button) findViewById(R.id.btn_transport_send);
        btnSend1 = (Button) findViewById(R.id.bt_send1);
        btnSend2 = (Button) findViewById(R.id.bt_send2);
        btnSend3 = (Button) findViewById(R.id.bt_send3);
        btnSend4 = (Button) findViewById(R.id.bt_send4);
        btnSend5 = (Button) findViewById(R.id.bt_send5);
        btnSend6 = (Button) findViewById(R.id.bt_send6);
        groupReceiveModel = (RadioGroup) findViewById(R.id.rg_receive_model);
        groupTransportModel = (RadioGroup) findViewById(R.id.rg_transport_model);

        boxAutoSend = (CheckBox) findViewById(R.id.cb_auto_send);
        editAutoTime = (EditText) findViewById(R.id.et_auto_time);
        textSendByte = (TextView) findViewById(R.id.tv_send_byte);
        textReceiveByte = (TextView) findViewById(R.id.tv_receive_byte);
        btnClear = (Button)findViewById(R.id.btn_clear);
    }

    /**
     *设置更改，关闭串口
     */
    public static void closeSerial(){
        if(!isSerialOpen)
            return;
        btnControlSerial.setText("打开串口");
        isSerialOpen = false;
        SerialPortUtil.closeSerialPort();
    }
    /**
     *打开串口
     */
    public void openSerial(){
        try{

//            Log.i("TAG","port="+port+" baud="+baud+" data="+data+" stop="+stop+" check="+check);
            SerialPortUtil.openSerialPort(this,"ttyO3",38400,8,1,'N');
            btnControlSerial.setText("关闭串口");
            isSerialOpen = true;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *获取用户设置的串口数据
     */


    public static void refreshReceive(String data){
        String str = textReceiveContent.getText().toString();
        if(str.length()>500){
            str = "";
        }
        textReceiveContent.setText(str+data);
    }

    public static void refreshSendByte(){
        textSendByte.setText("发送:"+ MyApplication.sendByte);
    }
    public static void refreshReceiveByte(){
        textReceiveByte.setText("接收:"+ MyApplication.receiveByte);
    }

}
