package com.frank.novoti.confesiones;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    void postConfession() {
        EditText postView = (EditText) findViewById(R.id.editText2);
        String thePostText = (String) postView.getText().toString();

        //Hola Renato
    }
}
