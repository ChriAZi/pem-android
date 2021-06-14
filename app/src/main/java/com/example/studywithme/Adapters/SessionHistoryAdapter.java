package com.example.studywithme.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionHistoryAdapter extends RecyclerView.Adapter<SessionHistoryAdapter.MyAdapter> {
    private List<Session> sessionList;
    private Context context;

    public SessionHistoryAdapter(Context context, List<Session> sessionList){
        this.context = context;
        this.sessionList= sessionList;
    }


    public MyAdapter onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.history_design,parent,false);
        return new MyAdapter(view);
    }

    @Override
    public void onBindViewHolder( MyAdapter holder, int position) {
        final Session sessionPro =sessionList.get(position);

        holder.session_uid.setText(sessionPro.getUid());
        holder.session_duration.setText(sessionPro.getDuration());
        holder.session_owner.setText((CharSequence) sessionPro.getOwner());
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView  session_uid ;
        TextView session_duration;
        TextView session_owner;
        public MyAdapter(View itemView) {
            super(itemView);

            session_uid = itemView.findViewById(R.id.session_uid);
            session_duration =itemView.findViewById(R.id.session_duration);
            session_owner = itemView.findViewById(R.id.session_owner);

        }
    }
}

/* public class SessionHistoryAdapter extends FirestoreRecyclerAdapter<Session,SessionHistoryAdapter.SessionHistoryHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
   /* public SessionHistoryAdapter(@NonNull FirestoreRecyclerOptions<Session> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder( SessionHistoryAdapter.SessionHistoryHolder holder, int position,  Session model) {
              holder.session_uid.setText(model.getUid());
              holder.session_duration.setText(model.getUid());
              holder.session_owner.setText((CharSequence) model.getOwner());
    }

    @NonNull

    @Override
    public SessionHistoryHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_design,parent,false);
        return new SessionHistoryHolder(v);
    }

    class SessionHistoryHolder extends RecyclerView.ViewHolder{
        TextView session_uid;
        TextView session_duration;
        TextView session_owner;

        public SessionHistoryHolder( View itemView) {
            super(itemView);

            session_uid = itemView.findViewById(R.id.session_uid);
            session_duration =itemView.findViewById(R.id.session_duration);
            session_owner = itemView.findViewById(R.id.session_owner);
        }
    }

}
*/

