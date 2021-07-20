package com.example.studywithme.ui.join;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.utils.StringHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the Session List
 */
public class SessionListAdapter extends RecyclerView.Adapter<SessionListAdapter.ItemViewHolder> {

    private ArrayList<Session> sessions;
    final private ItemViewHolder.OnItemClickListener onItemClickListener;

    //constructor
    public SessionListAdapter(List<Session> sessions, ItemViewHolder.OnItemClickListener onItemClickListener) {
        this.sessions = (ArrayList<Session>) sessions;
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * inflates the layout for the list items
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.session_join_item, parent, false);
        return new ItemViewHolder(view, onItemClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Session session = sessions.get(position);
        holder.sessionName.setText(session.getOwnerSetting().getName());
        holder.sessionDuration.setText(session.getDuration() + " Minuten");
        holder.sessionOwner.setText(session.getOwner().getName());
        holder.sessionCategory.setText(StringHelper.capitalize(session.getOwnerSetting().getCategory().name()));
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView sessionName;
        private final TextView sessionDuration;
        private final TextView sessionOwner;
        private final TextView sessionCategory;
        private final ItemViewHolder.OnItemClickListener onItemClickListener;

        public ItemViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.sessionName = itemView.findViewById(R.id.tv_session_name);
            this.sessionDuration = itemView.findViewById(R.id.tv_duration);
            this.sessionOwner = itemView.findViewById(R.id.tv_owner);
            this.sessionCategory = itemView.findViewById(R.id.tv_category);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        /**
         * calls onItemClick in the SessionListActivity when an item is clicked
         * @param v
         */
        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }
    }
}
