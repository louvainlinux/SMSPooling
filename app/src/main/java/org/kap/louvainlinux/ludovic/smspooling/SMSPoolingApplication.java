package org.kap.louvainlinux.ludovic.smspooling;

import android.app.Application;
import android.location.Location;
import android.util.Log;

/**
 * Created by Ludovic Zipsin on 26/11/16.
 * Mail: ludovic.j.r.zipsin@gmail.com
 * Github: https://github.com/LudoZipsin
 */
public class SMSPoolingApplication extends Application {

    private static final String TAG = "SMS Pooling";
    private static SMSPoolingApplication application;

    private DBHelper dbHelper;


    @Override
    public void onCreate(){
        super.onCreate();
        application = this;

        dbHelper = new DBHelper(this);
        dbHelper.initAPPData();
        Log.e("DBHelper on create", "init done");
        // Test if db
        //testDB(dbHelper);
        // Fin Test
        dbHelper.insertPool("pooling 1", "2016.11.28 23:30");
        dbHelper.insertPool("pooling 2", "2016.11.28 23:30");
        dbHelper.insertPool("pooling 3", "2016.11.28 23:30");
        dbHelper.insertPool("pooling 4", "2016.11.28 23:30");
        dbHelper.insertPool("pooling 6", "2016.11.28 23:30");
        dbHelper.insertParticipant("a","pooling 1");
        dbHelper.insertParticipant("b","pooling 1");
        dbHelper.insertParticipant("c","pooling 1");
        dbHelper.insertParticipant("d","pooling 1");
        dbHelper.insertParticipant("e","pooling 1");
        dbHelper.insertParticipant("f","pooling 1");
        dbHelper.insertParticipant("f","pooling 1");

    }

    private void testDB(DBHelper dbHelper){
        Log.i("Application, testDB", "inserting pool");
        Log.i("Application, testDB", "number of pool pre insert = " + dbHelper.numberOfPool());
        dbHelper.insertPool("Pool 1", "date 1");
        Log.i("Application, testDB", "number of pool post insert 1= " + dbHelper.numberOfPool());
        dbHelper.insertPool("Pool 2", "date 2");
        Log.i("Application, testDB", "number of pool post insert 2= " + dbHelper.numberOfPool());
        Log.i("Application, testDB", "deleting pool");
        Log.i("Application, testDB", "number of pool pre delete = " + dbHelper.numberOfPool());
        dbHelper.deletePool("Pool 1");
        Log.i("Application, testDB", "number of pool post delete 1= " + dbHelper.numberOfPool());
        dbHelper.deletePool("Pool 2");
        Log.i("Application, testDB", "number of pool post delete 2= " + dbHelper.numberOfPool());
        Log.i("Application, testDB", "Current pool = " + dbHelper.getAppPool());
        dbHelper.setAppPool("Pool 1");
        Log.i("Application, testDB", "Current pool = " + dbHelper.getAppPool());
        dbHelper.deactivateAppPool();
        Log.i("Application, testDB", "Current pool = " + dbHelper.getAppPool());
    }

    public static synchronized SMSPoolingApplication getApplicationInstance() {
        return application;
    }

    public DBHelper getDbHelper(){
        return this.dbHelper;
    }

}
