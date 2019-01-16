package com.example.boulo.unescoremarcablesite;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boulo.unescoremarcablesite.database.PlaceSavedModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
//Sectionned recycler view pour le fragment MyDestinationFragment
public class SectionnedRecyclerView extends StatelessSection {

    String title;
    ArrayList<PlaceSavedModel> list;
    MainActivity main ;

    public SectionnedRecyclerView(String title, ArrayList<PlaceSavedModel> list, MainActivity main) {
        // call constructor with layout resources for this Section header and items
        super(SectionParameters.builder()
                .itemResourceId(R.layout.row_sites)
                .headerResourceId(R.layout.item_header)
                .build());

        this.title = title ;
        this.list = list ;
        this.main = main ;
    }

    @Override
    public int getContentItemsTotal() {
        return list.size(); // number of items of this section
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        // return a custom instance of ViewHolder for the items of this section
        return new MyItemViewHolder(view);
    }

    //Titre des sections
    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

        headerHolder.tvTitle.setText(title);
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;

        HeaderViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.header);
        }
    }


    //gestion des items à l'intérieur des sections
    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyItemViewHolder itemHolder = (MyItemViewHolder) holder;

        // bind your view here
        itemHolder.tvItem.setText(list.get(position).getTitle());
        Picasso.get().load(list.get(position).getImage()).into(itemHolder.imageView, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap imageBitmap = ((BitmapDrawable) itemHolder.imageView.getDrawable()).getBitmap();
                RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(main.getResources(), imageBitmap);
                imageDrawable.setCircular(true);
                imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
                itemHolder.imageView.setImageDrawable(imageDrawable);
            }

            @Override
            public void onError(Exception e) {
                itemHolder.imageView.setImageResource(R.drawable.baseline_help_outline_black_48dp);
            }
        });
        itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.showSiteDetails(list.get(position).getTitle());
            }
        });
    }

    class MyItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvItem;
        private  final ImageView imageView ;
        private final View rootView ;

        public MyItemViewHolder(View itemView) {
            super(itemView);
            tvItem = (TextView) itemView.findViewById(R.id.r_site_name);
            imageView = (ImageView) itemView.findViewById(R.id.r_site_pic) ;
            rootView = itemView ;
        }
    }



}


