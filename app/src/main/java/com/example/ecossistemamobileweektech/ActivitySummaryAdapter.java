package com.example.ecossistemamobileweektech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ActivitySummaryAdapter extends RecyclerView.Adapter<ActivitySummaryAdapter.ViewHolder> {

    private List<String> activityList;

    public ActivitySummaryAdapter(List<String> activityList) {
        this.activityList = activityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = activityList.get(position);
        holder.text.setText(name);
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("activityName", name);
            Navigation.findNavController(v).navigate(R.id.action_dashboard_to_event_details, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(android.R.id.text1);
        }
    }
}