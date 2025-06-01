package com.example.findjewelry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.findjewelry.R;
import com.example.findjewelry.databinding.ActivityPlaceOrderBinding;
import com.example.findjewelry.models.CartItem;
import com.example.findjewelry.models.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.UUID;

public class PlaceOrderActivity extends AppCompatActivity {

    ActivityPlaceOrderBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    ArrayList<CartItem> cartItems;
    double totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaceOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        cartItems = (ArrayList<CartItem>) getIntent().getSerializableExtra("cart");

        for (CartItem item : cartItems) {
            totalAmount += item.getPrice() * item.getQuantity();
        }

        binding.total.setText("Total: " + totalAmount + " Taka");
        binding.payment.setText("Cash on Delivery");

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            finish();
        });

        binding.placeOrderBtn.setOnClickListener(v -> {
            String name = binding.name.getText().toString().trim();
            String phone = binding.phoneNumber.getText().toString().trim();
            String address = binding.address.getText().toString().trim();
            String city = binding.city.getText().toString().trim();
            String notes = binding.notes.getText().toString().trim();
            String paymentMethod = binding.payment.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || city.isEmpty()) {
                Toast.makeText(this, "Please fill all required details", Toast.LENGTH_SHORT).show();
                return;
            }

            placeOrder(name, phone, address, city, notes, paymentMethod);
        });
    }

    private void placeOrder(String name, String phone, String address, String city, String notes, String paymentMethod) {
        String uid = auth.getCurrentUser().getUid();
        String orderId = UUID.randomUUID().toString();
        String status = "Pending";

        Order order = new Order(orderId, uid, name, phone, cartItems, totalAmount,
                city, address, notes, paymentMethod, status,System.currentTimeMillis());

        firestore.collection("orders")
                .document(uid)
                .collection("userOrders")
                .document(orderId)
                .set(order)
                .addOnSuccessListener(unused -> {
                    firestore.collection("cart")
                            .document(uid)
                            .collection("items")
                            .get()
                            .addOnSuccessListener(query -> {
                                for (var doc : query.getDocuments()) {
                                    doc.getReference().delete();
                                }
                                Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(this, UserAccountActivity.class);
                                startActivity(intent);
                                finish();

                            });
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Order failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
