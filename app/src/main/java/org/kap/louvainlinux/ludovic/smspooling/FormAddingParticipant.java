package org.kap.louvainlinux.ludovic.smspooling;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FormAddingParticipant extends AppCompatActivity {

    private EditText editText;
    private Button buttonAdd;
    private DBHelper dbHelper;
    private String pool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_adding_participant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_form_participant);
        setSupportActionBar(toolbar);
        setTitle("Add Participant");
        toolbar.setTitleTextColor(getResources().getColor(R.color.cardview_light_background));
        pool = getIntent().getExtras().getString("pool");

        dbHelper = new DBHelper(getApplicationContext());

        editText = (EditText) findViewById(R.id.edit_text_participant_name);

        buttonAdd = (Button) findViewById(R.id.button_add_form_participant);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                if (text.equals("")) Toast.makeText(getApplicationContext(), "Participant name can't be void", Toast.LENGTH_SHORT).show();
                else {
                    //Pool pool = new Pool(text);
                    if (dbHelper.getParticipant(text, pool) == null) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("name",text);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "The participant " + text + " already exist in this pool.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
