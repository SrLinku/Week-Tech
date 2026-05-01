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
import com.example.ecossistemamobileweektech.database.AppDatabase;
import com.example.ecossistemamobileweektech.entity.Projeto;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;
import androidx.navigation.Navigation;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        View btnLogout = view.findViewById(R.id.btnLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.nav_user_selection));
        }

        TextView locationLink = view.findViewById(R.id.textViewLocationLink);
        locationLink.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.app.goo.gl/vj4U2rER3KLXQAL3A"));
            startActivity(intent);
        });

        RecyclerView recyclerViewProjects = view.findViewById(R.id.recyclerViewProjects);
        recyclerViewProjects.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Projeto> projects = AppDatabase.getInstance(requireContext()).projetoDao().getAll();
        ProjectAdapter projectAdapter = new ProjectAdapter(projects);
        recyclerViewProjects.setAdapter(projectAdapter);

        return view;
    }
}