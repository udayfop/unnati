package com.project.tmc.Retrofit;

import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.project.tmc.Extra.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {

  //  private static final String BASE_URL = "http://api.adroitiot.in/api/";
    private static final String BASE_URL = "http://api.adroitiot.in/";

    private static Retrofit retrofit;
    public static Retrofit getRetrofitClient() {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                httpClient.connectTimeout(10, TimeUnit.MINUTES);
                httpClient.readTimeout(10, TimeUnit.MINUTES);
                httpClient.writeTimeout(10, TimeUnit.MINUTES);
            }

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder().build();
                    Log.e("kksfassdgddfh", "onRequest  " + new Gson().toJson(request.body()));
                    return chain.proceed(request);
                }
            }).build();
            retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).client(httpClient.build()).build();
        }
        return retrofit;
    }
}
