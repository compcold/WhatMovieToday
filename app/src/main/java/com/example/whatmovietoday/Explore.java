package com.example.whatmovietoday;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;


public class Explore extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar toolbar;
    private static SwipePlaceHolderView mSwipeView;
    private Context mContext;
    private int randomMoviePage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Explore");

        mSwipeView = findViewById(R.id.swipeView);
        mContext = getApplicationContext();

        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(5)
                        .setRelativeScale(0.01f));


        System.out.println(MovieExploreHelper.mList.size());
        for (int i = 0; i< MovieExploreHelper.mList.size(); i++){
            System.out.println(MainActivity.randomMovies.get(i).getTitle());
            mSwipeView.addView(new ExploreCard(mContext, MainActivity.randomMovies.get(i), mSwipeView));
        }
    }

    public static void likeCard(){
        mSwipeView.doSwipe(true);
    }

    public static void dislikeCard(){
        mSwipeView.doSwipe(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        MainActivity.newMovieList();
        onBackPressed();
        return true;
    }
}

