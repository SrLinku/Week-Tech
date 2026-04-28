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

public class SpeakerRegistrationFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speaker_registration, container, false);

        TextInputEditText name = view.findViewById(R.id.editSpeakerName);
        TextInputEditText duration = view.findViewById(R.id.editSpeakerDuration);
        Button btnRegister = view.findViewById(R.id.btnRegisterSpeaker);

        btnRegister.setOnClickListener(v -> {
            String speakerName = name.getText().toString();
            String timeStr = duration.getText().toString();

            if (speakerName.isEmpty() || timeStr.isEmpty()) {
                Toast.makeText(getContext(), "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show();
                return;
            }

            int time = Integer.parseInt(timeStr);
            if (time < 40 || time > 60) {
                Toast.makeText(getContext(), "O tempo deve ser entre 40 e 60 minutos", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Inscrição de Palestrante confirmada!", Toast.LENGTH_SHORT).show();
                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }
}