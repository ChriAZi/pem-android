package com.example.studywithme.ui.join;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SessionListAdapter extends RecyclerView.Adapter<SessionListAdapter.SessionListViewHolder> {
    private ArrayList<Session> sessions;
    final private ListItemClickListener mOnClickListener;
    private SessionListActivity sessionListActivity;

    public SessionListAdapter(List<Session> sessions,ListItemClickListener onClickListener) {
        this.sessions = (ArrayList<Session>) sessions;
        this.mOnClickListener = onClickListener;
    }

    interface ListItemClickListener{
        void onListItemClick(int position);

        int getContentViewId();

        int getNavigationMenuItemId();
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String time = String.valueOf(session.getStartedAt().toDate());
        int sessionDuration = session.getDuration();
        String date = dateFormat.format(session.getStartedAt().toDate());
        Date d = session.getStartedAt().toDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE,sessionDuration);
        String endTime = dateFormat.format(cal.getTime());
        //should be called once the time of the session has passed
        if(System.currentTimeMillis() > d.getTime()){
            //Session ended
            session.setActive(false);
            sessionListActivity.endSession();
        }
        if(session.isActive() == true) {
            holder.sessionStart.setText("Started: " + date);
           // holder.sessionDuration.setText("Duration: " + session.getDuration() + " Minutes");
            holder.sessionDuration.setText("Session ends at " + endTime );
            holder.sessionOwner.setText("by: " + session.getOwner().getName());
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
