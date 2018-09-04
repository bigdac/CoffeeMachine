package vendingmachine.xr.com.coffeemachine.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
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
import com.xr.database.dao.daoImp.goodsDaoImp;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.adapter.ChoosecoffeAdapter;
import vendingmachine.xr.com.coffeemachine.adapter.MygoodsAdpter;
import vendingmachine.xr.com.coffeemachine.adapter.codeDialog;
import vendingmachine.xr.com.coffeemachine.mqtt.MQService;
import vendingmachine.xr.com.coffeemachine.mqtt.MQTTMessageReveiver;
import vendingmachine.xr.com.coffeemachine.pojo.Order;
import vendingmachine.xr.com.coffeemachine.pojo.goods;
import vendingmachine.xr.com.coffeemachine.utils.AryChangeManager;
import vendingmachine.xr.com.coffeemachine.utils.SerialPortUtil;

public class BuyFragment extends Fragment{
    View view;
    RecyclerView mRecycleView;//声明对象
    MygoodsAdpter adapter;//声明适配器
    RecyclerView recyclerView;
    goodsDaoImp goodsDaoImp;
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
    @BindView(R.id.bt_buy)
    Button bt_buy;
    @BindView(R.id.bt_buy_gm)
    Button bt_buy_gm;
    @BindView(R.id.iv_xq_pic)
    ImageView iv_xq_pic;
    @BindView(R.id.iv_xq_add)
    ImageView iv_xq_add;
    @BindView(R.id.iv_xq_reduce)
    ImageView iv_xq_reduce;
    @BindView(R.id.tv_xq_number)
    TextView tv_xq_number;
    String[] name;
    String[] effect;
    String[] address;
    String[] addressxq;
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
    int c2 = AryChangeManager.stringToHex(command2)[0];

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name = new String[]{"西洋参", "姜紫红茶", "黑枸杞", "参精名仕", "洛神魅丽", "葛藤独醒茶", "普洱花子", "沉香凝神"};
        effect = new String[]{"保护心血管系统；提高免疫力；促进血液活力；", "暖胃御寒，防治感冒。尔雅紫苏，香气迷人",
                "滋补肾阴、清肝明目。增强免疫力，延缓衰老 改善睡眠，治疗近视", "滋补肾精、大补元气增强精子生长， 彰显男士活力",
                "养颜瘦身，放松心情。", "千杯不怕，独自清醒", "降脂祛邪、润通轻盈。", "暖肾纳气，悠然自得"
        };
        address = new String[]{
                "产地", "配方", "产地", "配方", "配方", "配方", "配方", "配方"
        };
        addressxq = new String[]{
                "产地美国，真材实料。", "红茶、姜、紫苏、糖", "产地青海，真材实料。", "人参、玛咖、黄精、蛹虫草", "玫瑰茄、黑枸杞、玫瑰花、山楂、陈皮",
                "藤茶、葛根、胎菊、甘草", "普洱、决明子、山楂、桂花", "沉香叶、老叶茶"
        };
        int[] price = new int[]{10, 10, 10, 10, 10, 10, 10, 10};
        goodsDaoImp = new goodsDaoImp(getActivity());

        list_goods = new ArrayList<>();
        newlist_goods = new ArrayList<>();
//        for (int i = 0; i < 8; i++) {
//            goods good = new goods();
//            good.setGoodsName(name[i]);
//            good.setPrice(price[i]);
//            good.setGoodsId(i);
//            good.setNumber(1);
//            good.setFormulation(addressxq[i]);
//            good.setEfficiency(effect[i]);
//            good.setVal(1);
//            list_goods.add(good);
//            newlist_goods.add(good);
//        }

        running = true;
        if (  goodsDaoImp.findAllgoods().size()>0){
            for (int i =0;i<goodsDaoImp.findAllgoods().size();i++){
                goods good = goodsDaoImp.findAllgoods().get(i);
                list_goods.add(good);
                newlist_goods.add(good);
            }

        }
        try {
            SerialPortUtil.openSerialPort(getActivity(), "ttyO3", 38400, 8, 1, 'N');

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_buy, container, false);
        unbinder = ButterKnife.bind(this, view);


        mRecycleView = (RecyclerView) view.findViewById(R.id.grid);
//        LinearLayoutManager linerLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        mRecycleView.setLayoutManager(linerLayoutManager);
        mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        //获得适配器
//        mRecycleView.addItemDecoration(new SpacesItemDecoration(20));
        adapter = new MygoodsAdpter(getActivity(), list_goods);
        //设置适配器到组件
        mRecycleView.setAdapter(adapter);
        Log.e("test", "onCreateView:--> " + linearLayout1);
//
        service = new Intent(getActivity(), MQService.class);
        getActivity().startService(service);
        isBound = getActivity().bindService(service, connection, Context.BIND_AUTO_CREATE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recy_bringinto);

