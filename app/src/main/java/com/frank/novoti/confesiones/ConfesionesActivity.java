package com.frank.novoti.confesiones;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.frank.novoti.confesiones.R.attr.headerLayout;

public class ConfesionesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NuevaConfesionFragment.OnFragmentInteractionListener,
        ConfesionesFragment.OnFragmentInteractionListener,
        ModerarFragment.OnFragmentInteractionListener,
        CommentFragment.OnFragmentInteractionListener {

    private DatabaseReference mDatabase;
    private DatabaseReference mPostDatabase;
    private List<Pair<String, Post>> postList;
    private int currentIndex;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confesiones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


        Fragment fragment = new ConfesionesFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mPostDatabase = FirebaseDatabase.getInstance().getReference("posts");

        postList = new ArrayList<>();

        getLastPosts();

        RelativeLayout navHeader = (RelativeLayout) findViewById(R.id.navigation_header);
        /*navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent logInIntent = new Intent(ConfesionesActivity.this, LoginActivity.class);
                startActivity(logInIntent);
            }
        });*/

        AccessToken currentPerson = AccessToken.getCurrentAccessToken();
        Bundle params = new Bundle();
        params.putString("fields", "id,email,gender,cover,picture.width(180).height(180)");
        View headerLayout = navigationView.getHeaderView(0);

        if (currentPerson != null) {
            Log.d("lgeuado?", "estoy logueado mierda!");


            new GraphRequest(
                    currentPerson,
                    "me",
                    params,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            Log.d("Response", response.toString());
                            if (response != null) {
                                try {
                                    JSONObject data = response.getJSONObject();
                                    if (data.has("picture")) {
                                        String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                        new ProfilePictureTask(ConfesionesActivity.this, findViewById(android.R.id.content)).execute(profilePicUrl);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e("Exception", "Exception profile picture thrown");
                                }
                            }
                        }
                    }
            ).executeAsync();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.confesiones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            return true;
        } else if (id == R.id.action_comment) {
            Fragment fragment = new CommentFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        } else if (id == R.id.action_favorites) {
            return true;
        } else if (id == R.id.action_report) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Fragment fragment = new ConfesionesFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        } else if (id == R.id.nav_gallery) {
            Fragment fragment = new ModerarFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        } else if (id == R.id.nav_slideshow) {


        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    public void openNuevaConfesionFragment(View view) {
        Fragment fragment = new NuevaConfesionFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    public void getLastPosts() {

        Query lastPosts = mPostDatabase.limitToLast(300).orderByChild("approved").equalTo(false);
        currentIndex = -1;

        lastPosts.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("Child added first", "onChildAdded first:" + dataSnapshot.getKey());
                postList.add(new Pair<String, Post>(dataSnapshot.getKey(),
                        dataSnapshot.getValue(Post.class)));
                currentIndex++;
                // A new comment has been added, add it to the displayed list

                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("Child changed", "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                for (int i = 0; i < postList.size(); i++) {
                    if (dataSnapshot.getKey().equals(postList.get(i).first)) {
                        postList.get(i).second.setLikes(dataSnapshot.getValue(Post.class).getLikes());
                        postList.get(i).second.setDislikes(dataSnapshot.getValue(Post.class).getDislikes());
                        break;
                    }
                }

                updateTextViews();
                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("Child Removed", "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("Child Moved", "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void updateTextViews() {

        TextView postTextView = (TextView) findViewById(R.id.postText);
        TextView sentDateTextView = (TextView) findViewById(R.id.sentDateText);
        TextView ratingTextView = (TextView) findViewById(R.id.ratingTextView);
        TextView votesNumberTextView = (TextView) findViewById(R.id.votesNumberTextView);
        TextView numCommentsTextView = (TextView) findViewById(R.id.numCommentsTextView);


        postTextView.setText(postList.get(currentIndex).second.getText());
        sentDateTextView.setText(sdf.format(postList.get(currentIndex).second.getSentDate()));

        //Setting rating text view
        float percentage;
        float likesNumber = postList.get(currentIndex).second.getLikes();
        float dislikesNumber = postList.get(currentIndex).second.getDislikes();
        if ((likesNumber + dislikesNumber) != 0){
            percentage = likesNumber / (likesNumber + dislikesNumber) * 100;

        }
        else {
            percentage = 0;
        }
        ratingTextView.setText(String.valueOf((int)percentage) + "%");

        //Setting votes number text view
        votesNumberTextView.setText(String.valueOf((int)(likesNumber + dislikesNumber)));
        numCommentsTextView.setText(String.valueOf(postList.get(currentIndex).second.getNumComent()));
    }


    public void nextPost(View view){

        if (currentIndex > 0) {
            currentIndex--;
        }

        updateTextViews();
    }

    public void previousPost(View view){
        if (currentIndex < (postList.size() - 1)) {
            currentIndex++;
        }

        updateTextViews();
    }

    public void likePost(View view){
        String key = postList.get(currentIndex).first;

        Map<String, Object> postValues = postList.get(currentIndex).second.toMap();
        Map<String, Object> updatingLike = new HashMap<>();
        postValues.put("likes", ((int)postValues.get("likes") + 1));
        updatingLike.put("/posts/" + key, postValues);

        mDatabase.updateChildren(updatingLike);
    }

    public void dislikePost(View view){
        String key = postList.get(currentIndex).first;

        Map<String, Object> postValues = postList.get(currentIndex).second.toMap();
        Map<String, Object> updatingLike = new HashMap<>();
        postValues.put("dislikes", ((int)postValues.get("dislikes") + 1));
        updatingLike.put("/posts/" + key, postValues);

        mDatabase.updateChildren(updatingLike);
    }

    private void writeNewPost(Post post) {
        Gson gson = new Gson();
        gson.toJson(post);

        mDatabase.child("posts").push().setValue(post);
    }

    public void postConfession(View view) {
        EditText postTextView = (EditText) findViewById(R.id.confesionText);
        EditText postTags = (EditText) findViewById(R.id.confesionTags);
        Spinner categorySpinner = (Spinner) findViewById(R.id.universidades);

        String thePostText = postTextView.getText().toString();
        String[] tagArray = postTags.getText().toString().split(" ");
        String category = categorySpinner.getSelectedItem().toString();

        Date currentDate = new Date();

        Post newPost = new Post(thePostText, currentDate, Arrays.asList(tagArray), category);
        writeNewPost(newPost);


    }
}
