package net.goeasyway.uploadimage.utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lenovo on 2017/12/15.
 */

public class RetrofitUtils {
    private static RetrofitUtils retrofitUtils;
    private RetrofitUtils(){

    }
    public static RetrofitUtils getInstance(){
        if(retrofitUtils==null){
            synchronized (RetrofitUtils.class){
                if(retrofitUtils==null){
                    retrofitUtils=new RetrofitUtils();
                }
            }
        }
        return retrofitUtils;
    }

    private static Retrofit retrofit;
    public static synchronized Retrofit getRetrofit(String url){
     /*   HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("xxx",message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
*/

        LoggingInterceptor httpLoggingInterceptor = new LoggingInterceptor();


        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(500, TimeUnit.SECONDS)
                .readTimeout(500,TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
        if(retrofit==null){
            retrofit=new Retrofit.Builder().baseUrl(url)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public <T>T getApiService(String url,Class<T> cl){
        Retrofit retrofit = getRetrofit(url);//得到retrofit

        return retrofit.create(cl);//返回的就是网络接口对象

    }


}