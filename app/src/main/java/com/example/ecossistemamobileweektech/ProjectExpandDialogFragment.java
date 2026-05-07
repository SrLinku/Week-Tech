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
import com.example.ecossistemamobileweektech.database.AppDatabase;
import com.example.ecossistemamobileweektech.entity.Projeto;
import com.google.android.material.textfield.TextInputEditText;

public class ProjectExpandDialogFragment extends DialogFragment {

    private Projeto projeto;

    public static ProjectExpandDialogFragment newInstance(Projeto projeto) {
        ProjectExpandDialogFragment fragment = new ProjectExpandDialogFragment();
        fragment.projeto = projeto;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_details_expand, container, false);

        ImageButton btnClose = view.findViewById(R.id.btnCloseExpand);
        TextView title = view.findViewById(R.id.expandProjectTitle);
        TextView author = view.findViewById(R.id.expandProjectAuthor);
        TextView date = view.findViewById(R.id.expandProjectDate);
        TextView time = view.findViewById(R.id.expandProjectTime);
        TextView desc = view.findViewById(R.id.expandProjectDescription);
        
        TextInputEditText editName = view.findViewById(R.id.editParticipateName);
        TextInputEditText editRA = view.findViewById(R.id.editParticipateRA);
        CheckBox checkCoffee = view.findViewById(R.id.checkParticipateCoffee);
        
        Button btnParticipate = view.findViewById(R.id.btnParticipateProject);

        if (projeto != null) {
            title.setText(projeto.getNomeProjeto());
            author.setText("Por: " + projeto.getNomeAluno());
            date.setText("📅 " + projeto.getData());
            time.setText("⏰ " + projeto.getHorario());
            desc.setText(projeto.getDescricao());
            
            // Mostrar checkbox de Coffee Break apenas se habilitado para o projeto
            if (projeto.isHasCoffeeBreak()) {
                checkCoffee.setVisibility(View.VISIBLE);
            }
        }

        btnClose.setOnClickListener(v -> dismiss());

        btnParticipate.setOnClickListener(v -> {
            String name = editName.getText().toString();
            String ra = editRA.getText().toString();

            if (name.isEmpty() || ra.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, preencha Nome e RA", Toast.LENGTH_SHORT).show();
                return;
            }

            // Realizar inscrição direta
            AppDatabase db = AppDatabase.getInstance(requireContext());
            Participant newParticipant = new Participant(
                    name,
                    ra,
                    "N/A", // Curso não é mais obrigatório no fluxo simplificado
                    "N/A", // Série não é mais obrigatória no fluxo simplificado
                    "Projeto: " + projeto.getNomeProjeto(),
                    checkCoffee.isChecked()
            );

            db.participanteDao().insert(newParticipant);

            // SALVAR RA nas preferências para habilitar marcadores na Home
            requireActivity().getSharedPreferences("WeekTechPrefs", android.content.Context.MODE_PRIVATE)
                    .edit().putString("student_ra", ra).apply();

            Toast.makeText(getContext(), "Inscrição confirmada!", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        return view;
    }
}