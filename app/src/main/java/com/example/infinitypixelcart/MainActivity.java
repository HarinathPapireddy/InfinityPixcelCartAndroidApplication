package com.example.infinitypixelcart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infinitypixelcart.API.UserAuthenticationApi;
import com.example.infinitypixelcart.Intents.MainPage;
import com.example.infinitypixelcart.Intents.User_Registration;
import com.example.infinitypixelcart.Service.RetrofitService;
import com.example.infinitypixelcart.Service.TokenManager;
import com.example.infinitypixelcart.model.AuthTokenResponse;
import com.example.infinitypixelcart.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializer();

    }

    public void initializer() {

        Intent intent = new Intent(MainActivity.this, MainPage.class);
        if (TokenManager.getInstance(getApplicationContext()).contains() && TokenManager.getInstance(getApplicationContext()).isValid()) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            EditText Textusername = findViewById(R.id.editTextUsername);
            EditText Textpassword = findViewById(R.id.editTextPassword);
            TextView textViewRegister = findViewById(R.id.textViewRegister);

            Button login = findViewById(R.id.buttonLogin);
            final User user = new User();
            UserAuthenticationApi api = RetrofitService.getRetrofit().create(UserAuthenticationApi.class);
            login.setOnClickListener(view -> {

                final String username = Textusername.getText().toString().trim();
                final String password = Textpassword.getText().toString().trim();


                if (!username.equals("") && !password.equals("")) {
                    user.setUsername(username);
                    user.setPassword(password);
                    api.userAuthentication(user).enqueue(new Callback<AuthTokenResponse>() {
                        @Override
                        public void onResponse(Call<AuthTokenResponse> call, Response<AuthTokenResponse> response) {
                            if (response.isSuccessful() && response.code() == 200) {
                                Toast.makeText(MainActivity.this, "login success", Toast.LENGTH_SHORT).show();
                                TokenManager.getInstance(getApplicationContext()).saveToken(response.body().getToken());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            } else if (response.code() == 400) {
                                Toast.makeText(MainActivity.this, "Password Incorrect", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 404) {
                                Toast.makeText(MainActivity.this, "User Not Found please register", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthTokenResponse> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "Please enter All Fields", Toast.LENGTH_SHORT).show();
                }
            });

            textViewRegister.setOnClickListener(view -> {
                Intent register_intent = new Intent(MainActivity.this, User_Registration.class);
                startActivity(register_intent);
            });
        }
    }
}