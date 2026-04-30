package com.example.ecossistemamobileweektech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecossistemamobileweektech.entity.Projeto;
import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private List<Projeto> projectList;

    public ProjectAdapter(List<Projeto> projectList) {
        this.projectList = projectList;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Projeto projeto = projectList.get(position);
        holder.textViewTitle.setText(projeto.getNomeProjeto());
        holder.textViewSpeaker.setText(projeto.getNomeAluno());
        
        String dateTime = projeto.getData() + " - " + projeto.getHorario();
        holder.textViewTime.setText(dateTime);
        
        holder.textViewType.setText("PROJETO");
        holder.textViewType.setTextColor(holder.itemView.getContext().getColor(R.color.text_secondary));
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewSpeaker, textViewTime, textViewType;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewSpeaker = itemView.findViewById(R.id.textViewSpeaker);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewType = itemView.findViewById(R.id.textViewType);
        }
    }
}