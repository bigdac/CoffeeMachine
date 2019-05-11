package vendingmachine.xr.com.coffeemachine.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.database.dao.daoImp.SaleGoodsDaoImp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.activity.ChangePriceActivity;
import vendingmachine.xr.com.coffeemachine.http.HttpUtils;
import vendingmachine.xr.com.coffeemachine.pojo.SaleGoods;
import vendingmachine.xr.com.coffeemachine.pojo.goods;

public class ChangePriceAdapter extends RecyclerView.Adapter<ChangePriceAdapter.MyViewHolder> {
    private  Context context;
    private  List<goods> mData;
    private ChangePriceActivity changePriceActivity;
    private String deviceNo;
    private SaleGoodsDaoImp saleGoodsDaoImp;
    public ChangePriceAdapter(Context context, List<goods> list, Activity activity,String deviceNo ){
            saleGoodsDaoImp = new SaleGoodsDaoImp(context.getApplicationContext());
            this.context = context;
            this.mData = list;
            this.changePriceActivity = (ChangePriceActivity) activity;
            this.deviceNo= deviceNo;
    }

    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_changeprice,parent,false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.et_change_name.setText(mData.get(position).getGoodsName());
            holder.et_change_price.setText(mData.get(position).getPrice()+"");
            holder.bt_change_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = holder.et_change_name.getText().toString().trim();
                    String price = holder.et_change_price.getText().toString().trim();
                    if (!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(price)){
                        try {

                            List<SaleGoods> saleGoods = saleGoodsDaoImp.findByGoodsID(mData.get(position).getGoodsId());
                            if (saleGoods.size()==0){
                                showProgressDialog("正在修改请稍候");
                                Map <String,Object> params = new HashMap<>();
                                params.put("deviceNo",deviceNo);
                                params.put("commodityNo",mData.get(position).getGoodsId());
                                params.put("price",price);
                                params.put("commodityName",name);
                                new changePriceAsyncTask().execute(params);
                            }else {
                                Toast.makeText(context,"请先将此货物从设备加料中删除",Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

    }
    private ProgressDialog progressDialog;

    //显示等待dialog
    public void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams params = progressDialog.getWindow().getAttributes();
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.show();
    }


    class changePriceAsyncTask extends AsyncTask<Map<String,Object>,Void,Integer>{


        @Override
        protected Integer doInBackground(Map<String, Object>... maps) {
            int code = -1;
            Map<String,Object> param = maps[0];
            String result = HttpUtils.requestPost("http://47.98.131.11:8087/commodity/changePrice",param);
            Log.e("result", "doInBackground: -->"+result );
            try {
                JSONObject jsonObject = new JSONObject(result);
                code = jsonObject.getInt("resultCode");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return code;

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            switch (integer){
                case 0:
                    progressDialog.dismiss();
                    Toast.makeText( context,"修改成功",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    progressDialog.dismiss();
                    Toast.makeText( context,"修改失败，请重试",Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        EditText et_change_name,et_change_price;
        Button bt_change_up;
        public MyViewHolder(View itemView) {
            super(itemView);
            et_change_name = itemView.findViewById(R.id.et_change_name);
            et_change_price = itemView.findViewById(R.id.et_change_price);
            bt_change_up = itemView.findViewById(R.id.bt_change_up);
        }
    }

}
