package com.example.infinitypixelcart.Service;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class InterceptorService implements Interceptor {

    private String token;

    InterceptorService(String token){
        this.token=token;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Create a modified request with a custom header
        Request modifiedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer "+token)
                .build();

        // Proceed with the request
        Response response = chain.proceed(modifiedRequest);

        return response;
    }
}
