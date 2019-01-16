package com.example.boulo.unescoremarcablesite;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.boulo.unescoremarcablesite.database.PlaceSavedModel;
import com.example.boulo.unescoremarcablesite.database.RoomRepository;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.Section;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

//Liste des lieux sauvegardés dans room
public class MyDestinationFragment extends Fragment {


    MainActivity main ;
    ArrayList<PlaceSavedModel> places ;
    ArrayList<PlaceSavedModel> favori = new ArrayList<>();
    ArrayList<PlaceSavedModel> toVisit = new ArrayList<>() ;
    ArrayList<PlaceSavedModel> visited = new ArrayList<>();
    SectionedRecyclerViewAdapter sectionAdapter ;
    SwipeRefreshLayout mSwipeRefreshLayout ;

    public MyDestinationFragment() {
        // Required empty public constructor
    }


    public static MyDestinationFragment newInstance() {
        MyDestinationFragment fragment = new MyDestinationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main = (MainActivity) getActivity() ;
        places = main.getPlaceSaved() ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_my_destination, container, false);
        sectionAdapter = new SectionedRecyclerViewAdapter();

// Add your Sections

        for (int i = 0 ; i< places.size(); i++) {
            if (places.get(i).getStatus() == 1){
                toVisit.add(places.get(i)) ;
            }
            if (places.get(i).getStatus() == 2) {
                visited.add(places.get(i)) ;
            }
            if (places.get(i).getFavori() == 1) {
                favori.add(places.get(i)) ;
            }
        }

        sectionAdapter.addSection(new SectionnedRecyclerView("Favoris", favori, main));
        sectionAdapter.addSection(new SectionnedRecyclerView("A visiter", toVisit,main)) ;
        sectionAdapter.addSection(new SectionnedRecyclerView("Visité", visited,main));


// Set up your RecyclerView with the SectionedRecyclerViewAdapter
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sectionAdapter);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout) ;
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
        return view ;
    }


    public void refreshItems() {
        RoomRepository.getRoomRepository(main).getSavedPlaces() ;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void updateSection(ArrayList<PlaceSavedModel> places) {
        toVisit.removeAll(toVisit);
        visited.removeAll(visited) ;
        favori.removeAll(favori) ;
        for (int i = 0 ; i< places.size(); i++) {
            if (places.get(i).getStatus() == 1){
                toVisit.add(places.get(i)) ;
            }
            if (places.get(i).getStatus() == 2) {
                visited.add(places.get(i)) ;
            }
            if (places.get(i).getFavori() == 1) {
                favori.add(places.get(i)) ;
            }
        }
        sectionAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);

    }


}
