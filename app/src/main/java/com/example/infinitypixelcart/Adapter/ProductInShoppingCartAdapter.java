package com.example.infinitypixelcart.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infinitypixelcart.R;
import com.example.infinitypixelcart.model.ProductDTO;
import com.example.infinitypixelcart.model.ShoppingCartDTO;

import java.util.Base64;
import java.util.List;

public class ProductInShoppingCartAdapter extends RecyclerView.Adapter<ProductInShoppingCartAdapter.ProductInShoppingCartViewHolder> {

    private List<ShoppingCartDTO> shoppingCartProducts;

    private OnProductClick listener;
    public ProductInShoppingCartAdapter(List<ShoppingCartDTO> shoppingCartProducts) {
        this.shoppingCartProducts = shoppingCartProducts;
    }


    @NonNull
    @Override
    public ProductInShoppingCartAdapter.ProductInShoppingCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_in_shoppinng_cart,parent,false);
        return new ProductInShoppingCartViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ProductInShoppingCartAdapter.ProductInShoppingCartViewHolder holder, int position) {
        ShoppingCartDTO shoppingCartProduct = shoppingCartProducts.get(position);
        byte[] imagebytes = Base64.getDecoder().decode(shoppingCartProduct.getProduct().getImage());
        Bitmap bitmap = BitmapFactory.decodeByteArray(imagebytes, 0,imagebytes.length);
        holder.productImage.setImageBitmap(bitmap);
        holder.productName.setText(shoppingCartProduct.getProduct().getName());
        holder.productPrice.setText(String.valueOf(shoppingCartProduct.getProduct().getPrice()));
        holder.quantity.setText(String.valueOf(shoppingCartProduct.getQuantity()));

    }

    @Override
    public int getItemCount() {
        return shoppingCartProducts.size();
    }

    public class ProductInShoppingCartViewHolder extends RecyclerView.ViewHolder{
        TextView productName,productPrice,quantity;
        ImageView productImage;

        CardView productCardView;


        ImageButton addProduct,removeProduct,deleteProduct;



        public ProductInShoppingCartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productInShoppingCartProductName);
            productPrice = itemView.findViewById(R.id.productInShoppingCartProductPrice);
            productImage = itemView.findViewById(R.id.productInShoppingCartProductImage);
            productCardView = itemView.findViewById(R.id.productInShoppingCartProductCardView);
            quantity = itemView.findViewById(R.id.quantityDisplay);
            addProduct = itemView.findViewById(R.id.addQuantity);
            removeProduct = itemView.findViewById(R.id.removeQuantity);
            deleteProduct = itemView.findViewById(R.id.productShoppingCartDeleteButton);

            productCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition = getAdapterPosition();
                    if(adapterPosition != RecyclerView.NO_POSITION){
                        Long productId = shoppingCartProducts.get(adapterPosition).getProduct().getId();
                        if (listener != null) {
                            listener.OnProductClick(productId);
                        }
                    }
                }
            });

            addProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition= getAdapterPosition();
                    if(adapterPosition!=RecyclerView.NO_POSITION || listener!=null){
                        Long productId = shoppingCartProducts.get(adapterPosition).getId();
                        listener.OnAddQuantity(productId);
                    }
                }
            });

            removeProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition= getAdapterPosition();
                    if(adapterPosition!=RecyclerView.NO_POSITION || listener!=null){
                        Long productId = shoppingCartProducts.get(adapterPosition).getId();
                        listener.OnRemoveQuantity(productId);
                    }
                }
            });

            deleteProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition= getAdapterPosition();
                    if(adapterPosition!=RecyclerView.NO_POSITION || listener!=null){
                        Long productId = shoppingCartProducts.get(adapterPosition).getProduct().getId();
                        listener.OnProductDelete(productId);
                    }
                }
            });



        }
    }

    public void setOnClickListener(OnProductClick listener){
        this.listener = listener;
    }

    public interface OnProductClick{
        void OnProductClick(Long productId);
        void OnProductDelete(Long productId);
        void OnAddQuantity(Long cartId);
        void OnRemoveQuantity(Long cartId);
    }
}
