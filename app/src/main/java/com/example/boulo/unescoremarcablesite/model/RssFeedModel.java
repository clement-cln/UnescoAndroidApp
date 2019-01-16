package com.example.boulo.unescoremarcablesite.model;

//Model to save the data obtained through the online rss file
public class RssFeedModel {
    public String getTitle() {
        return title;
    }

    public String title;

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String link;
    public String description;
    public float latitude ;
    public  float longitude ;

    public String getImageSrc() {
        return imageSrc;
    }

    public String imageSrc ;

    public RssFeedModel(String title, String link, String description, String latitude , String longitude) {
        this.title = title;
        this.link = link;
        this.imageSrc = description.substring(description.indexOf("src=")+5, description.indexOf("target")-2);
        this.description = description.substring(description.indexOf("style='padding:5px'>")+20, description.indexOf("</p>"));
        this.latitude = Float.parseFloat(latitude) ;
        this.longitude = Float.parseFloat(longitude) ;
    }
}
