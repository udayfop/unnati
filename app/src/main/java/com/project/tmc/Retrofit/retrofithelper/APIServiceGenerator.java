package com.project.tmc.Retrofit.retrofithelper;

import java.util.HashMap;


public class APIServiceGenerator {
    private static String BASE_URL;

    public static <S> S createService(Class<S> serviceClass) {
        return RetrofitClient.getAPIClient(BASE_URL).create(serviceClass);
    }

    public static void setBaseUrl(String url){
        BASE_URL = url;
    }

    private static HashMap<String, String> headers = new HashMap<>();

    public static void addHeader(String key, String value){
        headers.put(key, value);
    }

    public static HashMap<String, String> getHeaders(){
        return headers;
    }
}