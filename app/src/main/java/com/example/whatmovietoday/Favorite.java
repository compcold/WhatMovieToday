package com.example.whatmovietoday;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;


public class Favorite extends Activity {

    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        recyclerView =  findViewById(R.id.recFavorite);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        listItems = new ArrayList<>();

        getFavorites();
    }


    public void getFavorites(){
        User u = MainActivity.getUser();
        List<Movie> mList = MainActivity.getMovieDao().getUserSaves(u.id);
        adapter = new SearchAdapter(listItems, getApplicationContext());
        recyclerView.setAdapter(adapter);
        RequestQueue queue = Volley.newRequestQueue(this);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();
        for (int i=0; i<mList.size(); i++) {
            String urlJSON = "https://api.themoviedb.org/3/movie/"+mList.get(i).id+"?api_key=e7f77194498797fa45d447e0d6e76c90";
            System.out.println(urlJSON);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, urlJSON, null, response -> {

                        String title = "";
                        String desc = "";
                        String poster = "https://image.tmdb.org/t/p/w400";
                        String id = "";

                        try {
                            title = response.getString("original_title");
                            desc = response.getString("overview");
                            poster += response.getString("poster_path");
                            id = response.getString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        System.out.println(title);

                        ListItem item = new ListItem(title, desc, poster, id);
                        listItems.add(item);

                    }, error -> error.printStackTrace());
            queue.add(jsonObjectRequest);
        }
        progressDialog.dismiss();
        adapter = new FavoriteAdapter(listItems, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}
