package com.example.boulo.unescoremarcablesite.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.example.boulo.unescoremarcablesite.MainActivity;
import com.example.boulo.unescoremarcablesite.model.RssFeedModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

//class to get the information of the rss file
public class FetchRSS {
    //
    private static String urlLinkStat = "http://whc.unesco.org/en/list/georss/?fbclid=IwAR2z61L-tPPsHeMOMSVg4Qo41eOVIxWZcgtBsrwdrey8jpvTTh4w-5Pfjpw";
    //list of fetched data
    private List<RssFeedModel> mFeedModelList ;
    private static FetchRSS instance ;
    private Activity main ;

    private FetchRSS(Activity main) {
       new FetchFeedTask().execute();
       this.main = main ;
    }

    //singleton
    public static FetchRSS getInstance(Activity main) {

        if (instance == null ) {
            instance = new FetchRSS(main) ;
        }
        return instance ;
    }

    // to get the data from everywhere in the app
    public ArrayList<RssFeedModel> getRssList() {
        ArrayList<RssFeedModel> rss = new ArrayList<>() ;
        if (mFeedModelList !=null)
        rss.addAll(mFeedModelList) ;
        return rss ;
    }

    //Async task to fetch the data
    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        private String urlLink;




        @Override
        protected void onPreExecute() {
            urlLink = urlLinkStat;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            //to access the website
            try {
                URL url = new URL(urlLink);
                //method to obtain the flux
                InputStream inputStream = url.openConnection().getInputStream();
                //call of parseFeed function to do the treatment of the flux
                mFeedModelList = parseFeed(inputStream);
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            ArrayList<RssFeedModel> rss = new ArrayList<>() ;
            if (mFeedModelList != null)
                rss.addAll(mFeedModelList) ;
            if (main instanceof MainActivity){
                //callback to main to update the list and the markers of the map
                ((MainActivity)main).addMarkerandUpdateList(rss) ;
            }

        }
    }

    public List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException,
            IOException {

        //init of the different parameters
        String title = null;
        String link = null;
        String description = null;
        String latitude = null ;
        String longitude = null ;
        boolean isItem = false;

        //init of the list
        List<RssFeedModel> items = new ArrayList<>();

        try {
            //transform the stream into a xml file
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);
            //move from an opening bracket to another
            xmlPullParser.nextTag();
            //go to the next tag until the end of the file
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                //get the name of the beacon
                int eventType = xmlPullParser.getEventType();
                String name = xmlPullParser.getName();

                if(name == null)
                    continue;
                //reset for the next item
                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }
                //test if the opening tag is an item
                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                //Log.d("MyXmlParser", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }
                //according to the beacon, save the corresponding attribute
                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                } else if (name.equalsIgnoreCase("geo:lat")){
                    latitude = result ;
                } else if (name.equalsIgnoreCase("geo:long")) {
                    longitude = result ;
                }
                // if the item is complete, the place is saved within the list
                if (title != null && link != null && description != null && latitude!=null && longitude!=null) {
                    if(isItem) {
                        RssFeedModel item = new RssFeedModel(title, link, description, latitude, longitude);
                        items.add(item);
                    }
                    //reset for the next item
                    title = null;
                    link = null;
                    description = null;
                    latitude = null ;
                    longitude = null ;
                    isItem = false;
                }
            }

            return items;
        } finally {
            //close of the stream connection
            inputStream.close();
        }
    }
}
