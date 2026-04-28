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
import com.google.android.material.textfield.TextInputEditText;

public class AddAdminFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_admin, container, false);

        TextInputEditText email = view.findViewById(R.id.editNewAdminEmail);
        TextInputEditText password = view.findViewById(R.id.editNewAdminPassword);
        Button btnSave = view.findViewById(R.id.btnSaveAdmin);

        btnSave.setOnClickListener(v -> {
            String newEmail = email.getText().toString();
            String newPass = password.getText().toString();

            if (!newEmail.isEmpty() && !newPass.isEmpty()) {
                DataRepository.getInstance().addAdmin(new Admin(newEmail, newPass));
                Toast.makeText(getContext(), "Administrador cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigateUp();
            } else {
                Toast.makeText(getContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}