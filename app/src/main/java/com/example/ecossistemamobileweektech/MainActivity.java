package com.example.ecossistemamobileweektech;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.widget.Toolbar;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private boolean isProfessionalMode = false;

    public void setProfessionalMode(boolean professional) {
        this.isProfessionalMode = professional;
        android.content.SharedPreferences prefs = getSharedPreferences("WeekTechPrefs", MODE_PRIVATE);
        boolean isSuper = prefs.getBoolean("is_super_admin", false);

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        if (navView != null) {
            // Itens de Aluno
            navView.getMenu().findItem(R.id.nav_home).setVisible(!professional);
            navView.getMenu().findItem(R.id.nav_project_registration).setVisible(!professional);
            navView.getMenu().findItem(R.id.nav_my_inscriptions).setVisible(!professional);
            
            // Itens de Admin
            navView.getMenu().findItem(R.id.nav_admin_dashboard).setVisible(professional);
            navView.getMenu().findItem(R.id.nav_admin_management).setVisible(professional && isSuper);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                // Remover o padding top automático pois a Toolbar já ocupa esse espaço
                v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Configurar destinos raiz.
        Set<Integer> topLevelDestinations = new HashSet<>();
        topLevelDestinations.add(R.id.nav_home);
        topLevelDestinations.add(R.id.nav_project_registration);
        topLevelDestinations.add(R.id.nav_admin_dashboard);
        topLevelDestinations.add(R.id.nav_my_inscriptions);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).build();
        
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Ocultar/Exibir BottomNav dependendo do destino e gerenciar visibilidade do item Admin
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int id = destination.getId();
            if (id == R.id.nav_auth_selection || 
                id == R.id.nav_user_selection || 
                id == R.id.nav_admin_login ||
                id == R.id.nav_student_login ||
                id == R.id.nav_signup) {
                navView.setVisibility(View.GONE);
                
                // Se voltar para seleção de auth, reseta o modo profissional por precaução
                if (id == R.id.nav_auth_selection) {
                    isProfessionalMode = false;
                }
            } else {
                navView.setVisibility(View.VISIBLE);
                
                // Gerencia visibilidade dos itens dependendo do modo
                navView.getMenu().findItem(R.id.nav_admin_dashboard).setVisible(isProfessionalMode);
                navView.getMenu().findItem(R.id.nav_home).setVisible(!isProfessionalMode);
                navView.getMenu().findItem(R.id.nav_project_registration).setVisible(!isProfessionalMode);
                navView.getMenu().findItem(R.id.nav_my_inscriptions).setVisible(!isProfessionalMode);
                
                // Gerenciar visibilidade da gestão de admins (apenas se for super admin)
                android.content.SharedPreferences prefs = getSharedPreferences("WeekTechPrefs", MODE_PRIVATE);
                boolean isSuper = prefs.getBoolean("is_super_admin", false);
                navView.getMenu().findItem(R.id.nav_admin_management).setVisible(isProfessionalMode && isSuper);

                // Ocultar Toolbar em destinos que já possuem cabeçalho customizado (Home)
                if (id == R.id.nav_home) {
                    if (getSupportActionBar() != null) getSupportActionBar().hide();
                } else {
                    if (getSupportActionBar() != null) getSupportActionBar().show();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
