package com.example.testproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.testproject.Activity.DetailActivity;
import com.example.testproject.Domain.Foods;
import com.example.testproject.Helper.ManagmentCart;
import com.example.testproject.R;

import java.util.ArrayList;

public class BestFoodAdapter extends RecyclerView.Adapter<BestFoodAdapter.ViewHolder> {
    private ArrayList<Foods> items;
    private Context context;
    private ManagmentCart managmentCart;

    public BestFoodAdapter(ArrayList<Foods> items, Context context) {
        this.items = items;
        this.context = context; // Giữ tham chiếu mạnh đến Context
        this.managmentCart = new ManagmentCart(context); // Khởi tạo giỏ hàng với Context trực tiếp
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_best_deal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foods food = items.get(position);

        holder.titleTxt.setText(food.getTitle());
        holder.priceTxt.setText(String.format("$%.2f", food.getPrice()));
        holder.timeTxt.setText(String.format("%d min", food.getTimeValue()));
        holder.starTxt.setText(String.valueOf(food.getStar()));

        Glide.with(context)
                .load(food.getImagePath())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);

        // Sự kiện click vào hình ảnh để xem chi tiết
        holder.pic.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", food);
            context.startActivity(intent);
        });

        // Sự kiện click vào TextView để thêm vào giỏ hàng
        holder.addBestFood.setOnClickListener(v -> {
            food.setNumberInCart(1); // Số lượng mặc định là 1 khi thêm
            managmentCart.insertFood(food);
            Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show(); // Hiển thị thông báo
        });
    }

    @Override
    public int getItemCount() {
        return (items != null) ? items.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, priceTxt, starTxt, timeTxt, addBestFood;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            starTxt = itemView.findViewById(R.id.starTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            addBestFood = itemView.findViewById(R.id.addBestFood); // TextView dùng để thêm vào giỏ hàng
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
