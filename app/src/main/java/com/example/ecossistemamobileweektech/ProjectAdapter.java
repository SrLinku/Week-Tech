package com.example.ecossistemamobileweektech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecossistemamobileweektech.database.AppDatabase;
import com.example.ecossistemamobileweektech.entity.Projeto;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private List<Projeto> projectList;
    private String studentRA;

    public ProjectAdapter(List<Projeto> projectList, Context context) {
        this.projectList = projectList;
        SharedPreferences prefs = context.getSharedPreferences("WeekTechPrefs", Context.MODE_PRIVATE);
        this.studentRA = prefs.getString("student_ra", "");
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

        // Verificar se o aluno já está inscrito neste projeto
        if (!studentRA.isEmpty()) {
            AppDatabase db = AppDatabase.getInstance(holder.itemView.getContext());
            // Busca por RA e nome da atividade (que contém o nome do projeto)
            String activityName = "Projeto: " + projeto.getNomeProjeto();
            
            // Nota: getByActivity no ParticipanteDao retorna lista, se não estiver vazia, está inscrito
            List<Participant> participants = db.participanteDao().getByActivity(activityName);
            boolean isInscribed = false;
            for (Participant p : participants) {
                if (p.getRa().equals(studentRA)) {
                    isInscribed = true;
                    break;
                }
            }
            
            if (isInscribed) {
                holder.badgeInscribed.setVisibility(View.VISIBLE);
            } else {
                holder.badgeInscribed.setVisibility(View.GONE);
            }
        } else {
            holder.badgeInscribed.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            // Abrir tela expandida
            if (v.getContext() instanceof AppCompatActivity) {
                ProjectExpandDialogFragment dialog = ProjectExpandDialogFragment.newInstance(projeto);
                dialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "project_expand");
            }
        });
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewSpeaker, textViewTime, textViewType;
        TextView badgeInscribed;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewSpeaker = itemView.findViewById(R.id.textViewSpeaker);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewType = itemView.findViewById(R.id.textViewType);
            badgeInscribed = itemView.findViewById(R.id.badgeInscribed);
        }
    }
}