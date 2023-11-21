package com.example.infinitypixelcart.BottomNavFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.infinitypixelcart.API.UserAuthenticationApi;
import com.example.infinitypixelcart.MainActivity;
import com.example.infinitypixelcart.R;
import com.example.infinitypixelcart.Service.RetrofitService;
import com.example.infinitypixelcart.Service.TokenManager;
import com.example.infinitypixelcart.model.AuthTokenResponse;
import com.example.infinitypixelcart.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link loginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class loginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Fragment fromFragment;
    private Fragment toFragment;

    public loginFragment(){

    }

    public loginFragment(Fragment fromFragment, Fragment toFragment) {
        // Required empty public constructor
        this.fromFragment=fromFragment;
        this.toFragment=toFragment;

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment loginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static loginFragment newInstance(String param1, String param2) {
        loginFragment fragment = new loginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    EditText editTextUserName;
    EditText editTextPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editTextUserName=view.findViewById(R.id.editTextUsernameFragment);
        editTextPassword=view.findViewById(R.id.editTextPasswordFragment);
        Button login = view.findViewById(R.id.buttonLoginFragment);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();
                if(username != null && password != null){
                    User user= new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    UserAuthenticationApi api = RetrofitService.getRetrofit().create(UserAuthenticationApi.class);
                    api.userAuthentication(user).enqueue(new Callback<AuthTokenResponse>() {
                        @Override
                        public void onResponse(Call<AuthTokenResponse> call, Response<AuthTokenResponse> response) {
                            if (response.isSuccessful() && response.code() == 200) {
                                Toast.makeText(getContext(), "login success", Toast.LENGTH_SHORT).show();
                                TokenManager.getInstance(getContext()).saveToken(response.body().getToken());
                                //startActivity(intent);
                                LoadFragment(toFragment);

                            } else if (response.code() == 400) {
                                Toast.makeText(getContext(), "Password Incorrect", Toast.LENGTH_SHORT).show();
                            } else if (response.code() == 404) {
                                Toast.makeText(getContext(), "User Not Found please register", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                LoadFragment(fromFragment);
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthTokenResponse> call, Throwable t) {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            LoadFragment(fromFragment);
                        }
                    });
                }
                else{
                    Toast.makeText(getContext(), "Please Enter All the Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;


    }
    private void LoadFragment(Fragment fragment){
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        ft.replace(R.id.container,fragment);
        ft.commit();
    }
}