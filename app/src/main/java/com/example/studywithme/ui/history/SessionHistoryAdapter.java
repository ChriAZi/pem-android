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

import java.util.ArrayList;
import java.util.List;


public class SessionHistoryAdapter extends RecyclerView.Adapter<SessionHistoryAdapter.ItemViewHolder> {

    private ArrayList<Session> sessions;

    public SessionHistoryAdapter(List<Session> sessions) {
        this.sessions = (ArrayList<Session>) sessions;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.session_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Session session = sessions.get(position);
        switch (session.getOwnerSetting().getCategories().get(0)) {
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
        holder.sessionDuration.setText(session.getDuration() + " Minuten");
        holder.sessionPartner.setText("Test Partner");


    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ImageView sessionImage;
        private final TextView sessionName;
        private final TextView sessionDuration;
        private final TextView sessionPartner;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.sessionImage = itemView.findViewById(R.id.iv_image);
            this.sessionName = itemView.findViewById(R.id.tv_session_name);
            this.sessionDuration = itemView.findViewById(R.id.tv_session_duration);
            this.sessionPartner = itemView.findViewById(R.id.tv_session_partner);
        }
    }

}
