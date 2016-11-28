package org.kap.louvainlinux.ludovic.smspooling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        DBHelper dbHelper = new DBHelper(context);

        // Get the data (SMS data) bound to intent
        Bundle bundle = intent.getExtras();

        SmsMessage[] msgs = null;


        if (bundle != null) {
            // Retrieve the SMS Messages received
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];

            // For every SMS message received
            for (int i=0; i < msgs.length; i++) {
                // Convert Object array
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                // Sender's phone number
                String phone = msgs[i].getOriginatingAddress();
                // Fetch the text message
                String voteMsg = msgs[i].getMessageBody().toString();
                int vote = toINT(voteMsg);
                dbHelper.makeVote(phone, vote, dbHelper.getAppPool());
            }

            // Display the entire SMS Message
        }

    }

    private int toINT(String string) {
        int r = 0;
        int count = 0;
        for (int i=0; i<string.length(); i++ ){
            if (Character.isDigit(string.charAt(i))){
                count = 1;
                r *=10;
                r += Character.getNumericValue(string.charAt(i));
            }
            else {
                if (count != 0) break;
            }
        }
        return r;
    }

}
