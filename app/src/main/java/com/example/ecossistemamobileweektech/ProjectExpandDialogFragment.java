package com.example.ecossistemamobileweektech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import com.example.ecossistemamobileweektech.database.AppDatabase;
import com.example.ecossistemamobileweektech.entity.Participante;
import com.example.ecossistemamobileweektech.entity.Projeto;

/**
 * DialogFragment responsável por exibir os detalhes de um projeto de forma expandida.
 * Permite que o usuário visualize informações completas e realize a inscrição.
 */
public class ProjectExpandDialogFragment extends DialogFragment {

    private Projeto projeto; // Objeto que contém os dados do projeto a ser exibido

    /**
     * Método estático para criar uma nova instância do diálogo passando o projeto.
     * @param projeto O objeto do projeto selecionado.
     * @return Uma instância de ProjectExpandDialogFragment.
     */
    public static ProjectExpandDialogFragment newInstance(Projeto projeto) {
        ProjectExpandDialogFragment fragment = new ProjectExpandDialogFragment();
        fragment.projeto = projeto;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Define o estilo do diálogo para ocupar a tela inteira se necessário
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla o layout do fragmento expandido
        View view = inflater.inflate(R.layout.fragment_project_details_expand, container, false);

        // Inicialização dos componentes da interface
        ImageButton btnClose = view.findViewById(R.id.btnCloseExpand);
        TextView title = view.findViewById(R.id.expandProjectTitle);
        TextView author = view.findViewById(R.id.expandProjectAuthor);
        TextView date = view.findViewById(R.id.expandProjectDate);
        TextView time = view.findViewById(R.id.expandProjectTime);
        TextView desc = view.findViewById(R.id.expandProjectDescription);
        TextView statusBadge = view.findViewById(R.id.textExpandStatus);
        View layoutFeedback = view.findViewById(R.id.layoutFeedbackStudent);
        TextView feedbackText = view.findViewById(R.id.textExpandFeedback);
        TextView feedbackLabel = view.findViewById(R.id.textFeedbackLabel);
        Button btnParticipate = view.findViewById(R.id.btnParticipateProject);

        // Componentes de Administração
        View layoutAdminStats = view.findViewById(R.id.layoutAdminStats);
        TextView textInscribedCount = view.findViewById(R.id.textInscribedCount);
        TextView textCoffeeCount = view.findViewById(R.id.textCoffeeCount);
        TextView textAttendedCount = view.findViewById(R.id.textAttendedCount);
        View layoutAdminActions = view.findViewById(R.id.layoutAdminActions);
        com.google.android.material.textfield.TextInputLayout inputLayoutFeedback = view.findViewById(R.id.inputLayoutFeedback);
        com.google.android.material.textfield.TextInputEditText editFeedback = view.findViewById(R.id.editAdminFeedback);
        Button btnApprove = view.findViewById(R.id.btnAdminApprove);
        Button btnReject = view.findViewById(R.id.btnAdminReject);

        // Recupera preferências para verificar o tipo de usuário logado
        SharedPreferences prefs = requireActivity().getSharedPreferences("WeekTechPrefs", Context.MODE_PRIVATE);
        boolean isAdmin = prefs.contains("admin_email");
        boolean isStudent = prefs.contains("student_ra");
        String studentRa = prefs.getString("student_ra", "");

        // Preenchimento dos dados caso o projeto não seja nulo
        if (projeto != null) {
            title.setText(projeto.getNomeProjeto());
            author.setText("Por: " + projeto.getNomeAluno());
            date.setText("📅 " + projeto.getData());
            time.setText("⏰ " + projeto.getHorario());
            
            String finalDesc = projeto.getDescricao();
            if (projeto.isHasCoffeeBreak()) {
                finalDesc += "\n\n☕ [Incluso: Coffee Break/Networking]";
            }
            desc.setText(finalDesc);

            // --- LÓGICA DE INTERFACE POR TIPO DE USUÁRIO ---

            if (isAdmin) {
                // MODO ADMINISTRADOR:
                // 1. Esconde botão de inscrição (admin não se inscreve)
                btnParticipate.setVisibility(View.GONE);
                
                // 2. Mostra estatísticas e painel de Aprovação/Recusa
                layoutAdminStats.setVisibility(View.VISIBLE);
                layoutAdminActions.setVisibility(View.VISIBLE);
                statusBadge.setVisibility(View.GONE);

                // Carrega estatísticas de inscritos
                loadAdminStatistics(textInscribedCount, textCoffeeCount, textAttendedCount);

                // Adiciona cliques nos contadores para ver as listas
                View coffeeContainer = (View) textCoffeeCount.getParent();
                coffeeContainer.setOnClickListener(v -> showParticipantsList("COFFEE"));

                View attendedContainer = (View) textAttendedCount.getParent();
                attendedContainer.setOnClickListener(v -> showParticipantsList("ATTENDED"));

                // Configura ações de gestão baseadas no status
                if (projeto.getStatus() == 3) {
                    // FLUXO DE CANCELAMENTO
                    inputLayoutFeedback.setVisibility(View.GONE);
                    layoutFeedback.setVisibility(View.VISIBLE);
                    feedbackLabel.setText("Motivo da Solicitação de Cancelamento:");
                    feedbackText.setText(projeto.getFeedback());

                    btnApprove.setText("CONFIRMAR EXCLUSÃO");
                    btnApprove.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark, null)));
                    btnApprove.setOnClickListener(v -> deleteProjectPermanently());

                    btnReject.setText("NEGAR CANCELAMENTO");
                    btnReject.setOnClickListener(v -> updateProjectStatus(1, "")); // Volta para aprovado
                } else {
                    // FLUXO DE APROVAÇÃO NORMAL
                    btnApprove.setOnClickListener(v -> updateProjectStatus(1, ""));
                    btnReject.setOnClickListener(v -> {
                        String feedback = editFeedback.getText().toString().trim();
                        if (feedback.isEmpty()) {
                            editFeedback.setError("Informe o motivo da recusa");
                            return;
                        }
                        updateProjectStatus(2, feedback);
                    });
                }

            } else if (isStudent) {
                // MODO ESTUDANTE:
                // 1. Esconde painel de admin (segurança)
                layoutAdminActions.setVisibility(View.GONE);
                
                int status = projeto.getStatus();
                if (status == 1) { // PROJETO APROVADO
                    statusBadge.setVisibility(View.GONE);
                    layoutFeedback.setVisibility(View.GONE);
                    
                    // Verifica se o aluno já está inscrito para desabilitar o botão
                    checkIfAlreadyInscribed(studentRa, btnParticipate);
                } else { // PENDENTE OU RECUSADO
                    btnParticipate.setVisibility(View.GONE);
                    statusBadge.setVisibility(View.VISIBLE);
                    
                    if (status == 2) { // Exibe feedback se foi recusado
                        statusBadge.setText("STATUS: RECUSADO");
                        statusBadge.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark, null)));
                        if (projeto.getFeedback() != null && !projeto.getFeedback().isEmpty()) {
                            layoutFeedback.setVisibility(View.VISIBLE);
                            feedbackText.setText(projeto.getFeedback());
                        }
                    } else {
                        statusBadge.setText("STATUS: PENDENTE");
                        statusBadge.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(android.R.color.holo_orange_dark, null)));
                    }
                }
            } else {
                // USUÁRIO NÃO LOGADO: Apenas visualização, sem ações
                btnParticipate.setVisibility(View.GONE);
                layoutAdminActions.setVisibility(View.GONE);
                statusBadge.setVisibility(View.GONE);
            }
        }

        // Configura o botão de fechar o diálogo
        btnClose.setOnClickListener(v -> dismiss());

        // Configura o botão para abrir o formulário de inscrição
        btnParticipate.setOnClickListener(v -> {
            showRegistrationDialog();
        });

        return view;
    }

    /**
     * Carrega as estatísticas de inscritos para administradores.
     */
    private void loadAdminStatistics(TextView inscribedText, TextView coffeeText, TextView attendedText) {
        if (projeto == null) return;

        String activityName = "PALESTRA".equals(projeto.getTipoEvento())
                ? projeto.getNomeProjeto()
                : "Projeto: " + projeto.getNomeProjeto();

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            java.util.List<Participante> participants = db.participanteDao().getByActivity(activityName);
            
            int total = participants.size();
            int coffee = 0;
            int attended = 0;
            for (Participante p : participants) {
                if (p.isCoffee()) coffee++;
                if (p.isAttended()) attended++;
            }

            int finalTotal = total;
            int finalCoffee = coffee;
            int finalAttended = attended;
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    inscribedText.setText(String.valueOf(finalTotal));
                    coffeeText.setText(String.valueOf(finalCoffee));
                    attendedText.setText(String.valueOf(finalAttended));
                });
            }
        }).start();
    }

    /**
     * Exibe um diálogo com a lista de nomes e RAs dos participantes.
     * @param filter "COFFEE" ou "ATTENDED"
     */
    private void showParticipantsList(String filter) {
        if (projeto == null) return;

        String activityName = "PALESTRA".equals(projeto.getTipoEvento())
                ? projeto.getNomeProjeto()
                : "Projeto: " + projeto.getNomeProjeto();

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            java.util.List<Participante> participants;
            String title;

            if ("COFFEE".equals(filter)) {
                participants = db.participanteDao().getCoffeeParticipantsByActivity(activityName);
                title = "Confirmados no Coffee Break";
            } else {
                participants = db.participanteDao().getAttendedParticipantsByActivity(activityName);
                title = "Presenças Confirmadas";
            }

            StringBuilder listBuilder = new StringBuilder();
            if (participants.isEmpty()) {
                listBuilder.append("Nenhum participante encontrado.");
            } else {
                for (Participante p : participants) {
                    listBuilder.append("• ").append(p.getName()).append(" (RA: ").append(p.getRa()).append(")\n");
                }
            }

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    new AlertDialog.Builder(requireContext())
                            .setTitle(title)
                            .setMessage(listBuilder.toString())
                            .setPositiveButton("Fechar", null)
                            .show();
                });
            }
        }).start();
    }

    /**
     * Verifica se o aluno já está inscrito na atividade.
     */
    private void checkIfAlreadyInscribed(String ra, Button btn) {
        if (projeto == null) return;
        
        // Define o nome da atividade conforme salvo no banco de participantes
        String activityName;
        if ("PALESTRA".equals(projeto.getTipoEvento())) {
            activityName = projeto.getNomeProjeto();
        } else {
            activityName = "Projeto: " + projeto.getNomeProjeto();
        }

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            java.util.List<Participante> participants = db.participanteDao().getByActivity(activityName);
            boolean inscribed = false;
            for (Participante p : participants) {
                if (p.getRa().equals(ra)) {
                    inscribed = true;
                    break;
                }
            }
            boolean finalInscribed = inscribed;
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    if (finalInscribed) {
                        btn.setEnabled(false);
                        btn.setText("JÁ INSCRITO");
                        btn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(getResources().getColor(android.R.color.darker_gray, null)));
                    } else {
                        btn.setVisibility(View.VISIBLE);
                        btn.setEnabled(true);
                        btn.setText("FAZER INSCRIÇÃO");
                    }
                });
            }
        }).start();
    }

    /**
     * Atualiza o status do projeto no banco de dados.
     * @param newStatus 1 para aprovado, 2 para recusado.
     * @param feedback Motivo em caso de recusa.
     */
    private void updateProjectStatus(int newStatus, String feedback) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            projeto.setStatus(newStatus);
            projeto.setFeedback(feedback);
            db.projetoDao().update(projeto);

            requireActivity().runOnUiThread(() -> {
                String msg = newStatus == 1 ? "Projeto aprovado!" : "Projeto recusado.";
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                dismiss();
                // Opcional: Recarregar a lista no fragmento pai se necessário
            });
        }).start();
    }

    /**
     * Remove o projeto definitivamente do banco de dados (Ação do Admin após solicitação de cancelamento).
     */
    private void deleteProjectPermanently() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            db.projetoDao().delete(projeto);

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(getContext(), "Projeto removido definitivamente.", Toast.LENGTH_SHORT).show();
                dismiss();
            });
        }).start();
    }

    /**
     * Exibe um AlertDialog para confirmar a inscrição do aluno no projeto.
     */
    private void showRegistrationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        // Infla o layout customizado do formulário de inscrição
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_registration_form, null);
        
        TextView textInfo = dialogView.findViewById(R.id.textDialogStudentInfo);
        CheckBox checkCoffee = dialogView.findViewById(R.id.checkDialogCoffee);

        // Recupera os dados do aluno salvos no SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("WeekTechPrefs", Context.MODE_PRIVATE);
        String savedName = prefs.getString("student_name", "");
        String savedRA = prefs.getString("student_ra", "");

        // Validação de segurança: o aluno deve estar logado
        if (savedName.isEmpty() || savedRA.isEmpty()) {
            Toast.makeText(getContext(), "Por favor, faça login primeiro", Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }

        // Exibe o nome e RA do aluno no diálogo
        textInfo.setText("Aluno: " + savedName + " (RA: " + savedRA + ")");

        // Verifica se o projeto oferece Coffee Break para mostrar o CheckBox
        if (projeto != null && projeto.isHasCoffeeBreak()) {
            checkCoffee.setVisibility(View.VISIBLE);
        }

        builder.setView(dialogView)
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    // Executa a lógica de inserção no banco de dados Room
                    AppDatabase db = AppDatabase.getInstance(requireContext());
                    
                    String activityName = "PALESTRA".equals(projeto.getTipoEvento()) 
                            ? projeto.getNomeProjeto() 
                            : "Projeto: " + projeto.getNomeProjeto();

                    // Cria uma nova instância da entidade Participante (Padronizada)
                    Participante newParticipant = new Participante(
                            savedName,
                            savedRA,
                            prefs.getString("student_course", "N/A"),
                            prefs.getString("student_semester", "N/A"),
                            activityName,
                            checkCoffee.isChecked()
                    );

                    // Insere no banco de dados através do DAO
                    db.participanteDao().insert(newParticipant);

                    Toast.makeText(getContext(), "Inscrição confirmada!", Toast.LENGTH_SHORT).show();
                    dismiss();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
