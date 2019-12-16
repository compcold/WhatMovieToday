package com.example.whatmovietoday;


import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Random;



public class MovieExploreHelper {

    public static ArrayList<MovieExploreObject> mList;


    private static void getDetails(String pageNumber){
        mList = new ArrayList<MovieExploreObject>();
        RequestQueue queue = MainActivity.getExplore();

        String urlJSON = "https://api.themoviedb.org/3/discover/movie?api_key=e7f77194498797fa45d447e0d6e76c90&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page="+pageNumber;
        //String urlJSON = "https://api.themoviedb.org/3/movie/"+movieID+"?api_key=e7f77194498797fa45d447e0d6e76c90";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlJSON, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("results");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);

                        String title =o.getString("original_title");
                        String desc = o.getString("overview");
                        String poster = "https://image.tmdb.org/t/p/w300" + o.getString("poster_path");
                        String id = o.getString("id");

                        MovieExploreObject m = new MovieExploreObject();
                        m.setDetails(title,desc,poster,id);
                        mList.add(m);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> System.out.println("fail"));
        queue.add(stringRequest);
    }


    public static void imageRequest(final ImageView i, RequestQueue q, String path) {
        ImageRequest imageRequest = new ImageRequest(path,
                response -> i.setImageBitmap(response),0, 0,null, null, error -> {
                    error.printStackTrace();
                });
        q.add(imageRequest);
    }


    public static void newRandomMovies(){
        int randomMoviePage = new Random().nextInt((499 - 1) + 1) + 1;
        ArrayList<MovieExploreObject> list = new ArrayList<MovieExploreObject>();
        getDetails(Integer.toString(randomMoviePage));
    }
}
