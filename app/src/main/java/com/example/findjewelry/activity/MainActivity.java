package com.example.findjewelry.activity;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.findjewelry.R;
import com.example.findjewelry.adapters.ProductAdapter;
import com.example.findjewelry.adapters.categoryAdapter;
import com.example.findjewelry.databinding.ActivityMainBinding;
import com.example.findjewelry.models.Product;
import com.example.findjewelry.models.category;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mancj.materialsearchbar.MaterialSearchBar;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    categoryAdapter categoryAdapter;
    ArrayList<category> categories;

    ProductAdapter productAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initCategories();
        initProduct();
        initSlider();
        setupSearchBar();

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            binding.productList.setPadding(0, 0, 0, 120);
            binding.productList.setClipToPadding(false);

            return insets;
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_cart) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                return true;
            } else if (id == R.id.nav_account) {
                startActivity(new Intent(MainActivity.this, UserAccountActivity.class));
                return true;
            }
            return false;
        });
    }
    private void initSlider(){
        binding.carousel.addData(new CarouselItem(R.drawable.im1,""));
        binding.carousel.addData(new CarouselItem(R.drawable.im2,""));
        binding.carousel.addData(new CarouselItem(R.drawable.im4,""));
    }
    void initCategories(){
        categories = new ArrayList<>();
        categories.add(new category("Ring", R.drawable.im6, "black", "Description",1));
        categories.add(new category("Necklace", R.drawable.im4, "black", "Description",2));
        categories.add(new category("Earring", R.drawable.im1, "black", "Description",3));
        categories.add(new category("Bracelet", R.drawable.im3, "black", "Description",4));

        categoryAdapter = new categoryAdapter(this, categories);
        categoryAdapter.setOnCategoryClickListener(selectedCategory -> {
            if (selectedCategory == null) {
                productAdapter.setFilteredList(products);
            } else {
                filterProductsByCategory(selectedCategory.getName());
            }
        });


        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        binding.categoriesList.setLayoutManager(layoutManager);
        binding.categoriesList.setAdapter(categoryAdapter);
    }
    void initProduct() {
        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this, products);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        binding.productList.setLayoutManager(layoutManager);
        binding.productList.setAdapter(productAdapter);

        binding.productList.setPadding(0, 0, 0, 120);
        binding.productList.setClipToPadding(false);

        FirebaseFirestore.getInstance().collection("products")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Product product = doc.toObject(Product.class);
                        products.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                });
    }

    private void setupSearchBar() {
        binding.search.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled) productAdapter.setFilteredList(products);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                filterProductsBySearch(text.toString());
            }

            @Override
            public void onButtonClicked(int buttonCode) {}
        });

        binding.search.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProductsBySearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filterProductsBySearch(String query) {
        ArrayList<Product> filtered = new ArrayList<>();
        for (Product p : products) {
            if (p.getName().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(p);
            }
        }
        productAdapter.setFilteredList(filtered);
    }
    void filterProductsByCategory(String categoryName) {
        ArrayList<Product> filtered = new ArrayList<>();
        for (Product product : products) {
            if (product.getName() != null && product.getName().equalsIgnoreCase(categoryName)) {
                filtered.add(product);
            }
        }
        productAdapter.setFilteredList(filtered);
    }



}