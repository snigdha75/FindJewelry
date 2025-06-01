package com.example.findjewelry.activity.admin;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.findjewelry.R;
import com.example.findjewelry.models.Product;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AddProductActivity extends AppCompatActivity {

    private EditText pname, pprice, pdescription, pstock, pstatus;
    private ImageView pimage;
    private MaterialButton btnImage, btnAddProduct;

    private Uri selectedImageUri = null;

    private FirebaseFirestore db;
    private ProgressDialog progressDialog;

    private final ActivityResultLauncher<String> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    Glide.with(this).load(uri).into(pimage);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        pname = findViewById(R.id.pname);
        pprice = findViewById(R.id.pprice);
        pdescription = findViewById(R.id.pdescription);
        pstock = findViewById(R.id.pstock);
        pstatus = findViewById(R.id.pstatus);
        pimage = findViewById(R.id.pimage);
        btnImage = findViewById(R.id.btnImage);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);

        btnImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
        btnAddProduct.setOnClickListener(v -> uploadProduct());


        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            finish();
        });

    }

    private void uploadProduct() {
        String name = pname.getText().toString().trim();
        String priceStr = pprice.getText().toString().trim();
        String description = pdescription.getText().toString().trim();
        String stockStr = pstock.getText().toString().trim();
        String status = pstatus.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || description.isEmpty() || stockStr.isEmpty() || status.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }
        double price;
        int stock;

        try {
            price = Double.parseDouble(priceStr);
            stock = Integer.parseInt(stockStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        try {
            String base64Image = encodeImageToBase64(selectedImageUri);
            Product product = new Product(name, 0, base64Image, status, price, description, stock, null);

            db.collection("products")
                    .add(product)
                    .addOnSuccessListener(documentReference -> {
                        documentReference.update("id", documentReference.getId());

                        progressDialog.dismiss();
                        Toast.makeText(AddProductActivity.this, "Product added", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(AddProductActivity.this, "Failed to add product", Toast.LENGTH_SHORT).show();
                    });

        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(this, "Error encoding image", Toast.LENGTH_SHORT).show();
        }
    }
    private String encodeImageToBase64(Uri imageUri) throws Exception {
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        Bitmap compressedBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
