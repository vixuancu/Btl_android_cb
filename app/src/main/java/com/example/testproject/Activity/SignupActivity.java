package com.example.testproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testproject.Domain.Users;
import com.example.testproject.R;
import com.example.testproject.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignupActivity extends BaseActivity {
    ActivitySignupBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setVariable();
    }
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void setVariable() {
        binding.signupBtn.setOnClickListener(view -> {
                String email = binding.userEdt.getText().toString();
                String password = binding.passEdt.getText().toString();

            if(!isValidEmail(email)) {
                Toast.makeText(SignupActivity.this, "Invalid Email Format", Toast.LENGTH_SHORT).show();
                return;
            }
                if(password.length()<6) {
                    Toast.makeText(SignupActivity.this, "your password must be 6 character", Toast.LENGTH_SHORT).show();
                    return;
                }
            // Lấy role từ RadioGroup
            String role = binding.radioUser.isChecked() ? "user" : "admin";

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // Lấy ID của người dùng
                            String userId = mAuth.getCurrentUser().getUid();
                            // Tạo đối tượng người dùng
                            Users newUser = new Users(userId, email, role);

                            // Lưu người dùng vào Firebase Database
                            database.getReference("Users").child(userId).setValue(newUser)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(SignupActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(SignupActivity.this, "Failed to save user role", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Kiểm tra lỗi nếu email đã tồn tại
                            if (task.getException() != null && task.getException().getMessage() != null &&
                                    task.getException().getMessage().contains("The email address is already in use")) {
                                Toast.makeText(SignupActivity.this, "This email is already registered.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e(TAG, "Failure: ", task.getException());
                                Toast.makeText(SignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
        });
        binding.signUptoLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }
}