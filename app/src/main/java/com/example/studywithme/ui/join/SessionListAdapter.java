package com.example.studywithme.ui.join;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.history.SessionHistoryAdapter;
import com.example.studywithme.ui.viewmodels.AbstractViewModel;
import com.example.studywithme.ui.viewmodels.QuestionnaireViewModel;
import com.example.studywithme.utils.ToastMaster;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SessionListAdapter extends RecyclerView.Adapter<SessionListAdapter.SessionListViewHolder> {
    private ArrayList<Session> sessions;
    final private ListItemClickListener mOnClickListener;

    public SessionListAdapter(List<Session> sessions,ListItemClickListener onClickListener) {
        this.sessions = (ArrayList<Session>) sessions;
        this.mOnClickListener = onClickListener;
    }

    interface ListItemClickListener{
        void onListItemClick(int position);
    }

    @NonNull
    @Override
    public SessionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.session_join_item, parent, false);
        return new SessionListAdapter.SessionListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionListViewHolder holder, int position) {
        Session session = sessions.get(position);
        if(session.isActive() == true) {
            holder.sessionStart.setText(session.getStartedAt().toDate().toString());
            holder.sessionDuration.setText(session.getDuration() + " Minuten");
            holder.sessionOwner.setText(session.getOwner().getName());
        }

    }

    public void setData(ArrayList<Session> newsessions){
        this.sessions = newsessions;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }


    public class SessionListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView sessionStart;
        private final TextView sessionDuration;
        private final TextView sessionOwner;
        public View layout;

        public SessionListViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView;
            this.sessionStart = itemView.findViewById(R.id.session_started_at);
            this.sessionDuration = itemView.findViewById(R.id.session_duration);
            this.sessionOwner= itemView.findViewById(R.id.session_owner);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnClickListener.onListItemClick(position);


        }
    }
}