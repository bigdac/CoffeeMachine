package vendingmachine.xr.com.coffeemachine.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.adapter.CoffeTestAdapter;
import vendingmachine.xr.com.coffeemachine.application.MyApplication;
import vendingmachine.xr.com.coffeemachine.pojo.goods;
import vendingmachine.xr.com.coffeemachine.utils.AryChangeManager;
import vendingmachine.xr.com.coffeemachine.utils.SerialPortUtil;

import static vendingmachine.xr.com.coffeemachine.utils.Utils.addText1;
import static vendingmachine.xr.com.coffeemachine.utils.Utils.clearText1;

public class CoffeeTestActivity extends AppCompatActivity {
    Button bt_cup_back,bt_cup_go,bt_cup_out,bt_cup_lb,bt_cup_lg;//杯子回到原点,杯子平台移到出饮料位置,杯子平台移动到取出位置,落杯，落盖
   /*0:胶囊平台移动到原点
    1：胶囊平台移到1货到
    2：胶囊平台移到2货到
    3：胶囊平台移到3货到
    4：胶囊平台移到4货到
    5：胶囊平台移动到酿造组
    6: 取杯口电磁铁
    */

    Button bt_cup_jnback,bt_cup_go1,bt_cup_go2,bt_cup_go3,bt_cup_go4,bt_cup_make,bt_cup_lgwz,bt_cup_dcf;
    Button tv_coffe_ddlg,tv_coffe_ddyg,tv_coffe_zdyg;
    Button bt_cup_ddcs,bt_cup_dddcf,bt_cup_dddy1,bt_cup_dddy2,bt_cup_st,bt_cup_glzd,bt_cup_glsd;
    static TextView tv_coofe_type ;
    static TextView tv_test_num,tv_test_mes;
    TextView tv_hd_xz;
    public static boolean running = false;
    Button bt_cup_lddd,bt_cup_ldcd,bt_cup_ldzd,bt_cup_cjn;
    RecyclerView rv_gy ;
    CoffeTestAdapter coffeTestAdapter;
    TextView tv_cup_szwd,tv_cup_szll;//当前设置温度，当前设置流量
    EditText et_cup_sdwd,et_cup_sdll;//设置温度,设置流量
    TextView tv_cup_sjwd,tv_cup_sjll;//当前温度,当前流量
    Button bt_cup_bc;//保存
    List<String> list;
    String data1;
    int [] size = new int[]{1,1,1,0,1,0,1,1,0,1,1,1,0,1,1,1,1,1,0,1};
    int Xaxis = -1;//货道
    int Yaxis = -1;//货道
    MyApplication application;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffetest);
        application= (MyApplication) getApplication();
        application.addActivity(this);
        running=true;
        rv_gy = findViewById(R.id.rv_gy);
        list = new ArrayList<>();
        for (int aSize : size) {
            list.add(aSize+"");
        }
        tv_coofe_type = findViewById(R.id.tv_coofe_type);
        rv_gy.setLayoutManager(new GridLayoutManager(this,5));
        coffeTestAdapter = new CoffeTestAdapter(this,list);
        rv_gy.setAdapter(coffeTestAdapter);
        coffeTestAdapter.SetOnItemClick(new CoffeTestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int hp, int hd) {

                    String i2 = AryChangeManager.dexToHex(0x32  ^0x06 ^ 0x04 ^0x03 ^ 0x04  ^ hd ^ hp);
                    String str = "32" + "00" + "06" + "04" + "03" + "04" + "0"+hd + "0"+hp + i2;
                    Log.e("test", "onClick: " + str + "---------" + i2);
                    SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                    getTime();
                    addText(tv_test_num,data1+" :  出胶囊");
                    addText(tv_test_num,data1+" :  发送内容：--->"+str);

            }

            @Override
            public void onLongItemClick(View view, int hp, int hd) {
                String temp = tv_cup_szwd.getText().toString().trim();
                String water = tv_cup_szll.getText().toString().trim();
                String waterHeight = Integer.toHexString(Integer.valueOf(water)/256);
                String waterLow = Integer.toHexString(Integer.valueOf(water)%256);
                String i2 = AryChangeManager.dexToHex(0x32 ^  0x02 ^5^ hd ^ hp ^ Integer.valueOf(temp,16) ^Integer.valueOf(waterHeight,16)^Integer.valueOf(waterLow,16) );
                String str = "32" + "00" + "02" + "05"+"0"+hp + "0"+hd +temp+"0"+waterHeight+waterLow + i2;
                Log.e("testDDDD", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  制作饮料");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });
        Log.e("ZZZZZZZZZAAAAAAA", "onCreate: -->"+coffeTestAdapter.getItemCount() );
        tv_test_num =findViewById(R.id.tv_test_num);
        et_hd = findViewById(R.id.et_hd);
        et_hp = findViewById(R.id.et_hp);
        tv_cup_szwd = findViewById(R.id.tv_cup_szwd);
        tv_cup_szll = findViewById(R.id.tv_cup_szll);
        tv_test_mes =findViewById(R.id.tv_test_mes);
        et_cup_sdwd = findViewById(R.id.et_cup_sdwd);
        et_cup_sdll = findViewById(R.id.et_cup_sdll);
        tv_cup_sjwd = findViewById(R.id.tv_cup_sjwd);
        tv_cup_sjll = findViewById(R.id.tv_cup_sjll);

        /*】
        * 保存流量
        * */
        bt_cup_bc = findViewById(R.id.bt_cup_bc);
        bt_cup_bc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = et_cup_sdwd.getText().toString().trim();
                String water = et_cup_sdll .getText().toString().trim();
                if (!TextUtils.isEmpty(temp)&&!TextUtils.isEmpty(water)){
                    if (Integer.valueOf(temp)>0&&Integer.valueOf(temp)<=100){
                        if (Integer.valueOf(water)>=100&&Integer.valueOf(water)<=500){
                            tv_cup_szwd.setText(temp);
                            tv_cup_szll.setText(water);
                        }else {
                            Toast.makeText(CoffeeTestActivity.this, "流量应在100—500之间", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(CoffeeTestActivity.this, "温度应在0—100之间", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(CoffeeTestActivity.this, "温度和流量不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
        * 新加 取杯口电磁
        * */
        bt_cup_dcf = findViewById(R.id.bt_cup_dcf);
        bt_cup_dcf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i2 = AryChangeManager.dexToHex(0x32^0^ 0x06 ^ 0x04 ^ 0x01^0x04^0^0);
                String str = "32" + "00" + "06" + "04" + "01" + "04" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  启动/断开取杯口电磁铁");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });

        /*
        * x新加  点动落盖
        * */
        tv_coffe_ddlg = findViewById(R.id.tv_coffe_ddlg);
        tv_coffe_ddlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i2 = AryChangeManager.dexToHex(0x32^0^ 0x06 ^ 0x04 ^ 0x06^0^0^0);
                String str = "32" + "00" + "06" + "04" + "06" + "00" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  点动落盖");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });

        /*
        * 点动  压杯盖
        * */
        tv_coffe_ddyg = findViewById(R.id.tv_coffe_ddyg);
        tv_coffe_ddyg .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i2 = AryChangeManager.dexToHex(0x32^0^ 0x06 ^ 0x04 ^ 0x06^0x01^0^0);
                String str = "32" + "00" + "06" + "04" + "06" + "01" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  点动压盖");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });

        /*
        * 新加  自动落杯落盖
         * */
        tv_coffe_zdyg = findViewById(R.id.tv_coffe_zdyg);
        tv_coffe_zdyg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i2 = AryChangeManager.dexToHex(0x32^0^ 0x06 ^ 0x04 ^ 0x06^0x02^0^0);
                String str = "32" + "00" + "06" + "04" + "06" + "02" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  自动落盖 /压盖");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });

        /*
        * 新加 点动抽水
        * */
        bt_cup_ddcs = findViewById(R.id.bt_cup_ddcs);
        bt_cup_ddcs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i2 = AryChangeManager.dexToHex(0x32^0^ 0x06 ^ 0x04 ^ 0x07^0^0^0);
                String str = "32" + "00" + "06" + "04" + "07" + "00" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  点动抽水");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });

        /*
        * 新加点动电磁阀
        *
        * */
        bt_cup_dddcf = findViewById(R.id.bt_cup_dddcf);
        bt_cup_dddcf .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i2 = AryChangeManager.dexToHex(0x32^0^ 0x06 ^ 0x04 ^ 0x07^0x01^0^0);
                String str = "32" + "00" + "06" + "04" + "07" + "01" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  点动电磁阀");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });

        /*
        * 新加  点动低压水箱1
        * */
        bt_cup_dddy1 = findViewById(R.id.bt_cup_dddy1);
        bt_cup_dddy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i2 = AryChangeManager.dexToHex(0x32^0^ 0x06 ^ 0x04 ^ 0x07^0x02^0^0);
                String str = "32" + "00" + "06" + "04" + "07" + "02" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  点动低压水箱1");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });

        /*
        * X新加  点动低压水箱2
        * */
        bt_cup_dddy2 = findViewById(R.id.bt_cup_dddy2);
        bt_cup_dddy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i2 = AryChangeManager.dexToHex(0x32^0^ 0x06 ^ 0x04 ^ 0x07^0x03^0^0);
                String str = "32" + "00" + "06" + "04" + "07" + "03" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  点动低压水箱2");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });
        /*
        * X新加  切换水箱
        * */
        bt_cup_st = findViewById(R.id.bt_cup_st);
        bt_cup_st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i2 = AryChangeManager.dexToHex(0x32^0^ 0x06 ^ 0x04 ^ 0x07^0x04^0^0);
                String str = "32" + "00" + "06" + "04" + "07" + "04" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  水桶切换");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });

        /*
        * 新加 锅炉自动加热
        * */
        bt_cup_glzd = findViewById(R.id.bt_cup_glzd);
        bt_cup_glzd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i2 = AryChangeManager.dexToHex(0x32^0^ 0x06 ^ 0x04 ^ 0x07^0x05^0^0);
                String str = "32" + "00" + "06" + "04" + "07" + "05" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  锅炉自动加热");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });
        /*
         * 新加 锅炉手动加热
         * */
        bt_cup_glsd = findViewById(R.id.bt_cup_glsd);
        bt_cup_glsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i2 = AryChangeManager.dexToHex(0x32^0^ 0x06 ^ 0x04 ^ 0x07^0x06^0^0);
                String str = "32" + "00" + "06" + "04" + "07" + "06" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  锅炉点动加热");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });
        tv_hd_xz = findViewById(R.id.tv_hd_xz);
        TextView tv_hd_1 = findViewById(R.id.tv_hd_11);
        tv_hd_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_hd_xz.setText("1");
            }
        });

        TextView tv_hd_2 = findViewById(R.id.tv_hd_21);
        tv_hd_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_hd_xz.setText("2");
            }
        });
        TextView tv_hd_3 = findViewById(R.id.tv_hd_31);
        tv_hd_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_hd_xz.setText("3");
            }
        });
        TextView tv_hd_4= findViewById(R.id.tv_hd_41);
        tv_hd_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_hd_xz.setText("4");
            }
        });
        TextView tv_hp_1= findViewById(R.id.tv_hp_1);
        tv_hp_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_hp.setText("1");
            }
        });
        TextView tv_hp_2= findViewById(R.id.tv_hp_2);
        tv_hp_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_hp.setText("2");
            }
        });
        TextView tv_hp_3= findViewById(R.id.tv_hp_3);
        tv_hp_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_hp.setText("3");
            }
        });
        TextView tv_hp_4= findViewById(R.id.tv_hp_4);
        tv_hp_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_hp.setText("4");
            }
        });
        TextView tv_hp_5= findViewById(R.id.tv_hp_5);
        tv_hp_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_hp.setText("5");
            }
        });

        bt_cup_back = (Button) findViewById(R.id.bt_cup_back);
        bt_cup_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //机器类型(1Byte)、机器编号(1Byte)、命令(1Byte)、数据包长度(1Byte)、命令类型（1Byte）、功能代码（1Byte）、货道代码（1Byte）、货盘代码（1Byte）、校验码(1Byte)。
                    String i2 = AryChangeManager.dexToHex(0x32^0^ 0x06 ^ 0x04 ^ 0x01^0^0^0);
                    String str = "32" + "00" + "06" + "04" + "01" + "00" +"00"+"00" + i2;
                    Log.e("test", "onClick: " + str+"---------"+i2);
                    SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                    getTime();
                    addText(tv_test_num,data1+" :  杯子回到原点");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });
        bt_cup_go = (Button) findViewById(R.id.bt_cup_go);
        bt_cup_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i2 = AryChangeManager.dexToHex(0x32^0^ 0x06 ^ 0x04 ^ 0x01^0x01^0^0);
                String str = "32" + "00" + "06" + "04" + "01" + "01" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  杯子平台移到出饮料位置");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });

        bt_cup_out = findViewById(R.id.bt_cup_out);
        bt_cup_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String i2 = AryChangeManager.dexToHex(0x32^0^ 0x06 ^ 0x04 ^ 0x01^0x02^0^0);
                String str = "32" + "00" + "06" + "04" + "01" + "02" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  杯子平台移动到取出位置");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });
        bt_cup_lb = findViewById(R.id.bt_cup_lb);
        bt_cup_lb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i2 = AryChangeManager.dexToHex(0x32^0x06 ^ 0x04 ^ 0x01^0x03^0^0);
                String str = "32" + "00" + "06" + "04" + "01" + "03" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  落杯");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });
