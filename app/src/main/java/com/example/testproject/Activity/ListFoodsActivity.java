package com.example.testproject.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.Adapter.FoodListAdapter;
import com.example.testproject.Domain.Foods;
import com.example.testproject.R;
import com.example.testproject.databinding.ActivityListFoodsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFoodsActivity extends BaseActivity {
    ActivityListFoodsBinding binding;
    private RecyclerView.Adapter adapterListFood;
    private int categoryId;
    private String categoryName;
    private String searchText;
    private boolean isSearch;
    private ArrayList<Foods> allFoods; // Danh sách toàn bộ món ăn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListFoodsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            getIntentExtra();
            initList(); // Lấy danh sách món ăn từ Firebase
        }catch (Exception e) {
            Log.e("DEBUG", "LOI LE ", e);
        }

    }

    private void initList() {
        DatabaseReference myRef = database.getReference("Foods");
        binding.progressBar.setVisibility(View.VISIBLE);
        allFoods = new ArrayList<>(); // Khởi tạo danh sách món ăn
        Query query;

        if (isSearch) {
            query = myRef.orderByChild("Title");
        } else {
            query = myRef.orderByChild("CategoryId").equalTo(categoryId);
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Foods food = issue.getValue(Foods.class);
                        if (food != null) {
                            if (isSearch) {
                                String title = food.getTitle().toLowerCase();
                                String searchQuery = searchText.toLowerCase();
                                if (title.contains(searchQuery)) {
                                    allFoods.add(food);
                                }
                            } else {
                                allFoods.add(food);
                            }
                        }
                    }
                    applyFilters(); // Gọi applyFilters để áp dụng bộ lọc
                    // Thiết lập RecyclerView sau khi đã có danh sách
                    if(allFoods.size()>0) {
                        binding.foodListView.setLayoutManager(new GridLayoutManager(ListFoodsActivity.this,2));
                        adapterListFood=new FoodListAdapter(allFoods);
                        binding.foodListView.setAdapter(adapterListFood);
                    }
                    binding.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    // Xử lý filter
    private void applyFilters() {
        String selectedPrice = getIntent().getStringExtra("selectedPrice");
        String selectedLocation = getIntent().getStringExtra("selectedLocation");
        String selectedTime = getIntent().getStringExtra("selectedTime");

        ArrayList<Foods> filteredList = new ArrayList<>();

        for (Foods food : allFoods) { // Lặp qua allFoods thay vì filteredList
            boolean matchesPrice = true;
            boolean matchesLocation = true;
            boolean matchesTime = true;
            try {
                // Kiểm tra điều kiện lọc giá
                if (selectedPrice != null) {
                    if (selectedPrice.equals("1$ - 10$")) {
                        matchesPrice = food.getPrice() <= 10;
                    } else if (selectedPrice.equals("10$ - 30$")) {
                        matchesPrice = food.getPrice() > 10 && food.getPrice() <= 30;
                    } else if (selectedPrice.equals("more than 30$")) {
                        matchesPrice = food.getPrice() > 30;
                    }
                }

                // Kiểm tra điều kiện lọc vị trí
                if (selectedLocation != null && food.getLocation() != null) {
                    // So sánh tên vị trí với giá trị được chọn
                    matchesLocation = food.getLocation().getLoc().equals(selectedLocation);
                }

                // Kiểm tra điều kiện lọc thời gian
                if (selectedTime != null) {
                    if (selectedTime.equals("0 - 10 min")) {
                        matchesTime = food.getTimeValue() < 10;
                    } else if (selectedTime.equals("10 - 30 min")) {
                        matchesTime = food.getTimeValue() >= 10 && food.getTimeValue() <= 30;
                    } else if (selectedTime.equals("more than 30 min")) {
                        matchesTime = food.getTimeValue() > 30;
                    }
                }

                // Nếu tất cả các điều kiện đều khớp, thêm món ăn vào danh sách lọc
                if (matchesPrice && matchesLocation && matchesTime) {
                    filteredList.add(food);
                }
            }catch (Exception e) {
                Log.e("DEBUG", "Lỗi khi lọc thực phẩm: " + food.getTitle() + ", Error: " + e.getMessage());
            }


        }

        // Cập nhật adapter với danh sách đã lọc
        if(filteredList.size()>0) {
            binding.foodListView.setLayoutManager(new GridLayoutManager(ListFoodsActivity.this,2));
            adapterListFood=new FoodListAdapter(filteredList);
            binding.foodListView.setAdapter(adapterListFood);
        }
    }

    private void getIntentExtra() {
        categoryId = getIntent().getIntExtra("CategoryId", 0);
        categoryName = getIntent().getStringExtra("CategoryName");
        Log.i("CategoryID", "[" + categoryId + "]");
        searchText = getIntent().getStringExtra("text");
        isSearch = getIntent().getBooleanExtra("isSearch", false);
        if (categoryName != null) {
            binding.titleTxt.setText(categoryName);
        }

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
