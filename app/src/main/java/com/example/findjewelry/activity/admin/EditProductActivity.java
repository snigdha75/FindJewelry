package com.example.findjewelry.activity.admin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.findjewelry.R;
import com.example.findjewelry.models.Product;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProductActivity extends AppCompatActivity {

    private EditText pname, pprice, pdescription, pstock, pstatus;
    private ImageView pimage;
    private MaterialButton btnUpdate;
    private FirebaseFirestore db;
    private ProgressDialog progressDialog;
    private String productId;
    private String imageBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        pname = findViewById(R.id.pname);
        pprice = findViewById(R.id.pprice);
        pdescription = findViewById(R.id.pdescription);
        pstock = findViewById(R.id.pstock);
        pstatus = findViewById(R.id.pstatus);
        pimage = findViewById(R.id.pimage);
        btnUpdate = findViewById(R.id.btnUpdate);

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.setCancelable(false);

        productId = getIntent().getStringExtra("productId");
        String name = getIntent().getStringExtra("name");
        double price = getIntent().getDoubleExtra("price", 0);
        String description = getIntent().getStringExtra("description");
        int stock = getIntent().getIntExtra("stock", 0);
        String status = getIntent().getStringExtra("status");
        imageBase64 = getIntent().getStringExtra("imageUrl");

        pname.setText(name);
        pprice.setText(String.valueOf(price));
        pdescription.setText(String.valueOf(description));
        pstock.setText(String.valueOf(stock));
        pstatus.setText(status);

        if (imageBase64 != null && !imageBase64.isEmpty()) {
            byte[] imageBytes = android.util.Base64.decode(imageBase64, android.util.Base64.DEFAULT);
            Glide.with(this)
                    .asBitmap()
                    .load(imageBytes)
                    .into(pimage);
        }

        btnUpdate.setOnClickListener(v -> updateProduct());
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            finish();
        });
    }

    private void updateProduct() {
        String name = pname.getText().toString().trim();
        String priceStr = pprice.getText().toString().trim();
        String description = pdescription.getText().toString().trim();
        String stockStr = pstock.getText().toString().trim();
        String status = pstatus.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || description.isEmpty() || stockStr.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        double price;
        double discount;
        int stock;
        try {
            price = Double.parseDouble(priceStr);
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        Product updatedProduct = new Product(name, 0, imageBase64, status, price, description, stock, productId);
        db.collection("products").document(productId)
                .set(updatedProduct)
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    Toast.makeText(EditProductActivity.this, "Product updated", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(EditProductActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                });
    }
}