//        bt_cup_lg = findViewById(R.id.bt_cup_lg);
//        bt_cup_lg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String i2 = AryChangeManager.dexToHex(0x32^0^ 0x06 ^ 0x04 ^ 0x01^0x04^0^0);
//                String str = "32" + "00" + "06" + "04" + "01" + "04" +"00"+"00" + i2;
//                Log.e("test", "onClick: " + str+"---------"+i2);
//                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
//                getTime();
//                addText(tv_test_num,data1+" :  落盖");
//                addText(tv_test_num,data1+" :  发送内容：--->"+str);
//            }
//        });
        bt_cup_lgwz = findViewById(R.id.bt_cup_lgwz);
        bt_cup_lgwz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i2 = AryChangeManager.dexToHex(0x32^0^ 0x06 ^ 0x04 ^ 0x01^0x05^0^0);
                String str = "32" + "00" + "06" + "04" + "01" + "05" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  落盖");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });

        //胶囊平台
        bt_cup_jnback = findViewById(R.id.bt_cup_jnback);
        bt_cup_jnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i2 = AryChangeManager.dexToHex(0x32^ 0x06 ^ 0x04 ^ 0x02^0^0^0);
                String str = "32" + "00" + "06" + "04" + "02" + "00" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  胶囊平台移动到原点");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });
        bt_cup_go1 = findViewById(R.id.bt_cup_go1);
        bt_cup_go1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i2 = AryChangeManager.dexToHex(0x32^ 0x06 ^ 0x04 ^ 0x02^0x01^0^0);
                String str = "32" + "00" + "06" + "04" + "02" + "01" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                addText(tv_test_num,data1+" :  胶囊平台移动到1");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });
        bt_cup_go2 = findViewById(R.id.bt_cup_go2);
        bt_cup_go2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String i2 = AryChangeManager.dexToHex(0x32^0x06 ^ 0x04 ^ 0x02^0x02^0^0);
                String str = "32" + "00" + "06" + "04" + "02" + "02" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  胶囊平台移动到2");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });
        bt_cup_go3 = findViewById(R.id.bt_cup_go3);
        bt_cup_go3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String i2 = AryChangeManager.dexToHex(0x32^0x06 ^ 0x04 ^ 0x02^0x03^0^0);
                String str = "32" + "00" + "06" + "04" + "02" + "03" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  胶囊平台移动到3");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });
        bt_cup_go4 = findViewById(R.id.bt_cup_go4);
        bt_cup_go4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String i2 = AryChangeManager.dexToHex(0x32^ 0x06 ^ 0x04 ^ 0x02^0x04^0^0);
                String str = "32" + "00" + "06" + "04" + "02" + "04" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  胶囊平台移动到4");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });
        bt_cup_make = findViewById(R.id.bt_cup_make);
        bt_cup_make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String i2 = AryChangeManager.dexToHex(0x32 ^ 0x06 ^ 0x04 ^ 0x02 ^ 0x05);
                    String str = "32" + "00" + "06" + "04" + "02" + "05" + "00" + "00"+ i2;
                    Log.e("test", "onClick: " + str + "---------" + i2);
                    SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                     getTime();
                      addText(tv_test_num,data1+" :  胶囊平台移动到酿造组");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);

            }
        });
        //胶囊通道
        bt_cup_lddd = findViewById(R.id.bt_cup_lddd);
        bt_cup_lddd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String x =tv_hd_xz.getText().toString().trim();
                if (!TextUtils.isEmpty(x)) {
                int Xaxis = Integer.valueOf(x, 16);
                String i2 = AryChangeManager.dexToHex(0x32^0x06 ^ 0x04 ^ 0x03^0x01^Xaxis);
                String str = "32" + "00" + "06" + "04" + "03" + "01" +"0"+x+"00"+ i2;
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        Log.e("test", "onClick: " + str+"---------"+i2);
                        SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                        getTime();
                        addText(tv_test_num,data1+" :  胶囊平台点动按下");
                        addText(tv_test_num,data1+" :  发送内容：--->"+str);
                        break;

                    case MotionEvent.ACTION_UP:
                        Log.e("test", "onClick: " + str+"---------"+i2);
                        SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                        getTime();
                        addText(tv_test_num,data1+" :  胶囊平台点动抬起");
                        addText(tv_test_num,data1+" :  发送内容：--->"+str);
                            break;
                }
                }else {
                    Toast.makeText(CoffeeTestActivity.this, "请输入货盘和货道", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        bt_cup_ldcd = findViewById(R.id.bt_cup_ldcd);
        bt_cup_ldcd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x =tv_hd_xz.getText().toString().trim();

                if (!TextUtils.isEmpty(x)) {
                    int Xaxis = Integer.valueOf(x, 16);
                    String i2 = AryChangeManager.dexToHex(0x32  ^ 0x06 ^ 0x04 ^ 0x03 ^ 0x02 ^ Xaxis );
                    String str = "32" + "00" + "06" + "04" + "03" + "02" + "0"+x + "00" + i2;
                    Log.e("test", "onClick: " + str + "---------" + i2);
                    SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                    getTime();
                    addText(tv_test_num,data1+" :  胶囊平台长动");
                    addText(tv_test_num,data1+" :  发送内容：--->"+str);
                }else {
                    Toast.makeText(CoffeeTestActivity.this, "请输入货盘和货道", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_cup_ldzd = findViewById(R.id.bt_cup_ldzd);
        bt_cup_ldzd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x =tv_hd_xz.getText().toString().trim();

                if (!TextUtils.isEmpty(x)){
                    int Xaxis = Integer.valueOf(x,16);
                String i2 = AryChangeManager.dexToHex(0x32^ 0x06 ^ 0x04 ^ 0x03^0x03^Xaxis);
                String str = "32" + "00" + "06" + "04" + "03" + "03" +"0"+x+"00"+ i2;
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                    getTime();
                    addText(tv_test_num,data1+" :  胶囊平台转动");
                    addText(tv_test_num,data1+" :  发送内容：--->"+str);
                    Log.e("test", "onClick: " + str+"---------"+i2);
            }else {
                    Toast.makeText(CoffeeTestActivity.this, "请输入货盘和货道", Toast.LENGTH_SHORT).show();
                }
            }
        });
//
//        bt_cup_cjn = findViewById(R.id.bt_cup_cjn);
//        bt_cup_cjn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String x =et_hd.getText().toString().trim();
//                String y =et_hp.getText().toString().trim();
//                Log.e("SSSSSSSS", "onClick: -->"+x+"....."+y );
//                if (!TextUtils.isEmpty(x)&&!TextUtils.isEmpty(y)) {
//
////                   int Xaxis = AryChangeManager.stringToHex(x)[0];
////                   int Yaxis= AryChangeManager.stringToHex(y)[0];
//                    int Xaxis = Integer.valueOf(x,16);
//                    int Yaxis= Integer.valueOf(y,16);
//                    Log.e("SSSSSSSS1111", "onClick: -->"+Xaxis+"....."+Yaxis );
//                    if (Xaxis != -1 && Yaxis != -1) {
//                        String i2 = AryChangeManager.dexToHex(0x32  ^0x06 ^ 0x04 ^0x03 ^ 0x04  ^ Xaxis ^ Yaxis);
//                        String str = "32" + "00" + "06" + "04" + "03" + "04" + "0"+x + "0"+y + i2;
//                        Log.e("test", "onClick: " + str + "---------" + i2);
//                        SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
//                        getTime();
//                        addText(tv_test_num,data1+" :  出胶囊");
//                        addText(tv_test_num,data1+" :  发送内容：--->"+str);
//                    }
//                }else {
//                    Toast.makeText(CoffeeTestActivity.this, "请输入货盘和货道", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        Button bt_cup_clean = findViewById(R.id.bt_cup_clean);
        bt_cup_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearText(tv_test_num);
                isWell=true;
                hasGoods =true;
                Message message = new Message();
                message.obj = "00";
                mHandler.sendMessage(message);
                String str = "32" + "00" + "03" + "00" + "31" ;
                Log.e("test", "onClick: " + str + "---------" );
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
            }
        });

        Button bt_coffe_gotest = findViewById(R.id.bt_coffe_gotest);
        bt_coffe_gotest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        /*制作饮料*/
