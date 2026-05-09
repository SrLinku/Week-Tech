package com.example.ecossistemamobileweektech;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecossistemamobileweektech.database.AppDatabase;
import com.example.ecossistemamobileweektech.entity.Admin;
import java.util.List;

public class AdminManagementFragment extends Fragment {

    private RecyclerView rvPending, rvActive;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_management, container, false);

        db = AppDatabase.getInstance(requireContext());
        rvPending = view.findViewById(R.id.rvPendingAdmins);
        rvActive = view.findViewById(R.id.rvActiveAdmins);

        rvPending.setLayoutManager(new LinearLayoutManager(getContext()));
        rvActive.setLayoutManager(new LinearLayoutManager(getContext()));

        loadAdmins();

        return view;
    }

    private void loadAdmins() {
        new Thread(() -> {
            List<Admin> pending = db.adminDao().getByStatus(0);
            List<Admin> active = db.adminDao().getByStatus(1);

            requireActivity().runOnUiThread(() -> {
                rvPending.setAdapter(new ManageAdminAdapter(pending, true, new ManageAdminAdapter.OnAdminActionListener() {
                    @Override
                    public void onApprove(Admin admin) {
                        updateAdminStatus(admin, 1);
                    }

                    @Override
                    public void onRefuse(Admin admin) {
                        deleteAdmin(admin); // Recusar deleta o pedido
                    }

                    @Override
                    public void onDelete(Admin admin) {
                        // Não usado para pendentes
                    }
                }));

                rvActive.setAdapter(new ManageAdminAdapter(active, false, new ManageAdminAdapter.OnAdminActionListener() {
                    @Override
                    public void onApprove(Admin admin) {}

                    @Override
                    public void onRefuse(Admin admin) {}

                    @Override
                    public void onDelete(Admin admin) {
                        deleteAdmin(admin);
                    }
                }));
            });
        }).start();
    }

    private void updateAdminStatus(Admin admin, int status) {
        new Thread(() -> {
            admin.setStatus(status);
            db.adminDao().update(admin);
            loadAdmins();
        }).start();
    }

    private void deleteAdmin(Admin admin) {
        new Thread(() -> {
            db.adminDao().delete(admin);
            loadAdmins();
        }).start();
    }
}
