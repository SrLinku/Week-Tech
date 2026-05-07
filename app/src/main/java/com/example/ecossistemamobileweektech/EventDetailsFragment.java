package com.example.ecossistemamobileweektech;

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
import com.example.ecossistemamobileweektech.entity.Projeto;
import java.util.List;

public class EventDetailsFragment extends Fragment {

    private String activityName;
    private boolean showingCoffeeList = false;
    private Projeto currentProject;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        if (getArguments() != null) {
            activityName = getArguments().getString("activityName");
        }

        TextView textTitle = view.findViewById(R.id.textEventTitle);
        TextView textParticipantsCount = view.findViewById(R.id.textEventParticipants);
        TextView textAttendedCount = view.findViewById(R.id.textEventAttended);
        TextView textCoffeeCount = view.findViewById(R.id.textEventCoffee);
        TextView textListLabel = view.findViewById(R.id.textListLabel);
        TextView textApprovalStatus = view.findViewById(R.id.textApprovalStatus);
        Button btnCoffeeList = view.findViewById(R.id.btnShowCoffeeList);
        Button btnApprove = view.findViewById(R.id.btnApproveProject);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewEventParticipants);

        textTitle.setText(activityName);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        AppDatabase db = AppDatabase.getInstance(requireContext());
        
        // Lógica de Aprovação para Projetos
        if (activityName != null && activityName.startsWith("Projeto: ")) {
            String projectName = activityName.replace("Projeto: ", "");
            currentProject = db.projetoDao().getByName(projectName);
            
            if (currentProject != null) {
                textApprovalStatus.setVisibility(View.VISIBLE);
                updateApprovalUI(textApprovalStatus, btnApprove);
                
                btnApprove.setOnClickListener(v -> {
                    currentProject.setApproved(true);
                    db.projetoDao().update(currentProject);
                    updateApprovalUI(textApprovalStatus, btnApprove);
                    Toast.makeText(getContext(), "Projeto Aprovado!", Toast.LENGTH_SHORT).show();
                });
            }
        }

        int totalInscritos = db.participanteDao().getCountByActivity(activityName);
        int totalAttended = db.participanteDao().getAttendedCountByActivity(activityName);
        int totalCoffee = db.participanteDao().getCoffeeCountByActivity(activityName);

        textParticipantsCount.setText(String.valueOf(totalInscritos));
        textAttendedCount.setText(String.valueOf(totalAttended));
        textCoffeeCount.setText(String.valueOf(totalCoffee));

        updateList(db, recyclerView, textListLabel, btnCoffeeList);

        btnCoffeeList.setOnClickListener(v -> {
            showingCoffeeList = !showingCoffeeList;
            updateList(db, recyclerView, textListLabel, btnCoffeeList);
        });

        return view;
    }

    private void updateApprovalUI(TextView statusText, Button approveBtn) {
        if (currentProject.isApproved()) {
            statusText.setText("Status: Aprovado");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark, null));
            approveBtn.setVisibility(View.GONE);
        } else {
            statusText.setText("Status: Pendente");
            statusText.setTextColor(getResources().getColor(android.R.color.holo_orange_dark, null));
            approveBtn.setVisibility(View.VISIBLE);
        }
    }

    private void updateList(AppDatabase db, RecyclerView recyclerView, TextView label, Button btn) {
        List<Participant> list;
        if (showingCoffeeList) {
            list = db.participanteDao().getCoffeeParticipantsByActivity(activityName);
            label.setText("Participantes Coffee Break");
            btn.setText("Mostrar Todos");
        } else {
            list = db.participanteDao().getByActivity(activityName);
            label.setText("Todos os Participantes");
            btn.setText("Lista Coffee Break");
        }
        recyclerView.setAdapter(new ParticipantAdapter(list));
    }
}