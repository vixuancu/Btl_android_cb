package com.example.testproject.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
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
        tax=Math.round(managmentCart.getTotalFee()*percentTax*100.0)/100;
        double total=Math.round((managmentCart.getTotalFee()+tax+delivery)*100)/100;
        double itemTotal=Math.round(managmentCart.getTotalFee()*100)/100;
        binding.totalFeeTxt.setText("$"+itemTotal);
        binding.taxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$"+delivery);
        binding.totalTxt.setText("$"+total);
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(view -> finish());
    }
}