package org.kap.louvainlinux.ludovic.smspooling;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;

public class PoolGrid extends AppCompatActivity {

    private final static int REQUEST_RESULT = 1;

    private DBHelper dbHelper;
    private SMSPoolingApplication application;
    private FloatingActionButton fab;

    private ArrayList<Pool> pools;
    private PoolAdapter poolAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESULT) {
            if(resultCode == Activity.RESULT_OK){
                //Pool pool = new Pool(data.getStringExtra("result"));
                Pool pool = new Pool(data.getExtras().getString("pool"));
                dbHelper.insertPool(pool.getName(), pool.getDate());
                pools.add(pool);
                Collections.sort(pools);
                poolAdapter.notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Nothing to do
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = SMSPoolingApplication.getApplicationInstance();
        setContentView(R.layout.activity_pool_grid);
        //this.dbHelper = application.getDbHelper();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pool_list);
        setSupportActionBar(toolbar);
        setTitle("Pools");
        toolbar.setTitleTextColor(getResources().getColor(R.color.cardview_light_background));

        //setContentView(R.layout.activity_pool_grid);
        recyclerView = (RecyclerView) findViewById(R.id.cardList_pool);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        this.dbHelper = new DBHelper(getApplicationContext());
        pools = this.dbHelper.getPools();
        Collections.sort(pools);

        poolAdapter = new PoolAdapter(pools);
        poolAdapter.setActivity((Activity) this);

        recyclerView.setAdapter(poolAdapter);

        fab = (FloatingActionButton) findViewById(R.id.pool_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FormAddingPool.class);
                startActivityForResult(intent, REQUEST_RESULT);
            }
        });

    }
}
