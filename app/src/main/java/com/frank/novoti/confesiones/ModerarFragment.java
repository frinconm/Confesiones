package com.frank.novoti.confesiones;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ModerarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ModerarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModerarFragment extends android.app.Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseReference mDatabase;
    private DatabaseReference mPostDatabase;

    private List<Pair<String, Post>> postToModerateList;
    private int currentIndexModerate;

    private TextView postTextView;
    private TextView sentDateTextView;
    private Button nextPostButton;
    private Button likeModerateButton;
    private Button dislikeModerateButton;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private OnFragmentInteractionListener mListener;

    public ModerarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModerarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModerarFragment newInstance(String param1, String param2) {
        ModerarFragment fragment = new ModerarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mPostDatabase = FirebaseDatabase.getInstance().getReference("posts");
        postToModerateList = new ArrayList<>();
        getLastPostsToModerate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moderar, container, false);

        postTextView = (TextView) view.findViewById(R.id.postModerateText);
        sentDateTextView = (TextView) view.findViewById(R.id.sentDateModerateText);
        nextPostButton = (Button) view.findViewById(R.id.nextModerateButton);
        likeModerateButton = (Button) view.findViewById(R.id.likeModerateButton);
        dislikeModerateButton = (Button) view.findViewById(R.id.dislikeModerateButton);

        nextPostButton.setOnClickListener(this);
        likeModerateButton.setOnClickListener(this);
        dislikeModerateButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextModerateButton:
                nextPost(v);
                break;
            case R.id.likeModerateButton:
                likeModeratePost(v);
                break;
            case R.id.dislikeModerateButton:
                dislikeModeratePost(v);
                break;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void getLastPostsToModerate() {

        Query lastPosts = mPostDatabase.limitToLast(300).orderByChild("approved").equalTo(false);
        currentIndexModerate = -1;

        lastPosts.addChildEventListener( new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("Child added first", "onChildAdded first:" + dataSnapshot.getKey());
                postToModerateList.add(new Pair<String, Post>(dataSnapshot.getKey(),
                        dataSnapshot.getValue(Post.class)));
                currentIndexModerate++;
                // A new comment has been added, add it to the displayed list

                // ...
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("Child changed", "onChildChanged:" + dataSnapshot.getKey());

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

        postTextView.setText(postToModerateList.get(currentIndexModerate).second.getText());
        sentDateTextView.setText(sdf.format(postToModerateList.get(currentIndexModerate).second.getSentDate()));

    }

    public void nextPost(View view){

        if (currentIndexModerate > 0) {
            currentIndexModerate--;
        }

        updateTextViews();
    }

    public void likeModeratePost(View view){
        String key = postToModerateList.get(currentIndexModerate).first;

        Map<String, Object> postValues = postToModerateList.get(currentIndexModerate).second.toMap();
        Map<String, Object> updatingLike = new HashMap<>();
        postValues.put("moderationLikes", ((int)postValues.get("moderationLikes") + 1));
        updatingLike.put("/posts/" + key, postValues);

        mDatabase.updateChildren(updatingLike);
    }

    public void dislikeModeratePost(View view){
        String key = postToModerateList.get(currentIndexModerate).first;

        Map<String, Object> postValues = postToModerateList.get(currentIndexModerate).second.toMap();
        Map<String, Object> updatingLike = new HashMap<>();
        postValues.put("moderationDislikes", ((int)postValues.get("moderationDislikes") + 1));
        updatingLike.put("/posts/" + key, postValues);

        mDatabase.updateChildren(updatingLike);
    }
}
