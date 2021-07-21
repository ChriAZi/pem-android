package com.example.studywithme.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studywithme.R;
import com.example.studywithme.data.models.SessionTask;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The Adapter handling the tasks displayed in the SessionDetailActivity
 */
public class SessionDetailTaskAdapter extends RecyclerView.Adapter<SessionDetailTaskAdapter.ItemViewHolder> {

    private List<SessionTask> tasks;

    public SessionDetailTaskAdapter(List<SessionTask> tasks) {
        this.tasks = tasks;
    }

    @Override
    public @NotNull ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new SessionDetailTaskAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionDetailTaskAdapter.ItemViewHolder holder, int position) {
        holder.task.setChecked(tasks.get(position).isDone());
        holder.task.setText(tasks.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox task;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.cb_task);
        }
    }
}
