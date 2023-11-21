package com.example.infinitypixelcart.BottomNavFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.infinitypixelcart.API.FetchingUserDetails;
import com.example.infinitypixelcart.Intents.AddressViewer;
import com.example.infinitypixelcart.R;
import com.example.infinitypixelcart.Service.RetrofitInterceptorService;
import com.example.infinitypixelcart.Service.TokenManager;
import com.example.infinitypixelcart.model.UserDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    TextView AccountName;
    TextView MobileNumber;
    ImageView AccountImage;
    CardView AccountDetails;
    CardView AddressDetails;
    CardView TransactionDetails;
    CardView OrderDetails;
    ImageView LogOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        AccountName = view.findViewById(R.id.accountName);
        MobileNumber = view.findViewById(R.id.mobileNumber);
        AccountImage = view.findViewById(R.id.accountImage);
        AccountDetails = view.findViewById(R.id.cardViewAccountDetails);
        AddressDetails = view.findViewById(R.id.cardViewAddressDetails);
        TransactionDetails = view.findViewById(R.id.cardViewTransactionDetails);
        OrderDetails = view.findViewById(R.id.cardViewYourOrders);
        String token = TokenManager.getInstance(getContext()).getToken();
        DecodedJWT decodedJWT = TokenManager.getInstance(getContext()).decodeJWT();
        FetchingUserDetails api = RetrofitInterceptorService.getRetrofit(token).create(FetchingUserDetails.class);
        api.getAccount(decodedJWT.getSubject()).enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.code() == 200) {
                    UserDTO userDTO = response.body();
                    AccountName.setText(userDTO.getName());
                    MobileNumber.setText(userDTO.getPhone());
                } else {
                    Toast.makeText(getContext(), "Please Login Again", Toast.LENGTH_SHORT).show();
                    LoadFragment(new loginFragment());

                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                Toast.makeText(getContext(), "Something Went Wrong!!", Toast.LENGTH_SHORT).show();
            }
        });

        AccountDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        AddressDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getContext(), AddressViewer.class);
                startActivity(intent);

            }
        });
        TransactionDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        OrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }
    private void LoadFragment(Fragment fragment){
        FragmentManager fm = getActivity().getSupportFragmentManager();;
        FragmentTransaction ft= fm.beginTransaction();
        ft.replace(R.id.container,fragment);
        ft.commit();
    }
}