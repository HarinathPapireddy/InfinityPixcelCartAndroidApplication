package com.example.infinitypixelcart.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infinitypixelcart.R;

import java.util.Base64;
import java.util.List;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.ProductImageViewHolder> {

    List<String> productImages;

    public ProductImageAdapter(List<String> productImages) {
        this.productImages = productImages;
    }

    @NonNull
    @Override
    public ProductImageAdapter.ProductImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_in_product_viewer,parent,false);

        return new ProductImageAdapter.ProductImageViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ProductImageAdapter.ProductImageViewHolder holder, int position) {
        String image = productImages.get(position);
        byte[] imageBytes = Base64.getDecoder().decode(image);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0,imageBytes.length);
        holder.imageView.setImageBitmap(bitmap);


    }

    @Override
    public int getItemCount() {
        return productImages.size();
    }

    public class ProductImageViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public ProductImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.product_in_product_viewer_product_image);
        }
    }
}
