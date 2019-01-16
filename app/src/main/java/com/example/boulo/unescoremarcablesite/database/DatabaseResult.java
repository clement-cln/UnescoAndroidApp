package com.example.boulo.unescoremarcablesite.database;

import java.util.ArrayList;

//Interface called when room finish a task
public interface DatabaseResult {

    public void onSuccessResult(String type ,ArrayList <PlaceSavedModel> places) ;
}
