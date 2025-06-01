package com.example.findjewelry.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.findjewelry.R;
import com.example.findjewelry.activity.admin.AddProductActivity;
import com.example.findjewelry.activity.admin.EditProductActivity;
import com.example.findjewelry.models.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private FirebaseFirestore firestore;

    public AdminProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.name.setText(product.getName());
        holder.price.setText(product.getPrice() + " Taka");

        Bitmap bitmap = base64ToBitmap(product.getImageUrl());
        if (bitmap != null) {
            holder.image.setImageBitmap(bitmap);
        } else {
        }

        holder.deleteBtn.setOnClickListener(v -> {
            String productId = product.getId();
            firestore.collection("products").document(productId)
                    .delete()
                    .addOnSuccessListener(unused -> {
                        productList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, productList.size());
                    });
        });

        holder.editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditProductActivity.class);
            intent.putExtra("productId", product.getId());
            intent.putExtra("name", product.getName());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("description", product.getDescription());
            intent.putExtra("stock", product.getStock());
            intent.putExtra("status", product.getStatus());
            intent.putExtra("imageUrl", product.getImageUrl());
            context.startActivity(intent);
        });

    }

    private Bitmap base64ToBitmap(String base64Str) {
        try {
            byte[] decodedBytes = android.util.Base64.decode(base64Str, android.util.Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, price;
        Button editBtn, deleteBtn;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.productImage);
            name = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
