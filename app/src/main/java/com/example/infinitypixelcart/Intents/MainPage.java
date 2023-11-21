package com.example.infinitypixelcart.Intents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.infinitypixelcart.API.FetchingProductsApi;
import com.example.infinitypixelcart.BottomNavFragments.CategoryFragment;
import com.example.infinitypixelcart.BottomNavFragments.HomeFragment;
import com.example.infinitypixelcart.BottomNavFragments.ProfileFragment;
import com.example.infinitypixelcart.BottomNavFragments.WishListFragment;
import com.example.infinitypixelcart.BottomNavFragments.loginFragment;
import com.example.infinitypixelcart.R;
import com.example.infinitypixelcart.Service.RetrofitService;
import com.example.infinitypixelcart.Service.TokenManager;
import com.example.infinitypixelcart.model.ProductDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainPage extends AppCompatActivity {

    FloatingActionButton shoppingCartButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        shoppingCartButton=findViewById(R.id.shoppingCartButton);
        initializer();
    }

    private void initializer() {
        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id==R.id.home){
                    LoadFragment(new HomeFragment());
                } else if (id==R.id.categoryBadge) {
                    LoadFragment(new CategoryFragment());
                }else if(id==R.id.account){
                    String token=TokenManager.getInstance(getApplicationContext()).getToken();
                    boolean hello = TokenManager.getInstance(getApplicationContext()).isValid();
                    if(TokenManager.getInstance(getApplicationContext()).contains() && TokenManager.getInstance(getApplicationContext()).isValid()) {
                        LoadFragment(new ProfileFragment());
                    }
                    else{
                        LoadFragment(new loginFragment(new HomeFragment(),new ProfileFragment()));
                    }
                } else {
                    if(TokenManager.getInstance(getApplicationContext()).contains() && TokenManager.getInstance(getApplicationContext()).isValid()) {
                        LoadFragment(new WishListFragment());
                    }
                    else{
                        LoadFragment(new loginFragment(new HomeFragment(),new WishListFragment()));
                    }
                }

                return true;
            }
        });
        navigationView.setSelectedItemId(R.id.home);
        shoppingCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this,ShoppingCart.class);
                startActivity(intent);
            }
        });
    }
    private void LoadFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        ft.replace(R.id.container,fragment);
        ft.commit();
    }
}