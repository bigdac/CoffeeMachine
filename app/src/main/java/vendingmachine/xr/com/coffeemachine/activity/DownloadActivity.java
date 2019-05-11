package vendingmachine.xr.com.coffeemachine.activity;


import android.app.DownloadManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;
import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.application.MyApplication;

/**
 * Created by demi on 2017/2/16.
 */

public class DownloadActivity extends CheckPermissionsActivity {
    MyApplication application;
    private TextView progress;
    private TextView file_name;
    private ProgressBar pb_update;
    private DownloadManager downloadManager;
    private DownloadManager.Request request;
    public static String downloadUrl = "http://47.98.131.11/testzzc/1/app-release.apk";

    Timer timer;
    long id;
    int sum = 0;

    TimerTask task;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int pro = bundle.getInt("pro");
            Log.i("proggggg", "-->" + pro);
            String name = bundle.getString("name");
            pb_update.setProgress(pro);
            progress.setText(String.valueOf(pro) + "%");
            file_name.setText(name);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        application= (MyApplication) getApplication();
        application.addActivity(this);
        progress = (TextView) findViewById(R.id.progress);
        file_name = (TextView) findViewById(R.id.file_name);
        pb_update = (ProgressBar) findViewById(R.id.pb_update);
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        request = new DownloadManager.Request(Uri.parse(downloadUrl));
        request.setTitle("CoffeeMachine");
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setMimeType("application/vnd.android.package-archive");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //创建目录
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();

        //设置文件存放路径
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "app-release.apk");
        pb_update.setMax(100);
        final DownloadManager.Query query = new DownloadManager.Query();
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                Cursor cursor = downloadManager.query(query.setFilterById(id));
                if (cursor != null && cursor.moveToFirst()) {
                    if (cursor.getInt(
                        cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        pb_update.setProgress(100);
                        install(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/app-release.apk");
                        task.cancel();
                    }
                    String title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE));
                    String address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    Log.i("bytes_downloaded", "-->" + bytes_downloaded);
                    Log.i("bytes_total", "-->" + bytes_total);
                    float x = bytes_downloaded;
                    float ss = (x / bytes_total) * 100;
                    BigDecimal bigDecimal = new BigDecimal(ss);
                    BigDecimal bigDecimal2 = bigDecimal.setScale(0, BigDecimal.ROUND_DOWN);
                    int pro = Integer.parseInt(bigDecimal2 + "");
                    Log.i("prossssssssssssss", "-->" + (bytes_downloaded) / bytes_total);
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putInt("pro", pro);
                    bundle.putString("name", title);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                }
                cursor.close();
            }
        };
        timer.schedule(task, 0, 1000);
        id = downloadManager.enqueue(request);
        task.run();


//
    }


    private void install(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//4.0以上系统弹出安装成功打开界面
        startActivity(intent);
//        Intent intent = new Intent("com.box.intent.OPENAPI_INSTALL_APP");
//        intent.putExtra("path", path);
//        intent.putExtra("type", 1);
//        sendBroadcast(intent);
    }
}