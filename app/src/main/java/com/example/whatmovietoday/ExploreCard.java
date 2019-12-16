package com.example.whatmovietoday;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;

import java.util.List;

@Layout(R.layout.explore_card)
public class ExploreCard {

    @View(R.id.imgPoster)
    public ImageView poster;

    @View(R.id.title)
    public TextView title;

    @View(R.id.description)
    public TextView description;

    @View(R.id.btnLike)
    public Button btnLike;

    @View(R.id.btnDislike)
    public Button btnDislike;

    private MovieExploreObject movie;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;
    private MovieDAO dao;


    public ExploreCard (Context mContext, MovieExploreObject movie, SwipePlaceHolderView mSwipeView) {
        this.mContext=mContext;
        this.movie=movie;
        this.mSwipeView=mSwipeView;
    }

    @Resolve
    public void onResolved() {
        if (movie.getDesc().length() > 250) {
            String t = movie.getDesc();
            String n = t.substring(0, 247) + "...";
            description.setText(n);
        } else {
            description.setText(movie.getDesc());
        }

        title.setText(movie.getTitle());
        RequestQueue queue = Volley.newRequestQueue(MainActivity.getContext());
        MovieExploreHelper.imageRequest(poster, queue, movie.getPoster());

        btnLike.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                addFavorite();
                Explore.likeCard();
            }
        });

        btnDislike.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                onSwipedOut();
                Explore.dislikeCard();
            }
        });
    }

    @SwipeOut
    public void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
    }

    @SwipeIn
    public void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
        addFavorite();
    }


    public void addFavorite(){
        User u = MainActivity.getUser();
        int uId = MainActivity.uDao.getId(u.username);

        Movie selectedMovie = new Movie();
        selectedMovie.id = Integer.parseInt(movie.getID());
        selectedMovie.userId = uId;

        dao = MainActivity.mDao;
        List<Movie> tmpTable = dao.getUserSaves(uId);

        Boolean exists = false;
        for (Movie m : tmpTable){
            if (m.id == Integer.parseInt(movie.getID())){
                exists = true;
            }
        }
        if (!exists){dao.insert(selectedMovie);}
    }
}