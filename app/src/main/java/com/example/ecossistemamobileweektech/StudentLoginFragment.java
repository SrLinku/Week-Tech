package com.example.ecossistemamobileweektech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.ecossistemamobileweektech.database.AppDatabase;
import com.example.ecossistemamobileweektech.entity.Usuario;
import com.google.android.material.textfield.TextInputEditText;

public class StudentLoginFragment extends Fragment {

    private TextInputEditText editRA, editPassword;
    private Button btnLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_login, container, false);

        editRA = view.findViewById(R.id.editStudentRA);
        editPassword = view.findViewById(R.id.editStudentPassword);
        btnLogin = view.findViewById(R.id.btnStudentLogin);

        btnLogin.setOnClickListener(v -> {
            String ra = editRA.getText().toString();
            String password = editPassword.getText().toString();

            if (ra.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Preencha RA e Senha", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(requireContext());
                Usuario user = db.usuarioDao().login(ra, password);

                requireActivity().runOnUiThread(() -> {
                    if (user != null) {
                        // Salvar dados do estudante
                        requireActivity().getSharedPreferences("WeekTechPrefs", android.content.Context.MODE_PRIVATE)
                                .edit()
                                .putString("student_name", user.getName())
                                .putString("student_ra", user.getRa())
                                .putString("student_course", user.getCourse())
                                .putString("student_semester", user.getSeries())
                                .remove("admin_email")
                                .apply();

                        Toast.makeText(getContext(), "Bem-vindo, " + user.getName(), Toast.LENGTH_SHORT).show();
                        
                        if (getActivity() instanceof MainActivity) {
                            ((MainActivity) getActivity()).setProfessionalMode(false);
                        }

                        Navigation.findNavController(view).navigate(R.id.nav_project_registration);
                    } else {
                        Toast.makeText(getContext(), "RA ou Senha incorretos", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });

        return view;
    }
}
