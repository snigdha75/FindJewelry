package com.example.findjewelry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.findjewelry.R;
import com.example.findjewelry.activity.admin.AdminLoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText userEmail, userPassword;
    private Button loginButton, signupButton, adminLoginButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        loginButton = findViewById(R.id.buttonLogin);
        signupButton = findViewById(R.id.buttonSignup);
        adminLoginButton = findViewById(R.id.buttonAdminLogin);

        loginButton.setOnClickListener(view -> {
            String email = userEmail.getText().toString().trim();
            String password = userPassword.getText().toString().trim();

            if (email.isEmpty()) {
                userEmail.setError("Email is required");
                userEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                userPassword.setError("Password is required");
                userPassword.requestFocus();
                return;
            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        signupButton.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

        adminLoginButton.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, AdminLoginActivity.class));
        });
    }
}
