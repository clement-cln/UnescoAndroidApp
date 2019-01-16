package com.example.boulo.unescoremarcablesite.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

//Instance to load the database. Singleton
@Database(entities = {PlaceSavedModel.class}, version = 2)
    public abstract class AppDatabase extends RoomDatabase {

        public abstract PlaceSavedDAO getPlaceDao();

        private static AppDatabase INSTANCE;

        public static AppDatabase getAppDatabase(Context context) {
            if (INSTANCE == null) {
                INSTANCE = Room
                        .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "db")
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration() //to migrate the database to one version to another without saving data
                        .build();
            }
            return INSTANCE;
        }

        public static void destroyInstance() {
            INSTANCE = null;
        }
    }

