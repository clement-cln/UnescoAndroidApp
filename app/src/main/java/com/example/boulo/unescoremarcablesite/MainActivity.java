package com.example.boulo.unescoremarcablesite;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.boulo.unescoremarcablesite.database.DatabaseResult;
import com.example.boulo.unescoremarcablesite.database.RoomRepository;
import com.example.boulo.unescoremarcablesite.database.PlaceSavedModel;
import com.example.boulo.unescoremarcablesite.model.RssFeedModel;
import com.example.boulo.unescoremarcablesite.utils.ListFragmentInterface;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements DatabaseResult, ListFragmentInterface {

    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private static ArrayList<RssFeedModel> rss ;
    private static ArrayList<PlaceSavedModel> placeSaved ;
    private ListFragment listFragment ;
    private MapsFragment mapsFragment ;
    private MyDestinationFragment myDestinationFragment ;
    private FloatingActionButton infoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        RoomRepository roomRepository = RoomRepository.getRoomRepository(this) ;
        roomRepository.getSavedPlaces();
        infoButton = findViewById(R.id.a_info_button);

        infoButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent infoIntent = new Intent(getApplication(), ApplicationInfosActivity.class);
                  getApplication().startActivity(infoIntent);
              }


        });

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {



                if (i == 1 &&listFragment!=null) {

                   listFragment.updateDataSet(rss);
                }
            }

            @Override
            public void onPageSelected(int i) {
                if (listFragment!=null) {
                    listFragment.updateDataSet(rss);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    //detect whereas or not the phone is connected in wifi or gms way
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;

    }

    //go to the previews fragment
    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    //Room callback
    @Override
    public void onSuccessResult(String type, ArrayList<PlaceSavedModel> places) {
        if (type.equalsIgnoreCase("getAll")) {
            this.placeSaved = places ;
            if (myDestinationFragment !=null) {
                myDestinationFragment.updateSection(places);
            }
        } else {
            RoomRepository.getRoomRepository(this).getSavedPlaces();
        }

    }

    //geter des listes room et rss
    public ArrayList<RssFeedModel> getRss() {
        return rss ;
    }

    public static ArrayList<PlaceSavedModel> getPlaceSaved() {
        return placeSaved;
    }

    //méthode appelée par les trois fragments quand l'utilisateur clique sur un élément
    @Override
    public void showSiteDetails(String siteName) {
        Bundle siteBundle = new Bundle();

        //si l'utilisateur est connecté on peut accéder aux données rss
        if (isConnected()) {
            RssFeedModel siteToDisplay = null;
            int placeStatus = 0 ;
            int placeFavori = 0 ;
            Long id = Long.valueOf(-1);

            //On recherche le site cliqué
            for(RssFeedModel rssFeedComparison : rss){
                if (rssFeedComparison.getTitle().equals(siteName)){
                    siteToDisplay = rssFeedComparison;
                }
                else{

                }
            }

            for (PlaceSavedModel placeSavedModel : placeSaved) {
                if(placeSavedModel.getTitle().equals(siteName)) {
                    placeStatus = placeSavedModel.getStatus() ;
                    placeFavori = placeSavedModel.getFavori() ;
                    id = placeSavedModel.getId() ;
                }
            }

            //Création du Bundle avec les données à afficher dans l'Activité

            siteBundle.putString("title", siteToDisplay.getTitle());
            siteBundle.putString("link", siteToDisplay.getLink());
            siteBundle.putString("description", siteToDisplay.getDescription());
            siteBundle.putString("imageSrc", siteToDisplay.getImageSrc());
            siteBundle.putFloat("lat", siteToDisplay.latitude);
            siteBundle.putFloat("long", siteToDisplay.longitude) ;
            siteBundle.putInt("status", placeStatus) ;
            siteBundle.putInt("favori" , placeFavori) ;
            siteBundle.putLong("id" , id);
            Intent siteDetailsIntent = new Intent(this, SiteDetailsActivity.class);
            siteDetailsIntent.putExtra("siteBundleKey", siteBundle);
            this.startActivity(siteDetailsIntent);
        }
        //Si l'utilisateur n'est pas connecté, alors l'activity de détails s'ouvre seulement si le lieu est enregistré dans room
        else {
            PlaceSavedModel siteToDisplay = null;

            for (PlaceSavedModel placeSavedModel : placeSaved) {
                if(placeSavedModel.getTitle().equals(siteName)) {
                    siteToDisplay = placeSavedModel ;

                }
            }

            //Création du Bundle avec les données à afficher dans l'Activité
            if (siteToDisplay != null) {
                siteBundle.putString("title", siteToDisplay.getTitle());
                siteBundle.putString("description", siteToDisplay.getDescription());
                siteBundle.putString("imageSrc", siteToDisplay.getImage());
                siteBundle.putFloat("lat", siteToDisplay.getLat());
                siteBundle.putFloat("long", siteToDisplay.getLongitude()) ;
                siteBundle.putInt("status", siteToDisplay.getStatus()) ;
                siteBundle.putInt("favori" , siteToDisplay.getFavori()) ;
                siteBundle.putLong("id" , siteToDisplay.getId());
                Intent siteDetailsIntent = new Intent(this, SiteDetailsActivity.class);
                siteDetailsIntent.putExtra("siteBundleKey", siteBundle);
                this.startActivity(siteDetailsIntent);
            }
        }
    }

    //View pager qui permet de sauvegarder les instances des divers fragments
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0 ) {
                mapsFragment = MapsFragment.newInstance() ;
                return mapsFragment ;
            } else  if (position == 1) {
                listFragment = ListFragment.newInstance() ;
                return listFragment ;
            } else  {
                myDestinationFragment = MyDestinationFragment.newInstance() ;
                return myDestinationFragment ;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    //callback de
    public void addMarkerandUpdateList(ArrayList<RssFeedModel> rss) {
        this.rss = rss ;
        mapsFragment.addMarker(rss);
        listFragment.updateDataSet(rss) ;
    }
}
