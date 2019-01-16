package com.example.boulo.unescoremarcablesite;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.boulo.unescoremarcablesite.model.RssFeedModel;
import com.example.boulo.unescoremarcablesite.utils.FetchRSS;
import com.example.boulo.unescoremarcablesite.utils.ListFragmentInterface;

import java.util.ArrayList;


public class ListFragment extends Fragment {

    private RecyclerView rcvSiteList;
    private SiteAdapter siteAdapter;
    private Activity main ;
    ArrayList<RssFeedModel> rss ;
    ArrayList<RssFeedModel> rssDisplayed ;
    private ListFragmentInterface mListener;
    private SwipeRefreshLayout mSwipeRefreshLayout ;
    private EditText editText ;
    private boolean isBusy ;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.main  = getActivity() ;
        if (getArguments() != null) {
        }
        isBusy = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final FrameLayout siteList = (FrameLayout) inflater.inflate(R.layout.fragment_list, container, false);
        rcvSiteList = siteList.findViewById(R.id.a_rcv_site_list);
        editText = siteList.findViewById(R.id.research_bar) ;

        //On donne une taille constante au recycler view
        rcvSiteList.setHasFixedSize(true);

        //On définit un linear Layoutmanager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcvSiteList.setLayoutManager(layoutManager);
        rssDisplayed = new ArrayList<>() ;
        if (((MainActivity)main).getRss() !=null) {
            rss = ((MainActivity)main).getRss() ;
            rssDisplayed.addAll(rss) ;
            siteAdapter = new SiteAdapter(rssDisplayed, mListener, (MainActivity) main);
            rcvSiteList.setAdapter(siteAdapter);
        }

        mSwipeRefreshLayout = siteList.findViewById(R.id.swipeRefreshLayout) ;
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                rss = FetchRSS.getInstance(main).getRssList() ;
                updateDisplayedList(editText.getText().toString());
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        //listener pour détecter lorsque l'utilisateur souhaite filtrer la liste
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isBusy) {
                    isBusy = true ;
                    updateDisplayedList(editText.getText().toString());
                }
            }
        });
        return siteList;
    }


    @Override
    public void onResume() {
        //On rafraichit la liste des éléments lorsque l'on sort du onPause (quand on utilise le multitasking ou que l'on tourne le téléphone)
        super.onResume();
        if (((MainActivity)main).getRss() !=null) {
            rss = ((MainActivity)main).getRss() ;
            rssDisplayed.addAll(rss) ;
            if (siteAdapter != null) {
                siteAdapter.notifyDataSetChanged();
            } else {
                siteAdapter = new SiteAdapter(rssDisplayed, mListener, (MainActivity) main);
                rcvSiteList.setAdapter(siteAdapter);}
        }

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListFragmentInterface) {
            mListener = (ListFragmentInterface) context;
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

    //Methode pour acquérir les données lorsque les données rss on finit de charger
    public void updateDataSet(ArrayList<RssFeedModel> rss) {
        if (rss != null) {
            this.rss = rss;
            updateDisplayedList(editText.getText().toString());
        }
    }

    //Remplace l'ancienne liste Rss par la nouvelle en accord avec le filtre crée par la barre de recherche
    private void updateDisplayedList(String search) {

        rssDisplayed.addAll(rss) ;
        if (search != null && !search.equals("")) {
            rssDisplayed.removeAll(rssDisplayed) ;
            for (int i = 0; i < rss.size(); i++) {
                if (rss.get(i).title.toLowerCase().contains(search.toLowerCase())) {
                    rssDisplayed.add(rss.get(i));
                }
            }
        }
        if (siteAdapter != null) {
            siteAdapter.notifyDataSetChanged();
        } else {
            siteAdapter = new SiteAdapter(rssDisplayed, mListener, (MainActivity) main);
            rcvSiteList.setAdapter(siteAdapter);
        }
        isBusy = false ;
    }


}
