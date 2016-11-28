package org.kap.louvainlinux.ludovic.smspooling;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class ParticipantList extends AppCompatActivity {

    private final static int REQUEST_RESULT_PARTICIPANT = 2;
    private final static String NONE = "";

    private DBHelper dbHelper;
    private SMSPoolingApplication application;
    private FloatingActionButton fab;

    private ArrayList<Participant> participants;
    private ParticipantAdapter participantAdapter;
    private RecyclerView recyclerView;

    private String pool;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_RESULT_PARTICIPANT) {
            if(resultCode == Activity.RESULT_OK){
                //Pool pool = new Pool(data.getStringExtra("result"));
                dbHelper.insertParticipant(data.getExtras().getString("name"), pool);
                Participant participant = dbHelper.getParticipant(data.getExtras().getString("name"), pool);
                participants.add(participant);
                Collections.sort(participants);
                participantAdapter.notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Nothing to do
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.participant_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_set_current_pool:
                if (dbHelper.getAppPool() == null) {
                    dbHelper.setAppPool(pool);
                    Toast.makeText(getApplicationContext(), "Pool " + pool + " enabled", Toast.LENGTH_SHORT).show();
                }
                else {
                    dbHelper.deactivateAppPool();
                    Toast.makeText(getApplicationContext(), "Pool disabled", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_attribute_number:
                int number = 0;
                for (Participant participant : participants){
                    number++;
                    participant.setNumber(number);
                    dbHelper.setParticipantNumber(number, participant.getName(), participant.getPool());
                }
                participantAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = SMSPoolingApplication.getApplicationInstance();
        pool = getIntent().getExtras().getString("pool");
        setContentView(R.layout.activity_participant_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_participant_list);
        setSupportActionBar(toolbar);
        setTitle("Participants");
        toolbar.setTitleTextColor(getResources().getColor(R.color.cardview_light_background));
        //Toast.makeText(getApplicationContext(), getIntent().getExtras().getString("pool"), Toast.LENGTH_LONG).show();

        recyclerView = (RecyclerView) findViewById(R.id.cardList_participant);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        this.dbHelper = new DBHelper(getApplicationContext());
        participants = this.dbHelper.getParticipants(pool);
        Log.e("current", Integer.toString(participants.size()));
        Collections.sort(participants);

        participantAdapter = new ParticipantAdapter(participants);
        participantAdapter.setActivity((Activity) this);

        recyclerView.setAdapter(participantAdapter);

        fab = (FloatingActionButton) findViewById(R.id.participant_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FormAddingParticipant.class);
                intent.putExtra("pool", pool);
                startActivityForResult(intent, REQUEST_RESULT_PARTICIPANT);
            }
        });

    }

}
