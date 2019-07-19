package vendingmachine.xr.com.coffeemachine.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xr.database.dao.daoImp.SaleGoodsDaoImp;
import com.xr.database.dao.daoImp.goodsDaoImp;

import org.json.JSONObject;

import java.util.List;
import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.activity.AddGoodsActivity;
import vendingmachine.xr.com.coffeemachine.mqtt.MQService;
import vendingmachine.xr.com.coffeemachine.pojo.SaleGoods;
import vendingmachine.xr.com.coffeemachine.pojo.goods;
import vendingmachine.xr.com.coffeemachine.utils.ScreenSizeUtils;

public class settingAdapter extends RecyclerView.Adapter<settingAdapter.MyViewHolder> {
    private  Context context;
    private  List<SaleGoods> mData;
    private  List<goods> mData1;
    private goodsDaoImp goodsDaoImp;
    private SaleGoodsDaoImp saleGoodsDaoImp;
    private long goodsId;
    private AddGoodsActivity addGoodsActivity;
    public settingAdapter(Context context, List<SaleGoods> list, Activity activity){
            goodsDaoImp = new goodsDaoImp(context.getApplicationContext());
            saleGoodsDaoImp = new SaleGoodsDaoImp(context.getApplicationContext());
            this.context = context;
            this.mData = list;
            this.addGoodsActivity = (AddGoodsActivity) activity;
    }

    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_setting,parent,false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_sg_pos.setText(mData.get(position).getHd()+"-"+mData.get(position).getHp());
        if (mData.get(position).getGoodsId()!=null){
            /*有货物删除显示，添加隐藏*/
            holder.bt_sg_del.setVisibility(View.VISIBLE);
            holder.bt_sg_add.setVisibility(View.GONE);
            holder.tv_sg_name.setVisibility(View.VISIBLE);
            holder.rl_set_bh.setVisibility(View.VISIBLE);
            holder.rl_set_wd.setVisibility(View.VISIBLE);
            holder.rl_set_ll.setVisibility(View.VISIBLE);
            holder.tv_set_wd.setVisibility(View.VISIBLE);
            holder.tv_set_b.setVisibility(View.VISIBLE);
            holder.tv_set_num.setVisibility(View.VISIBLE);
            holder.bt_set_fill.setVisibility(View.VISIBLE);
            holder.tv_set_ll.setVisibility(View.VISIBLE);
            Long goodsId= mData.get(position).getGoodsId();
            if (goodsId!=null){
                Log.e("GGGGGGFDFFF", "onBindViewHolder: -->"+goodsId );
                goods goods = goodsDaoImp.findById(goodsId);
                String goodsName = goods.getGoodsName();
                holder.tv_sg_name.setText(goodsName);
                holder.et_set_wd.setText(mData.get(position).getTemperature()+"");
                holder.et_set_ll.setText(mData.get(position).getWaterQuantity()+"");
                holder.tv_set_num.setText(mData.get(position).getGoodsNum()+"");
                holder.tv_set_ll.setText("流量:"+mData.get(position).getWaterQuantity()+"ml");
                holder.tv_set_wd.setText("温度："+mData.get(position).getTemperature()+"℃");
            }

        }else {
            /*有货物删除隐藏，添加显示*/
            holder.bt_sg_del.setVisibility(View.GONE);
            holder.bt_sg_add.setVisibility(View.VISIBLE);
            holder.tv_sg_name.setVisibility(View.GONE);
            holder.rl_set_bh.setVisibility(View.GONE);
            holder.rl_set_wd.setVisibility(View.GONE);
            holder.rl_set_ll.setVisibility(View.GONE);
            holder.tv_set_wd.setVisibility(View.GONE);
            holder.tv_set_b.setVisibility(View.GONE);
            holder.tv_set_num.setVisibility(View.GONE);
            holder.bt_set_fill.setVisibility(View.GONE);
            holder.tv_set_ll.setVisibility(View.GONE);
        }

