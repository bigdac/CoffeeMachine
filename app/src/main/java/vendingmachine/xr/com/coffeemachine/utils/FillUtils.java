package vendingmachine.xr.com.coffeemachine.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.OutputStream;

public class FillUtils {

    public static void filiI0(Context context,File fileApp){

        String path=context.getApplicationContext().getCacheDir()+"/my-cache/Muller";
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        try {
            File fileApp2=null;
            InputStream inputStream=new FileInputStream(fileApp);
            OutputStream output=new FileOutputStream(fileApp2);
            byte[] buffer=new byte[1024];
            int len=0;
            while ((len=inputStream.read())!=-1){
                output.write(buffer,0,len);
            }
            output.flush();
            inputStream.close();
            output.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


