package com.example.infinitypixelcart.ViewModels;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.infinitypixelcart.Service.ProductService;
import com.example.infinitypixelcart.model.ShoppingCartDTO;

import java.util.List;

public class ShoppingCartProducts extends ViewModel {
    private MutableLiveData<List<ShoppingCartDTO>> products = new MutableLiveData<>();

    private Context context;

    ProductService productService;

    public void setContext(Context context) {
        this.context = context;
        productService = new ProductService(context);
    }

    public LiveData<List<ShoppingCartDTO>> getProducts() {
        return products;
    }

    public void fetchProducts(){
        productService.getProductInShoppingCart(new ProductService.OnResponse<List<ShoppingCartDTO>>() {
            @Override
            public void OnSuccess(List<ShoppingCartDTO> v) {
                products.postValue(v);
            }

            @Override
            public void OnFailure(String Error) {
                Toast.makeText(context, Error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteProduct(Long productId){
        productService.deleteProductInCart(productId, new ProductService.OnResponse<String>() {
            @Override
            public void OnSuccess(String v) {
                Toast.makeText(context, v, Toast.LENGTH_SHORT).show();
                fetchProducts();
            }

            @Override
            public void OnFailure(String Error) {
                Toast.makeText(context, Error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addQuantity(final Long cartId){
        productService.addQuantityToProductsInCart(cartId, new ProductService.OnResponse<String>() {
            @Override
            public void OnSuccess(String v) {
                fetchProducts();
            }

            @Override
            public void OnFailure(String Error) {
                Toast.makeText(context, Error, Toast.LENGTH_SHORT).show();
                fetchProducts();
            }
        });
    }

    public void removeQuantity(final Long cartId){
        productService.decreaseQuantityOfProductInCart(cartId, new ProductService.OnResponse<String>() {
            @Override
            public void OnSuccess(String v) {
                fetchProducts();
            }

            @Override
            public void OnFailure(String Error) {
                Toast.makeText(context, Error, Toast.LENGTH_SHORT).show();
                fetchProducts();
            }
        });
    }


}
