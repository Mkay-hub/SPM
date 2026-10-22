package com.example.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.model.PantryItem;

import java.util.ArrayList;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<PantryItem> pantryItems = new ArrayList<>();
    private final onPantryItemClickListener listener;

    public interface onPantryItemClickListener {
        void onEditClick(PantryItem item);

        void onDeleteClick(PantryItem item);
    }

    public PantryAdapter(onPantryItemClickListener listener) {
        this.listener = listener;
    }

    public void setPantryItems(List<PantryItem> items) {
        this.pantryItems = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = pantryItems.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final TextView tvQuantity;
        private final TextView tvExpiry;
        private final ImageButton btnEdit;
        private final ImageButton btnDelete;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvIngredientName);
            tvQuantity = itemView.findViewById(R.id.tvQuantityAndUnit);
            tvExpiry = itemView.findViewById(R.id.tvExpiryDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }

        public void bind(PantryItem item, onPantryItemClickListener listener) {
            tvName.setText(item.getName());
            String quantityText = item.getQuantity() + " " + item.getUnit();
            tvQuantity.setText(quantityText);

            if (item.getExpirydate() != null && !item.getExpirydate().trim().isEmpty()) {
                tvExpiry.setText("Expires: " + item.getExpirydate());
                tvExpiry.setVisibility(View.VISIBLE);
            } else {
                tvExpiry.setVisibility(View.GONE);
            }

            btnEdit.setOnClickListener(v -> listener.onEditClick(item));
            btnDelete.setOnClickListener(v -> listener.onDeleteClick(item));
        }
    }
}




























