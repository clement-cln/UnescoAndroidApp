package com.example.boulo.unescoremarcablesite;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ApplicationInfosActivity extends AppCompatActivity {

    //Cette activity permet d'afficher les informations sur l'application (lien des données, licenses, explications des données disponibles)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_infos);
        Button retour = findViewById(R.id.retour) ;
        final Context context = this ;
        retour.setOnClickListener(new View.OnClickListener() {

            //Permet de retourner à l'activité principale
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(context, MainActivity.class);
                context.startActivity(mainIntent);
            }
        });
    }
}
