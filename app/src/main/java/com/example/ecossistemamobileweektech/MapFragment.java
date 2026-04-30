package com.example.ecossistemamobileweektech;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Fragmento responsável por exibir a localização do evento.
 * Fornece um link/botão que redireciona para o Google Maps externo.
 */
public class MapFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla o layout da tela de localização
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        
        Button btnOpenMaps = view.findViewById(R.id.btnOpenMaps);
        
        // Configura o clique do botão para abrir o endereço no Google Maps
        btnOpenMaps.setOnClickListener(v -> {
            // Endereço da Unicesumar Campus Londrina
            String address = "Av. Santa Mônica, 450 - Vila Santa Terezinha, Londrina - PR";
            
            // Cria uma URI para busca no mapa
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
            
            // Cria a Intent para abrir o aplicativo de mapas
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            
            // Verifica se existe um aplicativo de mapas disponível para processar a Intent
            if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // Caso não tenha o app, abre o link direto no navegador (Fallback)
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(address)));
                startActivity(browserIntent);
            }
        });
        
        return view;
    }
}
