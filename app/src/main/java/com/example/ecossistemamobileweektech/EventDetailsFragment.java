package com.example.ecossistemamobileweektech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecossistemamobileweektech.database.AppDatabase;
import java.util.List;

public class EventDetailsFragment extends Fragment {

    private String activityName;
    private boolean showingCoffeeList = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        if (getArguments() != null) {
            activityName = getArguments().getString("activityName");
        }

        TextView textTitle = view.findViewById(R.id.textEventTitle);
        TextView textParticipantsCount = view.findViewById(R.id.textEventParticipants);
        TextView textCoffeeCount = view.findViewById(R.id.textEventCoffee);
        TextView textListLabel = view.findViewById(R.id.textListLabel);
        Button btnCoffeeList = view.findViewById(R.id.btnShowCoffeeList);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewEventParticipants);

        textTitle.setText(activityName);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        AppDatabase db = AppDatabase.getInstance(requireContext());
        
        int totalInscritos = db.participanteDao().getCountByActivity(activityName);
        int totalCoffee = db.participanteDao().getCoffeeCountByActivity(activityName);

        textParticipantsCount.setText(String.valueOf(totalInscritos));
        textCoffeeCount.setText(String.valueOf(totalCoffee));

        updateList(db, recyclerView, textListLabel, btnCoffeeList);

        btnCoffeeList.setOnClickListener(v -> {
            showingCoffeeList = !showingCoffeeList;
            updateList(db, recyclerView, textListLabel, btnCoffeeList);
        });

        return view;
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