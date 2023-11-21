package com.example.infinitypixelcart.Intents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.infinitypixelcart.Adapter.ProductAdapter;
import com.example.infinitypixelcart.Adapter.ProductsInWishListCartAdapter;
import com.example.infinitypixelcart.R;
import com.example.infinitypixelcart.Service.ProductService;
import com.example.infinitypixelcart.ViewModels.ProductsViewModel;
import com.example.infinitypixelcart.model.ProductDTO;

import java.util.List;

public class OnProductSearch extends AppCompatActivity {

    RecyclerView recyclerView;
    ProductsViewModel productsViewModel;


    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_product_search);
        getSupportActionBar();

        recyclerView=findViewById(R.id.searchViewRecyclerView);
        productsViewModel = new ViewModelProvider(this).get(ProductsViewModel.class);
        productsViewModel.setContext(getApplicationContext());
        Bundle bundle = getIntent().getExtras();

        toolbar=findViewById(R.id.searchProductToolBar);
        getSupportActionBar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(bundle.getString("productName"));
        productsViewModel.fetchProductByName(bundle.getString("productName"));
        productsViewModel.getProducts().observe(this, new Observer<List<ProductDTO>>() {
            @Override
            public void onChanged(List<ProductDTO> productDTOS) {
                populateListView(productDTOS);
            }
        });


    }
    private void populateListView(List<ProductDTO> products){
        ProductAdapter productAdapter= new ProductAdapter(products);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(productAdapter);
        productAdapter.setOnProductListener(new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Long productId) {
                Bundle bundle= new Bundle();
                bundle.putLong("productId",productId);
                Intent intent = new Intent(getApplicationContext(), ProductViewer.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onProductAddToCart(Long productId) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_on_search_toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId= item.getItemId();
        if(itemId == android.R.id.home){
            super.onBackPressed();
        } else if (itemId == R.id.wishListCart) {
            showDialogBox();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogBox() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.wishlistdialogbox);
        ImageView cancelButton = dialog.findViewById(R.id.cancel_button);
        RecyclerView productsInWishList = dialog.findViewById(R.id.WishListDialogBox);
        ProductService productService = new ProductService(this);
        productsInWishList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        productService.getWishListedProducts(new ProductService.OnProductsFetchedListener() {
            @Override
            public void OnSuccess(List<ProductDTO> productList) {
                ProductsInWishListCartAdapter productsInWishListCartAdapter = new ProductsInWishListCartAdapter(productList);
                productsInWishList.setAdapter(productsInWishListCartAdapter);
            }
            @Override
            public void OnFailure(String Error) {
                Toast.makeText(OnProductSearch.this, Error, Toast.LENGTH_SHORT).show();
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}