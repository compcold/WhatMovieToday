package com.example.whatmovietoday;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;


public class Favorite extends AppCompatActivity {

    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ListItem> listItems;
    private Toolbar toolbar;

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

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Favorites");
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void getFavorites(){
        User u = MainActivity.getUser();
        List<Movie> mList = MainActivity.db.getMovieDAO().getUserSaves(u.id);
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

                        adapter = new FavoriteAdapter(listItems, getApplicationContext());
                        recyclerView.setAdapter(adapter);
                    }, error -> error.printStackTrace());
            queue.add(jsonObjectRequest);
        }
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
