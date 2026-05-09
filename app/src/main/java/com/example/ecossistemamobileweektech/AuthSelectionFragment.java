package com.example.ecossistemamobileweektech;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import androidx.navigation.fragment.NavHostFragment;

public class AuthSelectionFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth_selection, container, false);

        view.findViewById(R.id.btnStudentLogin).setOnClickListener(v -> 
            Navigation.findNavController(view).navigate(R.id.nav_student_login));

        view.findViewById(R.id.btnAdminLogin).setOnClickListener(v -> 
            Navigation.findNavController(view).navigate(R.id.nav_admin_login));

        view.findViewById(R.id.btnGoToSignup).setOnClickListener(v -> 
            Navigation.findNavController(view).navigate(R.id.nav_signup));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Verificar sessão existente após a view ser criada para evitar erro de inflação
        checkExistingSession();
    }

    private void checkExistingSession() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("WeekTechPrefs", Context.MODE_PRIVATE);
        
        String adminEmail = prefs.getString("admin_email", null);
        String studentRa = prefs.getString("student_ra", null);

        if (adminEmail != null) {
            // Admin logado
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).setProfessionalMode(true);
            }
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_auth_selection_to_admin_dashboard);
        } else if (studentRa != null) {
            // Estudante logado
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).setProfessionalMode(false);
            }
            NavHostFragment.findNavController(this)
                    .navigate(R.id.nav_home);
        }
    }
}
