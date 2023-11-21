package com.example.infinitypixelcart.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.infinitypixelcart.API.FetchingUserDetails;
import com.example.infinitypixelcart.API.UpdatingUserDetails;
import com.example.infinitypixelcart.model.AddressDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddressManipulationService {
    private List<AddressDTO> addressDTOS;
    private Context context;

    private String token=TokenManager.getInstance(context).getToken();

    private String username = TokenManager.getInstance(context).decodeJWT().getSubject();

    public AddressManipulationService(){

    }

    public AddressManipulationService(Context context){
        this.context=context;
    }

    public void getUserAddresses(OnAddressFetchedListener listener){
        FetchingUserDetails api = RetrofitService.getRetrofitWithInterceptor(token).create(FetchingUserDetails.class);
        api.getAllAddresses(username).enqueue(new Callback<List<AddressDTO>>() {
            @Override
            public void onResponse(Call<List<AddressDTO>> call, Response<List<AddressDTO>> response) {
                listener.OnAddressFetched(response.body());
            }

            @Override
            public void onFailure(Call<List<AddressDTO>> call, Throwable t) {

            }
        });
    }


    public void addUserAddress(AddressDTO addressDTO,OnAddressAddedListener listener){
        UpdatingUserDetails api= RetrofitService.getRetrofitWithInterceptor(token).create(UpdatingUserDetails.class);
        api.addAddress(addressDTO).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code()==201){
                    listener.OnAddressSuccessfullyAdded("Account successfully added");
                }
                else{
                    listener.OnAddressAdditionFailed("Please Check Your Internet Connection");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.OnAddressAdditionFailed("Something Went Wrong");
            }
        });
    }

    public void deleteUserAddress(final Long id,OnAddressDeletedListener listener){
        UpdatingUserDetails api= RetrofitService.getRetrofitWithInterceptor(token).create(UpdatingUserDetails.class);
        api.deleteAddress(username,id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code()==200){
                    listener.OnAddressDeletionSuccess("Deleted Address");
                }
                else {
                    listener.OnAddressDeletionFailure("Something Went Wrong");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                    listener.OnAddressDeletionFailure("Something Went Wrong");
            }
        });
    }




    public interface OnAddressFetchedListener {
        void OnAddressFetched(List<AddressDTO> addressDTOS);
    }

    public  interface OnAddressDeletedListener {

        void OnAddressDeletionSuccess(String message);
        void OnAddressDeletionFailure(String errorMessage);

    }

    public interface  OnAddressAddedListener{
        void OnAddressSuccessfullyAdded(String message);
        void OnAddressAdditionFailed(String message);
    }
}
