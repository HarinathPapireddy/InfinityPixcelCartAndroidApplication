package com.example.infinitypixelcart.Service;

import android.content.Context;
import android.content.SharedPreferences;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

public class TokenManager {
    private static final String PREF_NAME = "InfinityPixelCartPreferences";
    private static final String KEY_TOKEN = "Token";

    private SharedPreferences sharedPreferences;
    private static TokenManager instance;

    private TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static TokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new TokenManager(context);
        }
        return instance;
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public boolean contains(){
        return sharedPreferences.contains(KEY_TOKEN);
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public void clearToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_TOKEN);
        editor.apply();
    }
    public DecodedJWT decodeJWT() {
        try {
            DecodedJWT jwt = JWT.decode(getToken());
            return jwt;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle the exception as needed
        }
    }
    public  boolean isValid() {
        DecodedJWT decodedJWT= decodeJWT();
        Long expirationTime = decodedJWT.getClaim("exp").asLong()*1000;
        if (expirationTime > System.currentTimeMillis()) {
            // The token is still valid
            return true;
        } else {
            // The token has expired
            return false;
        }
    }

}
