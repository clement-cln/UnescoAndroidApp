package com.example.boulo.unescoremarcablesite;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boulo.unescoremarcablesite.database.PlaceSavedModel;
import com.example.boulo.unescoremarcablesite.database.RoomRepository;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class SiteDetailsActivity extends AppCompatActivity {

    @BindView(R.id.a_title)
    TextView title;
    @BindView(R.id.a_description)
    TextView description;
    @BindView(R.id.a_image_src)
    ImageView imageSrc;
    @BindView(R.id.a_visiter)
    Button toVisit ;
    @BindView(R.id.visite)
    Button visited ;
    @BindView(R.id.favori)
    CheckBox favorite ;
    private int status = 0 ;
    private int favori = 0 ;
    private Bundle siteBundle ;

    //Détails des différents lieux

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_details);
        ButterKnife.bind(this) ;

        //Mise en place des éléments visuels
        description.setMovementMethod(new ScrollingMovementMethod());
        siteBundle = getIntent().getBundleExtra("siteBundleKey");
        title.setText(siteBundle.getString("title"));
        description.setText(siteBundle.getString("description"));
        Picasso.get().load(siteBundle.getString("imageSrc")).resize(120,120).centerCrop().into(imageSrc, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap imageBitmap = ((BitmapDrawable) imageSrc.getDrawable()).getBitmap();
                RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                imageDrawable.setCircular(true);
                imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 8.0f);
                imageSrc.setImageDrawable(imageDrawable);
            }

            @Override
            public void onError(Exception e) {
                imageSrc.setImageResource(R.drawable.baseline_help_outline_black_48dp);
            }
        });
        status = siteBundle.getInt("status");
        favori = siteBundle.getInt("favori") ;

        //En fonction du status du lieu dans room
        if (status== 1){
            toVisit.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else if (status== 2){
            visited.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        if(favori == 1) {
            favorite.setChecked(true);
        }
        //Principe d'un radio group pour les deux boutons du bas de la page, changement du status
        toVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == 0) {
                    status = 1 ;
                    toVisit.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else if (status == 1) {
                    status = 0 ;
                    toVisit.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    status = 1 ;
                    toVisit.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    visited.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        visited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == 0) {
                    status = 2 ;
                    visited.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else if (status == 1) {
                    status = 2 ;
                    toVisit.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    visited.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                } else {
                    status = 0 ;
                    visited.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });
        //Enregistrement du lieu en favori
        favorite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (favori == 0) {
                    favori = 1 ;
                    favorite.setChecked(true);
                } else {
                    favori = 0 ;
                    favorite.setChecked(false);
                }
            }
        });

    }

    //Création du menu pour en savoir plus le site
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.plus).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                openWebSite() ;
                return false;
            }
        }) ;
        return super.onCreateOptionsMenu(menu);
    }

    //intent vers le site de description du lieu
    private void openWebSite() {
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(siteBundle.getString("link", "https://whc.unesco.org/fr/list/")));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //Quand l'activité passe en pause, si besoin le lieu est sauvegardé dans room
    @Override
    protected void onPause() {
        super.onPause();
        RoomRepository room = RoomRepository.getRoomRepository(this);
        if (favori != 0 || status != 0) {
            if (siteBundle.getLong("id") == -1) {
                room.insertSavedPlaces(new PlaceSavedModel(siteBundle.getString("title"), siteBundle.getString("description"), siteBundle.getFloat("lat"), siteBundle.getFloat("lond"), siteBundle.getString("imageSrc"), status, favori));
            } else {
                room.updateSavedPlaces(new PlaceSavedModel(siteBundle.getLong("id"), siteBundle.getString("title"), siteBundle.getString("description"), siteBundle.getFloat("lat"), siteBundle.getFloat("lond"), siteBundle.getString("imageSrc"), status, favori));
            }
        }else if (siteBundle.getLong("id")!= -1) {
                room.deleteSavedPlaces(new PlaceSavedModel(siteBundle.getLong("id") , siteBundle.getString("title"), siteBundle.getString("description"), siteBundle.getFloat("lat"), siteBundle.getFloat("lond"), siteBundle.getString("imageSrc"), status, favori));
            }
    }
}
