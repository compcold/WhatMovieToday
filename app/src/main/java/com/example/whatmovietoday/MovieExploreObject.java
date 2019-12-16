package com.example.whatmovietoday;

import android.widget.ImageButton;

public class MovieExploreObject {

    //UI Variables
    private String movieTitle;
    private String movieDesc;
    private String moviePoster;
    private String movieID;

    public void setDetails(String movieTitle, String movieDesc, String moviePoster, String movieID){
        this.movieDesc=movieDesc;
        this.moviePoster=moviePoster;
        this.movieTitle=movieTitle;
        this.movieID=movieID;
    }

    public String getTitle(){
        return movieTitle;
    }

    public String getDesc(){
        return movieDesc;
    }

    public String getPoster(){
        return moviePoster;
    }

    public String getID(){return movieID;}

}
