package com.example.findjewelry.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.findjewelry.R;
import com.example.findjewelry.databinding.ActivityProductBinding;
import com.example.findjewelry.models.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class productActivity extends AppCompatActivity {

    ActivityProductBinding binding;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    String productId, name, description, image;
    double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        productId = getIntent().getStringExtra("productId");
        name = getIntent().getStringExtra("name");
        description = getIntent().getStringExtra("description");
        image = getIntent().getStringExtra("imageUrl");
        price = getIntent().getDoubleExtra("price", 0);

        binding.productName.setText(name);
        binding.productPrice.setText("Price: " + price + " Taka");
        binding.productDescription.setText(description);

        if (image != null && !image.isEmpty()) {
            byte[] bytes = Base64.decode(image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            binding.productImage.setImageBitmap(bitmap);
        }

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            finish();
        });
        binding.addtocardbtn.setOnClickListener(v -> {
            String uid = auth.getCurrentUser().getUid();

            CartItem cartItem = new CartItem(productId, name, image, price, 1);

            firestore.collection("cart")
                    .document(uid)
                    .collection("items")
                    .document(productId)
                    .set(cartItem)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(productActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(productActivity.this, CartActivity.class));
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(productActivity.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
