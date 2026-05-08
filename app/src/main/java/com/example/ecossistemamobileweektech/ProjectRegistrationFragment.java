package com.example.ecossistemamobileweektech;

import android.content.Context;
import android.content.SharedPreferences;
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
    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_registration, container, false);

        db = AppDatabase.getInstance(requireContext());
        prefs = requireActivity().getSharedPreferences("WeekTechPrefs", Context.MODE_PRIVATE);
        
        layoutForm = view.findViewById(R.id.layoutForm);
        btnShowForm = view.findViewById(R.id.btnShowForm);
        
        TextInputEditText title = view.findViewById(R.id.editProjectTitle);
        TextInputEditText desc = view.findViewById(R.id.editProjectDescription);
        TextInputEditText date = view.findViewById(R.id.editProjectDate);
        TextInputEditText time = view.findViewById(R.id.editProjectTime);
        CheckBox checkCoffee = view.findViewById(R.id.checkProjectCoffee);
        Button btnRegister = view.findViewById(R.id.btnRegisterProject);

        // Aplicar máscaras de data e hora
        applyDateMask(date);
        applyTimeMask(time);

        recyclerView = view.findViewById(R.id.recyclerViewMyProjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        loadProjects();

        btnShowForm.setOnClickListener(v -> {
            layoutForm.setVisibility(View.VISIBLE);
            btnShowForm.setVisibility(View.GONE);
        });

        btnRegister.setOnClickListener(v -> {
            String studentName = prefs.getString("student_name", "");
            String studentRa = prefs.getString("student_ra", "");
            String projectTitle = title.getText().toString();
            String projectDesc = desc.getText().toString();
            String projectDate = date.getText().toString();
            String projectTime = time.getText().toString();
            boolean hasCoffee = checkCoffee.isChecked();

            if (studentRa.isEmpty()) {
                Toast.makeText(getContext(), "Erro: Aluno não identificado. Faça login.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (projectTitle.isEmpty()) {
                Toast.makeText(getContext(), "O título do projeto é obrigatório", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validação simples de data (DD/MM)
            if (projectDate.length() < 5) {
                Toast.makeText(getContext(), "Insira uma data válida (DD/MM)", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                int dia = Integer.parseInt(projectDate.substring(0, 2));
                int mes = Integer.parseInt(projectDate.substring(3, 5));
                if (dia < 1 || dia > 31 || mes < 1 || mes > 12) {
                    Toast.makeText(getContext(), "Data inválida!", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Formato de data inválido!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validação simples de hora (HH:MM)
            if (projectTime.length() < 5) {
                Toast.makeText(getContext(), "Insira um horário válido (HH:MM)", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                int hora = Integer.parseInt(projectTime.substring(0, 2));
                int min = Integer.parseInt(projectTime.substring(3, 5));
                if (hora > 23 || min > 59) {
                    Toast.makeText(getContext(), "Horário inválido!", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Formato de horário inválido!", Toast.LENGTH_SHORT).show();
                return;
            }

            Projeto novoProjeto = new Projeto(studentName, studentRa, projectTitle, projectDesc, projectDate, projectTime);
            novoProjeto.setHasCoffeeBreak(hasCoffee);
            
            db.projetoDao().insert(novoProjeto);

            Toast.makeText(getContext(), "Projeto enviado para aprovação!", Toast.LENGTH_SHORT).show();
            
            // Limpar campos e fechar formulário
            title.setText("");
            desc.setText("");
            date.setText("");
            time.setText("");
            checkCoffee.setChecked(false);
            layoutForm.setVisibility(View.GONE);
            btnShowForm.setVisibility(View.VISIBLE);
            
            // Recarregar lista
            loadProjects();
        });

        return view;
    }

    private void loadProjects() {
        String ra = prefs.getString("student_ra", "");
        // Aqui buscamos apenas os projetos deste aluno (pelo RA)
        List<Projeto> projects = db.projetoDao().getByRa(ra);
        adapter = new ProjectAdapter(projects, requireContext(), true);
        recyclerView.setAdapter(adapter);
    }

    private void applyDateMask(TextInputEditText editText) {
        editText.addTextChangedListener(new android.text.TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString().replaceAll("[^\\d]", "");
                String mask = "";
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }
                
                if (str.length() > 0) {
                    // Limita a 4 dígitos (DDMM)
                    String sub = str.substring(0, Math.min(str.length(), 4));
                    mask = sub;
                    if (sub.length() > 2) {
                        mask = sub.substring(0, 2) + "/" + sub.substring(2);
                    }
                }

                isUpdating = true;
                editText.setText(mask);
                editText.setSelection(mask.length());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void applyTimeMask(TextInputEditText editText) {
        editText.addTextChangedListener(new android.text.TextWatcher() {
            private boolean isUpdating = false;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString().replaceAll("[^\\d]", "");
                String mask = "";
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }
                if (str.length() > 0) {
                    mask += str.substring(0, Math.min(str.length(), 2));
                    if (str.length() > 2) {
                        mask += ":" + str.substring(2, Math.min(str.length(), 4));
                    }
                }
                isUpdating = true;
                editText.setText(mask);
                editText.setSelection(mask.length());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }
}
