package com.example.studywithme.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studywithme.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The Adapter handling the distractions displayed in the SessionDetailActivity
 */
public class DistractionAdapter extends RecyclerView.Adapter<DistractionAdapter.ItemViewHolder> {

    private List<String> distractions;

    public DistractionAdapter(List<String> distractions) {
        this.distractions = distractions;
    }

    @Override
    public @NotNull ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.distraction_item, parent, false);
        return new DistractionAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistractionAdapter.ItemViewHolder holder, int position) {
        holder.distraction.setText(distractions.get(position));
    }

    @Override
    public int getItemCount() {
        return distractions.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView distraction;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            distraction = itemView.findViewById(R.id.tv_distraction);
        }
    }
}
