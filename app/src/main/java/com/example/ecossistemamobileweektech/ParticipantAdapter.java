package com.example.ecossistemamobileweektech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ViewHolder> {
    private List<Participant> participants;

    public ParticipantAdapter(List<Participant> participants) {
        this.participants = participants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_participant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Participant participant = participants.get(position);
        holder.name.setText(participant.getName());
        holder.details.setText("RA: " + participant.getRa() + " | Curso: " + participant.getCourse() + " (" + participant.getSeries() + " série)");
        holder.event.setText("Atividade: " + participant.getActivity() + (participant.isCoffee() ? " (Com Coffee)" : " (Sem Coffee)"));

        if (participant.isAttended()) {
            holder.attendanceButton.setText("Presença Confirmada ✅");
            holder.attendanceButton.setEnabled(false);
            holder.attendanceButton.setTextColor(0xFF4CAF50); // Verde
        } else {
            holder.attendanceButton.setText("Confirmar Presença");
            holder.attendanceButton.setEnabled(true);
            holder.attendanceButton.setOnClickListener(v -> {
                participant.setAttended(true);
                notifyItemChanged(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, details, event;
        android.widget.Button attendanceButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewParticipantName);
            details = itemView.findViewById(R.id.textViewParticipantDetails);
            event = itemView.findViewById(R.id.textViewParticipantEvent);
            attendanceButton = itemView.findViewById(R.id.buttonMarkAttendance);
        }
    }
}