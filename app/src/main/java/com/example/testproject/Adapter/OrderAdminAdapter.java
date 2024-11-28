package com.example.testproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.Domain.Orders;
import com.example.testproject.R;

import java.util.List;

public class OrderAdminAdapter extends RecyclerView.Adapter<OrderAdminAdapter.OrderAdminViewHolder> {

    private Context context;
    private List<Orders> orderList;
    private OnOrderStatusUpdateListener listener;

    public interface OnOrderStatusUpdateListener {
        void onUpdateStatus(Orders order);
    }

    public OrderAdminAdapter(Context context, List<Orders> orderList, OnOrderStatusUpdateListener listener) {
        this.context = context;
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_admin, parent, false);
        return new OrderAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdminViewHolder holder, int position) {
        Orders order = orderList.get(position);

        holder.tvOrderId.setText("Order ID: #" + order.getOrderId1());
        holder.tvUserName.setText("User: " + order.getUsername());
        holder.txtPhone.setText("Phone: " + order.getPhone());
        holder.txtAdress.setText("Adress: " + order.getLocation());
        holder.tvOrderTime.setText("Order Time: " + order.getOrderDate());
        holder.tvTotalPrice.setText("Total: $" + order.getTotalPrice());
        holder.tvOrderStatus.setText("Status: " + order.getStatus());

        // Đổi màu trạng thái
        switch (order.getStatus()) {
            case "Pending":
                holder.tvOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_orange_light));
                break;
            case "Confirmed":
                holder.tvOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_blue_light));
                break;
            case "Shipped":
                holder.tvOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_light));
                break;
            case "Delivered":
                holder.tvOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                break;
            case "Cancelled":
                holder.tvOrderStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
                break;
        }

        holder.btnUpdateStatus.setOnClickListener(v -> listener.onUpdateStatus(order));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderAdminViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvUserName, tvOrderTime, tvTotalPrice, tvOrderStatus,txtPhone,txtAdress;
        Button btnUpdateStatus;

        public OrderAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            txtAdress = itemView.findViewById(R.id.txtAdress);
            tvOrderTime = itemView.findViewById(R.id.tvOrderTime);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            btnUpdateStatus = itemView.findViewById(R.id.btnUpdateStatus);
        }
    }
}

