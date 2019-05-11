package vendingmachine.xr.com.coffeemachine.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xr.database.dao.daoImp.goodsDaoImp;

import vendingmachine.xr.com.coffeemachine.activity.FirstActivity;
import vendingmachine.xr.com.coffeemachine.pojo.goods;
import java.util.List;

import vendingmachine.xr.com.coffeemachine.R;

public class MygoodsAdpter  extends RecyclerView.Adapter<MyViewHolder> {
    private LayoutInflater inflater;
    private Context mContext;
    private List<goods> mDatas;
    private goods goods ;
    int i = 1;
    private int mPosition = -1;

    int hasgood=-1;
    //创建构造参数
    public MygoodsAdpter(Context context , List<goods> datas){
        this.mContext = context;
        this.mDatas = datas;
        inflater = LayoutInflater.from(context);

        sign=new int[datas.size()+1];
        for(int i=0;i<datas.size()+1;i++)
            sign[i]=0;
    }

    //创建ViewHolder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_by_topitem , parent , false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }


    private int first=1;
    //绑定ViewHolder
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position ){
        //为textview 赋值

        final goods good = mDatas.get(position);
        holder.tv_name.setText(good.getGoodsName()) ;
//        holder.iv_goods.setImageResource(imgsid[position]);
        String url=good.getListPic();
        Glide.with(mContext).load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
               .override(200,202)
                .into( holder.iv_goods);
        holder.tv_price.setText(String.valueOf( mDatas.get(position).getPrice()));
        final boolean []open =new boolean[] {false} ;

//        holder.rl_goodscheck.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (open[0]) {
//                    holder.iv_cheeks.setImageResource(R.mipmap.imgno);
//                    open[0] = false;
//                    sign[position]=0;
//                    ((FirstActivity)mContext).myClick(position);
//                    ((FirstActivity)mContext).myClick2(position,0);
//                } else {
//                    open [0]= true;
//                    sign[position]=1;
//                    holder.iv_cheeks.setImageResource(R.mipmap.imgdh);
//                    Log.e("good", "onClick: -->"+good.getPosition() );
//                    ((FirstActivity)mContext).myClick(position);
//                    ((FirstActivity)mContext).myClick2(position,1);
//                }
//
//            }
//
//        });
        hasgood= mDatas.get(position).getHasGoods();
        if (hasgood==0){
            holder.tv_buy_bh.setVisibility(View.VISIBLE);
            holder.tv_bz1.setVisibility(View.GONE);
            holder.tv_buy_price.setVisibility(View.GONE);
            holder.iv_goods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"等待补货中。。。",Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            holder.tv_buy_bh.setVisibility(View.GONE);
            holder.tv_bz1.setVisibility(View.VISIBLE);
            holder.tv_buy_price.setVisibility(View.VISIBLE);
            holder.iv_goods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPosition = position;
                    ((FirstActivity)mContext).myClick1();
                    Log.e("PPPPPPPDDDDDD", "onClick: -->"+mPosition );
                }
            });
        }

    }
    @Override
    public int getItemCount() {
        //Log.i("TAG", "mDatas "+mDatas);

        return mDatas.size();

    }

   public int getPosition (){
        return mPosition;
   }


    //新增item
    public void addData(int pos){

        notifyItemInserted(pos);
    }

    //移除item
    public void deleateData(int pos){
        mDatas.remove(pos);
        notifyItemRemoved(pos);
    }


    int[] sign;

    public int[] getSign() {
        return sign;
    }
}


class MyViewHolder extends RecyclerView.ViewHolder  {

    TextView tv_name,tv_price,tv_xq_number,tv_buy_bh,tv_buy_price,tv_bz1;
    ImageView iv_goods,iv_xq_add,iv_xq_reduce;
//    ImageView iv_cheeks;
//    RelativeLayout rl_goodscheck;
    @SuppressLint("CutPasteId")
    public MyViewHolder(View itemView) {
        super(itemView);

        tv_name = (TextView) itemView.findViewById(R.id.tv_goods_name);
        iv_goods= (ImageView) itemView.findViewById(R.id.iv_goods_pic);
//        iv_cheeks = (ImageView) itemView.findViewById(R.id.iv_goods_check);
        tv_price= (TextView) itemView.findViewById(R.id.tv_buy_price);
//        rl_goodscheck=(RelativeLayout)itemView.findViewById(R.id.rl_goodscheck);
        tv_buy_bh = (TextView) itemView.findViewById(R.id.tv_buy_bh);
        tv_buy_price = itemView.findViewById(R.id.tv_buy_price);
        tv_bz1 = itemView.findViewById(R.id.tv_bz1);
    }



}
