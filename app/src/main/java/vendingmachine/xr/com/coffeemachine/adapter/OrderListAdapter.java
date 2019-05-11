package vendingmachine.xr.com.coffeemachine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.pojo.OrderList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {
    private  Context context;
    private  List<OrderList> mData;

    public OrderListAdapter(Context context, List<OrderList> list){
            this.context = context;
            this.mData = list;
    }

    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_orderlist,parent,false));
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
         holder.tv_list_name.setText(mData.get(position).getCommodityName());
        holder.tv_list_num.setText(mData.get(position).getOrderNo());
        holder.tv_list_price.setText(mData.get(position).getPrice());
        holder.tv_list_type.setText(mData.get(position).getPayType());
        holder.tv_list_success.setText(mData.get(position).getPayResult());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_list_name,tv_list_num,tv_list_price,tv_list_type,tv_list_success;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_list_name = itemView.findViewById(R.id.tv_list_name);
            tv_list_num = itemView.findViewById(R.id.tv_list_num);
            tv_list_price = itemView.findViewById(R.id.tv_list_price);
            tv_list_type = itemView.findViewById(R.id.tv_list_type);
            tv_list_success = itemView.findViewById(R.id.tv_list_success);
        }
    }

}
