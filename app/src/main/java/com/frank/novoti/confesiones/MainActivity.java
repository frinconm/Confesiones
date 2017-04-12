package com.frank.novoti.confesiones;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        updateListView();
    }

    private void writeNewPost(Post post) {
        Gson gson = new Gson();
        gson.toJson(post);

        mDatabase.child("posts").push().setValue(post);
    }

    public void postConfession(View view) {
        EditText postView = (EditText) findViewById(R.id.editText2);
        String thePostText = postView.getText().toString();
        List<String> tagList = new ArrayList<>();

        Date currentDate = new Date();

        Post newPost = new Post(thePostText, currentDate, tagList);
        writeNewPost(newPost);


        //Hola Renato
    }

    private void updateListView(){
        Query myTopPostsQuery = mDatabase.child("posts");

        final List<String> itemsListView = new ArrayList<>();
/*
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    itemsListView.add(postSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG?", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });*/

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, itemsListView);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    public void testingActivity(View view) {
        Intent intent = new Intent(this, Confesiones.class);
        startActivity(intent);
    }
}
