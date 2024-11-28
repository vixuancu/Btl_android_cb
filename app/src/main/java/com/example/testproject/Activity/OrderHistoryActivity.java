package com.example.testproject.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.Adapter.OrderAdapter;
import com.example.testproject.Domain.Orders;
import com.example.testproject.R;
import com.example.testproject.databinding.ActivityOrderHistoryBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends BaseActivity {
    ActivityOrderHistoryBinding binding; //view binding
    private OrderAdapter orderAdapter;
    private List<Orders> orderList  = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ánh xạ layout với View Binding
        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Setup Toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.toolbar.getNavigationIcon().setTint(getResources().getColor(android.R.color.white));
        }
        // Setup RecyclerView
        binding.recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo adapter một lần
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, orderList);
        binding.recyclerViewOrders.setAdapter(orderAdapter);
        // Load dữ liệu lịch sử đơn hàng

        loadOrderHistory();
    }

    private void loadOrderHistory() {
        String userId = getCurrentUserId();
        if (userId == null) {
            showToast("Người dùng chưa đăng nhập!");
            return;
        }

        DatabaseReference ordersRef = database.getReference("Orders");

        ordersRef.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear(); // Xóa dữ liệu cũ
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    Orders order = orderSnapshot.getValue(Orders.class);
                    if (order != null) {
                        orderList.add(order);
                        Log.d("DEBUG", "Order List Size: " + orderList.size());
                        Log.d("DEBUG", "Order Loaded: " + order.getOrderId1());

                    }
                }

                orderAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                checkEmptyList(); // Kiểm tra danh sách rỗng
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Không thể tải lịch sử đơn hàng!");
            }
        });
    }
    private void checkEmptyList() {
        if (orderList.isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE); // Hiển thị thông báo
            binding.recyclerViewOrders.setVisibility(View.GONE); // Ẩn RecyclerView
        } else {
            binding.emptyTxt.setVisibility(View.GONE); // Ẩn thông báo
            binding.recyclerViewOrders.setVisibility(View.VISIBLE); // Hiển thị RecyclerView
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}