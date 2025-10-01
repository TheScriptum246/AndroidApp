package com.example.projekatsalon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onAddToCartClick(Product product);
    }

    public ProductAdapter(Context context, List<Product> productList, OnProductClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());
        holder.productDescription.setText(product.getDescription());

        int imageResource = getImageResource(product.getImageName());
        holder.productImage.setImageResource(imageResource);

        holder.addToCartButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToCartClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private int getImageResource(String imageName) {
        switch (imageName) {
            case "polish":
                return R.drawable.polish;
            case "essential_kit":
                return R.drawable.essential_kit;
            case "manicure_starter":
                return R.drawable.manicure_starter;
            case "nail_file":
                return R.drawable.nail_file;
            case "lamp":
                return R.drawable.lamp;
            default:
                return R.drawable.ic_launcher_foreground;
        }
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productDescription;
        ImageView productImage;
        Button addToCartButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productDescription = itemView.findViewById(R.id.product_description);
            productImage = itemView.findViewById(R.id.product_image);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
        }
    }
}