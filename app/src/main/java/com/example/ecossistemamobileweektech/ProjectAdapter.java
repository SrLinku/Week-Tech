package com.example.ecossistemamobileweektech;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecossistemamobileweektech.database.AppDatabase;
import com.example.ecossistemamobileweektech.entity.Participante;
import com.example.ecossistemamobileweektech.entity.Projeto;
import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {

    private List<Projeto> projectList;
    private String studentRA;
    private boolean isManagementMode; // Para saber se estamos na tela de "Meus Projetos"

    public ProjectAdapter(List<Projeto> projectList, Context context) {
        this.projectList = projectList;
        SharedPreferences prefs = context.getSharedPreferences("WeekTechPrefs", Context.MODE_PRIVATE);
        this.studentRA = prefs.getString("student_ra", "");
        // Se a lista contém apenas projetos do RA logado, assume-se modo de gerenciamento
        this.isManagementMode = false; 
    }

    // Sobrecarga para definir explicitamente se o botão de cancelar deve aparecer
    public ProjectAdapter(List<Projeto> projectList, Context context, boolean isManagementMode) {
        this(projectList, context);
        this.isManagementMode = isManagementMode;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Projeto projeto = projectList.get(position);
        holder.textViewTitle.setText(projeto.getNomeProjeto());
        
        // Mostrar autor
        holder.textViewSpeaker.setText(projeto.getNomeAluno());
        
        String dateTime = projeto.getData() + " - " + projeto.getHorario();
        holder.textViewTime.setText(dateTime);
        
        String tipo = projeto.getTipoEvento();
        holder.textViewType.setText(tipo);
        
        // Destacar o tipo de evento com cores diferentes para Projeto e Palestra
        if ("PALESTRA".equals(tipo)) {
            holder.viewTypeIndicator.setBackgroundColor(holder.itemView.getContext().getColor(R.color.color_lecture_tag));
            holder.textViewType.setTextColor(holder.itemView.getContext().getColor(R.color.color_lecture_tag));
            holder.textViewType.setBackgroundTintList(android.content.res.ColorStateList.valueOf(holder.itemView.getContext().getColor(R.color.color_lecture_bg)));
        } else {
            holder.viewTypeIndicator.setBackgroundColor(holder.itemView.getContext().getColor(R.color.color_project_tag));
            holder.textViewType.setTextColor(holder.itemView.getContext().getColor(R.color.color_project_tag));
            holder.textViewType.setBackgroundTintList(android.content.res.ColorStateList.valueOf(holder.itemView.getContext().getColor(R.color.color_project_bg)));
        }

        // Exibir status do projeto se estiver no modo de gerenciamento
        if (isManagementMode) {
            holder.textStatusBadge.setVisibility(View.VISIBLE);
            int status = projeto.getStatus();
            if (status == 1) {
                holder.textStatusBadge.setText("APROVADO");
                holder.textStatusBadge.setBackgroundTintList(android.content.res.ColorStateList.valueOf(holder.itemView.getContext().getColor(android.R.color.holo_green_dark)));
            } else if (status == 2) {
                holder.textStatusBadge.setText("RECUSADO");
                holder.textStatusBadge.setBackgroundTintList(android.content.res.ColorStateList.valueOf(holder.itemView.getContext().getColor(android.R.color.holo_red_dark)));
            } else {
                holder.textStatusBadge.setText("PENDENTE");
                holder.textStatusBadge.setBackgroundTintList(android.content.res.ColorStateList.valueOf(holder.itemView.getContext().getColor(android.R.color.holo_orange_dark)));
            }
        } else {
            holder.textStatusBadge.setVisibility(View.GONE);
        }

        // Lógica de Inscrição (Badge)
        checkInscriptionStatus(holder, projeto);

        // Lógica de Cancelamento de Projeto (Apenas se for o autor e estiver na tela de gerenciamento)
        if (isManagementMode && projeto.getRa().equals(studentRA)) {
            holder.btnCancelProject.setVisibility(View.VISIBLE);
            holder.btnCancelProject.setOnClickListener(v -> {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Cancelar Projeto")
                        .setMessage("Tem certeza que deseja excluir o projeto: " + projeto.getNomeProjeto() + "?\nEsta ação não pode ser desfeita.")
                        .setPositiveButton("Sim, Excluir", (dialog, which) -> {
                            AppDatabase.getInstance(v.getContext()).projetoDao().delete(projeto);
                            projectList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, projectList.size());
                            Toast.makeText(v.getContext(), "Projeto excluído com sucesso", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Não", null)
                        .show();
            });
        } else {
            holder.btnCancelProject.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (v.getContext() instanceof AppCompatActivity) {
                ProjectExpandDialogFragment dialog = ProjectExpandDialogFragment.newInstance(projeto);
                dialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "project_expand");
            }
        });
    }

    private void checkInscriptionStatus(ProjectViewHolder holder, Projeto projeto) {
        if (!studentRA.isEmpty()) {
            AppDatabase db = AppDatabase.getInstance(holder.itemView.getContext());
            String activityName = "PALESTRA".equals(projeto.getTipoEvento()) 
                    ? projeto.getNomeProjeto() 
                    : "Projeto: " + projeto.getNomeProjeto();
            
            // Verificação simplificada (em um app real, idealmente seria um query direto no DAO)
            new Thread(() -> {
                List<Participante> participants = db.participanteDao().getByActivity(activityName);
                boolean isInscribed = false;
                for (Participante p : participants) {
                    if (p.getRa().equals(studentRA)) {
                        isInscribed = true;
                        break;
                    }
                }
                boolean finalIsInscribed = isInscribed;
                holder.itemView.post(() -> {
                    holder.badgeInscribed.setVisibility(finalIsInscribed ? View.VISIBLE : View.GONE);
                });
            }).start();
        } else {
            holder.badgeInscribed.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewSpeaker, textViewTime, textViewType;
        TextView badgeInscribed, textStatusBadge;
        Button btnCancelProject;
        View viewTypeIndicator;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewSpeaker = itemView.findViewById(R.id.textViewSpeaker);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewType = itemView.findViewById(R.id.textViewType);
            badgeInscribed = itemView.findViewById(R.id.badgeInscribed);
            textStatusBadge = itemView.findViewById(R.id.textStatusBadge);
            btnCancelProject = itemView.findViewById(R.id.btnCancelProject);
            viewTypeIndicator = itemView.findViewById(R.id.viewTypeIndicator);
        }
    }
}