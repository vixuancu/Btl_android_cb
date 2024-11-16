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
    private String selectedPrice;
    private int selectedLocationId;
    private String selectedTime;
    private boolean isFilter;
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
        } else if (isFilter) {
            // Nếu có bộ lọc,
            query=myRef;
            // Có thể thêm các điều kiện khác nếu bạn có thêm thông tin về Price, Location, Time từ các bộ lọc
        } else {
            query = myRef.orderByChild("CategoryId").equalTo(categoryId);

        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allFoods.clear(); // Xóa dữ liệu cũ để tránh trùng lặp khi tải lại
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
                    if (!allFoods.isEmpty()) {
                        applyFilters();//Gọi hàm lọc sau khi đã lấy toàn bộ món ăn
                    } else {
                        Log.d("DEBUG", "Không có dữ liệu để hiển thị");
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
        ArrayList<Foods> filteredList = new ArrayList<>();

        for (Foods food : allFoods) { // Lặp qua allFoods thay vì filteredList
            boolean matchesPrice = true;
            boolean matchesLocation = true;
            boolean matchesTime = true;
            try {
                // Kiểm tra điều kiện lọc giá
//                Log.d("DEBUG", "Gia tri cua selectedPrice: " + selectedPrice);
                if (selectedPrice != null) {
//                    Log.d("DEBUG", "Gia tri cua food.getPrice(): " + food.getPrice());
                    if (selectedPrice.equals("1$ - 10$")) {
                        matchesPrice = food.getPrice() >= 1 && food.getPrice() <= 10;

                    } else if (selectedPrice.equals("10$ - 30$")) {
                        matchesPrice = food.getPrice() > 10 && food.getPrice() <= 30;
                    } else if (selectedPrice.equals("more than 30$")) {
                        matchesPrice = food.getPrice() > 30;
                    }
                    else {
                        Log.d("DEBUG", "dell co Price");
                    }
                    Log.d("DEBUG", "Ket qua kiem tra matchesPrice: " + matchesPrice);
                }


                // Kiểm tra điều kiện lọc vị trí
                if (selectedLocationId !=-1 ) {
                    // So sánh tên vị trí với giá trị được chọn
//                    Log.d("DEBUG", "LocationId " + selectedLocationId);
//                    matchesLocation = food.getLocationId() == (selectedLocationId);

                    if (food.getLocationId() == selectedLocationId)
                    {
                         matchesLocation = food.getLocationId() == selectedLocationId;
                    }
                    Log.d("DEBUG", "Ket qua kiem tra matchesLocation: " + matchesLocation);
                }


                // Kiểm tra điều kiện lọc thời gian
                if (selectedTime != null) {
                    Log.d("DEBUG", "Gia tri cua food.getTimeValue(): " + food.getTimeValue());
                    if (selectedTime.equals("0 - 10 min")) {
                        matchesTime = food.getTimeValue() < 10;
                    } else if (selectedTime.equals("10 - 30 min")) {
                        matchesTime = food.getTimeValue() >= 10 && food.getTimeValue() <= 30;
                    } else if (selectedTime.equals("more than 30 min")) {
                        matchesTime = food.getTimeValue() > 30;
                    }else {
                        Log.d("DEBUG", "dell co Time");
                    }
                    Log.d("DEBUG", "Ket qua kiem tra matchesTime: " + matchesTime);
                }

                // Nếu tất cả các điều kiện đều khớp, thêm món ăn vào danh sách lọc
                if (matchesPrice && matchesLocation && matchesTime) {
                    filteredList.add(food);
                    Log.d("DEBUG", "Danh sach filteredList sau khi add: " + filteredList.size());
                }
            }catch (Exception e) {
                Log.e("DEBUG", "Loi khi loc thuc pham: " + food.getTitle() + ", Error: " + e.getMessage());
            }


        }
        // Cập nhật adapter với danh sách đã lọc
        if(!allFoods.isEmpty()) {
            binding.foodListView.setLayoutManager(new GridLayoutManager(ListFoodsActivity.this,2));
//            adapterListFood=new FoodListAdapter(filteredList.isEmpty() ? allFoods : filteredList); bug là rỗng nó load hết
            if(filteredList.isEmpty()) {
                binding.titleTxt.setText("No matching results");
            }
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
        isFilter = getIntent().getBooleanExtra("isFilter", false);
        selectedPrice = getIntent().getStringExtra("selectedPrice");
        selectedLocationId = getIntent().getIntExtra("selectedLocationId",-1);
        selectedTime = getIntent().getStringExtra("selectedTime");

        Log.d("DEBUG", "selectedPrice: " + selectedPrice);
        Log.d("DEBUG", "selectedLocationId: " + selectedLocationId);
        Log.d("DEBUG", "selectedTime: " + selectedTime);
        if (categoryName != null) {
            binding.titleTxt.setText(categoryName);
        }

        binding.backBtn.setOnClickListener(view -> finish());
    }
}
