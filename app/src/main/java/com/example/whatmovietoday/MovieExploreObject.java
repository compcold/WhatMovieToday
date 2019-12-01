package com.example.whatmovietoday;

import android.widget.ImageButton;

public class MovieExploreObject {

    //Api Variables
    private String movieID;
    private String imageID;
    private String descID;

    //UI Variables
    private String movieTitle;
    private ImageButton btnAccept;
    private ImageButton btnDecline;
    private String movieDesc;
    private String movieYear;
    private String movieActors;


    MovieExploreObject (String movieID){
        this.movieID=movieID;
    }


}
