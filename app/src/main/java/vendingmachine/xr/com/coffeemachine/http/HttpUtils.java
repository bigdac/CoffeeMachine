package vendingmachine.xr.com.coffeemachine.http;


import android.os.Environment;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vendingmachine.xr.com.coffeemachine.application.MyApplication;
import vendingmachine.xr.com.coffeemachine.utils.NetWorkUtil;


/**
 * Created by whd on 2017/12/23.
 */

public class HttpUtils {

    public static String ipAddress = "http://47.98.131.11:8087";

    public static String getInputStream(InputStream is) {
        String result = null;
        byte[] buffer = new byte[1024 * 10];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            result = new String(bos.toByteArray(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String postOkHpptRequest(String url, Map<String, Object> map) {
        String result = null;
        try {
            String CONTENT_TYPE = "application/json";

            String JSON_DATA = "{\n" +
                    "\"deviceId\":1129,\n" +
                    "\"deviceTimeControlDtos\":\n" +
                    "[\n" +
                    "{\n" +
                    "\"week\":2,\n" +
                    "\"deviceTimeControlList\":[\n" +
                    "         {\"temp\":2.0,\"openTime\":2,\"closeTime\":3},\n" +
                    "         {\"temp\":2.0,\"openTime\":4,\"closeTime\":5}\n" +
                    "     ]\n" +
                    "}";
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<String, Object> param : map.entrySet()) {
                jsonObject.put(param.getKey(), param.getValue());
            }

            RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE), jsonObject.toJSONString());

            Request request = new Request.Builder()
                    .addHeader("client", "android-xr")
                    .url(url)
                    .post(requestBody)
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String postOkHpptRequest2(String url, JSONArray jsonArray) {
        String result = null;
        try {
            String CONTENT_TYPE = "application/json";

            String JSON_DATA = "{\n" +
                    "    \"houseId\":1000,\n" +
                    "    \"controlledId\":[5,6]\n" +
                    "}";
            JSONObject jsonObject = new JSONObject();
//            for (Map.Entry<String,Object> param:map.entrySet()){
//                jsonObject.put(param.getKey(),param.getValue());
//            }

            RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE), jsonArray.toString());

            Request request = new Request.Builder()
                    .addHeader("client", "android-xr")
                    .url(url)
                    .post(requestBody)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String postOkHpptRequest3(String url, org.json.JSONObject jsonObject) {
        String result = null;
        try {
            String CONTENT_TYPE = "application/json";

            String JSON_DATA = "{\n" +
                    "\"deviceId\":1129,\n" +
                    "\"deviceTimeControlDtos\":\n" +
                    "[\n" +
                    "{\n" +
                    "\"week\":2,\n" +
                    "\"deviceTimeControlList\":[\n" +
                    "         {\"temp\":2.0,\"openTime\":2,\"closeTime\":3},\n" +
                    "         {\"temp\":2.0,\"openTime\":4,\"closeTime\":5}\n" +
                    "     ]\n" +
                    "}";


            RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE), jsonObject.toString());

            Request request = new Request.Builder()
                    .addHeader("client", "android-xr")
                    .url(url)
                    .post(requestBody)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String doDelete(String url, JSONArray jsonArray) {
        String result = null;
        try {
            String CONTENT_TYPE = "application/json";


            RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE), jsonArray.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .delete(requestBody)
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                result = response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getOkHpptRequest(String url) {
        File httpCacheDirectory = new File(MyApplication.getContext().getCacheDir(), "HttpCache");//这里为了方便直接把文件放在了SD卡根目录的HttpCache中，一般放在context.getCacheDir()中
        int cacheSize = 10 * 1024 * 1024;//设置缓存文件大小为10M
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        String result = null;
        try {

            Request request = new Request.Builder()
                    .addHeader("client", "android-xr")
                    .url(url)
                    .get()
                    .tag(1)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时
                    .readTimeout(5, TimeUnit.SECONDS)//读取超时
                    .writeTimeout(5, TimeUnit.SECONDS)//写入超时
                    .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)//添加自定义缓存拦截器（后面讲解），注意这里需要使用.addNetworkInterceptor
                    .cache(cache)//把缓存添加进来
                    .build();

            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                result = response.body().string();
            } else {
//                NetWorkUtil.showNoNetWorkDlg(MyApplication.getContext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String upLoadFile(String url, String fileNmae, File file) {
        String result = null;
        try {
            com.squareup.okhttp.Response response = OkHttpUtils.post()
                    .addHeader("content-type", "multipart/form-data")
                    .addFile("file", fileNmae, file)
                    .url(url)
                    .build()
                    .execute();
            if (response.isSuccessful()) {
                result = response.code() + "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    static Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            //网上很多示例代码都对在request请求前对其进行无网的判断，其实无需判断，无网自动访问缓存
//            if(!NetworkUtil.getInstance().isConnected()){
//                request = request.newBuilder()
//                        .cacheControl(CacheControl.FORCE_CACHE)//只访问缓存
//                        .build();
//            }
            Response response = chain.proceed(request);

            if (NetWorkUtil.isConn(MyApplication.getContext())) {
                int maxAge = 0;//缓存失效时间，单位为秒
                return response.newBuilder()
                        .removeHeader("Pragma")//清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .header("Cache-Control", "public ,max-age=" + maxAge)
                        .build();
            } else {
//                NetWorkUtil.showNoNetWorkDlg(MyApplication.getContext());
                //这段代码设置无效
//                int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
//                return response.newBuilder()
//                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                        .removeHeader("Pragma")
//                        .build();
            }
            return response;
        }
    };
    private static GetBuilder getBuilder;

    public static GetBuilder getBuilder() {
        if (getBuilder == null) {
            getBuilder = OkHttpUtils.get();
        }
        return getBuilder;
    }

    public static String baseUrl = "http://47.98.131.11:8087";

    public static String requestPost(String url,Map<String, Object> params) {
        String result = null;
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl)
                    .build();
            HttpService httpService = retrofit.create(HttpService.class);
            String CONTENT_TYPE = "application/json";
            Gson gson = new Gson();
            String content = gson.toJson(params);
            RequestBody body = RequestBody.create(MediaType.parse(CONTENT_TYPE), content);
            Call<ResponseBody> call=httpService.postQuest(url,body);
            retrofit2.Response<ResponseBody> response = call.execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String requestGet(String url) {
        String result = null;
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl)
                    .build();
            HttpService httpService = retrofit.create(HttpService.class);
            Call<ResponseBody> call=httpService.getRequest(url);
            retrofit2.Response<ResponseBody> response = call.execute();
            result = response.body().string();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String uploadFile(String userId,File file){
        String result=null;
        try {
            Retrofit retrofit=new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(baseUrl)
                    .build();
            HttpService userService=retrofit.create(HttpService.class);
            // 创建 RequestBody，用于封装构建RequestBody
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            // MultipartBody.Part  和后端约定好Key，这里的partName是用image
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            // 执行请求
            Call<ResponseBody> call = userService.uploadFile(userId, body);
            retrofit2.Response<ResponseBody> response=call.execute();
             boolean success=response.isSuccessful();
            if (success){
                result=response.code()+"";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public static File
    downLoadFile(String url){
        File file=null;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(3, TimeUnit.SECONDS)//设置连接超时
                    .readTimeout(5, TimeUnit.SECONDS)//读取超时
                    .writeTimeout(5, TimeUnit.SECONDS)//写入超时
                    .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)//添加自定义缓存拦截器（后面讲解），注意这里需要使用.addNetworkInterceptor
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            InputStream inputStream=response.body().byteStream();


            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
             file = new File(path,"/kkk.bin");

            if (file.exists()){
                file.delete();
            }
            //把数据存入路径+文件名
            FileOutputStream fos = new FileOutputStream(file);
            byte buf[] = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

}
