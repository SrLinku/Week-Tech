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
import com.google.android.material.textfield.TextInputEditText;

public class ProjectRegistrationFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_registration, container, false);

        TextInputEditText name = view.findViewById(R.id.editProjectOwnerName);
        TextInputEditText ra = view.findViewById(R.id.editProjectOwnerRA);
        TextInputEditText title = view.findViewById(R.id.editProjectTitle);
        TextInputEditText desc = view.findViewById(R.id.editProjectDescription);
        Button btn = view.findViewById(R.id.btnRegisterProject);

        btn.setOnClickListener(v -> {
            if (name.getText().toString().isEmpty() || title.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Nome e Título são obrigatórios", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Projeto enviado para avaliação!", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }
}