//        Button bt_make_coffe = findViewById(R.id.bt_make_coffe);
//        bt_make_coffe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isWell){
//                String x =et_hd.getText().toString().trim();
//                String y =et_hp.getText().toString().trim();
//                if (!TextUtils.isEmpty(x)&&!TextUtils.isEmpty(y)){
//                    int Xaxis = Integer.valueOf(x,16);
//                    int Yaxis= Integer.valueOf(y,16);
//                if (Xaxis!=-1&&Yaxis!=-1){
//                    String i2 = AryChangeManager.dexToHex(0x32  ^ 0x02 ^0x02^Xaxis^Yaxis^0x50^0x96 );
//                    String str = "32" + "00" + "02" + "02"+"0"+x + "0"+y+"50"+"00"+96 + i2;
//                    Log.e("testDDDD", "onClick: " + str+"---------"+i2);
//                    SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
//                    getTime();
//                    addText(tv_test_num,data1+" :  制作饮料");
//                    addText(tv_test_num,data1+" :  发送内容：--->"+str);
//                }
//                }else {
//                    Toast.makeText(CoffeeTestActivity.this,"请输入货盘和货道",Toast.LENGTH_SHORT).show();
//                }
//                }else {
//                    Toast.makeText(CoffeeTestActivity.this,"有故障无法执行",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        Button bt_coffe_fw = findViewById(R.id.bt_coffe_fw);
        bt_coffe_fw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i2 = AryChangeManager.dexToHex(0x32^0x06 ^ 0x04 ^ 0x04^0x01^0^0);
                String str = "32" + "00" + "06" + "04" + "04" + "01" +"00"+"00" + i2;
                Log.e("test", "onClick: " + str+"---------"+i2);
                SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));
                getTime();
                addText(tv_test_num,data1+" :  功能复位");
                addText(tv_test_num,data1+" :  发送内容：--->"+str);
            }
        });
        Button bt_refrash =findViewById(R.id.bt_refrash);
        bt_refrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RefrashGoods();
                getTime();
                addText(tv_test_num,data1+" :  刷新胶囊");
            }
        });

        timeRefrash();
    }

    public  void  getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        data1 = simpleDateFormat.format(date);
    }

    //添加日志
    private void addText(TextView textView, String content) {
        textView.append(content);
        textView.append("\n");
        int offset = textView.getLineCount() * textView.getLineHeight();
        if (offset > textView.getHeight()) {
            textView.scrollTo(0, offset - textView.getHeight());
        }
    }
    //清空日志
    private void clearText(TextView mTextView) {
        mTextView.setText("");

    }


    public void timeRefrash(){
        CountDownTimer countDownTimer = new CountDownTimer(4000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                RefrashGoods();
            }
        }.start();
    }
    EditText et_hp,et_hd;
    String sss;
    public  void RefrashGoods(){

        if (!TextUtils.isEmpty(drinks)&&drinks.length()>5) {
            list.clear();
        String drink1 = drinks.substring(0,2);
        String drink2 = drinks.substring(2,4);
        String drink3 = drinks.substring(4,6);
            int has1 = Integer.valueOf(drink1,16);
            int has2 = Integer.valueOf(drink2,16);
            int has3 = Integer.valueOf(drink3,16);
            sss = addZeroForNum(Integer.toBinaryString(has1),8)  +  addZeroForNum(Integer.toBinaryString(has2),8) +  addZeroForNum(Integer.toBinaryString(has3),8);
            for (int i = 0; i < sss.length(); i++) {
                if (i==5||i==11||i==17||i==23){

                }else {
                    list.add(sss.substring(i, i + 1));
                }

            }
            coffeTestAdapter.notifyDataSetChanged();
            Log.e("DDDDDDDDDDTTTT", "RefrashGoods: -->" + sss + "....");
        }
    }
    public static String addZeroForNum(String str,int strLength) {
        int strLen =str.length();
        if (strLen <strLength) {
            while (strLen< strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);//左补0
//    sb.append(str).append("0");//右补0
                str= sb.toString();
                strLen= str.length();
            }
        }

        return str;
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
                        if (isWell) {
                            if ("01".equals(errorCode)) {
                                Message message = new Message();
                                message.obj = "01";
                                mHandler.sendMessage(message);
                            }
                            if ("02".equals(errorCode)) {
                                Message message = new Message();
                                message.obj = "02";
                                mHandler.sendMessage(message);
                            }
                            if ("03".equals(errorCode)) {
                                Message message = new Message();
                                message.obj = "03";
                                mHandler.sendMessage(message);
                            }
                            if ("04".equals(errorCode)) {
                                Message message = new Message();
                                message.obj = "04";
                                mHandler.sendMessage(message);
                            }
                            if ("05".equals(errorCode)) {
                                Message message = new Message();
                                message.obj = "05";
                                mHandler.sendMessage(message);
                            }
                            if ("06".equals(errorCode)) {
                                Message message = new Message();
                                message.obj = "06";
                                mHandler.sendMessage(message);
                            }
                        }
                    }

