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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xr.database.dao.daoImp.goodsDaoImp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.adapter.ChangePriceAdapter;
import vendingmachine.xr.com.coffeemachine.application.MyApplication;
import vendingmachine.xr.com.coffeemachine.http.HttpUtils;
import vendingmachine.xr.com.coffeemachine.mqtt.MQService;
import vendingmachine.xr.com.coffeemachine.pojo.goods;
import vendingmachine.xr.com.coffeemachine.utils.NetWorkUtil;

public class ChangePriceActivity extends AppCompatActivity {
    Intent service;
    RecyclerView rv_change_price ;
    String szImei;
    private boolean isBound;
    List<goods> listGoods;
    SharedPreferences preferences;
    MyApplication application;
    ChangePriceAdapter changePriceAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeprice);
        service = new Intent(this, MQService.class);
        isBound = this.bindService(service, connection, Context.BIND_AUTO_CREATE);
        listGoods = new ArrayList<>();
        Button bt_change_back = findViewById(R.id.bt_change_back);
        bt_change_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        application= (MyApplication) getApplication();
        application.addActivity(this);
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        szImei = preferences.getString("szImei", "");
        rv_change_price = findViewById(R.id.rv_change_price);
        boolean isConn = NetWorkUtil.isConn(MyApplication.getContext());
        if (isConn){
            Map<String,Object> params = new HashMap<>();
            params.put("deviceNo",szImei);
            new GetGoodsListAsyncTask().execute(params);
        }else {
            Toast.makeText(ChangePriceActivity.this,"无网络连接",Toast.LENGTH_SHORT).show();
        }
        rv_change_price.setLayoutManager(new LinearLayoutManager(this));
        changePriceAdapter = new ChangePriceAdapter(this,listGoods,this,szImei);
        rv_change_price.setAdapter(changePriceAdapter);

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



    class GetGoodsListAsyncTask extends AsyncTask<Map<String,Object>,Void,String> {

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
                            Log.e("BBBBBBBAAAAA", "doInBackground: -->"+price+">>>>>"+goods.getPrice() );
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
                    changePriceAdapter.notifyDataSetChanged();
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
