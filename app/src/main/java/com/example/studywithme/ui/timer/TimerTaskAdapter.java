package com.example.studywithme.ui.timer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studywithme.R;
import com.example.studywithme.data.models.SessionTask;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Adapter for the Task List in the Timer Activity
 */
public class TimerTaskAdapter extends RecyclerView.Adapter<TimerTaskAdapter.ItemViewHolder> {

    private List<SessionTask> tasks;
    final private ItemViewHolder.OnCheckedChangeListener onCheckedChangeListener;

    public TimerTaskAdapter(List<SessionTask> tasks, ItemViewHolder.OnCheckedChangeListener onCheckedChangeListener) {
        this.tasks = tasks;
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    /**
     * inflates the layout for the list items
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public @NotNull ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TimerTaskAdapter.ItemViewHolder(view, onCheckedChangeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TimerTaskAdapter.ItemViewHolder holder, int position) {
        holder.task.setText(tasks.get(position).getDescription());
        holder.task.setClickable(true);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements CheckBox.OnCheckedChangeListener {

        private final CheckBox task;
        private final ItemViewHolder.OnCheckedChangeListener onCheckedChangeListener;

        public ItemViewHolder(@NonNull View itemView, OnCheckedChangeListener onCheckedChangeListener) {
            super(itemView);
            this.task = itemView.findViewById(R.id.cb_task);
            this.onCheckedChangeListener = onCheckedChangeListener;
            task.setOnCheckedChangeListener(this);
        }

        /**
         * calls onCheckedChange in the Timer Activity if the user clicks into a checkbox
         * @param compoundButton
         * @param b
         */
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            onCheckedChangeListener.onCheckedChange(getAdapterPosition(), b);
        }

        public interface OnCheckedChangeListener {
            void onCheckedChange(int position, boolean checked);
        }
    }
}
