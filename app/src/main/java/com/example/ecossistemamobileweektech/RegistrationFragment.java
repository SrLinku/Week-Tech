package com.example.ecossistemamobileweektech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.textfield.TextInputEditText;

public class RegistrationFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        TextInputEditText name = view.findViewById(R.id.editTextName);
        TextInputEditText ra = view.findViewById(R.id.editTextRA);
        TextInputEditText course = view.findViewById(R.id.editTextCourse);
        TextInputEditText series = view.findViewById(R.id.editTextSeries);
        AutoCompleteTextView eventSpinner = view.findViewById(R.id.autoCompleteEvent);
        CheckBox coffee = view.findViewById(R.id.checkBoxCoffee);
        Button btnRegister = view.findViewById(R.id.buttonRegister);
        
        Button btnGoToSpeaker = view.findViewById(R.id.btnGoToSpeaker);
        Button btnGoToProject = view.findViewById(R.id.btnGoToProject);
        Button btnBackToHome = view.findViewById(R.id.btnBackToHome);

        // Setup Event Spinner
        String[] events = getResources().getStringArray(R.array.events_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, events);
        eventSpinner.setAdapter(adapter);

        btnGoToSpeaker.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_registration_to_speaker));
        btnGoToProject.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_registration_to_project));
        btnBackToHome.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.nav_home));

        btnRegister.setOnClickListener(v -> {
            if (name.getText() != null && ra.getText() != null && course.getText() != null && series.getText() != null) {
                String participantName = name.getText().toString();
                String participantSeries = series.getText().toString();
                String selectedEvent = eventSpinner.getText().toString();

                if (participantName.isEmpty() || participantSeries.isEmpty()) {
                    Toast.makeText(getContext(), "Nome e Série são obrigatórios", Toast.LENGTH_SHORT).show();
                } else if (selectedEvent.isEmpty()) {
                    Toast.makeText(getContext(), "Selecione a atividade", Toast.LENGTH_SHORT).show();
                } else {
                    // Salvar no repositório para o Admin ver
                    Participant newParticipant = new Participant(
                            participantName,
                            ra.getText().toString(),
                            course.getText().toString(),
                            participantSeries,
                            selectedEvent,
                            coffee.isChecked()
                    );
                    DataRepository.getInstance().addParticipant(newParticipant);

                    String message = getString(R.string.msg_success_registration) + "\n" +
                            "Participante: " + participantName + "\n" +
                            "Série: " + participantSeries + "\n" +
                            "Atividade: " + selectedEvent + "\n" +
                            "Coffee Break: " + (coffee.isChecked() ? "Sim" : "Não");
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

                    // Voltar para a tela de início após o sucesso
                    Navigation.findNavController(view).navigate(R.id.nav_home);
                }
            }
        });

        return view;
    }
}