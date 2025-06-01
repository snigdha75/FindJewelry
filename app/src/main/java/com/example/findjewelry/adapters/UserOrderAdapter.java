package com.example.findjewelry.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.findjewelry.R;
import com.example.findjewelry.models.CartItem;
import com.example.findjewelry.models.Order;

import java.util.List;

public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.ViewHolder> {

    private final Context context;
    private final List<Order> orderList;

    public UserOrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public UserOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserOrderAdapter.ViewHolder holder, int position) {
        Order order = orderList.get(position);
        CartItem firstItem = order.getItems().get(0);

        if (firstItem.getImage() != null && !firstItem.getImage().isEmpty()) {
            try {
                byte[] decodedBytes = android.util.Base64.decode(firstItem.getImage(), android.util.Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                holder.productImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        holder.orderInfo.setText("Items: " + order.getItems().size() +
                "\nTotal: " + order.getTotalAmount() +
                "\nStatus: " + order.getStatus());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView orderInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.orderImage);
            orderInfo = itemView.findViewById(R.id.orderInfo);
        }
    }
}
