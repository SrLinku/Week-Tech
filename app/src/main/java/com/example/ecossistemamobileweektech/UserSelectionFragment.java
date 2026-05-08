package com.example.ecossistemamobileweektech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserSelectionFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_selection, container, false);

        // Esconder o menu inferior na tela de seleção para forçar a escolha
        BottomNavigationView navView = requireActivity().findViewById(R.id.bottom_navigation);
        if (navView != null) {
            navView.setVisibility(View.GONE);
        }

        view.findViewById(R.id.cardStudent).setOnClickListener(v -> {
            // Se for aluno, vai para a tela de login do estudante
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).setProfessionalMode(false);
            }
            Navigation.findNavController(view).navigate(R.id.action_user_selection_to_student_login);
        });

        view.findViewById(R.id.cardProfessional).setOnClickListener(v -> {
            // Se for profissional, ativa o modo profissional
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).setProfessionalMode(true);
            }
            Navigation.findNavController(view).navigate(R.id.action_user_selection_to_admin_login);
        });

        return view;
    }
}