package com.example.infinitypixelcart.Intents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infinitypixelcart.Adapter.ProductImageAdapter;
import com.example.infinitypixelcart.R;
import com.example.infinitypixelcart.Service.ProductService;
import com.example.infinitypixelcart.ViewModels.ProductsViewModel;
import com.example.infinitypixelcart.model.ProductDTO;

import java.util.List;

public class ProductViewer extends AppCompatActivity {

    Toolbar toolbar;

    TextView productName;
    TextView productRatings;
    TextView productPrice;
    TextView productDescription;
    RecyclerView recyclerView;
    Button wishListButton;

    Button addToCart;

    ProductService productService;
    private boolean flag;

    private boolean productExistsInCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productService = new ProductService(getApplicationContext());
        setContentView(R.layout.activity_product_viewer);
        toolbar=findViewById(R.id.productToolBar);
        getSupportActionBar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Product");
        productName=findViewById(R.id.productViewerProductName);
        productRatings=findViewById(R.id.productViewerProductRatings);
        productPrice=findViewById(R.id.productViewerProductPrice);
        productDescription=findViewById(R.id.productViewerProductDescription);
        wishListButton=findViewById(R.id.wishListButton);
        addToCart = findViewById(R.id.addToCartButton);
        recyclerView = findViewById(R.id.productViewerRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        initializer();
    }

    private void initializer() {
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            ProductDetails(bundle.getLong("productId"));
        }
    }

    private void ProductDetails(Long productId) {

        productService.productsExistInUserWishList(productId, new ProductService.OnProductExists() {
            @Override
            public void OnExists() {
                wishListButton.setText("Wish Listed");
                flag=true;
            }

            @Override
            public void OnNotExists() {
                flag=false;
            }
        });

        productService.productExistsInCart(productId, new ProductService.OnProductExists() {
            @Override
            public void OnExists() {
                addToCart.setText("added to Cart");
                productExistsInCart=true;
            }

            @Override
            public void OnNotExists() {
                addToCart.setText("Add to cart");
                productExistsInCart=false;
            }
        });

        ProductsViewModel productsViewModel = new ViewModelProvider(this).get(ProductsViewModel.class);
        productsViewModel.setContext(getApplicationContext());
        productsViewModel.getProductImagesById(productId, new ProductsViewModel.OnImagesFetched() {
            @Override
            public void OnSuccess(List<String> productImages) {

                ProductImageAdapter imageAdapter = new ProductImageAdapter(productImages);
                recyclerView.setAdapter(imageAdapter);

            }
        });
        productsViewModel.getProductDetailsById(productId, new ProductsViewModel.OnProductFetched() {
            @Override
            public void OnSuccess(ProductDTO product) {
                productName.setText(product.getName());
                productRatings.setText(product.getRatings());
                productPrice.setText(String.valueOf(product.getPrice()));
                productDescription.setText(product.getDescription());
            }
        });
        wishListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!flag) {
                    productsViewModel.addProductToWishList(productId);
                    wishListButton.setText("WishListed");
                    flag=true;
                }
                else {
                    productService.productRemovedFromWishList(productId, new ProductService.OnResponse() {
                        @Override
                        public void OnSuccess(Object v) {
                            Toast.makeText(ProductViewer.this, "Product Removed From WishList", Toast.LENGTH_SHORT).show();
                            wishListButton.setText("WishList");
                            flag=false;
                        }

                        @Override
                        public void OnFailure(String Error) {
                            Toast.makeText(ProductViewer.this, Error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!productExistsInCart){
                    productService.productAddToCart(productId, new ProductService.OnResponse<String>() {
                        @Override
                        public void OnSuccess(String v) {
                            addToCart.setText("Added To Cart");
                            productExistsInCart=true;
                        }

                        @Override
                        public void OnFailure(String Error) {
                            Toast.makeText(ProductViewer.this, Error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    productService.deleteProductInCart(productId, new ProductService.OnResponse<String>() {
                        @Override
                        public void OnSuccess(String v) {
                            addToCart.setText("add to cart");
                        }

                        @Override
                        public void OnFailure(String Error) {
                            Toast.makeText(ProductViewer.this, Error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId= item.getItemId();
        if(itemId == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}