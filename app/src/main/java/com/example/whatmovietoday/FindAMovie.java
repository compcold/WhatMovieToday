package com.example.whatmovietoday;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class FindAMovie extends AppCompatActivity {

    private User u = MainActivity.getUser();
    private List<String> genres;
    private androidx.appcompat.widget.Toolbar toolbar;
    private String genreID;
    private ImageView imgPoster;
    private TextView desc;
    private TextView title;
    private ProgressDialog progressDialog;
    private String posterURL;
    private String webURL;
    private String movieID;
    RequestQueue queue = MainActivity.getFavorite();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your movie is...");

        getFavGenres();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Movie!");
        progressDialog.show();

        (new Handler()).postDelayed(this::getMovie, 5000);


        imgPoster = findViewById(R.id.imgPoster);
        desc = findViewById(R.id.txtDesc);
        title = findViewById(R.id.txtTitle);
    }

    private void getFavGenres(){
        List<Movie> mList = MainActivity.db.getMovieDAO().getUserSaves(u.id);
        genres = new ArrayList<String>();

        for (int i=0; i<mList.size(); i++) {
            String urlJSON = "https://api.themoviedb.org/3/movie/"+mList.get(i).id+"?api_key=e7f77194498797fa45d447e0d6e76c90";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, urlJSON, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray array = jsonObject.getJSONArray("genres");

                        for (int j = 0; j < array.length(); j++) {
                            JSONObject o = array.getJSONObject(j);

                            String genre = o.getString("id");
                            System.out.println(genres.size());
                            genres.add(genre);

                            getOccurances();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, error -> System.out.println("fail"));
            queue.add(stringRequest);
        }
    }

    private void getOccurances(){
        Map<String,Integer> map = new HashMap<String, Integer>();
        for(int h=0;h<genres.size();h++){
            Integer count = map.get(genres.get(h));
            map.put(genres.get(h), count==null?1:count+1);   //auto boxing and count
        }
        System.out.println(map);

        Map.Entry<String, Integer> maxEntry = null;
        for (Map.Entry<String, Integer> m : map.entrySet()){
            if (maxEntry == null || m.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = m;
            }
        }
        genreID = maxEntry.getKey();
    }

    private void getMovie(){
        String urlJson = "https://api.themoviedb.org/3/discover/movie?api_key=e7f77194498797fa45d447e0d6e76c90&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1&with_genres="+genreID;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlJson, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("results");

                    Random rand = new Random();
                    int r = rand.nextInt(10-1) + 1;

                    JSONObject o = array.getJSONObject(r);

                    title.setText(o.getString("original_title"));
                    desc.setText(o.getString("overview"));
                    movieID = o.getString("id");
                    posterURL = "https://image.tmdb.org/t/p/w300" + o.getString("poster_path");
                    MovieExploreHelper.imageRequest(imgPoster, queue, posterURL);

                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> System.out.println("fail"));
        queue.add(stringRequest);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
