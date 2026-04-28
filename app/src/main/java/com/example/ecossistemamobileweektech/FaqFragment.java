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

public class FaqFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faq, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFaq);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<FaqItem> faqList = new ArrayList<>();
        faqList.add(new FaqItem("Como faço para confirmar presença?", "Basta acessar a aba de cada palestra e clicar no botão de confirmar presença após o início."));
        faqList.add(new FaqItem("Onde será o Coffee Break?", "No saguão principal do bloco de tecnologia."));
        faqList.add(new FaqItem("Posso me inscrever em mais de uma palestra?", "Sim, desde que os horários não coincidam."));
        faqList.add(new FaqItem("Haverá certificado?", "Sim, os certificados serão enviados por e-mail após o término da Mobile Week."));

        FaqAdapter adapter = new FaqAdapter(faqList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}