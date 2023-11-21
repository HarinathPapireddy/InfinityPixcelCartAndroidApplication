package com.example.infinitypixelcart.Service;

import android.content.Context;
import android.widget.Toast;

import com.example.infinitypixelcart.API.FetchingProductsApi;
import com.example.infinitypixelcart.API.FetchingUserDetails;
import com.example.infinitypixelcart.API.UpdatingUserDetails;
import com.example.infinitypixelcart.model.ProductDTO;
import com.example.infinitypixelcart.model.ShoppingCartDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductService {


    private Context context;

    private String token = TokenManager.getInstance(context).getToken();

    private String username = TokenManager.getInstance(context).decodeJWT().getSubject();

    private List<ProductDTO> products;

    private FetchingProductsApi api=RetrofitService.getRetrofit().create(FetchingProductsApi.class);

    public ProductService(Context context) {
        this.context = context;
    }

    public void getProductsByName(final String productName , final OnProductsFetchedListener listener){
        api.getProductByName(productName).enqueue(new Callback<List<ProductDTO>>() {
            @Override
            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                if(response.code()==200){
                    listener.OnSuccess(response.body());
                }
                else{
                    listener.OnFailure("Please Check Your Internet Connection");
                }
            }

            @Override
            public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
                listener.OnFailure("Something Went Wrong....");
            }
        });
    }

    public void getProductById(final Long productId, final OnProductFetchedListener listener){
        api.getProductById(productId).enqueue(new Callback<ProductDTO>() {
            @Override
            public void onResponse(Call<ProductDTO> call, Response<ProductDTO> response) {
                listener.OnSuccess(response.body());
            }

            @Override
            public void onFailure(Call<ProductDTO> call, Throwable t) {
                listener.OnFailure("Something Went Wrong");
            }
        });

    }

    public void getProductImagesById(final Long productId,OnProductImageFetched listener){
        api.getProductImage(productId).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                listener.OnSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                listener.OnFailure("Something Went Wrong");
            }
        });
    }

    public void addProductToWishList(final Long productId){
        UpdatingUserDetails userDetailsApi = RetrofitService.getRetrofitWithInterceptor(token).create(UpdatingUserDetails.class);
        userDetailsApi.addProductToCart(username,productId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(context, "Product WishListed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getWishListedProducts(OnProductsFetchedListener listener){
        FetchingUserDetails userDetailsApi=RetrofitService.getRetrofitWithInterceptor(token).create(FetchingUserDetails.class);
        userDetailsApi.getWishListedProducts(username).enqueue(new Callback<List<ProductDTO>>() {
            @Override
            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
                listener.OnSuccess(response.body());
            }

            @Override
            public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
                listener.OnFailure("Something Went Wrong");
            }
        });

    }

    public void productsExistInUserWishList(final Long productId, OnProductExists listener){
        FetchingUserDetails userDetailsApi=RetrofitService.getRetrofitWithInterceptor(token).create(FetchingUserDetails.class);
        userDetailsApi.productWishListedOrNot(username,productId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code()==302){
                    listener.OnExists();
                }
                else {
                    listener.OnNotExists();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
    public void productRemovedFromWishList(final Long productId,OnResponse listener){
        UpdatingUserDetails userDetailsApi=RetrofitService.getRetrofitWithInterceptor(token).create(UpdatingUserDetails.class);
        userDetailsApi.removeFromWishList(username,productId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                listener.OnSuccess(true);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.OnFailure("Something Went Wrong. Please Check Your Internet Connection");
            }
        });
    }
    public void productAddToCart(final Long productId,OnResponse<String> listener){
        UpdatingUserDetails api = RetrofitService.getRetrofitWithInterceptor(token).create(UpdatingUserDetails.class);
        api.addToShoppingCart(username,productId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                listener.OnSuccess("Product Added To Cart");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.OnFailure("Something Went Wrong");
            }
        });
    }
    public void getProductInShoppingCart(OnResponse<List<ShoppingCartDTO>> listener){
        FetchingUserDetails userDetailsApi=RetrofitService.getRetrofitWithInterceptor(token).create(FetchingUserDetails.class);
        userDetailsApi.getProductsInShoppingCart(username).enqueue(new Callback<List<ShoppingCartDTO>>() {
            @Override
            public void onResponse(Call<List<ShoppingCartDTO>> call, Response<List<ShoppingCartDTO>> response) {
               if(response.code()==200){
                   listener.OnSuccess(response.body());
               }
               else {
                   listener.OnFailure("Something Went Wrong");
               }
            }

            @Override
            public void onFailure(Call<List<ShoppingCartDTO>> call, Throwable t) {
                listener.OnFailure("Something Went Wrong");
            }
        });
    }

    public void productExistsInCart(final Long id , OnProductExists listener){
        FetchingUserDetails userDetailsApi=RetrofitService.getRetrofitWithInterceptor(token).create(FetchingUserDetails.class);
        userDetailsApi.productExistsInCart(username,id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code()==200){
                    listener.OnExists();
                }
                else listener.OnNotExists();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteProductInCart(final Long id, OnResponse<String> listener){
        UpdatingUserDetails api = RetrofitService.getRetrofitWithInterceptor(token).create(UpdatingUserDetails.class);
        api.deleteProductInCart(username,id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code()==200){
                    listener.OnSuccess("Product deleted from Cart");
                }
                else listener.OnFailure("Something Went Wrong");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addQuantityToProductsInCart(final Long cartId, OnResponse<String> listener){
        UpdatingUserDetails api = RetrofitService.getRetrofitWithInterceptor(token).create(UpdatingUserDetails.class);
        api.addQuantity(cartId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code()==200){
                    listener.OnSuccess("Success");
                }
                else {
                    listener.OnFailure("Something Went Wrong");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.OnFailure("Something Went Wrong");
            }
        });
    }
    public void decreaseQuantityOfProductInCart(final Long cartId,OnResponse<String> listener){
        UpdatingUserDetails api = RetrofitService.getRetrofitWithInterceptor(token).create(UpdatingUserDetails.class);
        api.removeQuantity(cartId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code()==200){
                    listener.OnSuccess("Success");
                }
                else{
                    listener.OnSuccess("Something Went Wrong");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.OnSuccess("Something Went Wrong");
            }
        });
    }




    public interface OnResponse<T>{
        void OnSuccess(T v);
        void OnFailure(String Error);
    }
    public interface OnProductExists {
        void OnExists();
        void OnNotExists();
    }

    public interface OnProductsFetchedListener{
        void OnSuccess(List<ProductDTO> productList);
        void OnFailure(String Error);
    }
    public interface OnProductFetchedListener{
        void OnSuccess(ProductDTO product);
        void OnFailure(String Error);
    }

    public interface OnProductImageFetched{
        void OnSuccess(List<String> productImages);
        void OnFailure(String Error);
    }

}