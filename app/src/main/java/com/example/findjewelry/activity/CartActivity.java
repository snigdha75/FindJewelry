package com.example.findjewelry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.findjewelry.R;
import com.example.findjewelry.adapters.CartAdapter;
import com.example.findjewelry.databinding.ActivityCartBinding;
import com.example.findjewelry.models.CartItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    CartAdapter adapter;
    ArrayList<CartItem> cartItems;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    double subtotal = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        cartItems = new ArrayList<>();
        adapter = new CartAdapter(this, cartItems, () -> loadCartItems());
        binding.cartList.setLayoutManager(new LinearLayoutManager(this));
        binding.cartList.setAdapter(adapter);

        loadCartItems();
        binding.continuebtn.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, PlaceOrderActivity.class);
            intent.putExtra("cart", cartItems);
            startActivity(intent);

        });
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            finish();
        });

        BottomNavigationView bottomNavigationView = findViewById(com.example.findjewelry.R.id.navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(CartActivity.this, MainActivity.class));
                return true;
            } else if (id == R.id.nav_cart) {
                return true;
            } else if (id == R.id.nav_account) {
                startActivity(new Intent(CartActivity.this, UserAccountActivity.class));
                return true;
            }
            return false;
        });
    }

    private void loadCartItems() {
        String uid = auth.getCurrentUser().getUid();
        firestore.collection("cart")
                .document(uid)
                .collection("items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    cartItems.clear();
                    subtotal = 0.0;

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        CartItem item = snapshot.toObject(CartItem.class);
                        if (item != null) {
                            item.setProductId(snapshot.getId());
                            subtotal += item.getPrice() * item.getQuantity();
                            cartItems.add(item);
                        }
                    }

                    binding.textView2.setText(subtotal + " Taka");
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(CartActivity.this, "Failed to load cart", Toast.LENGTH_SHORT).show());
    }


}
