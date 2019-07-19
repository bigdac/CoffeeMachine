package vendingmachine.xr.com.coffeemachine.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.xr.database.dao.daoImp.SaleGoodsDaoImp;
import com.xr.database.dao.daoImp.goodsDaoImp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pl.droidsonroids.gif.GifDrawable;
import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.activity.FirstActivity;
import vendingmachine.xr.com.coffeemachine.adapter.ChoosecoffeAdapter;
import vendingmachine.xr.com.coffeemachine.adapter.MygoodsAdpter;
import vendingmachine.xr.com.coffeemachine.adapter.codeDialog;
import vendingmachine.xr.com.coffeemachine.application.MyApplication;
import vendingmachine.xr.com.coffeemachine.mqtt.MQService;
import vendingmachine.xr.com.coffeemachine.mqtt.MQTTMessageReveiver;
import vendingmachine.xr.com.coffeemachine.pojo.Order;
import vendingmachine.xr.com.coffeemachine.pojo.SaleGoods;
import vendingmachine.xr.com.coffeemachine.pojo.goods;
import vendingmachine.xr.com.coffeemachine.utils.AryChangeManager;
import vendingmachine.xr.com.coffeemachine.utils.NetWorkUtil;
import vendingmachine.xr.com.coffeemachine.utils.SerialPortUtil;
import vendingmachine.xr.com.coffeemachine.utils.Utils;

