package vendingmachine.xr.com.coffeemachine.adapter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.os.IBinder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.activity.FirstActivity;
import vendingmachine.xr.com.coffeemachine.pojo.goods;

//快递列表适配器

public class ChoosecoffeAdapter extends RecyclerView.Adapter<ChoosecoffeAdapter.MyViewHolder> /*implements View.OnClickListener*/ {
    private Context context;
    private List<goods> data;
    private ButtonInterface buttonInterface;
    int[] i  ;
    private int defItem = -1;
    private OnItemListener onItemListener;
    //    private AdapterView.OnItemClickListener mOnItemClickListener;
    private int type = 0;
    private String shopId;
    private int clicked;

    Intent service;
    Boolean isBound;
    public ChoosecoffeAdapter(Context context, List<goods> list) {
        this.context = context;
        this.data = list;


    }



    public interface OnItemListener {
        void onClick(View v, int pos, String projectc);
    }

    public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }


    /**
     * 按钮点击事件需要的方法
     */
    public void buttonSetOnclick(ButtonInterface buttonInterface) {
        this.buttonInterface = buttonInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ButtonInterface {
        public void onclick(View view, int position);

        public void onLongClick(int position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.fragment_buy_buttomitem, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
         holder.tv_name.setText(data.get(position).getGoodsName());
         holder.tv_number.setText(String.valueOf(data.get(position).getVal()));

         holder.img_add.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 int val=data.get(position).getVal();
                val++;
                data.get(position).setVal(val);

                holder.tv_number.setText(String.valueOf(val));
                 ((FirstActivity)context).myClick2(position,val);
             }
         });
        holder.img_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.get(position).getVal()>1){
                    int val=data.get(position).getVal();
                    val--;
                    data.get(position).setVal(val);
                    holder.tv_number.setText(String.valueOf(val));
                    ((FirstActivity)context).myClick2(position,val);
                }


            }
        });
//        tv_time = (TextView) view.findViewById(R.id.tv_clock_time);
//        tv_day1= (TextView) view.findViewById(R.id.tv_clock_week1);
//        img_kg = (ImageView) view.findViewById(R.id.iv_clock_kg);
//        img_kg.setTag("open");
//        rl_d1 = (RelativeLayout) view.findViewById(R.id.rl_le_commonitem);
//            rl_d1= (RelativeLayout) view.findViewById(R.id.rl_house_it2);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                        Intent intent = new  Intent(context,RenameHourseActivity.class);
//                        intent.putExtra("houseName",houseName);
//                        intent.putExtra("houseAddress",houseAddress);
//                        startActivity(intent);
//
//                }
//            });



    }


//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.iv_clock_kg:
//
//                break;
//
//
//        }
//    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView img_add , img_reduce;

        TextView tv_name,tv_number;

        public MyViewHolder(View view) {
            super(view);


            tv_name = (TextView) view.findViewById(R.id.tv_buy_name);
            tv_number= (TextView) view.findViewById(R.id.tv_buy_number);
            img_add = (ImageView) view.findViewById(R.id.iv_buy_add);
            img_reduce = (ImageView) view.findViewById(R.id.iv_buy_reduce);

//            rl_d1= (RelativeLayout) view.findViewById(R.id.rl_house_it2);
////            itemView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////
////                        Intent intent = new  Intent(context,RenameHourseActivity.class);
////                        intent.putExtra("houseName",houseName);
////                        intent.putExtra("houseAddress",houseAddress);
////                        startActivity(intent);
////
////                }
////            });
//            img_kg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if ("open".equals(img_kg.getTag())) {
//                        img_kg.setImageResource(R.mipmap.bt_kgg);
//                        img_kg.setTag("close");
//                    }else if ("close".equals(img_kg.getTag())){
//                        img_kg.setImageResource(R.mipmap.bt_kg);
//                        img_kg.setTag("open");
//                    }
//                }
//            });
        }
    }
}