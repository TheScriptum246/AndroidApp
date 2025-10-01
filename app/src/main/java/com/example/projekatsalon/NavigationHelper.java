package com.example.projekatsalon;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.cardview.widget.CardView;

public class NavigationHelper {

    private Activity activity;
    private String currentUser;

    public NavigationHelper(Activity activity, String currentUser) {
        this.activity = activity;
        this.currentUser = currentUser;
    }

    public View setupNavigation(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View navView = inflater.inflate(R.layout.custom_navbar, parent, false);

        CardView storeCard = navView.findViewById(R.id.nav_store_card);
        CardView scheduleCard = navView.findViewById(R.id.nav_schedule_card);

        storeCard.setOnClickListener(v -> {
            if (currentUser == null || currentUser.isEmpty()) {
                Toast.makeText(activity, "Please log in first", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!(activity instanceof StoreActivity)) {
                Intent intent = new Intent(activity, StoreActivity.class);
                intent.putExtra("user_export_key", currentUser);
                activity.startActivity(intent);
            }
        });

        scheduleCard.setOnClickListener(v -> {
            if (currentUser == null || currentUser.isEmpty()) {
                Toast.makeText(activity, "Please log in first", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!(activity instanceof ScheduleActivity)) {
                Intent intent = new Intent(activity, ScheduleActivity.class);
                intent.putExtra("user_export_key", currentUser);
                activity.startActivity(intent);
            }
        });

        return navView;
    }
}