package com.example.testproject.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.testproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class BaseActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    public String TAG = "uilover";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
    }
    // Lấy User ID hiện tại
    protected String getCurrentUserId() {
        return mAuth.getCurrentUser() !=null ? mAuth.getCurrentUser().getUid():null;
    }
    // Hiển thị thông báo Toast
    protected  void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}