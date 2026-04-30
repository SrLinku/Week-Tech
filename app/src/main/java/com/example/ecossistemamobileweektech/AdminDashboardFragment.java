package com.example.ecossistemamobileweektech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.Navigation;
import com.example.ecossistemamobileweektech.database.AppDatabase;
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

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewParticipants);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        TextView textTotalParticipants = view.findViewById(R.id.textTotalParticipants);
        TextView textTotalCoffee = view.findViewById(R.id.textTotalCoffee);

        // OPERAÇÃO DE BANCO DE DADOS: Obtém a instância do banco Room
        AppDatabase db = AppDatabase.getInstance(requireContext());
        
        // OPERAÇÃO DE BANCO DE DADOS: Busca todos os participantes salvos
        List<Participant> participantList = db.participanteDao().getAll();
        
        // OPERAÇÃO DE BANCO DE DADOS: Busca a contagem específica de Coffee Break via SQL Query no DAO
        int totalInscritos = participantList.size();
        int totalCoffee = db.participanteDao().getCoffeeCount();

        // Atualiza a interface gráfica com os dados vindos do banco
        if (textTotalParticipants != null) textTotalParticipants.setText(String.valueOf(totalInscritos));
        if (textTotalCoffee != null) textTotalCoffee.setText(String.valueOf(totalCoffee));

        ParticipantAdapter adapter = new ParticipantAdapter(participantList);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.btnAddAdmin).setOnClickListener(v -> 
            Navigation.findNavController(view).navigate(R.id.action_dashboard_to_add_admin)
        );

        return view;
    }
}