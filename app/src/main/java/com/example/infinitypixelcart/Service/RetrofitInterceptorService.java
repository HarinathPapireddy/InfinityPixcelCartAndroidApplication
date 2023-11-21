package com.example.infinitypixelcart.Service;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInterceptorService {
    private static Retrofit retrofit;


    private RetrofitInterceptorService(String token){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new InterceptorService(token));
        OkHttpClient client = httpClient.build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.234.1:9090")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(client) // Set the OkHttpClient with the interceptor
                .build();
    }


    public static Retrofit getRetrofit(String token) {
        if(retrofit==null){
            new RetrofitInterceptorService(token);
        }
        return retrofit;
    }
}
