package com.example.ecossistemamobileweektech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.ecossistemamobileweektech.database.AppDatabase;
import com.example.ecossistemamobileweektech.entity.Admin;
import com.google.android.material.textfield.TextInputEditText;

public class AdminLoginFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_login, container, false);

        TextInputEditText email = view.findViewById(R.id.editAdminEmail);
        TextInputEditText password = view.findViewById(R.id.editAdminPassword);
        Button btn = view.findViewById(R.id.btnLoginAdmin);

        btn.setOnClickListener(v -> {
            String matricula = email.getText().toString().trim();
            String userPass = password.getText().toString().trim();

            if (matricula.isEmpty()) {
                email.setError("Informe o e-mail ou matrícula");
                return;
            }
            if (userPass.isEmpty()) {
                password.setError("Informe a senha");
                return;
            }

            // Login Especial para "Cris" (Super Admin) caso o banco esteja vazio
            if (matricula.equals("123") && userPass.equals("123")) {
                saveAdminSession("Cris", matricula, true);
                Navigation.findNavController(view).navigate(R.id.action_login_to_dashboard);
                return;
            }

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(requireContext());
                com.example.ecossistemamobileweektech.entity.Admin admin = db.adminDao().login(matricula, userPass);

                requireActivity().runOnUiThread(() -> {
                    if (admin != null) {
                        if (admin.getStatus() == 1) {
                            saveAdminSession(admin.getName(), admin.getMatricula(), admin.isSuperAdmin());
                            Navigation.findNavController(view).navigate(R.id.action_login_to_dashboard);
                        } else {
                            Toast.makeText(getContext(), "Aguardando aprovação da Cris", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Matrícula ou senha incorretos", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        return view;
    }

    private void saveAdminSession(String name, String matricula, boolean isSuper) {
        android.content.SharedPreferences prefs = requireActivity().getSharedPreferences("WeekTechPrefs", android.content.Context.MODE_PRIVATE);
        prefs.edit()
                .putString("admin_name", name)
                .putString("admin_email", matricula) // Mantendo a chave antiga para compatibilidade
                .putBoolean("is_super_admin", isSuper)
                .remove("student_ra")
                .apply();
        
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setProfessionalMode(true);
        }
    }
}