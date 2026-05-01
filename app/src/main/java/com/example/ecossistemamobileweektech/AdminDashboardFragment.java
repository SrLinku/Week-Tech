package com.example.ecossistemamobileweektech;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento do Painel Administrativo.
 * Exibe as estatísticas e a lista de participantes recuperadas do banco de dados Room.
 */
public class AdminDashboardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        View btnLogout = view.findViewById(R.id.btnAdminLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.nav_user_selection));
        }

        RecyclerView recyclerViewActivities = view.findViewById(R.id.recyclerViewActivities);
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getContext()));

        // OPERAÇÃO DE BANCO DE DADOS: Obtém a instância do banco Room
        AppDatabase db = AppDatabase.getInstance(requireContext());
        
        // Configura lista de atividades (padrão + projetos)
        List<ActivityItem> activityList = new ArrayList<>();
        String[] defaultEvents = getResources().getStringArray(R.array.events_array);
        for (String event : defaultEvents) {
            activityList.add(new ActivityItem(event, false, true));
        }
        
        List<Projeto> projects = db.projetoDao().getAll();
        for (Projeto p : projects) {
            activityList.add(new ActivityItem("Projeto: " + p.getNomeProjeto(), true, p.isApproved()));
        }

        ActivitySummaryAdapter activityAdapter = new ActivitySummaryAdapter(activityList);
        recyclerViewActivities.setAdapter(activityAdapter);

        view.findViewById(R.id.btnAddAdmin).setOnClickListener(v -> 
            Navigation.findNavController(view).navigate(R.id.action_dashboard_to_add_admin)
        );

        return view;
    }
}