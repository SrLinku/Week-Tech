package com.example.ecossistemamobileweektech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.textViewTitle.setText(event.getTitle());
        holder.textViewSpeaker.setText(event.getSpeaker());
        holder.textViewTime.setText(event.getTime());
        holder.textViewType.setText(event.getType().toUpperCase());
        
        // Dynamic color for badge if needed (simplified here)
        if ("Projeto".equalsIgnoreCase(event.getType())) {
            holder.textViewType.setTextColor(holder.itemView.getContext().getColor(R.color.text_secondary));
        } else {
            holder.textViewType.setTextColor(holder.itemView.getContext().getColor(R.color.primary));
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewSpeaker, textViewTime, textViewType;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewSpeaker = itemView.findViewById(R.id.textViewSpeaker);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewType = itemView.findViewById(R.id.textViewType);
        }
    }
}