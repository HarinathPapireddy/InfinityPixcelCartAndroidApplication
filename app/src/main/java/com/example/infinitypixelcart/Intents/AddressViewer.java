package com.example.infinitypixelcart.Intents;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.infinitypixelcart.Adapter.AddressAdapter;
import com.example.infinitypixelcart.R;
import com.example.infinitypixelcart.Service.AddressManipulationService;
import com.example.infinitypixelcart.Service.TokenManager;
import com.example.infinitypixelcart.ViewModels.AddressViewModel;
import com.example.infinitypixelcart.model.AddressDTO;

import java.util.List;

public class AddressViewer extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView addressRecyclerView;
    private List<AddressDTO> addressList;
    AddressViewModel addressViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrress_viewer);
        toolbar=findViewById(R.id.addressToolbar);
        if(toolbar!=null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Address Details");
        }
        addressRecyclerView=findViewById(R.id.recycler_view_address_viewer);
        addressViewModel = new ViewModelProvider(this).get(AddressViewModel.class);
        addressViewModel.setContext(getApplicationContext());
        initializer();
    }

    private void initializer() {
        addressViewModel.fetchAddressDetails();
        addressRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        addressViewModel.getAddressDetails().observe(this, new Observer<List<AddressDTO>>() {
            @Override
            public void onChanged(List<AddressDTO> addressDTOS) {
                addressList=addressDTOS;
                AddressAdapter addressAdapter = new AddressAdapter(addressList);
                addressRecyclerView.setAdapter(addressAdapter);
                addressAdapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Long productId) {
                        addressViewModel.deleteAddress(productId);
                    }
                });
            }
        });
    }
    private void populateAddress(List<AddressDTO> addresses){

    }

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result!=null && result.getResultCode()==RESULT_OK){
                addressViewModel.fetchAddressDetails();
            }
        }
    });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.address_custom_took_bar,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId= item.getItemId();
        if(itemId == android.R.id.home){
            super.onBackPressed();
        } else if (itemId == R.id.addAddress) {
//
            showDialogBox();


        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogBox() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_address_dialog_box);
        ImageView cancelButton = dialog.findViewById(R.id.cancel_button);
        Button addAddressButton = dialog.findViewById(R.id.address_add_button);
        EditText editTextName = dialog.findViewById(R.id.add_address_holder_name);
        EditText editTextPhone= dialog.findViewById(R.id.add_address_holder_phone);
        EditText editTextAddressType= dialog.findViewById(R.id.add_address_type);
        EditText editTextAddressLine1 = dialog.findViewById(R.id.add_address_line1);
        EditText editTextAddressLine2= dialog.findViewById(R.id.add_address_line2);
        EditText editTextAddressCity = dialog.findViewById(R.id.add_address_city);
        EditText editTextAddressPincode = dialog.findViewById(R.id.add_address_pincode);
        EditText editTextAddressState= dialog.findViewById(R.id.add_address_state);
        EditText editTextAddressCountry= dialog.findViewById(R.id.add_address_country);
        AddressManipulationService addressService = new AddressManipulationService(getApplicationContext());
        addAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String phone= editTextPhone.getText().toString().trim();
                String type= editTextAddressType.getText().toString().trim();
                String line1 = editTextAddressLine1.getText().toString().trim();
                String line2 = editTextAddressLine2.getText().toString().trim();
                String city = editTextAddressCity.getText().toString().trim();
                String pincode = editTextAddressPincode.getText().toString().trim();
                String state = editTextAddressState.getText().toString().trim();
                String country = editTextAddressCountry.getText().toString().trim();
                if(name.equals("") || phone.equals("") || type.equals("") || line1.equals("") || line2.equals("") || city.equals("") || pincode.equals("") || state.equals("") || country.equals("")){
                    Toast.makeText(getApplicationContext(), "Please Fill All the Fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    final AddressDTO addressDTO = new AddressDTO();
                    addressDTO.setName(name);
                    addressDTO.setPhone(phone);
                    addressDTO.setType(type);
                    addressDTO.setLine1(line1);
                    addressDTO.setLine2(line2);
                    addressDTO.setCity(city);
                    addressDTO.setPincode(pincode);
                    addressDTO.setState(state);
                    addressDTO.setCountry(country);
                    addressDTO.setUsername(TokenManager.getInstance(getApplicationContext()).decodeJWT().getSubject());
                    addressService.addUserAddress(addressDTO, new AddressManipulationService.OnAddressAddedListener() {
                        @Override
                        public void OnAddressSuccessfullyAdded(String message) {
                            addressViewModel.fetchAddressDetails();
                            Toast.makeText(AddressViewer.this, message, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        @Override
                        public void OnAddressAdditionFailed(String message) {
                            Toast.makeText(AddressViewer.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });

                }

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