package com.example.findjewelry.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findjewelry.R;
import com.example.findjewelry.activity.admin.AdminOrderDetailsActivity;
import com.example.findjewelry.models.Order;

import java.util.List;
public class AdminOrdersAdapter extends RecyclerView.Adapter<AdminOrdersAdapter.OrderViewHolder> {
    private final Context context;
    private final List<Order> orders;

    public AdminOrdersAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.name.setText("Name: " + order.getName());
        holder.total.setText("Total: " + order.getTotalAmount() + "Taka");
        holder.status.setText("Status: " + order.getStatus());

        holder.viewDetailsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminOrderDetailsActivity.class);
            intent.putExtra("order", order);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView name, total, status;
        Button viewDetailsBtn;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            total = itemView.findViewById(R.id.amount);
            status = itemView.findViewById(R.id.status);
            viewDetailsBtn = itemView.findViewById(R.id.detailsBtn);
        }
    }
}
