package com.example.infinitypixelcart.API;

import com.example.infinitypixelcart.model.ProductDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FetchingProductsApi {

    @GET("/api/public/getAllProducts")
    Call<List<ProductDTO>> getAllProducts();

    @GET("/api/public/getProductsByName")
    Call<List<ProductDTO>> getProductByName(@Query("productName") String query);

    @GET("/api/public/{id}")
    Call<ProductDTO> getProductById(@Path("id") Long productId);

    @GET("/api/public/getProductImages")
    Call<List<String>> getProductImage(@Query("productId") Long productId);

}
