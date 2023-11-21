package com.example.infinitypixelcart.API;

import com.example.infinitypixelcart.model.AuthTokenResponse;
import com.example.infinitypixelcart.model.User;
import com.example.infinitypixelcart.model.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAuthenticationApi {
    @POST("/userValidation")
    Call<AuthTokenResponse> userAuthentication(@Body User user);

    @POST("/userRegistration")
    Call<Void> userRegistration(@Body UserDTO userDTO);

}
