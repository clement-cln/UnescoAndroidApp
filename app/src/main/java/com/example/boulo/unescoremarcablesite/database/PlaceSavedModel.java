package com.example.boulo.unescoremarcablesite.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

//Entity that store a place saved by the user
@Entity(tableName = "place")
public class PlaceSavedModel {

        // id is automatically generated by room when a new entity is created
        @PrimaryKey(autoGenerate = true)
        private Long id;
        private String title;
        private String description;
        private float  lat;
        private float longitude ;
        //URL of the picture
        private String image;
        private int status ; // 0 none, 1 to visit, 2 visited
        private int favori ; //0 none, 1 favorite

        PlaceSavedModel() {
            // Les classes à persister doivent avoir un constructeur sans paramètre
        }

        @Ignore
        public PlaceSavedModel(String title, String description, float lat, float longitude, String image, int status, int favori) {
            this.title = title ;
            this.description = description ;
            this.lat = lat ;
            this.longitude = longitude ;
            this.image = image ;
            this.status =status ;
            this.favori = favori ;
        }

    @Ignore
    public PlaceSavedModel(Long id, String title, String description, float lat, float longitude, String image, int status, int favori) {
        this.id = id ;
        this.title = title ;
        this.description = description ;
        this.lat = lat ;
        this.longitude = longitude ;
        this.image = image ;
        this.status =status ;
        this.favori = favori ;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public float getLat() {
        return lat;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getTitle() {
        return title;
    }

    public int getFavori() {
        return favori;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFavori(int favori) {
        this.favori = favori;
    }
}

