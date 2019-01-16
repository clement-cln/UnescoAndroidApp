package com.example.boulo.unescoremarcablesite;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.boulo.unescoremarcablesite.model.RssFeedModel;
import com.example.boulo.unescoremarcablesite.utils.ListFragmentInterface;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


//Recycler view pour le ListFragment On ne le crée que lorsque la liste rss n'est pas vide
public class SiteAdapter extends RecyclerView.Adapter<SiteViewHolder> {
    private ArrayList<RssFeedModel> _listFeed;
    private ListFragmentInterface _mlistener;
    private MainActivity main ;

    SiteAdapter(ArrayList<RssFeedModel> listFeed, ListFragmentInterface mlistener, MainActivity main){
        this._listFeed = listFeed;
        this._mlistener = mlistener;
        this.main = main ;
    }

    @NonNull
    @Override
    public SiteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sites, parent, false);
        return new SiteViewHolder(rootview, _mlistener);
    }

    @Override
    public void onBindViewHolder(@NonNull SiteViewHolder siteViewHolder, int position) {
        RssFeedModel siteToDisplay = this._listFeed.get(position);


        siteViewHolder.siteName.setText(siteToDisplay.getTitle());
        Picasso.get()
                .load(siteToDisplay.getImageSrc())
                .resize(80,80)
                .centerCrop()
                .into(siteViewHolder.sitePicture, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap imageBitmap = ((BitmapDrawable) siteViewHolder.sitePicture.getDrawable()).getBitmap();
                        RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(main.getResources(), imageBitmap);
                        imageDrawable.setCircular(true);
                        imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                        siteViewHolder.sitePicture.setImageDrawable(imageDrawable);
                    }

                    @Override
                    public void onError(Exception e) {
                        siteViewHolder.sitePicture.setImageResource(R.drawable.baseline_help_outline_black_48dp);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return this._listFeed.size();
    }

}
