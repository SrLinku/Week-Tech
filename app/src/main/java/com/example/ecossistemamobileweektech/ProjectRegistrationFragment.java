package com.example.ecossistemamobileweektech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecossistemamobileweektech.database.AppDatabase;
import com.example.ecossistemamobileweektech.entity.Projeto;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

/**
 * Fragmento para o cadastro e visualização de projetos criados pelo aluno.
 */
public class ProjectRegistrationFragment extends Fragment {
    
    private AppDatabase db;
    private RecyclerView recyclerView;
    private ProjectAdapter adapter;
    private LinearLayout layoutForm;
    private Button btnShowForm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_registration, container, false);

        db = AppDatabase.getInstance(requireContext());
        
        layoutForm = view.findViewById(R.id.layoutForm);
        btnShowForm = view.findViewById(R.id.btnShowForm);
        
        TextInputEditText name = view.findViewById(R.id.editProjectOwnerName);
        TextInputEditText ra = view.findViewById(R.id.editProjectOwnerRA);
        TextInputEditText title = view.findViewById(R.id.editProjectTitle);
        TextInputEditText desc = view.findViewById(R.id.editProjectDescription);
        TextInputEditText date = view.findViewById(R.id.editProjectDate);
        TextInputEditText time = view.findViewById(R.id.editProjectTime);
        CheckBox checkCoffee = view.findViewById(R.id.checkProjectCoffee);
        Button btnRegister = view.findViewById(R.id.btnRegisterProject);

        recyclerView = view.findViewById(R.id.recyclerViewMyProjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        loadProjects();

        btnShowForm.setOnClickListener(v -> {
            layoutForm.setVisibility(View.VISIBLE);
            btnShowForm.setVisibility(View.GONE);
        });

        btnRegister.setOnClickListener(v -> {
            String studentName = name.getText().toString();
            String studentRa = ra.getText().toString();
            String projectTitle = title.getText().toString();
            String projectDesc = desc.getText().toString();
            String projectDate = date.getText().toString();
            String projectTime = time.getText().toString();
            boolean hasCoffee = checkCoffee.isChecked();

            if (studentName.isEmpty() || projectTitle.isEmpty() || studentRa.isEmpty()) {
                Toast.makeText(getContext(), "Nome, RA e Título são obrigatórios", Toast.LENGTH_SHORT).show();
            } else {
                Projeto novoProjeto = new Projeto(studentName, studentRa, projectTitle, projectDesc, projectDate, projectTime);
                novoProjeto.setHasCoffeeBreak(hasCoffee);
                
                db.projetoDao().insert(novoProjeto);

                Toast.makeText(getContext(), "Projeto cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                
                // Limpar campos e fechar formulário
                name.setText("");
                ra.setText("");
                title.setText("");
                desc.setText("");
                date.setText("");
                time.setText("");
                checkCoffee.setChecked(false);
                layoutForm.setVisibility(View.GONE);
                btnShowForm.setVisibility(View.VISIBLE);
                
                // Recarregar lista
                loadProjects();
            }
        });

        return view;
    }

    private void loadProjects() {
        List<Projeto> projects = db.projetoDao().getAll();
        adapter = new ProjectAdapter(projects, requireContext());
        recyclerView.setAdapter(adapter);
    }
}