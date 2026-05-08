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

        setupLogout(view);
        setupFilter();
        setupAdminAccess(view);

        // Iniciar com projetos pendentes
        loadFilteredProjects(R.id.chipPending);

        return view;
    }

    private void setupLogout(View view) {
        View btnLogout = view.findViewById(R.id.btnAdminLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                // Limpa totalmente a sessão
                SharedPreferences prefs = requireActivity().getSharedPreferences("WeekTechPrefs", Context.MODE_PRIVATE);
                prefs.edit().clear().apply();
                
                // Retorna para a tela de autenticação inicial
                Navigation.findNavController(view).navigate(R.id.nav_auth_selection);
            });
        }
    }

    private void setupFilter() {
        chipGroupFilter.setOnCheckedChangeListener((group, checkedId) -> loadFilteredProjects(checkedId));
    }

    /**
     * Verifica permissões do administrador logado para exibir ou ocultar a gestão de outros admins.
     */
    private void setupAdminAccess(View view) {
        View btnManageAdmins = view.findViewById(R.id.btnAddAdmin);
        SharedPreferences prefs = requireActivity().getSharedPreferences("WeekTechPrefs", Context.MODE_PRIVATE);
        boolean isSuper = prefs.getBoolean("is_super_admin", false);
        String fullName = prefs.getString("admin_name", "Admin");

        // Extrair primeiro nome
        String firstName = fullName.split(" ")[0];
        if (textViewAdminTitle != null) {
            textViewAdminTitle.setText(getString(R.string.title_admin_dashboard, firstName));
        }
        
        // Apenas o super administrador (Cris) pode gerenciar outros administradores
        btnManageAdmins.setVisibility(isSuper ? View.VISIBLE : View.GONE);
        if (btnManageAdmins instanceof android.widget.Button) {
            ((android.widget.Button) btnManageAdmins).setText("Gerenciar Admins");
        }

        btnManageAdmins.setOnClickListener(v ->
            Navigation.findNavController(view).navigate(R.id.action_dashboard_to_admin_management)
        );
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
        }

        List<Projeto> projects = db.projetoDao().getByStatus(status);
        
        // No dashboard administrativo, usamos o ProjectAdapter em modo de gerenciamento para exibir e interagir com os projetos
        ProjectAdapter projectAdapter = new ProjectAdapter(projects, requireContext(), true);
        recyclerViewActivities.setAdapter(projectAdapter);
    }
}
