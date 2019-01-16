package com.example.boulo.unescoremarcablesite.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;


//Interface that sum up the different methods of room databasa
@Dao
public interface PlaceSavedDAO {



        @Query("SELECT * FROM place")
        List<PlaceSavedModel> getAll();

        @Insert
        void insert(PlaceSavedModel... models);

        @Delete
        void delete(PlaceSavedModel model);

        @Update
        void update(PlaceSavedModel model) ;

//        @Query("SELECT * FROM book WHERE title LIKE :queryTitle")
//        Book findByTitle(String queryTitle);

}
