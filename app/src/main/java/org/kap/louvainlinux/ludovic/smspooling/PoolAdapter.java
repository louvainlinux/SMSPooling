package org.kap.louvainlinux.ludovic.smspooling;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Ludovic Zipsin on 27/11/16.
 * Mail: ludovic.j.r.zipsin@gmail.com
 * Github: https://github.com/LudoZipsin
 */
public class PoolAdapter extends RecyclerView.Adapter<PoolAdapter.PoolViewHolder> {

    private ArrayList<Pool> pools;
    private Activity activity;

    public PoolAdapter(ArrayList<Pool> pools){
        this.pools = pools;
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    @Override
    public int getItemCount(){
        return pools.size();
    }

    @Override
    public void onBindViewHolder(PoolViewHolder poolViewHolder, int i){
        final Pool pool = pools.get(i);
        poolViewHolder.vName.setText(pool.getName());
        poolViewHolder.vDate.setText(pool.getDate());
        poolViewHolder.vName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity.getBaseContext(), ParticipantList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("pool", pool.getName());
                activity.getBaseContext().startActivity(intent);
            }
        });
    }

    @Override
    public PoolViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pool_card, viewGroup, false);
        return new PoolViewHolder(itemView);
    }

    public static class PoolViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vDate;

        public PoolViewHolder(View v){
            super(v);
            vName = (TextView) v.findViewById(R.id.pool_name);
            vDate = (TextView) v.findViewById(R.id.pool_date);
        }

    }

}
