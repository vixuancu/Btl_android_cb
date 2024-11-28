package com.example.testproject.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.testproject.Adapter.OrderItemAdapter;
import com.example.testproject.Domain.Foods;
import com.example.testproject.Domain.OrderItem;
import com.example.testproject.Domain.Orders;
import com.example.testproject.R;
import com.example.testproject.databinding.ActivityOrderDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends BaseActivity  {
    private ActivityOrderDetailBinding binding; // View Binding
    private OrderItemAdapter orderItemAdapter;
    private List<OrderItem> orderItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ánh xạ layout với View Binding
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup Toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.toolbar.getNavigationIcon().setTint(getResources().getColor(android.R.color.white));
        }

        // Lấy thông tin đơn hàng từ Intent
        String orderId = getIntent().getStringExtra("orderId");
        if (orderId != null) {
            loadOrderDetails(orderId);
        } else {
            showToast("Không tìm thấy thông tin đơn hàng!");
            finish();
        }
    }

    private void loadOrderDetails(String orderId) {
        String userId = getCurrentUserId();
        if (userId == null) {
            showToast("Người dùng chưa đăng nhập!");
            return;
        }

        DatabaseReference orderRef = database.getReference("Orders").child(orderId);
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Orders order = snapshot.getValue(Orders.class);
                if (order != null) {
                    // Cập nhật thông tin
                    binding.textOrderId.setText("Mã đơn: #" + order.getOrderId1());
                    binding.textOrderStatus.setText("Trạng thái: " + order.getStatus());
                    binding.textOrderDate.setText("Ngày đặt: " + order.getOrderDate());
                    binding.textTotalPrice.setText("Tổng tiền: $" + order.getTotalPrice());

                    // Lấy danh sách món ăn từ nhánh "items"
                    orderItemList.clear();
                    for (DataSnapshot itemSnapshot : snapshot.child("items").getChildren()) { // Đảm bảo nhánh là "items"
                        OrderItem item = itemSnapshot.getValue(OrderItem.class);
                        if (item != null) {
                            orderItemList.add(item);
                        }
                    }

                    // Cập nhật RecyclerView với danh sách món ăn
                    if (orderItemAdapter == null) {
                        orderItemAdapter = new OrderItemAdapter(OrderDetailActivity.this, orderItemList);
                        binding.recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(OrderDetailActivity.this));
                        binding.recyclerViewOrderItems.setAdapter(orderItemAdapter);
                    } else {
                        orderItemAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Không thể tải chi tiết đơn hàng!");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}