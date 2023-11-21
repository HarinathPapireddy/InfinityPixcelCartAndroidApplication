package com.example.infinitypixelcart.ViewModels;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.infinitypixelcart.Service.ProductService;
import com.example.infinitypixelcart.model.ProductDTO;

import java.util.List;

public class ProductsViewModel extends ViewModel {

    private MutableLiveData<List<ProductDTO>> products = new MutableLiveData<>();
    Context context;
    private ProductService productService;
    public void setContext( Context context){
        this.context=context;
        productService=new ProductService(context);
    }

    public LiveData<List<ProductDTO>> getProducts(){
        return products;
    }

    public void fetchProductByName(String productName) {
        productService.getProductsByName(productName,new ProductService.OnProductsFetchedListener() {
            @Override
            public void OnSuccess(List<ProductDTO> productList) {
                products.postValue(productList);
            }

            @Override
            public void OnFailure(String Error) {
                Toast.makeText(context, Error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getProductDetailsById(Long productId,OnProductFetched listener){
        productService.getProductById(productId, new ProductService.OnProductFetchedListener() {
            @Override
            public void OnSuccess(ProductDTO product) {
                listener.OnSuccess(product);
            }

            @Override
            public void OnFailure(String Error) {
                Toast.makeText(context, Error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getProductImagesById(Long productId,OnImagesFetched listener){
        productService.getProductImagesById(productId, new ProductService.OnProductImageFetched() {
            @Override
            public void OnSuccess(List<String> productImages) {
                listener.OnSuccess(productImages);
            }

            @Override
            public void OnFailure(String Error) {
                Toast.makeText(context, Error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addProductToWishList(Long productId){
        productService.addProductToWishList(productId);
    }

    public void getWishListedProducts(){
        productService.getWishListedProducts(new ProductService.OnProductsFetchedListener() {
            @Override
            public void OnSuccess(List<ProductDTO> productList) {
                products.postValue(productList);
            }

            @Override
            public void OnFailure(String Error) {
                Toast.makeText(context, Error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface OnProductFetched{
        public void OnSuccess(ProductDTO product);
    }
    public interface OnImagesFetched{
        void OnSuccess(List<String> productImages);
    }


}
