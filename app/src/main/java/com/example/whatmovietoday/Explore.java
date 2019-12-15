package com.example.whatmovietoday;

import android.os.Bundle;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


public class Explore extends AppCompatActivity {

    private ViewPager viewMovie;
    private MovieAdapter adapter;
    private androidx.appcompat.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        viewMovie = findViewById(R.id.viewMovie);
        MovieExploreObject m = new MovieExploreObject("3");
        adapter = new MovieAdapter(this,m);
        viewMovie.setAdapter(adapter);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Explore");
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}