        //详情页面取消购买
        bt_buy_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout2.setVisibility(View.GONE);
                linearLayout1.setVisibility(View.VISIBLE);
                tv_xq_number.setText("1");
                i = 1;
            }
        });
        IntentFilter intentFilter = new IntentFilter("BuyFragment");

        receiver = new MessageReceiver();

        getActivity().registerReceiver(receiver, intentFilter);
        //详情界面立即购买
        bt_buy_gm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog("请稍候。。。。");
                if (mqService != null) {
                    try {

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("commodityNo", commodityNo);
                        jsonObject.put("name", tv_xq_name.getText());
                        jsonObject.put("number", i);
                        jsonObject.put("price", "0.01");
                        String s = jsonObject.toString();
                        boolean success = mqService.publish("coffee/1234/deal/create", 2, s);
                        Log.i("succ", "-->" + success);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        });

        //多个商品的立即购买
        bt_buy.setOnClickListener(new View.OnClickListener() {
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


        });


        //详情界面添加数量
        tv_xq_number.setText("1");
        iv_xq_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                speechText();
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


        return view;
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
        progressDialog.show();
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

    /**
     * 接收到mqtt的广播
     */

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            order = (Order) intent.getSerializableExtra("message");
            result = intent.getIntExtra("payresult", -1);
            String mess = intent.getStringExtra("mes_new");
            String mesdel = intent .getStringExtra("mes_delgoods");
            Log.e("mess", "onReceive: "+mess );
            if (order != null) {

                if (order.getResult() != -1) {
                    hideProgressDialog();

                    aliCode = order.getAliCode();
                    wxCode = order.getWxCode();
                    payDialog(aliCode, wxCode);

                } else {
                    hideProgressDialog();
                    dia = new Dialog(context, R.style.edit_AlertDialog_style);//设置进入时跳出提示框
                    dia.setContentView(R.layout.dialog_buy);
                    ImageView imageView = (ImageView) dia.findViewById(R.id.iv_pay_success);
                    imageView.setBackgroundResource(R.mipmap.buy_fail);
                    imageView.setImageResource(R.mipmap.getcode_fail);
                    dia.show();
                    dia.setCanceledOnTouchOutside(true); // 设置屏幕点击退出
                    Window w = dia.getWindow();
                    WindowManager.LayoutParams lp = w.getAttributes();
                    lp.x = 0;
                    dia.onWindowAttributesChanged(lp);
                    CountTimer countTimer = new CountTimer(4000, 1000);
                    countTimer.start();

                }


//               new LoadQrAsync().execute();

            }
            //支付结果 1成功 0失败
            if (result != -1) {
                if (result == 0) {
                    if (codeDialog.isShowing()){
                        codeDialog.dismiss();
                    }
//                   提示图片
                    dia = new Dialog(context, R.style.edit_AlertDialog_style);//设置进入时跳出提示框
                    dia.setContentView(R.layout.dialog_buy);
                    ImageView imageView = (ImageView) dia.findViewById(R.id.iv_pay_success);
                    imageView.setBackgroundResource(R.mipmap.buy_fail);
                    imageView.setImageResource(R.mipmap.buy_fail);
                    dia.show();
                    dia.setCanceledOnTouchOutside(true); // 设置屏幕点击退出
                    Window w = dia.getWindow();
                    WindowManager.LayoutParams lp = w.getAttributes();
                    lp.x = 0;
                    dia.onWindowAttributesChanged(lp);
                    CountTimer countTimer = new CountTimer(4000, 1000);
                    countTimer.start();
                } else if (result == 1) {
//                    ((FirstActivity)getActivity()).stopvideo();

//                    if (codeDialog.isShowing()){
//                        codeDialog.dismiss();
//                    }
                    dia = new Dialog(context, R.style.edit_AlertDialog_style);//设置进入时跳出提示框
                    dia.setContentView(R.layout.dialog_buy);
                    ImageView imageView = (ImageView) dia.findViewById(R.id.iv_pay_success);
                    imageView.setBackgroundResource(R.mipmap.buy_sussess);
                    dia.show();
                    dia.setCanceledOnTouchOutside(true); // 设置屏幕点击退出
                    Window w = dia.getWindow();
                    WindowManager.LayoutParams lp = w.getAttributes();
                    lp.x = 0;
                    dia.onWindowAttributesChanged(lp);
                    CountTimer countTimer = new CountTimer(4000, 1000);
                    countTimer.start();
                    /**
                     *
                     * 支付成功给mcu发指令
                     * ***/
                    int jy = AryChangeManager.stringToHex("06")[0];
                    int jy1 = AryChangeManager.stringToHex("01")[0];
                    int jy2 = AryChangeManager.stringToHex("00")[0];
                    int jy3 = AryChangeManager.stringToHex("1E")[0];
                    int jy4 = AryChangeManager.stringToHex("14")[0];
                    int jy5 = AryChangeManager.stringToHex("01")[0];
                    int jy6 = AryChangeManager.stringToHex("2C")[0];
                    String i2 = AryChangeManager.dexToHex(x1 ^ x2 ^ c2 ^ jy ^ jy1 ^ jy2 ^ jy3 ^ jy4 ^ jy5 ^ jy6);
                    String str = machineType + machineAdress + command2 + "06" + "01" + "00" + "1E" + "14" + "01" + "2C" + i2;
                    Log.e("test", "onClick: " + str);
                    if (isHexTransport) {
                        Toast.makeText(getActivity(), "购买1号", Toast.LENGTH_SHORT).show();
                        SerialPortUtil.sendHexSerialPort(AryChangeManager.stringToHex(str));

                    }

                }
            }

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
                    goods good = new goods();
                    good.setGoodsName(name);
                    good.setPrice(price);
                    good.setGoodsId(Long.valueOf(commodityNo));
                    good.setFormulation(formulation);
                    good.setDetailPic(detailPic);
                    good.setEfficiency(efficiency);
                    good.setListPic(listPic);
                    good.setNumber(1);
                    good.setVal(1);
                    goodsDaoImp.insert(good);
                    list_goods.add(good);
                    newlist_goods.add(good);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(mesdel)){
                String commodityNo = "";
                try {
                    JSONObject jsonObject = new JSONObject(mesdel);

                    if (jsonObject.has("commodityNo")){
                        commodityNo=jsonObject.getString("commodityNo");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                Log.e("good3", "onReceive:--> "+commodityNo );
                goods good3 = new goods();
                good3 = goodsDaoImp.findById(Long.valueOf(commodityNo));
                Log.e("good3", "onReceive: "+good3 );
                goodsDaoImp.delete(good3);
                list_goods.remove(good3);
                newlist_goods.remove(good3);
                adapter.notifyDataSetChanged();

            }
        }
    }









    /****
     * 倒计时
     */


    class CountTimer extends CountDownTimer {
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        /**
         * 倒计时过程中调用
         *
         * @param millisUntilFinished
         */
        @Override
        public void onTick(long millisUntilFinished) {

            Log.e("Tag", "倒计时=" + (millisUntilFinished / 1000));
        }

        /**
         * 倒计时完成后调用
         */

        @Override
        public void onFinish() {
            Log.e("Tag", "倒计时完成");
            //设置倒计时结束之后的按钮样式
            dia.dismiss();
        }
    }

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
//        sign=adapter.getSign();
//        choosecoffeAdapter = new ChoosecoffeAdapter(getActivity(),sign);
//        recyclerView.setAdapter(choosecoffeAdapter);
        sign = adapter.getSign();
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
        recyclerView.setAdapter(choosecoffeAdapter);
    }

    /**
     * 设置购买的数量
     **/

    public void setVal(int position, int val) {
        sign = adapter.getSign();
        newmgoods = new ArrayList<>();
        for (int i = 0; i < sign.length; i++) {
            newlist_goods.get(position).setVal(val);
            if (sign[i] == 1) {
                newmgoods.add(newlist_goods.get(i));
                Log.e("qqqeeeeee", "setVal: -->" + list_goods.get(i).getVal());

            }
        }
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

    /**
     * 显示详情界面
     **/
    public void setView() {
        int position = adapter.getPosition();
        Log.e("position", "setView:--> " + position);
        linearLayout1.setVisibility(View.GONE);
        linearLayout2.setVisibility(View.VISIBLE);
        Glide.with(getActivity()).load(list_goods.get(position).getDetailPic())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into( iv_xq_pic);
//        iv_xq_pic.setImageResource(adapter.getImgsid());
        tv_xq_name.setText(list_goods.get(position).getGoodsName());
        tv_xq_adress.setText("配方");
        tv_xq_adressmes.setText(list_goods.get(position).getFormulation());
        tv_xq_gxmes.setText(list_goods.get(position).getEfficiency());
        tv_xq_price.setText(list_goods.get(position).getPrice() + "元");
        commodityNo = String.valueOf(position);
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




    @Override
    public void onDestroy() {

        super.onDestroy();
    }





}
