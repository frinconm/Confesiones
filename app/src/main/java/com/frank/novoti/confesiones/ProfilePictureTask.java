package com.frank.novoti.confesiones;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Frank on 4/20/2017.
 */

public class ProfilePictureTask extends AsyncTask<String, Void, Bitmap> {

    private View rootView;
    private Context context;

    public ProfilePictureTask(Context context, View view) {
        this.rootView = view;
        this.context = context;
    }

    protected Bitmap doInBackground(String... params) {
        Bitmap profilePic = null;
        try {
            profilePic = BitmapFactory.decodeStream(new URL(params[0]).openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return profilePic;
    }

    protected void onPostExecute(Bitmap result) {
        NavigationView navigationView = (NavigationView) rootView.findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        ImageView loginPicture = (ImageView) headerLayout.findViewById(R.id.loginPicture);
        loginPicture.setImageBitmap(result);
    }
}
