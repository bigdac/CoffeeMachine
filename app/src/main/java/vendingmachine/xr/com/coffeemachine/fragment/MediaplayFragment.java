package vendingmachine.xr.com.coffeemachine.fragment;




import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;
import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.activity.FirstActivity;
import vendingmachine.xr.com.coffeemachine.adapter.CustomMediaController;
import static android.content.Context.MODE_PRIVATE;

public class MediaplayFragment extends Fragment {
    View view;
    VideoView mVideoView;
//    MediaPlayer mVideoView;
    private CustomMediaController mCustomMediaController;
    MediaController mController;

    Uri uri1;

    String [] url;
    String [] url1;
    int l = 0;
    SharedPreferences preferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mediaplay , container , false);
//        preferences = getActivity().getSharedPreferences("myurl", MODE_PRIVATE);

        Vitamio.isInitialized(getActivity());
        //定义全屏参数
        final int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = getActivity().getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);

//       String videourl =((FirstActivity)getActivity()).getVideourl();
//        if (videourl!=null){
//            String [] arr= videourl.split(",");
//
//            if (arr.length==1){
//                url1= new String[]{videourl};
//            }else {
//                url1=arr;
//
//            }
//
//        }

        //检查vitamio框架是否可用

        mVideoView = (VideoView) view.findViewById(R.id.video);
        String videoUrl1 = "mnt/sdcard/105.mp4" ;
        String videoUrl2 = "mnt/sdcard/103.mp4" ;
//        String videoUrl1 = "/storage/emulated/0/123.mp4" ;
//        Log.e("path", "onCreateView: "+videoUrl1 );
//        String videoUrl1 = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4" ;
//        String videoUrl2 = "http://pde49x7hh.bkt.clouddn.com/abc.mp4" ;

//         url = new String[]{videoUrl1,videoUrl1};

         uri1 = Uri.parse( videoUrl2 );

        MediaController mediaController = new MediaController(getActivity());
        mediaController.setVisibility(View.GONE);
        //设置视频控制器
//        videoView.setMediaController(new MediaController(getActivity()));

        //播放完成回调
//        videoView.setOnCompletionListener( new MyPlayerOnCompletionListener());

        //设置视频路径
//        videoView.setVideoURI(uri);
//
//        //开始播放视频
//        videoView.start();
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//            }
//        });
        if (Vitamio.isInitialized(getActivity())) {
//            mVideoView.setVideoURI(Uri.parse(url1[0]));
            mVideoView.setVideoURI(uri1);
            mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
            mVideoView.setMediaController(mCustomMediaController);
            mVideoView.setBufferSize(10240); //设置视频缓冲大小

            mVideoView.requestFocus();
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    // optional need Vitamio 4.0
                    Log.e("first", "onPrepared:... " );

                    mediaPlayer.setPlaybackSpeed(1.0f);




                }
            });

            mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

//                      l++;
//                    if (l<url1.length){
//                        mVideoView.setVideoURI(Uri.parse(url1[l]));
//                        mVideoView.start();
//                    }else if (l>=url1.length){
//                        l=0;
//                        mVideoView.setVideoURI(Uri.parse(url1[l]));

                        mVideoView.stopPlayback();
                        mVideoView.setVideoURI(uri1);
                        mVideoView.start();


                }
            });
            mVideoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {

                }
            });
            mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        //开始缓冲
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                            if (mVideoView.isPlaying()) {
                                mVideoView.pause();

                            }
                            break;
                        //缓冲结束
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                            mVideoView.start();
                            break;
                        //正在缓冲
                        case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:

                            break;
                    }
                    return true;
                }
            });


        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mVideoView.isPlaying()){
            mVideoView.start();
        }
    }


//    //声明及实现接口的方法
//    private FirstActivity.MyOnClick myOnClick = new FirstActivity.MyOnClick() {
//        @Override
//        public void myListener(int what) {
//            switch (what){
//
//                case 0:
//                        mVideoView.pause();
//                    break;
//
//                case 1:
//                        mVideoView.start();
//                    break;
//            }
//        }
//
//    };
    public void setStopplay(){
        mVideoView.pause();
    }
    public void setStarplay(){
        mVideoView.start();
    }

}
