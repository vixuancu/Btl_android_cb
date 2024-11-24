package com.example.testproject.Helper;

import android.content.Context;
import android.widget.Toast;


import com.example.testproject.Domain.Foods;

import java.util.ArrayList;


public class ManagmentCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagmentCart(Context context) {
        this.context = context;
        this.tinyDB=new TinyDB(context);
    }

    public void insertFood(Foods item) {
        ArrayList<Foods> listpop = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int i = 0; i < listpop.size(); i++) {
            if (listpop.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                n = i;
                break;
            }
        }
        if(existAlready){
            listpop.get(n).setNumberInCart(item.getNumberInCart());
        }else{
            listpop.add(item);
        }
        tinyDB.putListObject("CartList",listpop);// Lưu lại trạng thái mới
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Foods> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    public Double getTotalFee(){
        ArrayList<Foods> listItem=getListCart();
        double fee=0;
        for (int i = 0; i < listItem.size(); i++) {
            fee=fee+(listItem.get(i).getPrice()*listItem.get(i).getNumberInCart());
        }
        return fee;
    }
    public void minusNumberItem(ArrayList<Foods> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        if(listItem.get(position).getNumberInCart()==1){
            listItem.remove(position);
        }else{
            listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()-1);
        }
        // Nếu danh sách trống, xóa toàn bộ giỏ hàng
        if (listItem.isEmpty()) {
            clearCart();
        } else {
            tinyDB.putListObject("CartList", listItem);
        }

        changeNumberItemsListener.change();
    }
    public  void plusNumberItem(ArrayList<Foods> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()+1);
        tinyDB.putListObject("CartList",listItem);
        changeNumberItemsListener.change();
    }
    public void clearCart() {
        // Tạo danh sách trống
        ArrayList<Foods> emptyList = new ArrayList<>();
        // Lưu danh sách trống vào TinyDB để xóa giỏ hàng
        tinyDB.putListObject("CartList", emptyList);
        // Hiển thị thông báo giỏ hàng đã được xóa
//        Toast.makeText(context, "Cart has been cleared", Toast.LENGTH_SHORT).show();
    }
}