//                    if (!"00".equals(result)) {
//                        if ("01".equals(result)) {
//                            Message message = new Message();
//                            message.obj = "result01";
//                            mHandler.sendMessage(message);
//                        }
//                        if ("02".equals(result)) {
//                            Message message = new Message();
//                            message.obj = "result02";
//                            mHandler.sendMessage(message);
//                        }
//                    }
                    if (!"00".equals(nogoods)) {
                        if (hasGoods) {
                            if ("01".equals(nogoods)) {
                                Message message = new Message();
                                message.obj = "nogoods01";
                                mHandler.sendMessage(message);
                                hasGoods = false;
                            }
                            if ("02".equals(nogoods)) {
                                Message message = new Message();
                                message.obj = "nogoods02";
                                mHandler.sendMessage(message);
                                hasGoods = false;
                            }
                            if ("03".equals(nogoods)) {
                                Message message = new Message();
                                message.obj = "nogoods03";
                                mHandler.sendMessage(message);
                                hasGoods = false;
                            }
                            if ("04".equals(nogoods)) {
                                Message message = new Message();
                                message.obj = "nogoods04";
                                mHandler.sendMessage(message);
                                hasGoods = false;
                            }
                            if ("05".equals(nogoods)) {
                                Message message = new Message();
                                message.obj = "nogoods05";
                                mHandler.sendMessage(message);
                                hasGoods = false;
                            }
                            if ("06".equals(nogoods)) {
                                Message message = new Message();
                                message.obj = "nogoods06";
                                mHandler.sendMessage(message);
                                hasGoods = false;
                            }
                            if ("07".equals(nogoods)) {
                                Message message = new Message();
                                message.obj = "nogoods07";
                                mHandler.sendMessage(message);
                                hasGoods = false;
                            }
                            if ("08".equals(nogoods)) {
                                Message message = new Message();
                                message.obj = "nogoods08";
                                mHandler.sendMessage(message);
                                hasGoods = false;
                            }
                            if ("09".equals(nogoods)) {
                                Message message = new Message();
                                message.obj = "nogoods09";
                                mHandler.sendMessage(message);
                                hasGoods = false;
                            }
                        }
                    }
                    addText1(tv_test_mes,"错误码:"+errorCode+"  状态"+nowAction+"  结果:"+result+"  胶囊:"
                            +drinks+"  水糖:"+nogoods);
                    if (tv_test_mes.getText().toString().length()>500){
                        clearText1(tv_test_mes);
                    }
                    Log.e("mes", "refreshReceive: -->" + errorCode + "..." + nowAction + "..." + result+"..."+drinks+"..."+nogoods);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if("00".equals( msg.obj)){
                tv_coofe_type.setText("故障信息（无）");
                isWell=true;
            }
            if("01".equals( msg.obj)){
                tv_coofe_type.setText("落杯异常");
                isWell=false;
            }
            if("02".equals( msg.obj)){
                tv_coofe_type.setText("平台移动异常");
                isWell=false;
            }
            if("03".equals( msg.obj)){
                tv_coofe_type.setText("加热模块异常");
                isWell=false;
            }
            if("04".equals( msg.obj)){
                tv_coofe_type.setText("取杯异常");
                isWell=false;
            }
            if("05".equals( msg.obj)){
                tv_coofe_type.setText("出胶囊异常");
                isWell=false;
            }
            if("06".equals( msg.obj)){
                tv_coofe_type.setText("皮带检测异常");
                isWell=false;
            }

