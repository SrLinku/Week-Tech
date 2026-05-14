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
import com.example.ecossistemamobileweektech.entity.Admin;
import com.example.ecossistemamobileweektech.entity.Usuario;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;

public class SignupFragment extends Fragment {

    private View layoutStudent, layoutAdmin;
    private TextInputEditText editName, editRA, editSemester, editMatricula, editPassword, editAdminName;
    private AutoCompleteTextView autoCompleteCourse;
    private MaterialButtonToggleGroup toggleGroup;
    private boolean isStudent = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        layoutStudent = view.findViewById(R.id.layoutStudentFields);
        layoutAdmin = view.findViewById(R.id.layoutAdminFields);
        toggleGroup = view.findViewById(R.id.toggleGroupUserType);

        editName = view.findViewById(R.id.editSignupName);
        editRA = view.findViewById(R.id.editSignupRA);
        autoCompleteCourse = view.findViewById(R.id.autoCompleteSignupCourse);
        editSemester = view.findViewById(R.id.editSignupSemester);
        editMatricula = view.findViewById(R.id.editSignupMatricula);
        editAdminName = view.findViewById(R.id.editSignupAdminName);
        editPassword = view.findViewById(R.id.editSignupPassword);

        // Configurar dropdown de cursos
        String[] courses = getResources().getStringArray(R.array.courses_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, courses);
        autoCompleteCourse.setAdapter(adapter);

        toggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btnTypeStudent) {
                    isStudent = true;
                    layoutStudent.setVisibility(View.VISIBLE);
                    layoutAdmin.setVisibility(View.GONE);
                } else {
                    isStudent = false;
                    layoutStudent.setVisibility(View.GONE);
                    layoutAdmin.setVisibility(View.VISIBLE);
                }
            }
        });

        view.findViewById(R.id.btnFinishSignup).setOnClickListener(v -> handleSignup());

        return view;
    }

    private void handleSignup() {
        String pass = editPassword.getText().toString();
        if (pass.isEmpty()) {
            editPassword.setError("Defina uma senha");
            return;
        }

        AppDatabase db = AppDatabase.getInstance(requireContext());

        if (isStudent) {
            String name = editName.getText().toString();
            String ra = editRA.getText().toString();
            String course = autoCompleteCourse.getText().toString();
            String semester = editSemester.getText().toString();

            if (name.isEmpty() || ra.isEmpty() || course.isEmpty()) {
                Toast.makeText(getContext(), "Preencha todos os campos do aluno", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.usuarioDao().getByRA(ra) != null) {
                Toast.makeText(getContext(), "RA já cadastrado", Toast.LENGTH_SHORT).show();
                return;
            }

            Usuario user = new Usuario(name, ra, course, semester, pass);
            db.usuarioDao().insert(user);
            Toast.makeText(getContext(), "Conta de aluno criada! Faça login.", Toast.LENGTH_LONG).show();
            Navigation.findNavController(requireView()).navigate(R.id.nav_auth_selection);

        } else {
            String adminName = editAdminName.getText().toString().trim();
            String matricula = editMatricula.getText().toString();

            if (adminName.isEmpty()) {
                editAdminName.setError("Informe seu nome");
                return;
            }

            if (matricula.isEmpty()) {
                editMatricula.setError("Informe a matrícula");
                return;
            }

            if (db.adminDao().getByMatricula(matricula) != null) {
                Toast.makeText(getContext(), "Matrícula já cadastrada", Toast.LENGTH_SHORT).show();
                return;
            }

            // Super Admin Cris já deve existir ou ser o primeiro? 
            // O enunciado diz "Cris" é o super admin. Vou assumir que novos são comuns.
            Admin admin = new Admin(adminName, matricula, pass, false);
            db.adminDao().insert(admin);
            Toast.makeText(getContext(), "Solicitação enviada para Cris!", Toast.LENGTH_LONG).show();
            Navigation.findNavController(requireView()).navigate(R.id.nav_auth_selection);
        }
    }
}
