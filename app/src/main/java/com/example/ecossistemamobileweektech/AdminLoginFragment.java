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

public class AdminLoginFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_login, container, false);

        TextInputEditText email = view.findViewById(R.id.editAdminEmail);
        TextInputEditText password = view.findViewById(R.id.editAdminPassword);
        Button btn = view.findViewById(R.id.btnLoginAdmin);

        btn.setOnClickListener(v -> {
            String userEmail = email.getText().toString();
            String userPass = password.getText().toString();

            if (DataRepository.getInstance().validateAdmin(userEmail, userPass)) {
                Navigation.findNavController(view).navigate(R.id.action_login_to_dashboard);
            } else {
                Toast.makeText(getContext(), "Acesso Negado", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}