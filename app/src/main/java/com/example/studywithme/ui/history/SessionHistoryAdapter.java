package com.example.studywithme.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.utils.DateHelper;
import com.example.studywithme.utils.StringHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class SessionHistoryAdapter extends RecyclerView.Adapter<SessionHistoryAdapter.ItemViewHolder> {

    private ArrayList<Session> sessions;
    private final ItemViewHolder.OnItemClickListener onItemClickListener;

    public SessionHistoryAdapter(List<Session> sessions, ItemViewHolder.OnItemClickListener onItemClickListener) {
        this.sessions = (ArrayList<Session>) sessions;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public @NotNull ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.session_item, parent, false);
        return new ItemViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Session session = sessions.get(position);
        switch (session.getOwnerSetting().getCategory()) {
            case WORK:
                holder.sessionImage.setImageResource(R.drawable.work_image);
                break;
            case UNIVERSITY:
                holder.sessionImage.setImageResource(R.drawable.university_image);
                break;
            case HOBBY:
                holder.sessionImage.setImageResource(R.drawable.hobby_image);
                break;
            default:
                holder.sessionImage.setImageResource(R.drawable.work_image);
        }
        holder.sessionName.setText(session.getOwnerSetting().getName());
        holder.sessionDate.setText(DateHelper.formatDate((double) session.getStartedAt().toDate().getTime()));
        holder.sessionDuration.setText(session.getDuration() + " Minuten");
        if(session.getPartner() != null) {
            holder.sessionPartner.setText(session.getPartner().getName());
        } else {
            holder.sessionPartner.setText(R.string.no_partner);
        }
        holder.sessionCategory.setText(StringHelper.capitalize(session.getOwnerSetting().getCategory().name()));
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView sessionImage;
        private final TextView sessionName;
        private final TextView sessionDate;
        private final TextView sessionDuration;
        private final TextView sessionPartner;
        private final TextView sessionCategory;
        private final OnItemClickListener onItemClickListener;

        public ItemViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.sessionImage = itemView.findViewById(R.id.iv_image);
            this.sessionName = itemView.findViewById(R.id.tv_session_name);
            this.sessionDate = itemView.findViewById(R.id.tv_date);
            this.sessionDuration = itemView.findViewById(R.id.tv_duration);
            this.sessionPartner = itemView.findViewById(R.id.tv_partner);
            this.sessionCategory = itemView.findViewById(R.id.tv_category);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }
    }

}
