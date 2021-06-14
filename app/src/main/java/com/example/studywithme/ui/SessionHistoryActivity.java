package com.example.studywithme.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studywithme.Adapters.SessionHistoryAdapter;
import com.example.studywithme.R;
import com.example.studywithme.data.models.Session;
//import com.example.studywithme.ui.viewmodels.OnSessionAdded;
import com.example.studywithme.data.models.User;
import com.example.studywithme.ui.viewmodels.SessionHistoryViewModel;
import com.example.studywithme.utils.Constants;

import java.util.ArrayList;

public class SessionHistoryActivity extends AppCompatActivity  {

    //private static final String TAG ="SessionHistoryActivity";
    private ArrayList<Session> arrayList = new ArrayList<>();
    private SessionHistoryAdapter myAdapter;
    private RecyclerView recyclerView;

    private SessionHistoryViewModel sessionHistoryViewModel;

    private User user;


   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_history);
        recyclerView = findViewById(R.id.recyclerview);

         //user = getUserFromIntent();
         //User.setIdInPreferences(user.getUid(), this);


       sessionHistoryViewModel = new ViewModelProvider(this).get(SessionHistoryViewModel.class);

      // sessionHistoryViewModel.getSessions(User.getIdFromPreferences(this)).observe(this, sessions -> {
        //           myAdapter.notifyDataSetChanged();
       //});

       sessionHistoryViewModel.getSessions(user.getUid()).observe(this, sessions -> {
           myAdapter.notifyDataSetChanged();

        });
       initRecyclerView();
      }


     //private User getUserFromIntent() {
       // return (User) getIntent().getSerializableExtra(Constants.USER);
     //}

    private void initRecyclerView() {
        myAdapter  = new SessionHistoryAdapter(this,sessionHistoryViewModel.getSessions(user.getUid()).getValue());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

    }


}

   /* private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference sessionRf = db.collection("sessions");
    private SessionHistoryAdapter adapter;
    //private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_history);

       setUpRecyclerView();

    }

    private void setUpRecyclerView() {
        Query query = sessionRf.orderBy("owner",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Session> options = new FirestoreRecyclerOptions.Builder<Session>()
                .setQuery(query,Session.class)
                .build();
        adapter = new SessionHistoryAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
*/