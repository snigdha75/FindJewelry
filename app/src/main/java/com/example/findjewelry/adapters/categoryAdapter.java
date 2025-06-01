package com.example.findjewelry.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.findjewelry.R;
import com.example.findjewelry.databinding.ItemCategoriesBinding;
import com.example.findjewelry.models.category;

import java.util.ArrayList;

public class categoryAdapter extends RecyclerView.Adapter<categoryAdapter.CategoryViewHolder> {
    public interface OnCategoryClickListener {
        void onCategoryClick(category selectedCategory);
    }
    Context context;
    ArrayList<category> categories;
    private String selectedCategoryName = null;

    private OnCategoryClickListener listener;

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public categoryAdapter(Context context,ArrayList<category> categories){
        this.context = context;
        this.categories = categories;
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_categories,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        category cat = categories.get(position);
        holder.binding.level.setText(cat.getName());
        Glide.with(context)
                .load(cat.getIcon())
                .into(holder.binding.cartimage);

        holder.binding.cartimage.setBackgroundColor(Color.parseColor(cat.getColor()));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                if (cat.getName().equals(selectedCategoryName)) {
                    selectedCategoryName = null;
                    listener.onCategoryClick(null);
                } else {
                    selectedCategoryName = cat.getName();
                    listener.onCategoryClick(cat);
                }
                notifyDataSetChanged();
            }
        });
        if (cat.getName().equals(selectedCategoryName)) {
            holder.binding.level.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.binding.level.setTextColor(Color.BLACK); // normal
        }

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ItemCategoriesBinding binding;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCategoriesBinding.bind(itemView);
        }
    }

}
