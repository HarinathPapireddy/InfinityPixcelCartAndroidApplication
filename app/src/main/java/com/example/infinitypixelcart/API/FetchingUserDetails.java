package com.example.infinitypixelcart.API;

import com.example.infinitypixelcart.model.AddressDTO;
import com.example.infinitypixelcart.model.ProductDTO;
import com.example.infinitypixelcart.model.ShoppingCartDTO;
import com.example.infinitypixelcart.model.UserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FetchingUserDetails {
    @GET("/api/user/getAccount")
    Call<UserDTO> getAccount(@Query("username") String username);

    @GET("/api/user/getAllAddresses")
    Call<List<AddressDTO>> getAllAddresses(@Query("username") String username);

    @GET("/api/user/getAllWishListItems")
    Call<List<ProductDTO>> getWishListedProducts(@Query("username") String username);

    @GET("/api/user/productWishListedOrNot")
    Call<Void> productWishListedOrNot(@Query("username") String username,@Query("productId") Long id);

    @GET("/api/user/getProductsInShoppingCart")
    Call<List<ShoppingCartDTO>> getProductsInShoppingCart(@Query("username") String username);

    @GET("/api/user/productExistsInCart")
    Call<Void> productExistsInCart(@Query("username") String username,@Query("productId") Long id);

}
