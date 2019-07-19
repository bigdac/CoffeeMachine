package vendingmachine.xr.com.coffeemachine.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;
import vendingmachine.xr.com.coffeemachine.activity.CoffeeTestActivity;
import vendingmachine.xr.com.coffeemachine.activity.FirstActivity;
import vendingmachine.xr.com.coffeemachine.activity.TestActivity;
import vendingmachine.xr.com.coffeemachine.application.MyApplication;
import vendingmachine.xr.com.coffeemachine.fragment.BuyFragment;

/**
 * Created by Norton on 2017/6/6.
 */

public class SerialPortUtil1 {

    public static SerialPort serialPort = null;
    public static InputStream inputStream = null;
    public static OutputStream outputStream = null;
    public static Thread receiveThread = null;
    public static int[] values;
    public static String serialData ;
    public static boolean flag = false;

    /**
     * 打开串口的方法
     */
    public static void openSerialPort(Context context, String port, int baudRate, int dataBits, int stopBits, char parity) throws IOException {
        Log.i("test","打开串口");
        try {
            File file = new File("/dev/"+ port);
            serialPort = new SerialPort(file,baudRate,dataBits,stopBits,parity);
            //获取打开的串口0中的输入输出流，以便于串口数据的收发
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            flag = true;
            receiveSerialPort(context);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     *关闭串口的方法
     * 关闭串口中的输入输出流
     * 关闭串口
     */
    public static void closeSerialPort(){
        Log.i("test","关闭串口");
        try {
            if(inputStream != null) {
                inputStream.close();
            }
            if(outputStream != null){
                outputStream.close();
            }
            flag = false;
            serialPort.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送十六进制串口数据的方法
     * @param data 要发送的数据
     */
    public static void sendHexSerialPort(int[] data){

        MyApplication.sendByte += data.length;

        try {
            //将16进制的int类型的数组转换为byte数组
            byte[] buf = AryChangeManager.hexToByte(data);
            //将数据写入串口
//            byte[] buf = new byte[2];
//            buf[0]=0x01;
//            buf[1]=0x02;
            Log.e("sent1","hex发送串口数据111111111-->"+data.toString());
            outputStream.write(buf);
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("test","串口数据发送失败");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 发送十六进制文件的方法
     * @param data 要发送的数据
     */
    public static void sendFileSerialPort(File data){

//        MyApplication.sendByte += data.length;

        try {
            //将16进制的int类型的数组转换为byte数组
            FileInputStream fileInputStream = new FileInputStream(data);
            byte[] buf = toByteArray(fileInputStream);
            //将数据写入串口
            Log.e("sent","hex发送串口数据-->"+buf.toString());
            outputStream.write(buf);
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("test","串口数据发送失败");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 文件转byte数组
     * @param in 要发送的数据
     */
    private static byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }


    /**
     * 发送文本串口数据的方法
     * @param data 要发送的数据
     */
    public static void sendTextSerialPort(String data){
        Log.i("test","发送串口数据-->"+data);
        try {
            //将16进制的int类型的数组转换为byte数组
//            byte[] buf = AryChangeManager.hexToByte(data);
            //将数据写入串口
            outputStream.write(data.getBytes());
            Log.e("sent","text发送串口数据-->"+data.getBytes().toString());
            outputStream.flush();
            Log.i("test","串口数据发送成功");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("test","串口数据发送失败");
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 接收串口数据的方法
     */

    public static void receiveSerialPort(final Context context){
        Log.i("test","接收串口数据");
        /*定义一个handler对象要来接收子线程中接收到的数据
            并调用Activity中的刷新界面的方法更新UI界面
         */
        final Handler handler = new Handler(context.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==1){
                    if(values!=null){
                        MyApplication.receiveByte += values.length;
//                        MainActivity.refreshReceiveByte();
                        StringBuilder builder = new StringBuilder();
                        for(int i:values){
                            builder.append(AryChangeManager.dexToHex(i));
                            builder.append("");
                        }

//                    Toast.makeText(context,"串口接收到数据："+builder.toString(),Toast.LENGTH_LONG).show();
//                        Log.i("test","接收串口数据"+builder.toString());
                        //对接收到的数据进行处理
                        if (FirstActivity.running){
                                FirstActivity.refreshReceive(builder.toString());
                        }
                        if (TestActivity.running){
                            TestActivity.refreshReceive(builder.toString());
                        }
                        if (CoffeeTestActivity.running){
                            CoffeeTestActivity.refreshReceive(builder.toString());
                        }
                        values = null;
                    }else{
//                        MainActivity.refreshReceive(serialData+" ");
                        serialData = "";
                    }
                }
            }
        };
        /*创建子线程接收串口数据
         */
        receiveThread = new Thread(){
            @Override
            public void run() {
                while (flag){
                    try {
                        byte[] readData = new byte[1024];
                        if (inputStream == null) {
                            return;
                        }
                        int size = inputStream.read(readData);

                        if (size>0&&flag) {
                            if(BuyFragment.isHexReceive){
                                //当前为16进制接收
                                values = new int[size];
                                for (int i = 0; i < values.length; i++) {
                                    values[i] |= (readData[i] & 0x000000ff);

                                }
                            }else{
                                //当前为文本模式接收
                                serialData = new String(readData,0,size);

                            }
                        /*将接收到的数据封装进Message中，然后发送给主线程
                            */
                            handler.sendEmptyMessage(1);
                            Thread.sleep(200);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        //启动接收线程
        receiveThread.start();
    }


}
