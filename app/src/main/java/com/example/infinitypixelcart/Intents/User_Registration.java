package com.example.infinitypixelcart.Intents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.infinitypixelcart.API.UserAuthenticationApi;
import com.example.infinitypixelcart.MainActivity;
import com.example.infinitypixelcart.R;
import com.example.infinitypixelcart.Service.RetrofitService;
import com.example.infinitypixelcart.model.UserDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class User_Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        initializer();

    }

    private void initializer() {
        EditText editText_Name=findViewById(R.id.editTextRegistrationName);
        EditText editText_Username =  findViewById(R.id.editTextRegistrationUsername);
        EditText editText_Password = findViewById(R.id.editTextRegistrationPassword);
        EditText editText_Confirm_Password = findViewById(R.id.editTextRegistrationConfirmPassword);
        EditText editText_Email= findViewById(R.id.editTextRegistrationEmail);
        EditText editText_Phone=findViewById(R.id.editTextRegistrationPhoneNumber);
        RadioGroup genderRadioGroup= findViewById(R.id.genderRadioGroup);
        Button registerButton = findViewById(R.id.userRegistrationButton);


        registerButton.setOnClickListener(view -> {
            String name = editText_Name.getText().toString();
            String username = editText_Username.getText().toString();
            String password = editText_Password.getText().toString();
            String confirmPassword =editText_Confirm_Password.getText().toString();
            String email = editText_Email.getText().toString();
            String phone = editText_Phone.getText().toString();

            int selectedId = genderRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedId);


            String gender =selectedRadioButton.getText().toString();
            if(!confirmPassword.equals("") && password.equals(confirmPassword)){
                if(!name.equals("") || !username.equals("") || !password.equals("") || !email.equals("") ||  !phone.equals("") || !gender.equals("")){
                    final UserDTO user =  new UserDTO();
                    user.setName(name);
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setEmail(email);
                    user.setGender(gender);
                    user.setPhone(phone);
                    registerUser(user);
                }
                else{
                    Toast.makeText(this, "All Fields are Mandatory!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Passwords Should match !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser(UserDTO user) {
        UserAuthenticationApi api = RetrofitService.getRetrofit().create(UserAuthenticationApi.class);
        api.userRegistration(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code()==200){
                    Intent intent = new Intent(User_Registration.this, MainActivity.class);
                    Toast.makeText(User_Registration.this, "User Registration Successful", Toast.LENGTH_SHORT).show();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else if(response.code()==302){
                    Toast.makeText(User_Registration.this, "Username or Email already Exists.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(User_Registration.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(User_Registration.this, "Something Went wrong !!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}