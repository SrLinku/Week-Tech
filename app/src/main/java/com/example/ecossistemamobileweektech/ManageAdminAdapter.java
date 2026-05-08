package com.example.ecossistemamobileweektech;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecossistemamobileweektech.entity.Admin;
import java.util.List;

public class ManageAdminAdapter extends RecyclerView.Adapter<ManageAdminAdapter.AdminViewHolder> {

    private List<Admin> admins;
    private boolean isPending;
    private OnAdminActionListener listener;

    public interface OnAdminActionListener {
        void onAction(Admin admin);
    }

    public ManageAdminAdapter(List<Admin> admins, boolean isPending, OnAdminActionListener listener) {
        this.admins = admins;
        this.isPending = isPending;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manage_admin, parent, false);
        return new AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        Admin admin = admins.get(position);
        holder.textMatricula.setText("Matrícula: " + admin.getMatricula());
        holder.textName.setText("Nome: " + admin.getName());

        if (isPending) {
            holder.btnAction.setVisibility(View.VISIBLE);
            holder.btnAction.setText("APROVAR");
            holder.btnAction.setOnClickListener(v -> listener.onAction(admin));
            holder.btnDelete.setVisibility(View.GONE);
        } else {
            holder.btnAction.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(v -> listener.onAction(admin));
        }
    }

    @Override
    public int getItemCount() {
        return admins.size();
    }

    static class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView textMatricula, textName;
        Button btnAction;
        ImageButton btnDelete;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            textMatricula = itemView.findViewById(R.id.textAdminMatricula);
            textName = itemView.findViewById(R.id.textAdminName);
            btnAction = itemView.findViewById(R.id.btnAdminAction);
            btnDelete = itemView.findViewById(R.id.btnAdminDelete);
        }
    }
}
