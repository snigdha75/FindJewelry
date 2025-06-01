package com.example.findjewelry.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.findjewelry.R;
import com.example.findjewelry.activity.productActivity;
import com.example.findjewelry.databinding.ItemProductBinding;
import com.example.findjewelry.models.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>  {
    Context context;
    ArrayList<Product> products;

    public ProductAdapter(Context context,ArrayList<Product> products){
        this.context = context;
        this.products = products;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);

        byte[] imageBytes = android.util.Base64.decode(product.getImageUrl(), android.util.Base64.DEFAULT);
        android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        holder.binding.pimage.setImageBitmap(bitmap);

        holder.binding.label.setText(product.getName());
        holder.binding.pprice.setText(product.getPrice() + " Taka");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, productActivity.class);
            intent.putExtra("productId", product.getId());
            intent.putExtra("name", product.getName());
            intent.putExtra("imageUrl", product.getImageUrl());
            intent.putExtra("description", product.getDescription());
            intent.putExtra("price", product.getPrice());

            context.startActivity(intent);

        });
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        ItemProductBinding binding;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemProductBinding.bind(itemView);
        }
    }
    public void setFilteredList(ArrayList<Product> filteredList) {
        this.products = filteredList;
        notifyDataSetChanged();
    }

}
