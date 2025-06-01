package com.example.findjewelry.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.findjewelry.R;
import com.example.findjewelry.models.CartItem;

import java.util.List;
public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ItemViewHolder> {
    private final Context context;
    private final List<CartItem> items;

    public OrderItemsAdapter(Context context, List<CartItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_product, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.name.setText(item.getName());
        holder.price.setText("৳" + item.getPrice());
        holder.quantity.setText("Qty: " + item.getQuantity());
        if (item.getImage() != null && !item.getImage().isEmpty()) {
            byte[] imageBytes = Base64.decode(item.getImage(), Base64.DEFAULT);
            holder.imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, quantity;
        ImageView imageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);
            quantity = itemView.findViewById(R.id.productQuantity);
            imageView = itemView.findViewById(R.id.productImage);
        }
    }
}
