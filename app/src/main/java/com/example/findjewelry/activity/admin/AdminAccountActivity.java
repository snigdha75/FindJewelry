package com.example.findjewelry.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.findjewelry.R;
import com.example.findjewelry.activity.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AdminAccountActivity extends AppCompatActivity {
    Button logoutBtn;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_account);

        logoutBtn = findViewById(R.id.logoutBtn);
        bottomNavigationView = findViewById(R.id.nav);

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(AdminAccountActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        bottomNavigationView.setSelectedItemId(R.id.nav_account);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, AdminHomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_orders) {
                startActivity(new Intent(this, AdminOrderActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_account) {
                return true;
            }
            return false;
        });
    }
}