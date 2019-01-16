package com.example.boulo.unescoremarcablesite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


//Activité d'ouverture de l'application
public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Handler handler = new Handler();
        //délais pour ouvrir le reste de l'application
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                openNewActivity();

            }
        }, 2000);

    }

    //En fonction de si c'est la première fois ou non que l'utilisateur ouvre l'application, on ouvre l'activité principale ou la liste des outils utilisés dans l'application
    private void openNewActivity() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        boolean firstTime = sharedPref.getBoolean("firstTime", true);
        if (!firstTime) {
            Intent mainIntent = new Intent(this, MainActivity.class);
            this.startActivity(mainIntent);
        }else {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("firstTime", false);
            editor.commit();
            Intent mainIntent = new Intent(this, ApplicationInfosActivity.class);
            this.startActivity(mainIntent);
        }

    }
}
