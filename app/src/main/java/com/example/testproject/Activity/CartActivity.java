package com.example.testproject.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.Adapter.CartAdapter;
import com.example.testproject.Domain.Foods;
import com.example.testproject.Domain.OrderItem;
import com.example.testproject.Domain.Orders;
import com.example.testproject.Helper.ChangeNumberItemsListener;
import com.example.testproject.Helper.ManagmentCart;
import com.example.testproject.R;
import com.example.testproject.databinding.ActivityCartBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartActivity extends BaseActivity {
    private ActivityCartBinding binding;
    private RecyclerView.Adapter adapter;
    private ManagmentCart managmentCart;
    private double tax;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        managmentCart = new ManagmentCart(this);
        setVariable();
        caculateCart();
        initList();
    }

    private void initList() {
        if(managmentCart.getListCart().isEmpty()) {
            binding.emptyTxt.setVisibility(View.VISIBLE);
            binding.scrollViewCart.setVisibility(View.GONE);
        }else {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        binding.cardView.setLayoutManager(linearLayoutManager);
        adapter= new CartAdapter(managmentCart.getListCart(), this, () -> {
            caculateCart();
            initList(); // Gọi lại để kiểm tra danh sách rỗng hay không
        });

        binding.cardView.setAdapter(adapter);
    }

    private void caculateCart() {
        double percentTax=0.02;// percent 2% tax
        double delivery=10; //dollar
        tax=Math.round(managmentCart.getTotalFee()*percentTax*100.0)/100.0;
        double total=Math.round((managmentCart.getTotalFee()+tax+delivery)*100.0)/100.0;
        double itemTotal=Math.round(managmentCart.getTotalFee()*100.0)/100.0;
        binding.totalFeeTxt.setText(String.format("$%.2f",itemTotal));
        binding.taxTxt.setText(String.format("$%.2f", tax));
        binding.deliveryTxt.setText(String.format("$%.2f",delivery));
        binding.totalTxt.setText(String.format("$%.2f",total));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(view -> finish());
        binding.placeOrderBtn.setOnClickListener(view -> showPlaceOrderDialog());
    }
    // test dialog các thức các thứ=====================================

    private void showPlaceOrderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_place_order, null);
        builder.setView(dialogView);

        // Tìm các view trong dialog
        RadioGroup paymentMethodGroup = dialogView.findViewById(R.id.paymentMethodGroup);
        Button btnPlaceOrder = dialogView.findViewById(R.id.btnPlaceOrder);
        TextView totalAmountTextView = dialogView.findViewById(R.id.totalAmount);
        EditText recipientName = dialogView.findViewById(R.id.recipientName);
        EditText recipientAddress = dialogView.findViewById(R.id.recipientAddress);
        EditText recipientPhone = dialogView.findViewById(R.id.recipientPhone);
        // Tính tổng số tiền từ giỏ hàng (bao gồm phí giao hàng và thuế nếu có)
        double percentTax = 0.02;  // Thuế 2%
        double deliveryFee = 10;   // Phí giao hàng cố định $10
        double itemTotal = managmentCart.getTotalFee();
        double tax = Math.round(itemTotal * percentTax * 100.0) / 100.0;
        double totalAmount = Math.round((itemTotal + tax + deliveryFee) * 100.0) / 100.0;

        // Hiển thị tổng tiền trong TextView
        totalAmountTextView.setText(String.format("$%.2f", totalAmount)); // Đảm bảo hiển thị chính xác

        AlertDialog dialog = builder.create();
        dialog.show();

        // Xử lý sự kiện khi người dùng nhấn nút "Đặt hàng"
        btnPlaceOrder.setOnClickListener(view -> {
            String name = recipientName.getText().toString();
            String address = recipientAddress.getText().toString();
            String phone = recipientPhone.getText().toString();
            if(name.isEmpty()) {
                Toast.makeText(CartActivity.this, "Vui long dien ten", Toast.LENGTH_SHORT).show();
                return;
            }
            if(address.isEmpty()) {
                Toast.makeText(CartActivity.this, "Vui long dien dia chi", Toast.LENGTH_SHORT).show();
                return;
            }
            if(phone.isEmpty()) {
                Toast.makeText(CartActivity.this, "Vui long dien so dien thoai", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!name.isEmpty()&&!address.isEmpty()&&!phone.isEmpty()) {
                // Lấy phương thức thanh toán đã chọn
                int selectedPaymentMethod = paymentMethodGroup.getCheckedRadioButtonId();
                if (selectedPaymentMethod == R.id.radioCash) {
                    // Nếu người dùng chọn thanh toán bằng tiền mặt, xử lý đặt hàng
                    placeOrder(name, address, phone,totalAmount);
                    dialog.dismiss();
                } else {
                    Toast.makeText(CartActivity.this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }


    private void placeOrder(String name, String address, String phone,double totalAmount) {
        // Lấy thời gian hiện tại
        String orderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        // Chuyển đổi danh sách Foods thành OrderItem
        List<OrderItem> orderItems = new ArrayList<>();
        for (Foods food : managmentCart.getListCart()) {
            orderItems.add(new OrderItem(
                    food.getId(),
                    food.getNumberInCart(),
                    food.getPrice(),
                    food.getTitle(),
                    food.getImagePath()
            ));
        }
        // Tạo đơn hàng
        Orders order = new Orders();
        order.setOrderId1(database.getReference("Orders").push().getKey()); // Tạo ID đơn hàng mới
        order.setUserId(getCurrentUserId());
        order.setStatus("Pending");
        order.setOrderDate(orderDate);
        order.setTotalPrice(totalAmount);
        order.setLocation(address);
        order.setUsername(name);
        order.setPhone(phone);
        order.setItems(orderItems); // Lấy danh sách món từ giỏ hàng
        //-----
        // Lưu đơn hàng lên Firebase
        database.getReference("Orders").child(order.getOrderId1()).setValue(order)
                .addOnSuccessListener(aVoid -> {
                    managmentCart.clearCart(); // Reset giỏ hàng
                    adapter.notifyDataSetChanged(); // Cập nhật giao diện
                    binding.emptyTxt.setVisibility(View.VISIBLE);
                    binding.scrollViewCart.setVisibility(View.GONE);
                    Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to place order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // ------Giả lập xử lý đặt hàng thành công

       // initList(); // Làm mới giao diện và hiển thị "Giỏ hàng trống"

    }

}