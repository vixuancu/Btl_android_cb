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
        binding.loginBtn.setOnClickListener(view -> {
            String email = binding.userEdt.getText().toString();
            String password = binding.passEdt.getText().toString();
            if(!email.isEmpty() && !password.isEmpty()) {
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, task -> {
                    if(task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else {
                        Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(LoginActivity.this, "may deo biet dien email, password a", Toast.LENGTH_SHORT).show();
            }
        });
        binding.loginToSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
}

}