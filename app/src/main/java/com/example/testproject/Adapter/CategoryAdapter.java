package com.example.testproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.testproject.Activity.ListFoodsActivity;
import com.example.testproject.Domain.Category;
import com.example.testproject.Domain.Foods;
import com.example.testproject.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewholder> {
    ArrayList<Category> items;
    Context context;

    public CategoryAdapter(Context context,ArrayList<Category> items) {
        this.items = items;this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_category,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.viewholder holder, int position) {
        Category category = items.get(position);
        holder.titleTxt.setText(category.getName());
        switch (position) {
            case  0 : {
                holder.pic.setBackgroundResource(R.drawable.cat_0_background);
                break;
            }
            case  1 : {
                holder.pic.setBackgroundResource(R.drawable.cat_1_background);
                break;
            }
            case  2 : {
                holder.pic.setBackgroundResource(R.drawable.cat_2_background);
                break;
            }
            case  3 : {
                holder.pic.setBackgroundResource(R.drawable.cat_3_background);
                break;
            }
            case  4 : {
                holder.pic.setBackgroundResource(R.drawable.cat_4_background);
                break;
            }
            case  5 : {
                holder.pic.setBackgroundResource(R.drawable.cat_5_background);
                break;
            }
            case  6 : {
                holder.pic.setBackgroundResource(R.drawable.cat_6_background);
                break;
            }
            case  7 : {
                holder.pic.setBackgroundResource(R.drawable.cat_7_background);
                break;
            }

        }
        int drawableResourceId = context.getResources().getIdentifier(category.getImagePath()
                ,"drawable",context.getPackageName());
        Glide.with(context)
                .load(drawableResourceId)
                .into(holder.pic);

        holder.itemView.setOnClickListener(view -> {

                Intent intent = new Intent(context, ListFoodsActivity.class);
            Log.d("DEBUG", "Clicked position: " + position);
            Log.d("DEBUG", "CategoryId: " + category.getId());
            Log.d("DEBUG", "CategoryName: " + category.getName());
                intent.putExtra("CategoryId", category.getId());  // Lấy Id
                intent.putExtra("CategoryName", category.getName());  // Lấy Name
                context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public class viewholder extends RecyclerView.ViewHolder{
        TextView titleTxt;
        ImageView pic;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.catNameTxt);
            pic = itemView.findViewById(R.id.imgCat);

        }
    }
}
