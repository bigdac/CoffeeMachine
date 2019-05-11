package vendingmachine.xr.com.coffeemachine.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xr.database.dao.daoImp.SaleGoodsDaoImp;
import com.xr.database.dao.daoImp.goodsDaoImp;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.adapter.settingAdapter;
import vendingmachine.xr.com.coffeemachine.application.MyApplication;
import vendingmachine.xr.com.coffeemachine.http.HttpUtils;
import vendingmachine.xr.com.coffeemachine.mqtt.MQService;
import vendingmachine.xr.com.coffeemachine.pojo.SaleGoods;
import vendingmachine.xr.com.coffeemachine.pojo.goods;
import vendingmachine.xr.com.coffeemachine.utils.NetWorkUtil;

public class AddGoodsActivity extends AppCompatActivity {
    Intent service;
    private boolean isBound;
    SharedPreferences preferences;
    String szImei;
    goodsDaoImp goodsDaoImp;
    List<goods> listGoods;
    List<SaleGoods> saleGoodsList;
    RecyclerView rv_goods_add;
    settingAdapter settingAdapter;
    SaleGoodsDaoImp saleGoodsDaoImp;
    MyApplication application;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgoods);
        service = new Intent(this, MQService.class);
        isBound = this.bindService(service, connection, Context.BIND_AUTO_CREATE);
        application= (MyApplication) getApplication();
        application.addActivity(this);
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        szImei = preferences.getString("szImei", "");
        Map<String,Object> params = new HashMap<>();
        params.put("deviceNo",szImei);
        listGoods= new ArrayList<>();
        goodsDaoImp =new goodsDaoImp(getApplicationContext());
        saleGoodsDaoImp = new SaleGoodsDaoImp(getApplicationContext());
        saleGoodsList = saleGoodsDaoImp.findAllgoods();
        if (saleGoodsList.size()!=20){
            for (int i =0;i<20;i++){
                SaleGoods saleGoods = new SaleGoods();
                saleGoods.setId((long)i);
                int b1 =0;
                int b2 =0;
                if(i<4){
                    b1=i+1;
                    b2=1;
                }else if (i>=4&&i<8){
                    b1=i-3;
                    b2=2;
                }else if (i>=8&&i<12){
                    b1=i-7;
                    b2=3;

                }else if (i>=12&&i<16){
                    b1=i-11;
                    b2=4;
                }else if (i>=16&&i<20){
                    b1=i-15;
                    b2=5;
                }
            /*    else if (i>=20&&i<24){
                    b1=i-19;
                    b2=6;
                }*/
                saleGoods.setHd(b1+"");
                saleGoods.setHp(b2+"");
                saleGoodsDaoImp.insert(saleGoods);
                saleGoodsList.add(saleGoods);
            }
        }
        boolean isConn = NetWorkUtil.isConn(MyApplication.getContext());
        if (isConn){
            new GetGoodsListAsyncTask().execute(params);
        }else {
            Toast.makeText(AddGoodsActivity.this,"无网络连接",Toast.LENGTH_SHORT).show();
        }

        rv_goods_add = findViewById(R.id.rv_goods_add);
        Button bt_goods_back = findViewById(R.id.bt_goods_back);
        bt_goods_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             finish();
            }
        });
        rv_goods_add.setLayoutManager(new GridLayoutManager(this,4));

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

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void SentMessage(JSONObject jsonObject){

                mqService.sentGoodsMes(jsonObject);

    }

    class GetGoodsListAsyncTask extends AsyncTask<Map<String,Object>,Void,String>{

        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String code = "";
            Map<String,Object> param = maps[0];
            String result = HttpUtils.requestPost(HttpUtils.ipAddress+"/device/getCommodityList",param);
            Log.e("result", "doInBackground: -->"+result );
            if (!TextUtils.isEmpty(result)){
                try {
                    JSONObject jsonObject = new JSONObject(result);
                     code = jsonObject.getString("resultCode");
                     if ("0".equals(code)){
                         goodsDaoImp.deleteAll();
                         JSONArray jsonArray = jsonObject.getJSONArray("commodityList");
                         for (int i =0;i<jsonArray.length();i++){
                             goods goods = new goods();
                             JSONObject jsonObject1 =jsonArray.getJSONObject(i);
                             String commodityNo = jsonObject1.getString("commodityNo");
                             String commodityName =jsonObject1.getString("commodityName");
                             String efficiency = jsonObject1.getString("efficiency");
                             double price = jsonObject1.getDouble("price");
                             String formulation =jsonObject1.getString("formulation");
                             String listPicture = jsonObject1.getString("listPicture");
                             String detailPicture = jsonObject1.getString("detailPicture");
                             int total = jsonObject1.getInt("total");
                             goods.setGoodsId(Long.valueOf(commodityNo));
                             goods.setGoodsName(commodityName);
                             goods.setEfficiency(efficiency);
                             goods.setFormulation(formulation);
                             goods.setListPic(listPicture);
                             goods.setDetailPic(detailPicture);
                             goods.setPrice(price);
                             goods.setNumber(total);
                             goodsDaoImp.insert(goods);
                             listGoods.add(goods);
                         }
                     }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return code;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            switch (s){
                case "0":
                    settingAdapter = new settingAdapter(AddGoodsActivity.this,saleGoodsList,AddGoodsActivity.this);
                    rv_goods_add.setAdapter(settingAdapter);
                    break;
                    default:
                        Toast.makeText(AddGoodsActivity.this,"加载失败请重试",Toast.LENGTH_SHORT).show();
                        break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound && connection != null) {
            unbindService(connection);
        }
    }
}
