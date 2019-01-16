package com.example.boulo.unescoremarcablesite;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boulo.unescoremarcablesite.utils.ListFragmentInterface;


//Class pour créer les items du recycler view et implémenter une méthode onClick
public class SiteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    //List of data displayed in one row of the List
    public TextView siteName;
    public ImageView sitePicture;
    private ListFragmentInterface _mlistener;



    public SiteViewHolder(@NonNull View rootView, ListFragmentInterface mlistener) {
        super(rootView);
        this._mlistener = mlistener;
        this.siteName = rootView.findViewById(R.id.r_site_name);
        this.sitePicture = rootView.findViewById(R.id.r_site_pic);
        rootView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        _mlistener.showSiteDetails(siteName.getText().toString());
    }
}
