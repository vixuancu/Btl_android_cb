package com.example.testproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.Adapter.BestFoodAdapter;
import com.example.testproject.Adapter.CategoryAdapter;
import com.example.testproject.Domain.Category;
import com.example.testproject.Domain.Foods;
import com.example.testproject.Domain.Location;
import com.example.testproject.Domain.Price;
import com.example.testproject.Domain.Time;
import com.example.testproject.R;
import com.example.testproject.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        
        initLocation();
        initTime();
        initPrice();
        initBestFood();
        initCategory();
        setVariable();
        performSearch();

    }

    private void openFilter() {
        // Lấy giá trị từ Spinner cho Price, Location và Time
        String selectedPrice = binding.priceSp.getSelectedItem().toString();
        String selectedLocation = binding.locationSp.getSelectedItem().toString();
        String selectedTime = binding.timeSp.getSelectedItem().toString();


        HashMap<String, Integer> locationMap = new HashMap<>();
        locationMap.put("HaNoi-cs1", 0);  // Hà Nội có ID là 0
        locationMap.put("HaNoi-cs2", 1); // Đà Nẵng có ID là 1
        // Tạo Intent để mở ListFoodsActivity
        Intent intent = new Intent(MainActivity.this, ListFoodsActivity.class);

        // Truyền các giá trị lọc qua Intent
        intent.putExtra("selectedPrice", selectedPrice);
//        intent.putExtra("selectedLocation", selectedLocation);
        intent.putExtra("selectedTime", selectedTime);
        intent.putExtra("isFilter", true);
        // Tìm locationId từ selectedLocation
        Integer locationId = locationMap.get(selectedLocation);
        if (locationId != null) {
            intent.putExtra("selectedLocationId", locationId);
        } else {
            Log.d("DEBUG", "Khong tim thay LocationId cho vi tri: " + selectedLocation);
        }
        // Khởi chạy ListFoodsActivity
        startActivity(intent);
    }

    private void performSearch() {
        String text = binding.searchEdt.getText().toString();
        if (!text.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, ListFoodsActivity.class);
            intent.putExtra("text", text);
            intent.putExtra("isSearch", true);
            startActivity(intent);
        }
    }
    // Hàm thực hiện tìm kiếm
    private void setVariable() {
        // logout
        binding.logoutBtn.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        });
        // order history
        binding.btnOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,OrderHistoryActivity.class));
            }
        });

        // Đặt lắng nghe cho nút tìm kiếm
        binding.searchBtn.setOnClickListener(view -> {
            performSearch(); // Gọi hàm tìm kiếm
        });
        // filter
        binding.filterBtn.setOnClickListener(v -> openFilter());

// Đặt lắng nghe cho hành động của bàn phím trên searchEdt
        binding.searchEdt.setOnEditorActionListener((v, actionId, event) -> {
            // Kiểm tra nếu hành động là tìm kiếm (Enter)
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                performSearch(); // Gọi hàm tìm kiếm
                return true; // Đánh dấu rằng sự kiện đã được xử lý
            }
            return false; // Không xử lý sự kiện
        });



        binding.cartBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,CartActivity.class)));
    }

    private void initBestFood() {
        DatabaseReference myRef = database.getReference("Foods");
        binding.progressBarBestFood.setVisibility(View.VISIBLE);
        ArrayList<Foods> list =new ArrayList<>();
        Query query = myRef.orderByChild("BestFood").equalTo(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        list.add(issue.getValue(Foods.class));
                    }
                    if(!list.isEmpty()) {
                        binding.bestFoodView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));
//                        RecyclerView.Adapter adapter= new BestFoodAdapter(list);
                        RecyclerView.Adapter adapter = new BestFoodAdapter(list, MainActivity.this);
                        binding.bestFoodView.setAdapter(adapter);
                    }
                    binding.progressBarBestFood.setVisibility(View.GONE);
                    }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    };
    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        ArrayList<Category> list =new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Category category = issue.getValue(Category.class);
                        if (category != null) {
                            Log.d("DEBUG", "CategoryId: " + category.getId());
                            Log.d("DEBUG", "CategoryName: " + category.getName());
                            list.add(category);
                        } else {
                            Log.d("DEBUG", "Category is null for: " + issue.getKey());
                        }
                    }
                    if(list.size()>0) {
                        binding.categoryView.setLayoutManager((new GridLayoutManager(MainActivity.this,4)));
                        RecyclerView.Adapter adapter= new CategoryAdapter(MainActivity.this,list);
                        binding.categoryView.setAdapter(adapter);
                    }
                    binding.progressBarCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initLocation() {
        DatabaseReference myRef = database.getReference("Location");
        ArrayList<Location> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot issue: snapshot.getChildren()) {
                        list.add(issue.getValue(Location.class));
                    }
                    ArrayAdapter<Location> adapter= new ArrayAdapter<>(MainActivity.this,R.layout.sp_item,list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.locationSp.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initTime() {
        DatabaseReference myRef = database.getReference("Time");
        ArrayList<Time> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot issue: snapshot.getChildren()) {
                        list.add(issue.getValue(Time.class));
                    }
                    ArrayAdapter<Time> adapter= new ArrayAdapter<>(MainActivity.this,R.layout.sp_item,list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.timeSp.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initPrice() {
        DatabaseReference myRef = database.getReference("Price");
        ArrayList<Price> list = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot issue: snapshot.getChildren()) {
                        list.add(issue.getValue(Price.class));
                    }
                    ArrayAdapter<Price> adapter= new ArrayAdapter<>(MainActivity.this,R.layout.sp_item,list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.priceSp.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}