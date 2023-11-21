package com.example.infinitypixelcart.API;

import com.example.infinitypixelcart.model.AddressDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UpdatingUserDetails {

    @POST("/api/user/addAddress")
    Call<Void> addAddress(@Body AddressDTO addressDTO);

    @DELETE("/api/user/removeAddress")
    Call<Void> deleteAddress(@Query("username") String username,@Query("addressId") Long Id);

    @POST("/api/user/addToWishList")
    Call<Void> addProductToCart(@Query("username") String username,@Query("productId") Long productId);

    @DELETE("/api/user/removeFromWishList")
    Call<Void> removeFromWishList(@Query("username") String username,@Query("productId") Long productId);

    @POST("/api/user/addToShoppingCart")
    Call<Void> addToShoppingCart(@Query("username") String username,@Query("productId") Long productId);

    @DELETE("/api/user/deleteProductInCart")
    Call<Void> deleteProductInCart(@Query("username") String username,@Query("productId") Long productId);

    @PUT("/api/user/addQuantity")
    Call<Void> addQuantity(@Query("cartId") Long cartId);

    @PUT("/api/user/decreaseQuantity")
    Call<Void> removeQuantity(@Query("cartId") Long cartId);
}
