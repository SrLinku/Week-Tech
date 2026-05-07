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
import com.example.ecossistemamobileweektech.database.AppDatabase;
import com.example.ecossistemamobileweektech.entity.Projeto;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        
        Button btnGoToProject = view.findViewById(R.id.btnGoToProject);

        // Configuração do Spinner de eventos
        try {
            List<String> eventList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.events_array)));
            
            // Buscar apenas projetos aprovados do banco e adicionar à lista
            List<Projeto> projetos = AppDatabase.getInstance(requireContext()).projetoDao().getApproved();
            for (Projeto p : projetos) {
                eventList.add("Projeto: " + p.getNomeProjeto());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, eventList);
            eventSpinner.setAdapter(adapter);

            // Ouvinte para mudar a visibilidade do Coffee Break
            eventSpinner.setOnItemClickListener((parent, view1, position, id) -> {
                String selection = (String) parent.getItemAtPosition(position);
                updateCoffeeVisibility(selection, coffee);
            });
        } catch (Exception e) {
            List<String> fallbackEvents = new ArrayList<>(Arrays.asList("Palestra Geral", "Workshop"));
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, fallbackEvents);
            eventSpinner.setAdapter(adapter);
        }

        // Processar argumentos se vier da tela expandida
        if (getArguments() != null) {
            String preSelected = getArguments().getString("selectedActivity");
            boolean withCoffee = getArguments().getBoolean("withCoffee");
            
            if (preSelected != null) {
                eventSpinner.setText(preSelected, false);
                updateCoffeeVisibility(preSelected, coffee);
                coffee.setChecked(withCoffee);
            }
        }

        btnGoToProject.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_registration_to_project));

        btnRegister.setOnClickListener(v -> {
            if (name.getText() != null && ra.getText() != null && course.getText() != null && series.getText() != null) {
                String participantName = name.getText().toString();
                String participantSeries = series.getText().toString();
                String selectedEvent = eventSpinner.getText().toString();

                if (participantName.isEmpty() || participantSeries.isEmpty()) {
                    Toast.makeText(getContext(), "Nome e Série são obrigatórios", Toast.LENGTH_SHORT).show();
                } else {
                    // OPERAÇÃO DE BANCO DE DADOS: Criação do objeto participante com os dados do formulário
                    Participant newParticipant = new Participant(
                            participantName,
                            ra.getText().toString(),
                            course.getText().toString(),
                            participantSeries,
                            selectedEvent,
                            coffee.isChecked()
                    );
                    
                    // OPERAÇÃO DE BANCO DE DADOS: Inserção do participante no banco Room
                    AppDatabase.getInstance(requireContext()).participanteDao().insert(newParticipant);

                    Toast.makeText(getContext(), "Inscrição realizada e salva no banco!", Toast.LENGTH_LONG).show();
                    Navigation.findNavController(view).navigate(R.id.nav_home);
                }
            }
        });

        return view;
    }

    private void updateCoffeeVisibility(String selection, CheckBox coffee) {
        if (selection == null) {
            coffee.setVisibility(View.GONE);
            return;
        }

        if (selection.startsWith("Projeto: ")) {
            String projectName = selection.replace("Projeto: ", "");
            Projeto p = AppDatabase.getInstance(requireContext()).projetoDao().getByName(projectName);
            if (p != null && p.isHasCoffeeBreak()) {
                coffee.setVisibility(View.VISIBLE);
            } else {
                coffee.setVisibility(View.GONE);
                coffee.setChecked(false);
            }
        } else {
            // Regra para eventos padrão (apenas o de Networking/Encerramento tem coffee)
            if (selection.contains("Networking") || selection.contains("Encerramento")) {
                coffee.setVisibility(View.VISIBLE);
            } else {
                coffee.setVisibility(View.GONE);
                coffee.setChecked(false);
            }
        }
    }
}