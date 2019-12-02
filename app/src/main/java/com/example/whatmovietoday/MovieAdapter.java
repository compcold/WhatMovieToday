package com.example.whatmovietoday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

public class MovieAdapter extends PagerAdapter {

    public MovieExploreObject m;
    private Context context;
    private LayoutInflater inflator;
    private ImageView movieImage;
    private TextView txtMovieTitle;
    private TextView txtMovieDesc;
    private RequestQueue queue = Volley.newRequestQueue(MainActivity.getContext());


    public MovieAdapter(Context context, MovieExploreObject m){
        this.context=context;
        this.m=m;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int pos){
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.slide_movie, container, false);

        movieImage = layout.findViewById(R.id.imgMovie);
        txtMovieTitle = layout.findViewById(R.id.txtMovieTitle);
        txtMovieDesc = layout.findViewById(R.id.txtMovieDesc);

        JsonRequest();

        System.out.println("here");

        container.addView(layout);
        return layout;

    }

    public void JsonRequest(){
        String urlJSON = "https://api.themoviedb.org/"+m.getID()+"/?api_key=e7f77194498797fa45d447e0d6e76c90";
        System.out.println(urlJSON);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlJSON, null, response -> {

                    String title ="";
                    String desc = "";
                    String poster = "https://image.tmdb.org/t/p/w400";

                    try {
                        title = response.getString("original_title");
                        desc = response.getString("overview");
                        poster += response.getString("poster_path");
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }

                    txtMovieTitle.setText(title);
                    txtMovieDesc.setText(desc);
                    imageRequest(poster);

                }, error -> System.out.println("That didn't work!"));
        queue.add(jsonObjectRequest);
    }


    public void imageRequest(String s) {
        ImageRequest imageRequest = new ImageRequest(s,
                response -> movieImage.setImageBitmap(response),0, 0,null, null, error -> {
                    //Do nothing
                });
        queue.add(imageRequest);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        super.destroyItem(container,position,object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
