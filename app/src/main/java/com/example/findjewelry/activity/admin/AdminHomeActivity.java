package com.example.findjewelry.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.findjewelry.R;
import com.example.findjewelry.adapters.AdminProductAdapter;
import com.example.findjewelry.adapters.ProductAdapter;
import com.example.findjewelry.models.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdminHomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MaterialButton addBtn;
    ArrayList<Product> productList;
    AdminProductAdapter adapter;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        recyclerView = findViewById(R.id.product);
        FloatingActionButton addProductBtn = findViewById(R.id.addBtn);

        productList = new ArrayList<>();
        adapter = new AdminProductAdapter(this, productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadProducts();

        addProductBtn.setOnClickListener(v -> {
            startActivity(new Intent(AdminHomeActivity.this, AddProductActivity.class));
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_orders) {
                startActivity(new Intent(AdminHomeActivity.this, AdminOrderActivity.class));
                return true;
            } else if (id == R.id.nav_account) {
                startActivity(new Intent(AdminHomeActivity.this, AdminAccountActivity.class));
                return true;
            }
            return false;
        });

    }

    private void loadProducts() {
        db.collection("products")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear();
                    for (var doc : queryDocumentSnapshots.getDocuments()) {
                        Product product = doc.toObject(Product.class);
                        if (product != null) {
                            productList.add(product);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminHomeActivity.this, "Failed to load products", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProducts();
    }
}
