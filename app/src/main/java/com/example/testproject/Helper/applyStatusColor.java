package com.example.testproject.Helper;

import android.content.Context;
import android.widget.TextView;

public class applyStatusColor {
    public static void applyStatusColor(TextView textView, String status, Context context) {
        int color;
        switch (status) {
            case "Pending":
                color = context.getResources().getColor(android.R.color.holo_orange_light);
                break;
            case "Confirmed":
                color = context.getResources().getColor(android.R.color.holo_blue_light);
                break;
            case "Shipped":
                color = context.getResources().getColor(android.R.color.holo_green_light);
                break;
            case "Delivered":
                color = context.getResources().getColor(android.R.color.holo_green_dark);
                break;
            case "Cancelled":
                color = context.getResources().getColor(android.R.color.holo_red_light);
                break;
            default:
                color = context.getResources().getColor(android.R.color.darker_gray);
                break;
        }
        textView.setTextColor(color);
    }

}