            holder.bt_sg_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialogfin(position);
                }
            });
            holder.bt_sg_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SaleGoods saleGoods = mData.get(position);
                    saleGoods.setGoodsId(null);
                    saleGoodsDaoImp.update(saleGoods);
                    notifyDataSetChanged();
                }
            });

            //补货
            holder.bt_set_bh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String num = holder.et_set_bh.getText().toString().trim();
                    if (!TextUtils.isEmpty(num)) {
                        int count = Integer.valueOf(num);
                        if (count <= 15) {
                            SaleGoods saleGoods = mData.get(position);
                            int num1 = saleGoods.getGoodsNum();
                            if (num1 + count <= 15) {
                                saleGoods.setGoodsNum(num1 + count);
                                holder.tv_set_num.setText(num1 + count+"");
                                saleGoodsDaoImp.update(saleGoods);
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("cupNumber", 1);
                                    jsonObject.put("capsuleNumber", num1);
                                    jsonObject.put("commodityNo", saleGoods.getGoodsId());
                                    jsonObject.put("sugarStatus", 0);
                                    jsonObject.put("waterStatus", 0);
                                    jsonObject.put("switchStatus", 1);
                                    addGoodsActivity.SentMessage(jsonObject);
                                    Toast.makeText(context, "补货成功", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(context, "添加货物后已超过15个", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "添加货物不能大于15个", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            //补满
            holder.bt_set_fill.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    SaleGoods saleGoods = mData.get(position);
                    int num1 =  saleGoods.getGoodsNum();
                    if (num1!=15){
                        saleGoods.setGoodsNum(15);
                        int addNum = 15-num1;
                        holder.tv_set_num.setText("15");
                        saleGoodsDaoImp.update(saleGoods);
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("cupNumber",1);
                            jsonObject.put("capsuleNumber",addNum);
                            jsonObject.put("commodityNo",saleGoods.getGoodsId());
                            jsonObject.put("sugarStatus",0);
                            jsonObject.put("waterStatus",0);
                            jsonObject.put("switchStatus",1);
                            addGoodsActivity.SentMessage(jsonObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(context,"补货成功",Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(context,"已经添加15个胶囊",Toast.LENGTH_SHORT).show();
                    }

                }
            });
            holder.bt_set_sdll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String water  = holder.et_set_ll.getText().toString().trim();
                    if (!TextUtils.isEmpty(water)){
                        if (Integer.valueOf(water)>=100&&Integer.valueOf(water)<=500) {
                        SaleGoods saleGoods = mData.get(position);
                        saleGoods.setWaterQuantity(Integer.valueOf(water));
                        holder.tv_set_ll.setText("流量:"+mData.get(position).getWaterQuantity()+"ml");
                        saleGoodsDaoImp.update(saleGoods);
                        Toast.makeText(context,"流量设置成功",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context,"流量在100-500之间",Toast.LENGTH_SHORT).show();
                     }
                    }else {
                        Toast.makeText(context,"请先填写出水量",Toast.LENGTH_SHORT).show();
                    }

                }
            });
            holder.bt_set_sdwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String temperature  = holder.et_set_wd.getText().toString().trim();
                    if (!TextUtils.isEmpty(temperature)){
                        if (Integer.valueOf(temperature)>0&&Integer.valueOf(temperature)<=100) {
                            SaleGoods saleGoods = mData.get(position);
                            saleGoods.setTemperature(Integer.valueOf(temperature));
                            holder.tv_set_wd.setText("温度：" + mData.get(position).getTemperature() + "℃");
                            Toast.makeText(context, "温度设置成功", Toast.LENGTH_SHORT).show();
                            saleGoodsDaoImp.update(saleGoods);
                        }else {
                            Toast.makeText(context,"温度在0-100之间",Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(context,"请先填写温度",Toast.LENGTH_SHORT).show();
                    }
                }
            });


    }

    Dialog dialog;

    private void customDialogfin(final int pos) {
        dialog  = new Dialog(context, R.style.MyDialog);
        mData1 = goodsDaoImp.findAllgoods();
        View view = View.inflate(context, R.layout.dialog_chooseequ, null);
        TextView tv_choose_qx = (TextView) view.findViewById(R.id.tv_choose_qx);
        final EditText et_hd = view.findViewById(R.id.et_hd);
        final EditText et_hp = view.findViewById(R.id.et_hp);
        RecyclerView rv_choose = (RecyclerView) view.findViewById(R.id.rv_choose);
        rv_choose.setLayoutManager(new LinearLayoutManager(context));
        GoodsListAdapter goodsListAdapter = new GoodsListAdapter(context,mData1);
        rv_choose.setAdapter(goodsListAdapter);
        goodsListAdapter.SetOnItemClick(new GoodsListAdapter.OnItemClickListener() {
            @Override
            public void onclick(View view, int position) {
              goods goods = mData1.get(position);
              long goodsId = goods.getGoodsId();
              SaleGoods saleGoods = mData.get(pos);
              saleGoods.setGoodsId(goodsId);
              saleGoods.setGoodsNum(0);
              saleGoods.setTemperature(75);
              saleGoods.setWaterQuantity(280);
              mData.set(pos,saleGoods);
              saleGoodsDaoImp.update(saleGoods);
              dialog.dismiss();
              notifyDataSetChanged();
            }
        });
        dialog.setContentView(view);
        //使得点击对话框外部不消失对话框
        dialog.setCanceledOnTouchOutside(false);
        //设置对话框的大小
        view.setMinimumHeight((int) (ScreenSizeUtils.getInstance(context).getScreenHeight() * 0.23f));
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenSizeUtils.getInstance(context).getScreenWidth() * 0.75f);
        lp.height = (int) (ScreenSizeUtils.getInstance(context).getScreenHeight() * 0.45f);
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
        tv_choose_qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        dialog.show();

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        Button bt_sg_add,bt_sg_del;
        TextView tv_sg_name,tv_sg_pos,tv_set_wd,tv_set_num,tv_set_b,tv_set_ll;//商品名，位置信息,温度,数量,数量显示
        RelativeLayout rl_set_bh,rl_set_wd,rl_set_ll;//设备加料，设备设定温度,流量设定
        EditText et_set_bh ,et_set_wd,et_set_ll;//设备补货,温度,流量
        Button bt_set_bh,bt_set_sdwd,bt_set_sdll,bt_set_fill;//设备补货,温度,流量,补满
        public MyViewHolder(View itemView) {
            super(itemView);
            bt_sg_add = itemView.findViewById(R.id.bt_sg_add);
            bt_sg_del = itemView.findViewById(R.id.bt_sg_del);
            tv_sg_name = itemView.findViewById(R.id.tv_sg_name);
            tv_sg_pos = itemView.findViewById(R.id.tv_sg_pos);
            rl_set_bh = itemView.findViewById(R.id.rl_set_bh);
            rl_set_wd = itemView.findViewById(R.id.rl_set_wd);
            rl_set_ll = itemView.findViewById(R.id.rl_set_ll);
            tv_set_wd = itemView.findViewById(R.id.tv_set_wd);
            tv_set_num = itemView.findViewById(R.id.tv_set_num);
            et_set_bh = itemView.findViewById(R.id.et_set_bh);
            et_set_wd = itemView.findViewById(R.id.et_set_wd);
            et_set_ll = itemView.findViewById(R.id.et_set_ll);
            bt_set_bh = itemView.findViewById(R.id.bt_set_bh);
            bt_set_sdwd = itemView.findViewById(R.id.bt_set_sdwd);
            bt_set_sdll = itemView.findViewById(R.id.bt_set_sdll);
            bt_set_fill = itemView.findViewById(R.id.bt_set_fill);
            tv_set_b = itemView.findViewById(R.id.tv_set_b);
            tv_set_ll = itemView.findViewById(R.id.tv_set_ll);
        }
    }

}
