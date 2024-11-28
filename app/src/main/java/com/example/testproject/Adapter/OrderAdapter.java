package com.example.testproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.Activity.OrderDetailActivity;
import com.example.testproject.Domain.Orders;
import com.example.testproject.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private List<Orders> orderList;

    public OrderAdapter(Context context, List<Orders> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Orders order = orderList.get(position);
        holder.textOrderId.setText("Mã đơn: #" + order.getOrderId1());
        holder.textOrderStatus.setText("Trạng thái: " + order.getStatus());
        holder.textOrderDate.setText("Ngày đặt: " + order.getOrderDate());

        // Chuyển đến màn hình chi tiết đơn hàng khi nhấn vào
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
            intent.putExtra("orderId", order.getOrderId1());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textOrderId, textOrderStatus, textOrderDate;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textOrderId = itemView.findViewById(R.id.textOrderId);
            textOrderStatus = itemView.findViewById(R.id.textOrderStatus);
            textOrderDate = itemView.findViewById(R.id.textOrderDate);
        }
    }
}

