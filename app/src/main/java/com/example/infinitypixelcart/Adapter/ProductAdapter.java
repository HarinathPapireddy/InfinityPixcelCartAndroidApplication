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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infinitypixelcart.R;
import com.example.infinitypixelcart.model.ProductDTO;

import java.util.Base64;
import java.util.List;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    private List<ProductDTO> productsList;


    private OnProductClickListener onProductClickListener;


    public ProductAdapter(List<ProductDTO> productsList) {
        this.productsList = productsList;
    }


    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_item_list,parent,false);

        return new ProductHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        ProductDTO product = productsList.get(position);
        byte[] imagebytes = Base64.getDecoder().decode(product.getImage());
        Bitmap bitmap = BitmapFactory.decodeByteArray(imagebytes, 0,imagebytes.length);
        holder.productImage.setImageBitmap(bitmap);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.valueOf(product.getPrice()));
        holder.productRating.setText(product.getRatings());
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }


    class ProductHolder extends RecyclerView.ViewHolder {
        TextView productName,productPrice;
        TextView productRating;
        ImageView productImage;

        CardView productCardView;

        ImageView addToCart;


        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.productInShoppingCartProductImage);
            productName=(TextView) itemView.findViewById(R.id.productInShoppingCartProductName);
            productPrice= (TextView) itemView.findViewById(R.id.productInShoppingCartProductPrice);
            productRating=itemView.findViewById(R.id.productInShoppingCartProductRatings);
            productCardView=itemView.findViewById(R.id.productInShoppingCartProductCardView);
            addToCart=itemView.findViewById(R.id.imageViewAddToCart);

            productCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition = getAdapterPosition();
                    if(adapterPosition != RecyclerView.NO_POSITION){
                        Long productId = productsList.get(adapterPosition).getId();
                        if (onProductClickListener != null) {
                            onProductClickListener.onProductClick(productId);
                        }
                    }
                }
            });
            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });


        }
    }

    public void setOnProductListener(OnProductClickListener listener){
        this.onProductClickListener=listener;
    }
    public interface OnProductClickListener{
        void onProductClick(Long productId);
        void onProductAddToCart(Long productId);
    }
}
