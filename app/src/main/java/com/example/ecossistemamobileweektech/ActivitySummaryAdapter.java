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

    private List<ActivityItem> activityList;

    public ActivitySummaryAdapter(List<ActivityItem> activityList) {
        this.activityList = activityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityItem item = activityList.get(position);
        holder.textName.setText(item.getName());
        
        if (item.isProject()) {
            holder.textStatus.setVisibility(View.VISIBLE);
            if (item.isApproved()) {
                holder.textStatus.setText("Status: Aprovado");
                holder.textStatus.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_green_dark));
            } else {
                holder.textStatus.setText("Status: Pendente");
                holder.textStatus.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_orange_dark));
            }
        } else {
            holder.textStatus.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("activityName", item.getName());
            Navigation.findNavController(v).navigate(R.id.action_dashboard_to_event_details, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textActivityName);
            textStatus = itemView.findViewById(R.id.textActivityStatus);
        }
    }
}