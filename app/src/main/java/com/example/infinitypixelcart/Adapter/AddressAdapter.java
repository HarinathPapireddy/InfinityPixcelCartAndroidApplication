package com.example.infinitypixelcart.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infinitypixelcart.R;
import com.example.infinitypixelcart.model.AddressDTO;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressHolder> {

    private List<AddressDTO> addresses;

    private OnItemClickListener itemClickListener;

    public class AddressHolder extends RecyclerView.ViewHolder {
        TextView addressType,addressHolderName, addressHolderPhone,address;
        ImageView deleteAddressButton;

        public AddressHolder(@NonNull View itemView) {
            super(itemView);
            addressType=itemView.findViewById(R.id.address_item_list_address_type);
            addressHolderName=itemView.findViewById(R.id.address_item_list_address_holder_name);
            addressHolderPhone =itemView.findViewById(R.id.address_item_list_address_holder_phone_number);
            address=itemView.findViewById(R.id.address_item_list_address);
            deleteAddressButton = itemView.findViewById(R.id.address_item_list_address_delete_button);

            deleteAddressButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Long addressId = addresses.get(position).getId();
                        if (itemClickListener != null) {
                            itemClickListener.onItemClick(addressId);
                        }
                    }
                }
            });
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Long productId);
    }



    public AddressAdapter(List<AddressDTO> addresses) {
        this.addresses = addresses;
    }

    @NonNull
    @Override
    public AddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_item_list,parent,false);

        return new AddressHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressHolder holder, int position) {

        AddressDTO addressDTO = addresses.get(position);
        holder.addressHolderName.setText(addressDTO.getName());
        holder.addressType.setText(addressDTO.getType());
        holder.addressHolderPhone.setText(addressDTO.getPhone());
        holder.address.setText(addressDTO.getLine1()+", "+addressDTO.getLine2()+",\n"+ addressDTO.getCity()+" "+addressDTO.getPincode()+",\n"+addressDTO.getState()+", "+addressDTO.getCountry()+".");
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }


}
