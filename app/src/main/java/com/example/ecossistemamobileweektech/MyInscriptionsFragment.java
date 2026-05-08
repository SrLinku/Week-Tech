package com.example.ecossistemamobileweektech;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.ecossistemamobileweektech.database.AppDatabase;
import com.example.ecossistemamobileweektech.entity.Participante;
import java.util.List;

public class MyInscriptionsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textNoInscriptions;
    private AppDatabase db;
    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_inscriptions, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewInscriptions);
        textNoInscriptions = view.findViewById(R.id.textNoInscriptions);

        db = AppDatabase.getInstance(requireContext());
        prefs = requireActivity().getSharedPreferences("WeekTechPrefs", Context.MODE_PRIVATE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Carregar RA automaticamente do login
        String savedRA = prefs.getString("student_ra", "");
        if (!savedRA.isEmpty()) {
            loadInscriptions(savedRA);
        } else {
            textNoInscriptions.setText("Faça login para ver suas inscrições.");
            textNoInscriptions.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void loadInscriptions(String ra) {
        List<Participante> inscriptions = db.participanteDao().getByRa(ra);
        if (inscriptions.isEmpty()) {
            textNoInscriptions.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textNoInscriptions.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            // Usar adaptador em modo não-admin para esconder o botão de confirmação
            recyclerView.setAdapter(new ParticipantAdapter(inscriptions, false));
        }
    }
}