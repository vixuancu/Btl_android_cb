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
import com.example.testproject.Helper.ChangeNumberItemsListener;
import com.example.testproject.Helper.ManagmentCart;
import com.example.testproject.R;
import com.example.testproject.databinding.ActivityCartBinding;

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
        adapter= new CartAdapter(managmentCart.getListCart(), this, () -> caculateCart());
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
            // Lấy phương thức thanh toán đã chọn
            int selectedPaymentMethod = paymentMethodGroup.getCheckedRadioButtonId();
            if (selectedPaymentMethod == R.id.radioCash) {
                // Nếu người dùng chọn thanh toán bằng tiền mặt, xử lý đặt hàng
                placeOrder();
                dialog.dismiss();
            } else {
                Toast.makeText(CartActivity.this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void placeOrder() {
        // Giả lập xử lý đặt hàng thành công
        managmentCart.clearCart();  // Reset giỏ hàng
        adapter.notifyDataSetChanged();  // Cập nhật giao diện giỏ hàng
        binding.emptyTxt.setVisibility(View.VISIBLE);
        binding.scrollViewCart.setVisibility(View.GONE);

        // Hiển thị thông báo đặt hàng thành công (có thể sử dụng Toast hoặc Snackbar)
        Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
    }

}