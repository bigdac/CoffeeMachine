package vendingmachine.xr.com.coffeemachine.activity;





import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.xr.database.dao.daoImp.ADDaoImp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.fragment.BuyFragment;
import vendingmachine.xr.com.coffeemachine.fragment.MediaplayFragment;
import vendingmachine.xr.com.coffeemachine.fragment.XqFragment;
import vendingmachine.xr.com.coffeemachine.mqtt.MQService;
import vendingmachine.xr.com.coffeemachine.mqtt.MQTTMessageReveiver;
import vendingmachine.xr.com.coffeemachine.pojo.AD;

public class FirstActivity extends AppCompatActivity {
    MessageReceiver receiver;
    BuyFragment buyFragment;
    MediaplayFragment videoFragment;
    XqFragment xqFragment;
    ADDaoImp adDaoImp;
    AD ad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        adDaoImp=new ADDaoImp(getApplicationContext());
        ad = new AD();
        AD ad1= new AD();
        List<AD> ads = adDaoImp.findAllad();
        if (ads.size()>0){
           ad1 = ads.get(0);
           String s1 = ad1.getAdVideo();
           String s2 = ad1.getAdPictire();
           if (s1!=null){
            videourl=s1;
           }
           if (s2!=null){
               picurl =s2;
           }
        }

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        IntentFilter intentFilter = new IntentFilter("FirstActivity");

        receiver = new MessageReceiver();

       registerReceiver(receiver, intentFilter);

        videoFragment = new MediaplayFragment();
        if (buyFragment==null){
            buyFragment = new BuyFragment();
        }
        xqFragment = new XqFragment();

        FragmentManager  fragmentManager = getSupportFragmentManager();
        FragmentTransaction   transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.left_fragment,xqFragment,"leftListFragment");//添加leftListFragment并为其设置tag leftListFragment
        transaction.add(R.id.right_fragment,buyFragment,"rightListFragment");////添加rightListFragment并为其设置tag rightListFragment
        transaction.commit();

        if (ad1.getAdPictire()!=null){
           adpic();
        }else if (ad1.getAdVideo()!=null){
            advideo();
        }


    }
    public void  advideo(){
        FragmentManager  fragmentManager1 = getSupportFragmentManager();
        FragmentTransaction   transaction1 = fragmentManager1.beginTransaction();
        MediaplayFragment videoFragment = new MediaplayFragment();
        transaction1.replace(R.id.left_fragment,videoFragment,"leftListFragment");//添加leftListFragment并为其设置tag leftListFragment
        transaction1.commit();
    }
    public void  adpic(){
        FragmentManager  fragmentManager1 = getSupportFragmentManager();
        FragmentTransaction   transaction1 = fragmentManager1.beginTransaction();
        XqFragment xqFragment = new XqFragment();
        transaction1.replace(R.id.left_fragment,xqFragment,"leftListFragment");//添加leftListFragment并为其设置tag leftListFragment
        transaction1.commit();
    }

    public void myClick(int pos){
        buyFragment.setAAA(pos);
    }
    public void myClick2(int pos,int val){
        buyFragment.setVal(pos,val);
    }
    public void myClick1(){
        buyFragment.setView();

    }
    public void starvideo(){
       videoFragment.setStarplay();
    }
    public void stopvideo(){
        videoFragment.setStopplay();
    }
    /**
     * 接收到mqtt的广播
     * */
    String videourl ="";
    String picurl="";
    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mess = intent.getStringExtra("mes_url");
            if (!mess.isEmpty()){
                try {
                    adDaoImp.deleteAll();
                    JSONObject jsonObject = new JSONObject(mess);

                    if (jsonObject.has("adVideo")){
                     videourl = jsonObject.getString("adVideo");
                    }
                    if (jsonObject.has("adPictire")){
                        picurl = jsonObject.getString("adPictire");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            if (!videourl.isEmpty()){
                ad.setAdVideo(videourl);
                ad.setId(1L);
                adDaoImp.insert(ad);
               advideo();
            }else if (!picurl.isEmpty()){
                ad.setAdPictire(picurl);
                ad.setId(1L);
                adDaoImp.insert(ad);
                adpic();
            }


        }
    }
    public  String  getPicurl(){
        Log.e("url","getVideourl: -->" +picurl );
        return picurl;
    }

    public String getVideourl() {
        Log.e("url","getVideourl: -->" +videourl );
        return videourl;

    }

    @Override
    protected void onDestroy() {

        if (receiver != null) {
           unregisterReceiver(receiver);
        }
        super.onDestroy();
    }
}
