package com.example.findjewelry.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.findjewelry.R;
import com.example.findjewelry.adapters.AdminOrdersAdapter;
import com.example.findjewelry.models.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class AdminOrderActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdminOrdersAdapter adapter;
    private ArrayList<Order> orders;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);

        recyclerView = findViewById(R.id.adminOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orders = new ArrayList<>();
        adapter = new AdminOrdersAdapter(this, orders);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadAllOrders();

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_orders);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(AdminOrderActivity.this, AdminHomeActivity.class));
                return true;
            } else if (id == R.id.nav_orders) {
                return true;
            } else if (id == R.id.nav_account) {
                startActivity(new Intent(AdminOrderActivity.this, AdminAccountActivity.class));
                return true;
            }
            return false;
        });
    }
    private void loadAllOrders() {
        db.collectionGroup("userOrders")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    orders.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Order order = doc.toObject(Order.class);
                        if (order != null) {
                            orders.add(order);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load orders", Toast.LENGTH_SHORT).show();
                });
    }
}
