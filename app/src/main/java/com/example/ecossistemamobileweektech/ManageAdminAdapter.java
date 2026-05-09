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

    private List<com.example.ecossistemamobileweektech.entity.Admin> admins;
    private boolean isPending;

    public interface OnAdminActionListener {
        void onApprove(com.example.ecossistemamobileweektech.entity.Admin admin);
        void onRefuse(com.example.ecossistemamobileweektech.entity.Admin admin);
        void onDelete(com.example.ecossistemamobileweektech.entity.Admin admin);
    }

    private OnAdminActionListener listener;

    public ManageAdminAdapter(List<com.example.ecossistemamobileweektech.entity.Admin> admins, boolean isPending, OnAdminActionListener listener) {
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
        com.example.ecossistemamobileweektech.entity.Admin admin = admins.get(position);
        holder.textMatricula.setText("Matrícula: " + admin.getMatricula());
        holder.textName.setText("Nome: " + admin.getName());

        if (isPending) {
            holder.layoutPendingActions.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.GONE);
            
            holder.btnApprove.setOnClickListener(v -> listener.onApprove(admin));
            holder.btnRefuse.setOnClickListener(v -> listener.onRefuse(admin));
        } else {
            holder.layoutPendingActions.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnDelete.setOnClickListener(v -> listener.onDelete(admin));
        }
    }

    @Override
    public int getItemCount() {
        return admins.size();
    }

    static class AdminViewHolder extends RecyclerView.ViewHolder {
        TextView textMatricula, textName;
        View layoutPendingActions;
        Button btnApprove, btnRefuse;
        ImageButton btnDelete;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            textMatricula = itemView.findViewById(R.id.textAdminMatricula);
            textName = itemView.findViewById(R.id.textAdminName);
            layoutPendingActions = itemView.findViewById(R.id.layoutPendingActions);
            btnApprove = itemView.findViewById(R.id.btnAdminApprove);
            btnRefuse = itemView.findViewById(R.id.btnAdminRefuse);
            btnDelete = itemView.findViewById(R.id.btnAdminDelete);
        }
    }
}
