package com.example.infinitypixelcart.Intents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infinitypixelcart.Adapter.ProductInShoppingCartAdapter;
import com.example.infinitypixelcart.R;
import com.example.infinitypixelcart.Service.ProductService;
import com.example.infinitypixelcart.ViewModels.ShoppingCartProducts;
import com.example.infinitypixelcart.model.ShoppingCartDTO;

import java.util.List;

public class ShoppingCart extends AppCompatActivity {

    
    Toolbar shoppingCartToolbar;
    RecyclerView recyclerView;

    TextView totalPrice;

    Button checkout;

    ShoppingCartProducts shoppingCartProducts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        totalPrice=findViewById(R.id.totalPriceDisplay);
        shoppingCartToolbar = findViewById(R.id.shoppingCartToolBar);
        getSupportActionBar();
        setSupportActionBar(shoppingCartToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Shopping Cart");
        recyclerView = findViewById(R.id.product_in_shopping_cart);

        shoppingCartProducts = new ViewModelProvider(this).get(ShoppingCartProducts.class);
        shoppingCartProducts.setContext(getApplicationContext());
        shoppingCartProducts.fetchProducts();
        shoppingCartProducts.getProducts().observe(this, new Observer<List<ShoppingCartDTO>>() {
            @Override
            public void onChanged(List<ShoppingCartDTO> shoppingCartDTOS) {
                initializer(shoppingCartDTOS);
            }
        });
    }

    private void initializer(List<ShoppingCartDTO> products) {
        ProductInShoppingCartAdapter adapter = new ProductInShoppingCartAdapter(products);
        totalPrice.setText(String.valueOf(calculateTotal(products)));
        adapter.setOnClickListener(new ProductInShoppingCartAdapter.OnProductClick() {
            @Override
            public void OnProductClick(Long productId) {

            }

            @Override
            public void OnProductDelete(Long productId) {
                shoppingCartProducts.deleteProduct(productId);
            }

            @Override
            public void OnAddQuantity(Long cartId) {
                shoppingCartProducts.addQuantity(cartId);
            }

            @Override
            public void OnRemoveQuantity(Long cartId) {
                shoppingCartProducts.removeQuantity(cartId);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


    }
    private double calculateTotal(List<ShoppingCartDTO> cartProducts){
        double total = 0;
        for(ShoppingCartDTO cartDTO:cartProducts){
            total+=cartDTO.getProduct().getPrice()*cartDTO.getQuantity();
        }
        return total;
    }
}