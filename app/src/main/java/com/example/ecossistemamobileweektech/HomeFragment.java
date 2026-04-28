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
import java.util.ArrayList;
import java.util.List;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.navigation.Navigation;
import android.content.Intent;
import android.net.Uri;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView locationLink = view.findViewById(R.id.textViewLocationLink);
        locationLink.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.app.goo.gl/vj4U2rER3KLXQAL3A"));
            startActivity(intent);
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Event> events = new ArrayList<>();
        events.add(new Event("Abertura", "Diretoria", "19:00", "Palestra"));
        events.add(new Event("Desenvolvimento Android", "João Silva", "20:00", "Palestra"));
        events.add(new Event("UI/UX Design", "Maria Souza", "21:00", "Palestra"));
        events.add(new Event("Workshop Kotlin", "Pedro Santos", "19:30", "Projeto"));

        EventAdapter adapter = new EventAdapter(events);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabAddEvent);
        fab.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.nav_registration);
        });

        FloatingActionButton fabAdmin = view.findViewById(R.id.fabAdmin);
        fabAdmin.setOnClickListener(v -> {
            // Voltando ao fluxo normal de login para segurança, conforme solicitado
            Navigation.findNavController(view).navigate(R.id.nav_admin_login);
        });

        return view;
    }
}