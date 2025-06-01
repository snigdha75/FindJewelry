package com.example.findjewelry.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.findjewelry.R;
import com.example.findjewelry.databinding.ItemCartBinding;
import com.example.findjewelry.models.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    ArrayList<CartItem> cartItems;
    CartDeleteListener deleteListener;

    public interface CartDeleteListener {
        void onItemDeleted();
    }

    public CartAdapter(Context context, ArrayList<CartItem> cartItems, CartDeleteListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartViewHolder(ItemCartBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.binding.pname.setText(item.getName());
        holder.binding.pprice.setText(item.getPrice() + " Taka");
        holder.binding.quantity.setText(item.getQuantity() + " item(s)");

        if (item.getImage() != null && !item.getImage().isEmpty()) {
            try {
                byte[] imageBytes = android.util.Base64.decode(item.getImage(), android.util.Base64.DEFAULT);
                android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                holder.binding.cartimage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        holder.binding.deleteBtn.setOnClickListener(v -> {
            FirebaseFirestore.getInstance()
                    .collection("cart")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("items")
                    .document(item.getProductId())
                    .delete()
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show();
                        cartItems.remove(position);
                        notifyItemRemoved(position);
                        deleteListener.onItemDeleted();
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show());
        });
    }


    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding binding;

        public CartViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