import static android.content.Context.DISPLAY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class BuyFragment extends Fragment {
    View view;
    RecyclerView mRecycleView;//声明对象
    MygoodsAdpter adapter;//声明适配器
    RecyclerView recyclerView;
    goodsDaoImp goodsDaoImp;
    SaleGoodsDaoImp saleGoodsDaoImp;
    goods good;
    ChoosecoffeAdapter choosecoffeAdapter;
    Unbinder unbinder;
    boolean checks = false;
    int first = 1;
    int sposition;
    @BindView(R.id.li_buy_1)
    LinearLayout linearLayout1;
    @BindView(R.id.li_buy_2)
    LinearLayout linearLayout2;
    @BindView(R.id.tv_xq_name)
    TextView tv_xq_name;
    @BindView(R.id.tv_xq_adress)
    TextView tv_xq_adress;
    @BindView(R.id.tv_xq_adressmes)
    TextView tv_xq_adressmes;
    @BindView(R.id.tv_xq_gx)
    TextView tv_xq_gx;
    @BindView(R.id.tv_xq_gxmes)
    TextView tv_xq_gxmes;
    @BindView(R.id.tv_xq_price)
    TextView tv_xq_price;
    @BindView(R.id.bt_buy_qx)
    Button bt_buy_qx;
    //    @BindView(R.id.bt_buy)
//    Button bt_buy;
    @BindView(R.id.bt_buy_gm)
    Button bt_buy_gm;
    @BindView(R.id.iv_xq_pic)
    ImageView iv_xq_pic;
    @BindView(R.id.tv_buy_mes)
    TextView tv_buy_mes;
    //    @BindView(R.id.iv_xq_add)
//    ImageView iv_xq_add;
//    @BindView(R.id.iv_xq_reduce)
//    ImageView iv_xq_reduce;
//    @BindView(R.id.tv_xq_number)
//    TextView tv_xq_number;

    int i = 1;
    List<goods> list_goods;
    List<goods> newlist_goods;
    MQService mqService;
    private MQTTMessageReveiver myReceiver;//启动服务
    Intent service;
    private boolean isBound;
    MessageReceiver receiver;
    public static boolean running = false;
    public static boolean isHexReceive = true;//当前为16进制接收
    private static boolean isHexTransport = true;//当前为16进制发送
    String commodityNo;
    String machineType = "32";
    String machineAdress = "00";
    String command1 = "01";
    String command2 = "02";
    int x1 = AryChangeManager.stringToHex(machineType)[0];
    int x2 = AryChangeManager.stringToHex(machineAdress)[0];
    int c1 = AryChangeManager.stringToHex(command1)[0];
    int c2 = AryChangeManager.stringToHex(command2)[0];
    SharedPreferences preferences;
    boolean CanBuy = false;
    String szImei;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goodsDaoImp = new goodsDaoImp(getActivity());
        saleGoodsDaoImp = new SaleGoodsDaoImp(getActivity());
        running = true;
        list_goods = new ArrayList<>();
        newlist_goods = new ArrayList<>();
        List<SaleGoods> saleGoods1=saleGoodsDaoImp.findAllgoods();
        Log.i("saleGoods1","MMMMM__>"+saleGoods1);
        for (int i =0 ;i<20;i++){
            SaleGoods saleGoods = saleGoodsDaoImp.findById(i);
            if (saleGoods!=null){
                Long goodsId = saleGoods.getGoodsId();
                if (goodsId!=null){
                    goods goods = goodsDaoImp.findById(goodsId);
                    goods good = new goods();
                    good.setNumber(saleGoods.getGoodsNum());
                    good.setHd(saleGoods.getHd());
                    good.setHp(saleGoods.getHp());
                    good.setTemperature(saleGoods.getTemperature());
                    good.setWaterQuantity(saleGoods.getWaterQuantity());
                    good.setPrice(goods.getPrice());
                    Log.e("DDDDDDDSSSSSSS", "onCreate: "+goods.getPrice());
                    good.setHasGoods(goods.getHasGoods());
                    good.setDetailPic(goods.getDetailPic());
                    good.setListPic(goods.getListPic());
                    good.setEfficiency(goods.getEfficiency());
                    good.setFormulation(goods.getFormulation());
                    good.setGoodsName(goods.getGoodsName());
                    list_goods.add(good);
                    newlist_goods.add(good);

                }
            }
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_buy, container, false);
        CanBuy =false;
        unbinder = ButterKnife.bind(this, view);
        preferences = getActivity().getSharedPreferences("my", MODE_PRIVATE);
        szImei = preferences.getString("szImei", "");
        mRecycleView = (RecyclerView) view.findViewById(R.id.grid);
        mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        //获得适配器
        adapter = new MygoodsAdpter(getActivity(), list_goods);
        //设置适配器到组件
        mRecycleView.setAdapter(adapter);
        Log.e("test", "onCreateView:--> " + linearLayout1);
//
        service = new Intent(getActivity(), MQService.class);
        isBound = getActivity().bindService(service, connection, Context.BIND_AUTO_CREATE);
//        recyclerView = (RecyclerView) view.findViewById(R.id.recy_bringinto);

        //详情页面取消购买
        bt_buy_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout2.setVisibility(View.GONE);
                linearLayout1.setVisibility(View.VISIBLE);
//                tv_xq_number.setText("1");
                i = 1;
//                ((FirstActivity)getActivity()).startAD();
//                ((FirstActivity)getActivity()).stopBuyGoods();

            }
        });
        IntentFilter intentFilter = new IntentFilter("BuyFragment");
        receiver = new MessageReceiver();
        getActivity().registerReceiver(receiver, intentFilter);
        //详情界面立即购买
        bt_buy_gm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBuyMess();
                if (Utils.isFastClick()) {
                        showProgressDialog("请稍候。。。。");
//                    ((FirstActivity)getActivity()).stopAD();
                        if (mqService != null) {
                            try {
//                            mqService.stopDownload();
                                boolean isConn = NetWorkUtil.isConn(MyApplication.getContext());
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("commodityNo", commodityNo);
                                jsonObject.put("name", tv_xq_name.getText());
                                jsonObject.put("number", i);
                                jsonObject.put("price", 0.01);
                                String s = jsonObject.toString();
                                Log.e("PPPPPPPPPPPP", "onClick: -->" + s);
                                boolean success = mqService.publish("coffee/" + szImei + "/deal/create", 2, s);
                                Log.i("succ", "-->" + success);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }


                }
            }
        });

        //多个商品的立即购买（保留以后再做）
       /* bt_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = 0;
                for (int i = 0; i < adapter.getSign().length; i++) {
                    int sign[] = adapter.getSign();
                    for (int a = 0; a < sign.length; a++) {
                        if (sign[a] == 1) {
                            size = 1;
                        }
                    }
                }
                Log.e("size", "onClick: " + size);
                if (size > 0) {
                    showProgressDialog("请稍候。。。。");
                    if (mqService != null) {
                        try {
                            goods good = null;
                            JSONObject jsonObject = new JSONObject();
                            JSONArray jsonArrayid = new JSONArray();
                            JSONArray jsonArrayname = new JSONArray();
                            JSONArray jsonArraynum = new JSONArray();
                            JSONArray jsonArrayprice = new JSONArray();
                            for (int i = 0; i < newmgoods.size(); i++) {
                                good = newmgoods.get(i);
                                jsonArrayid.put(good.getGoodsId());
                                jsonArrayname.put(good.getGoodsName());
                                jsonArraynum.put(good.getVal());
                                jsonArrayprice.put(0.01);
                                Log.e("goods", "onClick:--> " + good.getGoodsName() + "..." + good.getVal());
                            }
                            jsonObject.put("commodityNo", jsonArrayid);
                            jsonObject.put("number", jsonArraynum);
                            jsonObject.put("name", jsonArrayname);
                            jsonObject.put("price", jsonArrayprice);
                            String s = jsonObject.toString();
                            boolean success = mqService.publish("coffee/1234/deal/create", 2, s);
                            Log.i("succ", "-->" + success);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                } else {
                    Toast.makeText(getActivity(), "请选择商品", Toast.LENGTH_SHORT).show();
                }

            }


        });*/


        //详情界面添加数量（保留以后再做）
     /*   tv_xq_number.setText("1");
        iv_xq_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textToVoice(getActivity(),"帅个毛线");
                i = i + 1;
                tv_xq_number.setText(String.valueOf(i));
            }
        });
        iv_xq_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i > 1) {
                    i = i - 1;
                    tv_xq_number.setText(String.valueOf(i));
                }

            }
        });
*/

        hasgoodCount hasgoodCount = new hasgoodCount(4000,1000);
        hasgoodCount.start();

        return view;
    }

    class hasgoodCount extends CountDownTimer{

        public hasgoodCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {

            Log.e("goods", "倒计时: -->"+(l)/1000 );
        }

        @Override
        public void onFinish() {
            hasgoods();
        }
    }

    public void SeterrorCode (int code ,String errorCode){
        if (code==1){
            tv_buy_mes.setText(errorCode);
            tv_buy_mes.setTextColor(getActivity().getResources().getColor(R.color.red_normal));
            CanBuy=false;
        }
        if (code==2){
            tv_buy_mes.setText(errorCode);
            tv_buy_mes.setTextColor(getActivity().getResources().getColor(R.color.black));
            CanBuy=true;
        }
        if (code==3){
            tv_buy_mes.setText(errorCode);
            tv_buy_mes.setTextColor(getActivity().getResources().getColor(R.color.black));
            CanBuy=false;
        }
    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
//            outRect.left = space;
//            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
//            if (parent.getChildPosition(view) == 0)
//                outRect.top = space;
        }
    }

    private ProgressDialog progressDialog;

    //显示等待dialog
    public void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(message);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams params = progressDialog.getWindow().getAttributes();
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        params.y = 360;
        mHandler.sendEmptyMessageDelayed(MSG_DISMISS_DIALOG, 8000);
        progressDialog.show();
    }

    private static int MSG_DISMISS_DIALOG = 0;
    private static int QQQ_DISMISS_DIALOG = 1;
    private static int UI_DISMISS_DIALOG = 2;
    private static int OUTGOOD_DISMISS_DIALOG = 3;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            if(MSG_DISMISS_DIALOG == msg.what){
                if(null != progressDialog){
                    if(progressDialog.isShowing()){
                        Log.i("TTTTTT", "handler get mesage");
                        progressDialog.dismiss();
                        mqService.starThread();
                    }
                }
            }
            if(QQQ_DISMISS_DIALOG == msg.what){
                if(null != dia){
                    if(dia.isShowing()){
                        Log.i("TTTTTT", "handler get mesage");
                        dia.dismiss();

                    }
                }
            }
            if(UI_DISMISS_DIALOG == msg.what){
                if (downloadThread!=null){
                    downloadThread.setStop(true);
                    downloadThread=null;
                }
                if (dia!=null&&dia.isShowing()){
                    dia.dismiss();
                }
//                ((FirstActivity)getActivity()).startAD();
//                tv_xq_number.setText("1");
                i = 1;

            }
            if(OUTGOOD_DISMISS_DIALOG == msg.what){
                linearLayout2.setVisibility(View.GONE);
                linearLayout1.setVisibility(View.VISIBLE);
//                提示图片
                dia = new Dialog(getActivity(), R.style.edit_AlertDialog_style);//设置进入时跳出提示框
                dia.setContentView(R.layout.dialog_goodsout);
                ImageView imageView = (ImageView) dia.findViewById(R.id.iv_pay_success);
                try {
                    GifDrawable gifDrawable1=new GifDrawable(getResources(),R.mipmap.goodsout);
                    gifDrawable1.start();
                    imageView.setImageDrawable(gifDrawable1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dia.show();
                dia.setCanceledOnTouchOutside(false); // 设置屏幕点击退出
                Window w = dia.getWindow();
                WindowManager.LayoutParams lp = w.getAttributes();
                lp.x = 0;
                dia.onWindowAttributesChanged(lp);
                if (posz!=-1){
                    goods goods= list_goods.get(posz);
                    SaleGoods saleGoods = saleGoodsDaoImp.findByHdHp(goods.getHd(),goods.getHp());
                    int goodsNum = saleGoods.getGoodsNum();
                    if (goodsNum>0) {
                        saleGoods.setGoodsNum(goodsNum - 1);
                        saleGoodsDaoImp.update(saleGoods);
                    }
                }
                ((FirstActivity)getActivity()).hideADpage();
                hasgoodCount hasgoodCount = new hasgoodCount(3000,1000);
                hasgoodCount.start();
            }
        }


    };

    /*判断是否有货*/
    String sss;
  /*  public void hasgoods(){
//        String drinks = FirstActivity.getDrinks();
        String drinks = "FFFFFF";
        if (!TextUtils.isEmpty(drinks)){
            String drink1 = drinks.substring(0,2);
            String drink2 = drinks.substring(2,4);
            String drink3 = drinks.substring(4,6);
            int has1 = Integer.valueOf(drink1,16);
            int has2 = Integer.valueOf(drink2,16);
            int has3 = Integer.valueOf(drink3,16);
            String s1=addZeroForNum(Integer.toBinaryString(has1),8);
            String s2=addZeroForNum(Integer.toBinaryString(has2),8);
            String s3=addZeroForNum(Integer.toBinaryString(has3),8);
            sss =  s1 +  s2 + s3;
            String z = "";
            Log.e("GGGGGGTTTTT", "hasgoods: -->"+sss+">>>"+sss.length() );
//            for (int i = 0;i<sss.length();i++){
//                if (i<6){
//                    list_goods.get(4*i).setHasgoods(Integer.valueOf(sss.substring(i,i+1)));
//                    goodsDaoImp.update(list_goods.get(4*i));
//                }else if (i>=6&&i<12){
//                    list_goods.get(4*(i-6)+1).setHasgoods(Integer.valueOf(sss.substring(i,i+1)));
//                    Log.e("GGGGGGGGTTTTT!", "hasgoods: ==>"+sss.substring(i,i+1) );
//                    goodsDaoImp.update(list_goods.get(4*(i-6)+1));
//                }else if (i>=12&&i<18){
//                    list_goods.get(4*(i-12)+2).setHasgoods(Integer.valueOf(sss.substring(i,i+1)));
//                    goodsDaoImp.update(list_goods.get(4*(i-12)+2));
//                }else if (i>=18&&i<24){
//                    list_goods.get(4*(i-18)+3).setHasgoods(Integer.valueOf(sss.substring(i,i+1)));
//                    goodsDaoImp.update(list_goods.get(4*(i-18)+3));
//                }
//            }
            for (int i = 0 ;i<list_goods.size();i++){
              goods goods = list_goods.get(i);
              int  hd = Integer.valueOf(goods.getHd());
              int  hp = Integer.valueOf(goods.getHp());
              goods.setHasGoods(Integer.valueOf(sss.substring(6*(hd-1)+(hp-1),6*(hd-1)+hp)));
              list_goods.set(i,goods);
              goodsDaoImp.update(goods);
            }
            adapter.notifyDataSetChanged();

        }


    }*/

    public void hasgoods(){
        String drinks = FirstActivity.getDrinks();
//        String drinks = "FFFFFF";
        if (!TextUtils.isEmpty(drinks)){
            String drink1 = drinks.substring(0,2);
            String drink2 = drinks.substring(2,4);
            String drink3 = drinks.substring(4,6);
            int has1 = Integer.valueOf(drink1,16);
            int has2 = Integer.valueOf(drink2,16);
            int has3 = Integer.valueOf(drink3,16);
            String s1=addZeroForNum(Integer.toBinaryString(has1),8);
            String s2=addZeroForNum(Integer.toBinaryString(has2),8);
            String s3=addZeroForNum(Integer.toBinaryString(has3),8);
            sss =  s1 +  s2 + s3;
            String z = "";
            Log.e("GGGGGGTTTTT", "hasgoods: -->"+sss+">>>"+sss.length() );
//            for (int i = 0;i<sss.length();i++){
//                if (i<6){
//                    list_goods.get(4*i).setHasgoods(Integer.valueOf(sss.substring(i,i+1)));
//                    goodsDaoImp.update(list_goods.get(4*i));
//                }else if (i>=6&&i<12){
//                    list_goods.get(4*(i-6)+1).setHasgoods(Integer.valueOf(sss.substring(i,i+1)));
//                    Log.e("GGGGGGGGTTTTT!", "hasgoods: ==>"+sss.substring(i,i+1) );
//                    goodsDaoImp.update(list_goods.get(4*(i-6)+1));
//                }else if (i>=12&&i<18){
//                    list_goods.get(4*(i-12)+2).setHasgoods(Integer.valueOf(sss.substring(i,i+1)));
//                    goodsDaoImp.update(list_goods.get(4*(i-12)+2));
//                }else if (i>=18&&i<24){
//                    list_goods.get(4*(i-18)+3).setHasgoods(Integer.valueOf(sss.substring(i,i+1)));
//                    goodsDaoImp.update(list_goods.get(4*(i-18)+3));
//                }
//            }
            for (int i = 0 ;i<list_goods.size();i++){
                goods goods = list_goods.get(i);
                int  hd = Integer.valueOf(goods.getHd());
                int  hp = Integer.valueOf(goods.getHp());
                goods.setHasGoods(Integer.valueOf(sss.substring(6*(hd-1)+(hp-1),6*(hd-1)+hp)));
                list_goods.set(i,goods);
                goodsDaoImp.update(goods);
            }
            adapter.notifyDataSetChanged();

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
    //隐藏等待dialog
    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    codeDialog codeDialog;

    private void payDialog(String aliCode, String wxCode) {
        codeDialog = new codeDialog(getActivity(), aliCode, wxCode);

        codeDialog.setOnNegativeClickListener(new codeDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClick() {
//                dialog.dismiss();
            }
        });
        codeDialog.setOnPositiveClickListener(new codeDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClick() {

            }
        });
        WindowManager.LayoutParams params = codeDialog.getWindow().getAttributes();
        codeDialog.getWindow().setGravity(Gravity.BOTTOM);
        params.y = 270;
        codeDialog.setCanceledOnTouchOutside(true);
        codeDialog.setCancelable(true);
        codeDialog.show();
        initstar();
    }

    Order order = null;
    int result = -1;
    String aliCode;
    String wxCode;
    Dialog dia;
    boolean buy = false;
    boolean isgoing ; // 判断是否出货
    DownloadThread downloadThread;//读取出货线程
    /**
     * 接收到mqtt的广播
     */

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            order = (Order) intent.getSerializableExtra("message");
            result = intent.getIntExtra("payresult", -1);
            String mess = intent.getStringExtra("mes_new");
            String mesdel = intent.getStringExtra("mes_delgoods");
            Log.e("mess", "onReceive: " + mess);
            Log.e("DDDDDDDDD", "onReceive: -->"+result );

            if (order != null) {
                    int a =0;
                    int b = a;
                    if (order.getResult() !=-1&&progressDialog.isShowing()) {
                    hideProgressDialog();
                    aliCode = order.getAliCode();
                    wxCode = order.getWxCode();
                    if (!TextUtils.isEmpty(aliCode)&&(!TextUtils.isEmpty(wxCode))){
                        payDialog(aliCode, wxCode);
                    }
                } else if (order.getResult() == -1&&progressDialog.isShowing()){
                    hideProgressDialog();
                    dia = new Dialog(context, R.style.edit_AlertDialog_style);//设置进入时跳出提示框
                    dia.setContentView(R.layout.dialog_buy);
                    ImageView imageView = (ImageView) dia.findViewById(R.id.iv_pay_success);
                    imageView.setImageResource(R.mipmap.getcode_fail);
                    dia.show();
                    dia.setCanceledOnTouchOutside(false); // 设置屏幕点击退出
                    Window w = dia.getWindow();
                    WindowManager.LayoutParams lp = w.getAttributes();
                    lp.x = 0;
                    dia.onWindowAttributesChanged(lp);
                    mHandler.sendEmptyMessageDelayed(QQQ_DISMISS_DIALOG, 4000);


                }

            }
            //支付结果 1成功 0失败
            if (result != -1&&codeDialog.isShowing()) {
                if (codeDialog != null && codeDialog.isShowing()) {
                    codeDialog.dismiss();
                }
                if (result == 0) {

//                   提示图片
                    dia = new Dialog(context, R.style.edit_AlertDialog_style);//设置进入时跳出提示框
                    dia.setContentView(R.layout.dialog_buy);
                    ImageView imageView = (ImageView) dia.findViewById(R.id.iv_pay_success);
                    imageView.setImageResource(R.mipmap.buy_fail);
                    dia.show();
                    dia.setCanceledOnTouchOutside(true); // 设置屏幕点击退出
                    Window w = dia.getWindow();
                    WindowManager.LayoutParams lp = w.getAttributes();
                    lp.x = 0;
                    dia.onWindowAttributesChanged(lp);
                    mHandler.sendEmptyMessageDelayed(QQQ_DISMISS_DIALOG, 4000);
                } else if (result == 1) {
//                    ((FirstActivity)getActivity()).stopvideo();

//                    if (codeDialog.isShowing()){
//                        codeDialog.dismiss();
//                    }
                    /**
                     * 支付成功时将语音归零
                     * **/

                    StartMakedrinks(context);

                }
            }
