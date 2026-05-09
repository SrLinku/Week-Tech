package com.example.ecossistemamobileweektech;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecossistemamobileweektech.database.AppDatabase;
import com.example.ecossistemamobileweektech.entity.Projeto;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento da Tela Inicial (Home) para Alunos.
 * Exibe a lista de eventos (Palestras e Projetos).
 */
public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setupLogout(view);
        setupLocationLink(view);
        setupEventsList(view);

        return view;
    }

    private void setupLogout(View view) {
        View btnLogout = view.findViewById(R.id.btnLogout);
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                // Limpa a sessão do estudante
                SharedPreferences prefs = requireActivity().getSharedPreferences("WeekTechPrefs", Context.MODE_PRIVATE);
                prefs.edit().clear().apply();
                
                // Retorna para a tela de autenticação inicial limpando o histórico
                Navigation.findNavController(view).navigate(R.id.nav_auth_selection);
            });
        }
    }

    private void setupLocationLink(View view) {
        TextView locationLink = view.findViewById(R.id.textViewLocationLink);
        locationLink.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.app.goo.gl/vj4U2rER3KLXQAL3A"));
            startActivity(intent);
        });
    }

    private void setupEventsList(View view) {
        RecyclerView recyclerViewProjects = view.findViewById(R.id.recyclerViewProjects);
        recyclerViewProjects.setLayoutManager(new LinearLayoutManager(getContext()));

        new Thread(() -> {
            // Buscar apenas projetos reais aprovados do banco de dados
            List<Projeto> dbProjects = AppDatabase.getInstance(requireContext()).projetoDao().getApproved();
            for (Projeto p : dbProjects) {
                p.setTipoEvento("PROJETO");
            }

            requireActivity().runOnUiThread(() -> {
                ProjectAdapter projectAdapter = new ProjectAdapter(dbProjects, requireContext());
                recyclerViewProjects.setAdapter(projectAdapter);
            });
        }).start();
    }
}
