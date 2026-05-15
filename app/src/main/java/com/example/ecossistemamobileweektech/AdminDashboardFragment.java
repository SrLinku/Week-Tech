package com.example.ecossistemamobileweektech;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.Navigation;
import com.example.ecossistemamobileweektech.database.AppDatabase;
import com.example.ecossistemamobileweektech.entity.Projeto;
import com.google.android.material.chip.ChipGroup;
import java.util.List;

/**
 * Painel Administrativo principal.
 * Gerencia a filtragem de projetos por status e controla o acesso a funções restritas.
 */
public class AdminDashboardFragment extends Fragment {

    private AppDatabase db;
    private RecyclerView recyclerViewActivities;
    private ChipGroup chipGroupFilter;
    private android.widget.TextView textViewAdminTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        db = AppDatabase.getInstance(requireContext());
        recyclerViewActivities = view.findViewById(R.id.recyclerViewActivities);
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getContext()));
        
        chipGroupFilter = view.findViewById(R.id.chipGroupFilter);
        textViewAdminTitle = view.findViewById(R.id.textViewAdminTitle);

        setupFilter();
        setupAdminAccess(view);

        // Iniciar com projetos pendentes
        loadFilteredProjects(R.id.chipPending);

        return view;
    }

    private void setupFilter() {
        chipGroupFilter.setOnCheckedChangeListener((group, checkedId) -> loadFilteredProjects(checkedId));
    }

    /**
     * Atualiza o título com o nome do administrador.
     */
    private void setupAdminAccess(View view) {
        SharedPreferences prefs = requireActivity().getSharedPreferences("WeekTechPrefs", Context.MODE_PRIVATE);
        String fullName = prefs.getString("admin_name", "Admin");

        // Extrair primeiro nome
        String firstName = fullName.split(" ")[0];
        if (textViewAdminTitle != null) {
            textViewAdminTitle.setText(getString(R.string.title_admin_dashboard, firstName));
        }
    }

    /**
     * Carrega a lista de projetos do banco de dados filtrada pelo status selecionado.
     */
    private void loadFilteredProjects(int checkedId) {
        int status = 0; // Pendente por padrão
        if (checkedId == R.id.chipApproved) {
            status = 1;
        } else if (checkedId == R.id.chipRefused) {
            status = 2;
        } else if (checkedId == R.id.chipCancellation) {
            status = 3;
        }

        List<Projeto> projects = db.projetoDao().getByStatus(status);
        
        // No dashboard administrativo, usamos o ProjectAdapter em modo de gerenciamento para exibir e interagir com os projetos
        ProjectAdapter projectAdapter = new ProjectAdapter(projects, requireContext(), true);
        recyclerViewActivities.setAdapter(projectAdapter);
    }
}
