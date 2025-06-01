package com.example.findjewelry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.findjewelry.R;
import com.example.findjewelry.adapters.UserOrderAdapter;
import com.example.findjewelry.models.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class UserAccountActivity extends AppCompatActivity {

    private RecyclerView userOrders;
    private List<Order> orderList;
    private UserOrderAdapter adapter;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        userOrders = findViewById(R.id.userOrders);
        logoutBtn = findViewById(R.id.logoutBtn);
        userOrders.setLayoutManager(new LinearLayoutManager(this));

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        orderList = new ArrayList<>();
        adapter = new UserOrderAdapter(this, orderList);
        userOrders.setAdapter(adapter);

        fetchOrders();

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(UserAccountActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(UserAccountActivity.this, MainActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_cart) {
                startActivity(new Intent(UserAccountActivity.this, CartActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_account) {
                return true;
            }
            return false;
        });

    }

    private void fetchOrders() {
        String uid = auth.getCurrentUser().getUid();
        firestore.collection("orders")
                .document(uid)
                .collection("userOrders")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    orderList.clear();
                    for (var doc : queryDocumentSnapshots.getDocuments()) {
                        Order order = doc.toObject(Order.class);
                        orderList.add(order);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to load orders", Toast.LENGTH_SHORT).show());
    }
}