//            if ("result01".equals(msg.obj)){
//                tv_coofe_type.setText("正在出货");
//                isWell=false;
//            }
//            if ("result02".equals(msg.obj)){
//                tv_coofe_type.setText("出货成功");
//                isWell=true;
//            }
            if ("nogoods03".equals(msg.obj)){
                tv_coofe_type.setText("缺杯缺水");
                isWell=false;
            }
            if ("nogoods01".equals(msg.obj)){
                tv_coofe_type.setText("缺杯子");
                isWell=false;
            }
            if ("nogoods02".equals(msg.obj)){
                tv_coofe_type.setText("缺水");
                isWell=false;
            }
            if ("nogoods04".equals(msg.obj)){
                tv_coofe_type.setText("缺糖");
                isWell=false;
            }
            if ("nogoods05".equals(msg.obj)){
                tv_coofe_type.setText("缺糖缺杯");
                isWell=false;
            }
            if ("nogoods06".equals(msg.obj)){
                tv_coofe_type.setText("缺糖缺水");
                isWell=false;
            }
            if ("nogoods07".equals(msg.obj)){
                tv_coofe_type.setText("缺水缺糖缺杯");
                isWell=false;
            }
            if ("nogoods08".equals(msg.obj)){
                tv_coofe_type.setText("缺盖");
                isWell=false;
            }
            if ("nogoods09".equals(msg.obj)){
                tv_coofe_type.setText("缺杯缺盖");
                isWell=false;
            }

        }


    };



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
        running= false;
    }
}
