package com.example.whatmovietoday;

import android.app.Activity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;

public class Explore extends Activity {

    private ViewPager viewMovie;
    private MovieAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        viewMovie = findViewById(R.id.viewMovie);
        MovieExploreObject m = new MovieExploreObject("3");
        adapter = new MovieAdapter(this,m);
        viewMovie.setAdapter(adapter);

    }
}