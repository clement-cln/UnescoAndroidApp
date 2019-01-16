package com.example.boulo.unescoremarcablesite.database;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

//Repository that use the interface of Room. All the functions are implemented here through an Async Task that trigger a callback DatabaseResult when finished.
public class RoomRepository {

        private static RoomRepository INSTANCE;
        private AppDatabase appDatabase;
        private static DatabaseResult main ;


        private RoomRepository(Context context) {
            appDatabase = AppDatabase.getAppDatabase(context) ;
        }

        public static RoomRepository getRoomRepository(Context context) {
            if (INSTANCE == null) {
                INSTANCE = new RoomRepository(context) ;
            }
            if (context instanceof DatabaseResult)
                main = (DatabaseResult) context ;
            return INSTANCE;
        }

        public static void destroyInstance() {
            INSTANCE = null;
        }

    public void  getSavedPlaces() {
        new DownloadPlacesTask().execute(appDatabase) ;
    }

        private class DownloadPlacesTask extends AsyncTask<AppDatabase, Void, ArrayList<PlaceSavedModel>> {


            @Override
            protected ArrayList<PlaceSavedModel> doInBackground(AppDatabase... roomDatabases) {
                ArrayList<PlaceSavedModel> placeSavedModels  =new ArrayList<>() ;
                placeSavedModels.addAll(appDatabase.getPlaceDao().getAll() );
                return placeSavedModels;
            }


            @Override
            protected void onPostExecute(ArrayList<PlaceSavedModel> places) {
                super.onPostExecute(places);

                main.onSuccessResult("getAll", places);
            }
        }

    public void  insertSavedPlaces(PlaceSavedModel place) {
        new InsertPlacesTask().execute(place) ;
    }

    private class InsertPlacesTask extends AsyncTask<PlaceSavedModel, Void,Boolean> {


        @Override
        protected Boolean doInBackground(PlaceSavedModel... place) {

           appDatabase.getPlaceDao().insert(place) ;
            return true;
        }


        @Override
        protected void onPostExecute(Boolean finish) {
            super.onPostExecute(finish);

            main.onSuccessResult("insert", null);
        }
    }

    public void  deleteSavedPlaces(PlaceSavedModel place) {
        new DeletePlacesTask().execute(place) ;
    }

    private class DeletePlacesTask extends AsyncTask<PlaceSavedModel, Void,Boolean> {


        @Override
        protected Boolean doInBackground(PlaceSavedModel... place) {

            appDatabase.getPlaceDao().delete(place[0]) ;
            return true;
        }


        @Override
        protected void onPostExecute(Boolean finish) {
            super.onPostExecute(finish);

            main.onSuccessResult("delete", null);
        }
    }

    public void  updateSavedPlaces(PlaceSavedModel place) {
        new UpdatePlacesTask().execute(place) ;
    }

    private class UpdatePlacesTask extends AsyncTask<PlaceSavedModel, Void,Boolean> {


        @Override
        protected Boolean doInBackground(PlaceSavedModel... place) {

            appDatabase.getPlaceDao().update(place[0]) ;
            return true;
        }


        @Override
        protected void onPostExecute(Boolean finish) {
            super.onPostExecute(finish);

            main.onSuccessResult("delete", null);
        }
    }
}
