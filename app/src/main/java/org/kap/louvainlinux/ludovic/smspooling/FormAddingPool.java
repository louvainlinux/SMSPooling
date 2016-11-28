package org.kap.louvainlinux.ludovic.smspooling;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FormAddingPool extends AppCompatActivity {

    private EditText editText;
    private Button buttonAdd;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_adding_pool);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_form_pool);
        setSupportActionBar(toolbar);
        setTitle("Add Pool");
        toolbar.setTitleTextColor(getResources().getColor(R.color.cardview_light_background));

        dbHelper = new DBHelper(getApplicationContext());

        editText = (EditText) findViewById(R.id.edit_text_pool_name);

        buttonAdd = (Button) findViewById(R.id.button_add_form);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editText.getText().toString();
                if (text.equals("")) Toast.makeText(getApplicationContext(), "Pool name can't be void", Toast.LENGTH_SHORT).show();
                else {
                    Pool pool = new Pool(text);
                    if (dbHelper.getPool(pool.getName()) == null) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("pool",text);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "The pool " + text + " already exist.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
