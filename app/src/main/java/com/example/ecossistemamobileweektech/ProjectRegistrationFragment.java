package com.example.ecossistemamobileweektech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.ecossistemamobileweektech.database.AppDatabase;
import com.example.ecossistemamobileweektech.entity.Projeto;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Fragmento para o cadastro de projetos.
 * Permite que o participante envie informações sobre um projeto para o banco de dados.
 */
public class ProjectRegistrationFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_registration, container, false);

        TextInputEditText name = view.findViewById(R.id.editProjectOwnerName);
        TextInputEditText ra = view.findViewById(R.id.editProjectOwnerRA);
        TextInputEditText title = view.findViewById(R.id.editProjectTitle);
        TextInputEditText desc = view.findViewById(R.id.editProjectDescription);
        TextInputEditText date = view.findViewById(R.id.editProjectDate);
        TextInputEditText time = view.findViewById(R.id.editProjectTime);
        Button btn = view.findViewById(R.id.btnRegisterProject);

        btn.setOnClickListener(v -> {
            String studentName = name.getText().toString();
            String studentRa = ra.getText().toString();
            String projectTitle = title.getText().toString();
            String projectDesc = desc.getText().toString();
            String projectDate = date.getText().toString();
            String projectTime = time.getText().toString();

            if (studentName.isEmpty() || projectTitle.isEmpty() || studentRa.isEmpty()) {
                Toast.makeText(getContext(), "Nome, RA e Título são obrigatórios", Toast.LENGTH_SHORT).show();
            } else {
                // OPERAÇÃO DE BANCO DE DADOS: Criação do objeto Projeto
                Projeto novoProjeto = new Projeto(studentName, studentRa, projectTitle, projectDesc, projectDate, projectTime);
                
                // OPERAÇÃO DE BANCO DE DADOS: Inserção do projeto no banco de dados local via Room
                AppDatabase.getInstance(requireContext()).projetoDao().insert(novoProjeto);

                Toast.makeText(getContext(), "Projeto cadastrado com sucesso no banco!", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }
}