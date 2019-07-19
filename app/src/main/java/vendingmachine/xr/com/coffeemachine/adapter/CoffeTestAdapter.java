package vendingmachine.xr.com.coffeemachine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import vendingmachine.xr.com.coffeemachine.R;

public class CoffeTestAdapter extends RecyclerView.Adapter<CoffeTestAdapter.MyViewHolder> {
    private  Context context;
    private  List<String> mData;
    private  OnItemClickListener onItemClickListener;
    public CoffeTestAdapter(Context context, List<String> list){
            this.context = context;
            this.mData = list;
    }

    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_coffetest,parent,false));
    }
    int hp;
    int hd;
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//            holder.tv_coffe_num.setText(position+1+"");
        if(position<5){
            holder.tv_coffe_num.setText("1-"+(position+1));

        }else if (position>=5&&position<10){
            holder.tv_coffe_num.setText("2-"+(position-4));
        }else if (position>=10&&position<15){
            holder.tv_coffe_num.setText("3-"+(position-9));
        }else {
            holder.tv_coffe_num.setText("4-"+(position-14));
        }
//        holder.tv_coffe_num.setText(position+1+"");
            if ("1".equals(mData.get(position))){
                holder.rl_coffe.setBackgroundColor(context.getResources().getColor(R.color.green));
            }else if ("0".equals(mData.get(position) )){
                holder.rl_coffe.setBackgroundColor(context.getResources().getColor(R.color.red_normal));
            }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position<5){
                    hd = 1 ;
                    hp = position+1;
                }else if (position>=5&&position<10){
                    hd = 2 ;
                    hp = position-4;
                }else if (position>=10&&position<15){
                    hd = 3 ;
                    hp = position-9;

                }else {
                    hd = 4 ;
                    hp = position-14;
                }
                onItemClickListener.onItemClick(v,hp,hd);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(position<5){
                    hd = 1 ;
                    hp = position+1;
                }else if (position>=5&&position<10){
                    hd = 2 ;
                    hp = position-4;
                }else if (position>=10&&position<15){
                    hd = 3 ;
                    hp = position-9;

                }else {
                    hd = 4 ;
                    hp = position-14;
                }
                onItemClickListener.onLongItemClick(v,hp,hd);
                return true;
            }
        });
    }

    public   interface  OnItemClickListener {
        void onItemClick(View view,int hp ,int hd);
        void onLongItemClick(View view , int hp ,int hd);

    }
    public void SetOnItemClick( OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener ;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout rl_coffe;
        TextView tv_coffe_num;
        public MyViewHolder(View itemView) {
            super(itemView);
            rl_coffe= itemView.findViewById(R.id.rl_coffe);
            tv_coffe_num = itemView.findViewById(R.id.tv_coffe_num);
        }
    }

    interface  setOncLicked{}


}
