package com.example.ecossistemamobileweektech;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecossistemamobileweektech.database.AppDatabase;
import com.example.ecossistemamobileweektech.entity.Participante;
import com.example.ecossistemamobileweektech.entity.Projeto;
import java.util.List;

/**
 * Adaptador para exibir a lista de participantes inscritos em uma atividade ou as inscrições de um aluno.
 */
public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder> {

    private List<Participante> participantList;
    private boolean isAdminMode;

    public ParticipantAdapter(List<Participante> participantList, boolean isAdminMode) {
        this.participantList = participantList;
        this.isAdminMode = isAdminMode;
    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Se estivermos vendo as inscrições do aluno (não modo admin), usamos o novo layout com botões
        if (!isAdminMode) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_participant_inscription, parent, false);
            return new ParticipantViewHolder(view);
        }
        // No modo admin (lista de inscritos em um projeto), mantemos o layout simples
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ParticipantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {
        Participante p = participantList.get(position);
        
        if (isAdminMode) {
            holder.text1.setText(p.getName() + " (" + p.getRa() + ")");
            holder.text2.setText(p.getActivity() + (p.isCoffee() ? " - [Coffee Break]" : ""));
            if (p.isAttended()) {
                holder.text1.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_green_dark));
            }
        } else {
            // Modo "Minhas Inscrições"
            if (holder.textActivity != null) holder.textActivity.setText(p.getActivity());
            if (holder.textDetails != null) {
                String details = p.isCoffee() ? "✓ Com Coffee Break" : "Sem Coffee Break";
                holder.textDetails.setText(details);
            }

            if (holder.btnDetails != null) {
                holder.btnDetails.setOnClickListener(v -> showActivityDetails(holder, p));
            }

            if (holder.btnConfirmPresence != null) {
                // Se já confirmou presença, desabilita o botão
                if (p.isAttended()) {
                    holder.btnConfirmPresence.setText("PRESENÇA CONFIRMADA");
                    holder.btnConfirmPresence.setEnabled(false);
                    holder.btnConfirmPresence.setTextColor(holder.itemView.getContext().getColor(android.R.color.darker_gray));
                } else {
                    holder.btnConfirmPresence.setOnClickListener(v -> confirmPresence(holder, p));
                }
            }

            if (holder.btnCancel != null) {
                if (p.isAttended()) {
                    // Se a presença foi confirmada, não permite cancelar
                    holder.btnCancel.setVisibility(View.GONE);
                } else {
                    holder.btnCancel.setVisibility(View.VISIBLE);
                    holder.btnCancel.setOnClickListener(v -> {
                        int currentPos = holder.getAdapterPosition();
                        if (currentPos != RecyclerView.NO_POSITION) {
                            confirmCancellation(holder, participantList.get(currentPos), currentPos);
                        }
                    });
                }
            }
        }
    }

    private void showActivityDetails(ParticipantViewHolder holder, Participante p) {
        String activityName = p.getActivity();
        AppDatabase db = AppDatabase.getInstance(holder.itemView.getContext());
        
        new Thread(() -> {
            Projeto projeto = null;
            if (activityName.startsWith("Projeto: ")) {
                String name = activityName.replace("Projeto: ", "");
                projeto = db.projetoDao().getByName(name);
            } else {
                // É uma palestra fixa
                projeto = new Projeto();
                projeto.setNomeProjeto(activityName);
                projeto.setNomeAluno("Convidado Especial");
                projeto.setDescricao("Palestra oficial da Mobile Week Tech.");
                projeto.setData("22/05");
                projeto.setHorario("19:00");
                projeto.setTipoEvento("PALESTRA");
                projeto.setStatus(1);
                
                // Ativa flag de coffee break se o nome da atividade sugerir
                if (activityName.contains("Coffee Break")) {
                    projeto.setHasCoffeeBreak(true);
                }
            }

            if (projeto != null) {
                final Projeto finalProj = projeto;
                ((FragmentActivity) holder.itemView.getContext()).runOnUiThread(() -> {
                    ProjectExpandDialogFragment dialog = ProjectExpandDialogFragment.newInstance(finalProj);
                    dialog.show(((FragmentActivity) holder.itemView.getContext()).getSupportFragmentManager(), "ProjectDetails");
                });
            }
        }).start();
    }

    private void confirmPresence(ParticipantViewHolder holder, Participante p) {
        new AlertDialog.Builder(holder.itemView.getContext())
                .setTitle("Confirmar Presença")
                .setMessage("Deseja confirmar sua presença em: " + p.getActivity() + "?\nEsta ação não pode ser desfeita.")
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    new Thread(() -> {
                        AppDatabase db = AppDatabase.getInstance(holder.itemView.getContext());
                        p.setAttended(true);
                        db.participanteDao().update(p);
                        ((FragmentActivity) holder.itemView.getContext()).runOnUiThread(() -> {
                            notifyItemChanged(holder.getAdapterPosition());
                            Toast.makeText(holder.itemView.getContext(), "Presença confirmada!", Toast.LENGTH_SHORT).show();
                        });
                    }).start();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void confirmCancellation(ParticipantViewHolder holder, Participante p, int position) {
        new AlertDialog.Builder(holder.itemView.getContext())
                .setTitle("Cancelar Inscrição")
                .setMessage("Deseja realmente cancelar sua inscrição em: " + p.getActivity() + "?")
                .setPositiveButton("Sim, Cancelar", (dialog, which) -> {
                    new Thread(() -> {
                        AppDatabase.getInstance(holder.itemView.getContext()).participanteDao().delete(p);
                        ((FragmentActivity) holder.itemView.getContext()).runOnUiThread(() -> {
                            participantList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, participantList.size());
                            Toast.makeText(holder.itemView.getContext(), "Inscrição cancelada.", Toast.LENGTH_SHORT).show();
                        });
                    }).start();
                })
                .setNegativeButton("Não", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return participantList.size();
    }

    static class ParticipantViewHolder extends RecyclerView.ViewHolder {
        // Layout Simples (Admin)
        TextView text1, text2;
        
        // Layout Custom (Minhas Inscrições)
        TextView textActivity, textDetails;
        Button btnDetails, btnCancel, btnConfirmPresence;

        public ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
            
            textActivity = itemView.findViewById(R.id.textInscribedActivity);
            textDetails = itemView.findViewById(R.id.textInscribedDetails);
            btnDetails = itemView.findViewById(R.id.btnViewDetails);
            btnCancel = itemView.findViewById(R.id.btnCancelInscription);
            btnConfirmPresence = itemView.findViewById(R.id.btnConfirmPresence);
        }
    }
}
