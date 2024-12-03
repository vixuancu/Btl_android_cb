package com.example.testproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.Adapter.OrderAdminAdapter;
import com.example.testproject.Domain.Orders;
import com.example.testproject.R;
import com.example.testproject.databinding.ActivityAdminBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends BaseActivity implements OrderAdminAdapter.OnOrderStatusUpdateListener {
    ActivityAdminBinding binding;
    private OrderAdminAdapter orderAdminAdapter;
    private List<Orders> orderList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sử dụng View Binding
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup RecyclerView
        binding.recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách và adapter
        orderList = new ArrayList<>();
        orderAdminAdapter = new OrderAdminAdapter(this, orderList, this::showUpdateStatusDialog);
        binding.recyclerViewOrders.setAdapter(orderAdminAdapter);

        // Kiểm tra nếu người dùng bị đăng xuất
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(AdminActivity.this, LoginActivity.class));
            finish(); // Đảm bảo không quay lại AdminActivity
            return;
        }

        // Load dữ liệu đơn hàng và xử lý đăng xuất
        loadOrders();
        logOut();
    }

    private  void logOut() {
        binding.btnLogOutAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(AdminActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
    private void loadOrders() {
        // Hiển thị ProgressBar trong khi tải dữ liệu
        binding.progressBar.setVisibility(View.VISIBLE);

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    Orders order = orderSnapshot.getValue(Orders.class);
                    if (order != null) {
                        orderList.add(order);
                    }
                }
                orderAdminAdapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Log.e("AdminActivity", "Failed to load orders: " + error.getMessage());
                Toast.makeText(AdminActivity.this, "Error loading orders.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Gọi hàm này khi nhấn nút "Update Status"
    @Override
    public void onUpdateStatus(Orders order) {
        // Hiển thị hộp thoại cập nhật trạng thái
        showUpdateStatusDialog(order);
    }
    private void showUpdateStatusDialog(Orders order) {
        String[] statuses = {"Pending", "Confirmed", "Shipped", "Delivered", "Cancelled"};
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Update Order Status")
                .setItems(statuses, (dialog, which) -> updateOrderStatus(order, statuses[which]))
                .show();
    }

    private void updateOrderStatus(Orders order, String status) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(order.getOrderId1());
        orderRef.child("status").setValue(status)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Order status updated to " + status, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
