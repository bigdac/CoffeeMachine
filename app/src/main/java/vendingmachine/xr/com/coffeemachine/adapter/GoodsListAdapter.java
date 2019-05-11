package vendingmachine.xr.com.coffeemachine.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;
import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.pojo.goods;

public class GoodsListAdapter extends RecyclerView.Adapter<GoodsListAdapter.MyViewHolder> {
    private  Context context;
    private  List<goods> mData;
    private OnItemClickListener onItemClickListener;
    public GoodsListAdapter(Context context, List<goods> list){
            this.context = context;
            this.mData = list;
    }

    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_goodslist,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
         holder.tv_list_name.setText((position+1)+":"+mData.get(position).getGoodsName());
         holder.bt_list_add.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      onItemClickListener.onclick(v, position);
                  }
              });
    }
    /**
     * 按钮点击事件对应的接口
     */
    public interface OnItemClickListener {
        public void onclick(View view, int position);
    }
    public void SetOnItemClick(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener ;
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        Button bt_list_add;
        TextView tv_list_name;
        public MyViewHolder(View itemView) {
            super(itemView);
            bt_list_add =itemView.findViewById(R.id.bt_list_add);
            tv_list_name = itemView.findViewById(R.id.tv_list_name);
        }
    }

}
