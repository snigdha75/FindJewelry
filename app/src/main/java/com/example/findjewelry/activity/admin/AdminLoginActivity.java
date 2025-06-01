package com.example.findjewelry.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.findjewelry.R;
import com.example.findjewelry.activity.LoginActivity;
import com.example.findjewelry.activity.SignupActivity;
import com.example.findjewelry.databinding.ActivityAdminLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginActivity extends AppCompatActivity {
    ActivityAdminLoginBinding binding;
    FirebaseAuth auth;
    private final String admin_email = "admin@findjewelry.com";
    private final String admin_password = "admin1234";
    private Button userLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.loginBtn.setOnClickListener(v -> {
            String email = binding.email.getText().toString();
            String pass = binding.password.getText().toString();

            if (email.equals(admin_email) && pass.equals(admin_password)) {
               auth.signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(authResult -> {
                            Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(this, AdminHomeActivity.class));
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Admin Login Failed", Toast.LENGTH_SHORT).show());
           } else {
                Toast.makeText(this, "Invalid Admin Credentials", Toast.LENGTH_SHORT).show();
            }
        });

        userLogin = findViewById(R.id.userLogin);

        userLogin.setOnClickListener(view -> {
            startActivity(new Intent(AdminLoginActivity.this, LoginActivity.class));
        });
    }
}
