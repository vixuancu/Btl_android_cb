package com.example.testproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testproject.R;
import com.example.testproject.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariable();
//        retrySignIn(binding.userEdt.getText().toString(),binding.passEdt.getText().toString(),3);

    }



    private void setVariable() {
        // Xử lý khi nhấn nút đăng nhập
        binding.loginBtn.setOnClickListener(view -> {
            String email = binding.userEdt.getText().toString().trim();
            String password = binding.passEdt.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty()) {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = getCurrentUserId(); // Sử dụng hàm từ BaseActivity
                        DatabaseReference userRef = database.getReference("Users").child(userId);

                        // Lấy vai trò từ Firebase
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String role = snapshot.child("role").getValue(String.class);
                                    if ("admin".equals(role)) {
                                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                    } else {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    }
                                    finish(); // Đóng LoginActivity sau khi chuyển trang
                                } else {
                                    showToast("User role not found.");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                showToast("Error fetching user role: " + error.getMessage());
                            }
                        });
                    } else {
                        showToast("Authentication failed: " + task.getException().getMessage());
                    }
                });
            } else {
                showToast("Please fill in both email and password.");
            }
        });

        // Xử lý khi nhấn nút chuyển đến màn hình đăng ký
        binding.loginToSignUpBtn.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));
    }

}