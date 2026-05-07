package com.example.ecossistemamobileweektech;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecossistemamobileweektech.database.AppDatabase;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class MyInscriptionsFragment extends Fragment {

    private TextInputEditText editRA;
    private RecyclerView recyclerView;
    private TextView textNoInscriptions;
    private AppDatabase db;
    private SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_inscriptions, container, false);

        editRA = view.findViewById(R.id.editStudentRA);
        recyclerView = view.findViewById(R.id.recyclerViewInscriptions);
        textNoInscriptions = view.findViewById(R.id.textNoInscriptions);
        Button btnSave = view.findViewById(R.id.btnSaveRA);

        db = AppDatabase.getInstance(requireContext());
        prefs = requireActivity().getSharedPreferences("WeekTechPrefs", Context.MODE_PRIVATE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Carregar RA salvo, se existir
        String savedRA = prefs.getString("student_ra", "");
        if (!savedRA.isEmpty()) {
            editRA.setText(savedRA);
            loadInscriptions(savedRA);
        }

        btnSave.setOnClickListener(v -> {
            String ra = editRA.getText().toString();
            if (ra.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, insira seu RA", Toast.LENGTH_SHORT).show();
                return;
            }

            // Salvar RA nas preferências
            prefs.edit().putString("student_ra", ra).apply();
            Toast.makeText(getContext(), "RA Identificado!", Toast.LENGTH_SHORT).show();
            
            loadInscriptions(ra);
        });

        return view;
    }

    private void loadInscriptions(String ra) {
        List<Participant> inscriptions = db.participanteDao().getByRa(ra);
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