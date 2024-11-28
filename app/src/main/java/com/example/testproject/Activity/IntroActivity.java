package com.example.testproject.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testproject.R;
import com.example.testproject.databinding.ActivityIntroBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IntroActivity extends BaseActivity {
    ActivityIntroBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo View Binding
        binding=ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Kiểm tra trạng thái đăng nhập
        if (mAuth.getCurrentUser() != null) {
            // Người dùng đã đăng nhập, chuyển đến MainActivity
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            finish(); // Đóng IntroActivity để người dùng không quay lại được
            return; // Kết thúc phương thức tại đây
        }
        setVariable();
        getWindow().setStatusBarColor(Color.parseColor("#FFE4B5"));

    }

    private void setVariable() {
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(mAuth.getCurrentUser()!=null) {
                startActivity(new Intent(IntroActivity.this, MainActivity.class));
            }else {
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            }
            }
        });
        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroActivity.this,SignupActivity.class));
            }
        });
    }
}