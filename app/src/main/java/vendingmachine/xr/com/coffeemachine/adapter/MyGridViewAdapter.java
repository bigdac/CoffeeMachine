package vendingmachine.xr.com.coffeemachine.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import vendingmachine.xr.com.coffeemachine.R;

public class MyGridViewAdapter extends BaseAdapter {
    Context context;
    boolean isCheck;// 选中
    Boolean boo[] = {false, false, false, false, false,false, false, false};

    int first = 1 ;
    int imgsid[] = {R.mipmap.img1,R.mipmap.img2,R.mipmap.img3,R.mipmap.img4,R.mipmap.img5
    ,R.mipmap.img6,R.mipmap.img7,R.mipmap.img8};
    public MyGridViewAdapter(Context context) {
        this.context = context;
        isCheck = false;
    }

    @Override
    public int getCount() {
        return imgsid.length;
    }

    @Override
    public Object getItem(int position) {
        return imgsid[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));// 设置ImageView对象布局
            imageView.setAdjustViewBounds(true);// 设置边界对齐
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);// 设置刻度的类型
            imageView.setPadding(8, 8, 8, 8);// 设置间距
        } else {
            imageView = (ImageView) convertView;
        }

            imageView.setImageDrawable(makeBmp(imgsid[position], boo[position]));



        // imageView.setImageResource(imgsid[position]);//为ImageView设置图片资源
        return imageView;
    }

    private LayerDrawable makeBmp(int id, boolean isChosen) {
        Bitmap bitmap = ((BitmapDrawable) context.getResources()
                .getDrawable(id)).getBitmap();

        // 根据isChosen来选取对勾的图片
        Bitmap seletedBmp;
        if (isChosen == true) {
            seletedBmp = BitmapFactory.decodeResource(context.getResources(),R.mipmap.imgdh
                    );
        } else {
            seletedBmp = BitmapFactory.decodeResource(context.getResources(),R.mipmap.imgno
                    );
        }

        // 使 Drawable叠加层（合成图片)
        Drawable[] array = new Drawable[2];
        array[0] = new BitmapDrawable(bitmap);
        array[1] = new BitmapDrawable(seletedBmp);
        // 层图形对象
        LayerDrawable mLayerDrawable = new LayerDrawable(array);// 参数为上面的Drawable数组
        /*
         * Specify modifiers to the bounds for the drawable[index].索引号 left += l
         * top += t; right -= r; bottom -= b;
         */
        mLayerDrawable.setLayerInset(0, 0, 0, 0, 0); // 第一个参数0代表数组的第一个元素
        mLayerDrawable.setLayerInset(1, 120, 10, 10, 120);// 第一个参数1代表数组的第二个元素
        return mLayerDrawable; // 返回合成后的图
    }
    /*
     * 被点击的时候调用,改变点击后的状态
     */
    public void changeState(int position) {

        if (isCheck == false) {
            if (position < boo.length) {
                boo[position] = !boo[position];
                isCheck = !isCheck;
            }
        } else {
            if (position < boo.length) {
                boo[position] = !boo[position];
                isCheck = !isCheck;
            }
        }
        // 调用适配器的更新状态方法
        notifyDataSetChanged();
    }
    public  void  view (){
        first=1;
        notifyDataSetChanged();
    }
    public  void  disview (){
       first=2;
        notifyDataSetChanged();
    }
}
