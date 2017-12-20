package net.goeasyway.uploadimage.utils;

import android.os.Build;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Lenovo on 2017/12/15.
 */

public class LoggingInterceptor implements Interceptor {
    private static final String UA = "User-Agent";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader(UA, makeUA())
                .build();
        return chain.proceed(request);
    }
    private String makeUA() {
        String s = Build.BRAND + "/" + Build.MODEL + "/" + Build.VERSION.RELEASE;
        return Build.BRAND + "/" + Build.MODEL + "/" + Build.VERSION.RELEASE;
    }

}