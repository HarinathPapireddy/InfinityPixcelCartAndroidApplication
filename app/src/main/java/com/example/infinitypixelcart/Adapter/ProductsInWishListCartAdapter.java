package com.example.infinitypixelcart.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infinitypixelcart.R;
import com.example.infinitypixelcart.model.ProductDTO;

import java.util.Base64;
import java.util.List;

public class ProductsInWishListCartAdapter extends RecyclerView.Adapter<ProductsInWishListCartAdapter.ProductsInWishListCartViewHolder> {
    private List<ProductDTO> products;


    public ProductsInWishListCartAdapter(List<ProductDTO> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductsInWishListCartAdapter.ProductsInWishListCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_in_wishlist_cart,parent,false);
        return new ProductsInWishListCartViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ProductsInWishListCartAdapter.ProductsInWishListCartViewHolder holder, int position) {
        ProductDTO product = products.get(position);
        byte[] imagebytes = Base64.getDecoder().decode(product.getImage());
        Bitmap bitmap = BitmapFactory.decodeByteArray(imagebytes, 0,imagebytes.length);
        holder.productImage.setImageBitmap(bitmap);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.valueOf(product.getPrice()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductsInWishListCartViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName;
        TextView productPrice;
        public ProductsInWishListCartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productInWishListProductImage);
            productName = itemView.findViewById(R.id.productInWishListProductName);
            productPrice = itemView.findViewById(R.id.productInWishListProductPrice);
        }
    }
}
