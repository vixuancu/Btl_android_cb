package com.example.testproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testproject.Domain.Foods;
import com.example.testproject.Domain.OrderItem;
import com.example.testproject.R;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder>{
    private List<OrderItem> orderItemList;
    private Context context;

    // Constructor
    public OrderItemAdapter(Context context, List<OrderItem> orderItemList) {
        this.context = context;
        this.orderItemList = orderItemList;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem item = orderItemList.get(position);
        holder.foodName.setText(item.getTitle());
        holder.foodPrice.setText("$" + item.getPrice());
        holder.foodQuantity.setText("x" + item.getQuantity());
        Glide.with(context).load(item.getImagePath()).into(holder.foodImage);
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodPrice, foodQuantity;
        ImageView foodImage;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.tvFoodName);
            foodPrice = itemView.findViewById(R.id.tvFoodPrice);
            foodQuantity = itemView.findViewById(R.id.tvFoodQuantity);
            foodImage = itemView.findViewById(R.id.ivFoodImage);
        }
    }
}
