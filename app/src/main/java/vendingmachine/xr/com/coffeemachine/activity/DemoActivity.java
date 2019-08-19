package vendingmachine.xr.com.coffeemachine.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

import io.vov.vitamio.utils.Log;
import vendingmachine.xr.com.coffeemachine.R;
import vendingmachine.xr.com.coffeemachine.http.HttpUtils;

public class DemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
    }
    public void onDown(View view){
        new DownAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    class DownAsyncTask extends AsyncTask<Void,Void,File>{

        @Override
        protected File doInBackground(Void... voids) {
            File file=HttpUtils.downLoadFile("http://47.98.131.11/coffe/mcu/kkk.bin");
            if (file!=null && file.exists()){
                Log.i("DownAsyncTask","-->"+file.getName());
            }
            return file;
        }
    }
}
