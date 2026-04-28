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
import java.util.List;

public class AdminDashboardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewParticipants);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Participant> participantList = DataRepository.getInstance().getParticipants();
        ParticipantAdapter adapter = new ParticipantAdapter(participantList);
        recyclerView.setAdapter(adapter);

        view.findViewById(R.id.btnAddAdmin).setOnClickListener(v -> 
            Navigation.findNavController(view).navigate(R.id.action_dashboard_to_add_admin)
        );

        return view;
    }
}