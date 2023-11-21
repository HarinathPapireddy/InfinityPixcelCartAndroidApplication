package com.example.infinitypixelcart.ViewModels;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.infinitypixelcart.Service.AddressManipulationService;

import com.example.infinitypixelcart.model.AddressDTO;

import java.util.List;

public class AddressViewModel extends ViewModel {
    private MutableLiveData<List<AddressDTO>> addressDTOs = new MutableLiveData<>();
    private AddressManipulationService addressService;
    Context context;
    public void setContext( Context context){
        this.context=context;
        addressService= new AddressManipulationService(context);
    }


    public LiveData<List<AddressDTO>> getAddressDetails() {
        return addressDTOs;
    }

    public void fetchAddressDetails() {
        addressService.getUserAddresses(new AddressManipulationService.OnAddressFetchedListener() {
            @Override
            public void OnAddressFetched(List<AddressDTO> address) {
                addressDTOs.postValue(address);
            }
        });
    }

    public void addAddressDetails(AddressDTO addressDTO){
        addressService.addUserAddress(addressDTO, new AddressManipulationService.OnAddressAddedListener() {
            @Override
            public void OnAddressSuccessfullyAdded(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnAddressAdditionFailed(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteAddress(Long productId) {
        addressService.deleteUserAddress(productId, new AddressManipulationService.OnAddressDeletedListener() {
            @Override
            public void OnAddressDeletionSuccess(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                fetchAddressDetails();
            }

            @Override
            public void OnAddressDeletionFailure(String errorMessage) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
