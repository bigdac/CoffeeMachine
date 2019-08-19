package vendingmachine.xr.com.coffeemachine.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;
import java.util.ArrayList;
import java.util.List;

import vendingmachine.xr.com.coffeemachine.R;


public class XqFragment extends Fragment {
    View view;

    private Banner banner;
    private ArrayList<Integer> list_path;
    private ArrayList<Integer> list_path1;
    private ArrayList<String> list_title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_manager,container,false);
        list_path = new ArrayList<>();
        list_path1=new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();
//        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
//        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
//        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
//        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");
        list_path.add(R.mipmap.adbig1);
        list_path.add(R.mipmap.adbig2);
        list_path.add(R.mipmap.adbig3);
        list_path1.add(R.mipmap.load_pic1);
        list_path1.add(R.mipmap.load_pic2);
        list_path1.add(R.mipmap.pic5);
        banner = (Banner)view.findViewById(R.id.banner);
        initView(list_path1);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        ((FirstActivity) context).setMyPersonOnClick(myOnClick);
    }

    private void initView(List<?> list) {

        //放图片地址的集合

        Log.e("SSSSSSAAAAAAAA222", "myListener: --》"+list_path1.size() );
//        list_title.add("好好学习");
//        list_title.add("天天向上");
//        list_title.add("热爱劳动");
//        list_title.add("不搞对象");
//        String picurl = ((FirstActivity)getActivity()).getPicurl();
//        if (!picurl.isEmpty()){
//            String [] arr = picurl.split(",");
//            list_path.clear();
//            list_title.clear();
//            for (int i=0;i<arr.length;i++){
//                list_path.add(arr[i]);
//            }
////            list_title.add("热爱劳动");
////            list_title.add("不搞对象");
//        }
//        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader1());
        //设置图片网址或地址的集合
        banner.setImages(list);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
//        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(5000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
//                .setOnBannerListener()
                //必须最后调用的方法，启动轮播图。
                .start();


    }
    //轮播图的监听方法

    public void OnBannerClick(int position) {

        Log.i("tag", "你点了第"+position+"张轮播图");
    }
    //自定义的图片加载器
    private class MyLoader1 extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    }
    //wangluo
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }

//    //声明及实现接口的方法
//    private FirstActivity.MyOnClick myOnClick = new FirstActivity.MyOnClick() {
//        @Override
//        public void myListener(int what) {
//                switch (what){
//
//                    case 0:
////                        banner.stopAutoPlay();
////                        //设置图片加载器，图片加载器在下方
////                        banner.setImageLoader(new MyLoader1());
////                        //设置图片网址或地址的集合
////                        banner.update(list_path1);
//                        banner.stopAutoPlay();
//                        initView(list_path1);
//                        break;
//
//                    case 1:
////                        banner.stopAutoPlay();
////                        //设置图片加载器，图片加载器在下方
////                        banner.setImageLoader(new MyLoader1());
////                        //设置图片网址或地址的集合
////                        banner.update(list_path);
//                        Log.e("SSSSSSAAAAAAAA", "myListener: --》"+list_path1.size() );
//                        banner.stopAutoPlay();
//                        initView(list_path);
//                        break;
//                }
//        }
//
//    };


}
