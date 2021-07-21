package com.example.studywithme.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.SessionCategory;
import com.example.studywithme.data.models.User;
import com.example.studywithme.utils.DateHelper;
import com.example.studywithme.utils.StringHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * the adapter handling the list of sessions to be shown in the SessionHistoryActivity
 */

public class SessionHistoryAdapter extends RecyclerView.Adapter<SessionHistoryAdapter.ItemViewHolder> {

    private ArrayList<Session> sessions;
    private final ItemViewHolder.OnItemClickListener onItemClickListener;
    private final Context context;

    public SessionHistoryAdapter(List<Session> sessions, ItemViewHolder.OnItemClickListener onItemClickListener, Context context) {
        this.sessions = (ArrayList<Session>) sessions;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
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
        /*
          Checks if the user requesting the view is the owner of a session and adapting all view content based on that information
         */
        boolean isOwner = User.getIdFromPreferences(context).equals(session.getOwner().getUid());
        SessionCategory category;
        String name;

        if (isOwner && session.getPartner() != null) {
            holder.sessionPartner.setText(session.getPartner().getName());
        } else if (!isOwner) {
            holder.sessionPartner.setText(session.getOwner().getName());
        } else {
            holder.sessionPartner.setText(R.string.private_session);
        }

        if (isOwner) {
            category = session.getOwnerSetting().getCategory();
            name = session.getOwnerSetting().getName();
        } else {
            category = session.getPartnerSetting().getCategory();
            name = session.getPartnerSetting().getName();
        }
        /*
        Sets stock image based on the SessionCategory
         */
        switch (category) {
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

        holder.sessionCategory.setText(StringHelper.capitalize(category.name()));
        holder.sessionName.setText(name);
        holder.sessionDate.setText(DateHelper.formatDate((double) session.getStartedAt().toDate().getTime()));
        holder.sessionDuration.setText(session.getDuration() + " Minuten");
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

        /**
         * Interface to allow handling of onClick events per item in the parent activity
         */
        public interface OnItemClickListener {
            void onItemClick(int position);
        }
    }

}
