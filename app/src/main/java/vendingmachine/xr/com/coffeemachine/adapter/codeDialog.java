package vendingmachine.xr.com.coffeemachine.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.utils.ZXingUtils;

/**
 * Created by win7 on 2018/3/9.
 */

/**
 *
 */
public class codeDialog extends Dialog {

    Context context;
    private String name;
    String aliCode;
    String wxCode;
    @BindView(R.id.iv_alPay)
    ImageView iv_alPay;
    @BindView(R.id.iv_wxPay)
    ImageView iv_wxPay;
    @BindView(R.id.tv_time)
    TextView tv_time;
    public codeDialog(@NonNull Context context,String aliCode,String wxCode) {
        super(context, R.style.MyDialog);
        this.context=context;
        this.aliCode=aliCode;
        this.wxCode=wxCode;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.codedialog);
        ButterKnife.bind(this);



                Bitmap bitmap = ZXingUtils.createQRImage(aliCode, 300, 300,BitmapFactory.decodeResource(context.getResources(), R.mipmap.alpay_bz));
                iv_alPay.setImageBitmap(bitmap);

                Bitmap bitmap1 = ZXingUtils.createQRImage(wxCode, 300, 300,BitmapFactory.decodeResource(context.getResources(), R.mipmap.wxpay_bz));
                iv_wxPay.setImageBitmap(bitmap1);





    }




    @Override
    protected void onStart() {
        super.onStart();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void onClick(View view){
        switch(view.getId()){
//            case R.id.tv_dialog_qx:
//                if (onNegativeClickListener!=null){
//                    onNegativeClickListener.onNegativeClick();
//                }
//                break;
//            case R.id.tv_dialog_qd:
//                if (onPositiveClickListener!=null){
//                    name=et_name.getText().toString();
//                    onPositiveClickListener.onPositiveClick();
//                }
//                break;
        }
    }
    private OnPositiveClickListener onPositiveClickListener;

    public void setOnPositiveClickListener(OnPositiveClickListener onPositiveClickListener) {


        this.onPositiveClickListener = onPositiveClickListener;
    }

    private OnNegativeClickListener onNegativeClickListener;

    public void setOnNegativeClickListener(OnNegativeClickListener onNegativeClickListener) {

        this.onNegativeClickListener = onNegativeClickListener;
    }

    public interface OnPositiveClickListener {
        void onPositiveClick();
    }

    public interface OnNegativeClickListener {
        void onNegativeClick();
    }
}
