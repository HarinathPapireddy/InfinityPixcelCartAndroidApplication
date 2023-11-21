package com.example.infinitypixelcart.Service;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static Retrofit.Builder retrofit;
    private RetrofitService() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.234.1:9090")
                .addConverterFactory(GsonConverterFactory.create(new Gson()));
    }
    public static Retrofit getRetrofit(){
        if(retrofit==null){
            new RetrofitService();
        }
        return retrofit.build();
    }

    public static Retrofit getRetrofitWithInterceptor(String token){
        if(retrofit==null){
            new RetrofitService();
        }
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new InterceptorService(token));
        OkHttpClient client = httpClient.build();
        return retrofit.client(client).build();
    }
}

