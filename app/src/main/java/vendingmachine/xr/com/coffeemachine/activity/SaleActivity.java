package vendingmachine.xr.com.coffeemachine.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.adapter.OrderListAdapter;
import vendingmachine.xr.com.coffeemachine.application.MyApplication;
import vendingmachine.xr.com.coffeemachine.http.HttpUtils;
import vendingmachine.xr.com.coffeemachine.pojo.OrderList;

public class SaleActivity extends AppCompatActivity {
    private TimePickerView pvCustomTime;
    TextView tv_choose_day,tv_choose_day1;
    String data,data1;
    SharedPreferences preferences;
    String szImei;
    List<OrderList> orderLists;
    String commodityName;
    TextView tv_choose,tv_choose_money,tv_success,tv_fail;
    RecyclerView rv_list;
    OrderListAdapter orderListAdapter;
    MyApplication application;
    int chooseTime=0;
    long dateFirst = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        application= (MyApplication) getApplication();
        application.addActivity(this);
        preferences = getSharedPreferences("my", MODE_PRIVATE);
        szImei = preferences.getString("szImei", "");
        orderLists= new ArrayList<>();
        tv_choose_day = (TextView) findViewById(R.id.tv_choose_day);
        tv_choose_day1 =findViewById(R.id.tv_choose_day1);
        tv_choose = findViewById(R.id.tv_choose);
        tv_choose_money =findViewById(R.id.tv_choose_money);
        rv_list = findViewById(R.id.rv_list);
        tv_success = findViewById(R.id.tv_success);
        tv_fail = findViewById(R.id.tv_fail);
        initCustomTimePicker();
        Map<String,Object> param = new HashMap<>();
        param.put("deviceNo",szImei);
        new GetSaleListAsyncTask().execute(param);
        Button bt_choose = findViewById(R.id.bt_choose);
        bt_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvCustomTime.show();
            }
        });
        Button bt_choose_back = findViewById(R.id.bt_choose_back);
        bt_choose_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rv_list.setLayoutManager(new LinearLayoutManager(this));
        orderListAdapter= new OrderListAdapter(this,orderLists);
        rv_list.setAdapter(orderListAdapter);
    }
    private void initCustomTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH), selectedDate.get(Calendar.DAY_OF_MONTH));
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调

                if (chooseTime==0){
                    tv_choose_day.setText("起始时间："+getTime(date));
                    dateFirst = date.getTime();
                    data = getTime(date);
                    chooseTime=1;
                }else if (chooseTime==1){
                    Log.e("DDDDDDDDD", "onTimeSelect: --》"+dateFirst+"....."+date.getTime() );
                    if (dateFirst<=date.getTime()) {
                        tv_choose_day1.setText("结束时间" + getTime(date));
                        data1 = getTime(date);
                        Map<String, Object> param = new HashMap<>();
                        param.put("deviceNo", szImei);
                        param.put("startDate", data);
                        param.put("endDate", data1);
                        new GetSaleListAsyncTask().execute(param);
                    }else {
                        Toast.makeText(SaleActivity.this,"结束时间需大于起始时间",Toast.LENGTH_SHORT).show();
                    }
                    chooseTime=0;
                }

            }
        })
                /*.setType(TimePickerView.Type.ALL)//default is all
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentTextSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
               /*.setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)*/
                /*.animGravity(Gravity.RIGHT)// default is center*/
                .setTitleBgColor(Color.WHITE)
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        TextView ivCancel = (TextView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setContentTextSize(18)
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setType(new boolean[]{ true, true, true,false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 0, 0, 0)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .build();

    }
    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    String totalAmount,totalCount,successCount,failCount ;

    class GetSaleListAsyncTask extends AsyncTask<Map<String,Object>,Void,String>{

        @Override
        protected String doInBackground(Map<String, Object>... maps) {
            String code = "";
            Map<String,Object> param = maps[0];
            String result = HttpUtils.postOkHpptRequest("http://47.98.131.11:8087/device/getOrderList",param);
            Log.e("result", "doInBackground: -->"+result );
            if (!TextUtils.isEmpty(result)){
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getString("resultCode");
                    totalAmount = jsonObject.getString("totalAmount");
                    totalCount = jsonObject.getString("totalCount");
                    successCount = jsonObject.getString("successCount");
                    failCount = jsonObject.getString("failCount");
                    if ("0".equals(code)) {
                        orderLists.clear();
                        JSONArray jsonArray = jsonObject.getJSONArray("orderList");
                        for (int j =0;j<jsonArray.length();j++){
                            JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                commodityName = jsonObject1.getString("commodityName");
                                String payResult = jsonObject1.getString("payResult");
                                String orderNo = jsonObject1.getString("orderNo");
                                String payType = jsonObject1.getString("payType");
                                String price  = jsonObject1.getString("price");
                                OrderList orderList = new OrderList();
                                orderList.setPayResult(payResult);
                                orderList.setOrderNo(orderNo);
                                orderList.setPayType(payType);
                                orderList.setPrice(price);
                                orderList.setCommodityName(commodityName);
                                orderLists.add(orderList);
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
                case  "0":
                    tv_choose.setText("交易总数："+totalCount);
                    tv_choose_money.setText("成交金额"+totalAmount);
                    tv_success.setText("交易成功数："+successCount);
                    tv_fail.setText("交易失败数："+failCount);
                    Log.e("DDDDDD", "onPostExecute: -->"+orderLists.size() );
                    orderListAdapter.notifyDataSetChanged();
                    if ("0".equals(totalCount)){
                        Toast.makeText(SaleActivity.this,"暂时无数据",Toast.LENGTH_SHORT).show();
                    }
                    break;
                    default:

                        break;
            }
        }
    }

}