//
            if (!TextUtils.isEmpty(mess)) {
                try {
                    JSONObject jsonObjectnew = new JSONObject(mess);
                    String commodityNo = "";
                    String name = "";
                    String formulation = "";
                    String efficiency = "";
                    String listPic = "";
                    String detailPic = "";
                    int price = 0;
                    if (jsonObjectnew.has("commodityNo")) {
                        commodityNo = jsonObjectnew.getString("commodityNo");
                    }
                    if (jsonObjectnew.has("price")) {
                        price = jsonObjectnew.getInt("price");
                    }
                    if (jsonObjectnew.has("name")) {
                        name = jsonObjectnew.getString("name");
                    }
                    if (jsonObjectnew.has("formulation")) {
                        formulation = jsonObjectnew.getString("formulation");
                    }
                    if (jsonObjectnew.has("efficiency")) {
                        efficiency = jsonObjectnew.getString("efficiency");
                    }
                    if (jsonObjectnew.has("listPic")) {
                        listPic = jsonObjectnew.getString("listPic");
                    }
                    if (jsonObjectnew.has("detailPic")) {
                        detailPic = jsonObjectnew.getString("detailPic");
                    }
                    List<SaleGoods> saleGoods = saleGoodsDaoImp.findByGoodsID(Long.valueOf(commodityNo));
                    goods good = goodsDaoImp.findById(Long.valueOf(commodityNo));
                    if (good!=null&&saleGoods.size()!=0){
                        good.setGoodsName(name);
                        good.setPrice(price);
                        good.setGoodsId(Long.valueOf(commodityNo));
                        good.setFormulation(formulation);
                        good.setDetailPic(detailPic);
                        good.setEfficiency(efficiency);
                        good.setListPic(listPic);
                        good.setNumber(1);
                        good.setVal(1);
                        goodsDaoImp.update(good);
                        int pos = list_goods.indexOf(good);
                        list_goods.set(pos,good);
                        newlist_goods.add(pos,good);
                        adapter.notifyDataSetChanged();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(mesdel)) {
                String commodityNo = "";
                try {
                    JSONObject jsonObject = new JSONObject(mesdel);

                    if (jsonObject.has("commodityNo")) {
                        commodityNo = jsonObject.getString("commodityNo");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("good3", "onReceive:--> " + commodityNo);
                goods good3 = new goods();
                good3 = goodsDaoImp.findById(Long.valueOf(commodityNo));
                Log.e("good3", "onReceive: " + good3);
                goodsDaoImp.delete(good3);
                list_goods.remove(good3);
                newlist_goods.remove(good3);
                adapter.notifyDataSetChanged();

            }
        }
    }
    Dialog diaer;
    private void StartMakedrinks(Context context) {
        diaer = new Dialog(context, R.style.edit_AlertDialog_style);//设置进入时跳出提示框
        diaer.setContentView(R.layout.dialog_buy);
        ImageView imageView =   diaer.findViewById(R.id.iv_pay_success);
        imageView.setBackgroundResource(R.mipmap.buy_sussess);
        diaer.show();
        textToVoice(getActivity(), "正在出货");
        diaer.setCanceledOnTouchOutside(false); // 设置屏幕点击退出
        Window w = diaer.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        diaer.onWindowAttributesChanged(lp);
        diaer.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (isgoing){
                    textToVoice(getActivity(), "出货成功");


//                                try {
//                                    JSONObject jsonObject = new JSONObject();
//                                    jsonObject.put("orderNo",order.getOrderNo());
//                                    jsonObject.put("result",1);
//                                    mqService.sentGoodsSuccess(jsonObject);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
                }else {
                    textToVoice(getActivity(), "出货失败");
                    downloadThread.setStop(true);
                    downloadThread=null;

//                    try {
//                        JSONObject jsonObject = new JSONObject();
//                        jsonObject.put("orderNo",order.getOrderNo());
//                        jsonObject.put("result",0);
//                        mqService.sentGoodsSuccess(jsonObject);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });
        /*
         * 支付成功给mcu发指令
         * */
//        downloadThread = new DownloadThread();
//        downloadThread.start();测试

        String action = FirstActivity.getNowAction();
        Log.e("action", "onReceive: --" + action);
        if (!TextUtils.isEmpty(action)){

//      mqService.starThread();
                if (action.equals("00")) {
                    sendBuyMess();
                }
                downloadThread = new DownloadThread();
                downloadThread.start();
//                ((FirstActivity)getActivity()).stopAD();


        }


    }


    class DownloadThread extends Thread {
        boolean isStop = false;
        int j =1;
        int count = 0;
        public void setStop(boolean stop) {
            isStop = stop;
        }

        @Override
        public void run() {
            super.run();
            while (!isStop) {
//               这里是读文件流 和写文件流的操作
                try {

                    Thread.sleep(2000);
                    String result = FirstActivity.getResult();
//                    String result = "01";
                    Log.e("FFFFFFFFTTTT", "run: -->读取状态" + result);
//                    count++;
//                    if (count==10){
//                        result="02";
//                    }
//                    if (count==15){
//                        result="00";
//                    }

                    if ("01".equals(result)) {
                     //  正在出货
                        count=1;
                    }
                    if ("02".equals(result)) {
                            isgoing=true;
                            diaer.dismiss();
                            if (j==1) {
                                Message message = new Message();
                                message.what = OUTGOOD_DISMISS_DIALOG;
                                //然后将消息发送出去
                                mHandler.sendMessage(message);
                                j=0;
                            }
                    }
                    if ("03".equals(result)) {
                            isgoing=false;
                            i = 1;
                            dia.dismiss();
                    }
                    if (count==1){
                        if ("00".equals(result)){
                            Message message=new Message();
                            message.what=UI_DISMISS_DIALOG;
                            //然后将消息发送出去
                            mHandler.sendMessage(message);
                            count=0;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


    int b1=0;
    int b2 = 0;
    public void sendBuyMess() {
        b1=0;
        b2=0;
        Log.e("position", "sendBuyMess: -->"+posz );
        int size1 = list_goods.size();
        goods goods= list_goods.get(posz);
        b1 =Integer.valueOf(goods.getHd());
        b2 =Integer.valueOf(goods.getHp());
        String temperature1 =  Integer.toHexString(goods.getTemperature()) ;
        String waterHeight = Integer.toHexString(goods.getWaterQuantity()/256);
        String waterLow = Integer.toHexString(goods.getWaterQuantity()%256);
        Log.e("position", "sendBuyMess: -->"+posz+goods.getHd()+"....."+goods.getHp() );
        String i2 = AryChangeManager.dexToHex(x1 ^ x2 ^ c2 ^5^ b1 ^ b2 ^ Integer.valueOf(temperature1,16)^Integer.valueOf(waterHeight,16)^Integer.valueOf(waterLow,16) );
        String str = machineType + machineAdress + command2 + "05"+"0"+b1 + "0"+b2 +temperature1+"0"+waterHeight+waterLow + i2;
            Log.e("str", "sendBuyMess: -->"+str );
        Log.e("strzzz", "sendBuyMess: -->"+temperature1+"...."+ Integer.valueOf(temperature1,16)+"...."+waterHeight+"..."+waterLow );

        if (isHexTransport) {

            SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));

        }}

    /**
     * 绑定service
     */
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
    int[] sign;
    int mp;
    List<goods> mgoods;
    List<goods> newmgoods;

    /**
     * 下部的购买列表
     ***/
    public void setAAA(int position) {
       /* sign = adapter.getSign();
        mgoods = new ArrayList<>();
        for (int i = 0; i < sign.length; i++) {
            if (sign[i] == 1) {
                mgoods.add(list_goods.get(i));
            }
        }

        Log.e("qqqqqSSS", mgoods.size() + "??");
        for (int i = 0; i < mgoods.size(); i++) {
            Log.e("qqqqqSSS", mgoods.get(i).getPrice() + "??");
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        choosecoffeAdapter = new ChoosecoffeAdapter(getActivity(), mgoods);
        recyclerView.setAdapter(choosecoffeAdapter);*/
    }

    /**
     * 设置购买的数量
     **/

    public void setVal(int position, int val) {
    /*    sign = adapter.getSign();
        newmgoods = new ArrayList<>();
        for (int i = 0; i < sign.length; i++) {
            newlist_goods.get(position).setVal(val);
            if (sign[i] == 1) {
                newmgoods.add(newlist_goods.get(i));
                Log.e("qqqeeeeee", "setVal: -->" + list_goods.get(i).getVal());


            }
        }*/
    }

    /**
     * 支付二维码的倒计时功能
     */
    Timer timer;
    TimerTask task;
    int recLen = 180;

    private void initstar() {//二维码的倒计时
        final TextView tv_time = codeDialog.findViewById(R.id.tv_time);
        if (codeDialog.isShowing()) {
            Log.e("Show", "initstar:show ");
        }
//        final TextView tv_time = (TextView)getActivity().findViewById(R.id.tv_time);
        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {   // UI thread
                    @Override
                    public void run() {
                        recLen--;
                        tv_time.setText(recLen + "s");
                        if (codeDialog.isShowing() == false) {
                            timer.cancel();
                            recLen = 180;
                        }
                        if (recLen == 0) {
                            timer.cancel();
                            codeDialog.dismiss();
                            mqService.starThread();
                            recLen = 180;
                            if (codeDialog.isShowing()) {
                                Log.e("Show", "initstar:show1 ");
                            }
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);    // timeTask

    }

public boolean getCanBuy(){
        return CanBuy;
}

    /**
     * 显示详情界面
     **/
    int posz=-1;
    public void setView() {
        Log.e("BBBBBBDDDDDDD", "setView: -->"+CanBuy );
//        if (CanBuy) {
            int position = adapter.getPosition();
            posz = position;
            Log.e("position", "setView:--> " + position + "。。。" + posz);
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(list_goods.get(position).getDetailPic())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(iv_xq_pic);
//        iv_xq_pic.setImageResource(adapter.getImgsid());
            tv_xq_name.setText(list_goods.get(position).getGoodsName());
            tv_xq_adress.setText("配方");
            tv_xq_adressmes.setText(list_goods.get(position).getFormulation());
            tv_xq_gxmes.setText(list_goods.get(position).getEfficiency());
            tv_xq_price.setText(list_goods.get(position).getPrice() + "元");
//        commodityNo = String.valueOf(position);
            commodityNo = list_goods.get(position).getGoodsId() + "";
//            StartMakedrinks(getActivity());
//        ((FirstActivity)getActivity()).stopAD();
//        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        running = false;
        if (isBound && connection != null) {
            getActivity().unbindService(connection);
        }
        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
        if (downloadThread!=null){
            downloadThread=null;
        }
    }


    public static void textToVoice(Context context, String text) {
        //1.创建SpeechSynthesizer对象, 第一个参数上下文,第二个参数：本地合成时传InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(context, null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        //3.开始合成,第一个参数就是转成声音的文字,可以自定义  第二个参数是合成监听器对 象。 我们不需要对声音有什么特殊处理,就传null
        mTts.startSpeaking(text, null);
    }





}
