package org.kap.louvainlinux.ludovic.smspooling;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Ludovic Zipsin on 27/11/16.
 * Mail: ludovic.j.r.zipsin@gmail.com
 * Github: https://github.com/LudoZipsin
 */
public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder> {

    private ArrayList<Participant> participants;
    private Activity activity;

    public ParticipantAdapter(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getItemCount(){
        return participants.size();
    }

    @Override
    public void onBindViewHolder(ParticipantViewHolder participantViewHolder, int i){
        final Participant participant = participants.get(i);
        participantViewHolder.vName.setText(participant.getName());
        participantViewHolder.vNumber.setText(Integer.toString(participant.getNumber()));
        participantViewHolder.vVote.setText(Integer.toString(participant.getVote()));
    }


    @Override
    public ParticipantViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.participant_card, viewGroup, false);
        return new ParticipantViewHolder(itemView);
    }

    public static class ParticipantViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vNumber;
        protected TextView vVote;

        public ParticipantViewHolder(View v){
            super(v);
            vName = (TextView) v.findViewById(R.id.participant_name);
            vNumber = (TextView) v.findViewById(R.id.participant_number);
            vVote = (TextView) v.findViewById(R.id.participant_vote);
        }

    }

}
