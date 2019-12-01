package com.example.whatmovietoday;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Search extends AppCompatActivity {
    private Button btnBack;
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ListItem> listItems;
    private static final String URL_DATA = "https://api.themoviedb.org/3/discover/movie?api_key=e7f77194498797fa45d447e0d6e76c90";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RequestQueue queue = Volley.newRequestQueue(this);

        btnBack = (Button) findViewById(R.id.back);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        listItems = new ArrayList<>();
        loadRecyclerViewData();
      //  JsonRequest(queue);
//        for(int  i =0; i<4;i++){
//            ListItem listItem = new ListItem(
//                    "heading"+(i+1),"description"
//            );
//            listItems.add(listItem);
//        }

        //adapter = new MyAdapter(listItems, this);
        //recyclerView.setAdapter(adapter);
        //System.out.println(Arrays.deepToString(listItems.toArray()));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadRecyclerViewData() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_DATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray array= jsonObject.getJSONArray("results");
                    for(int i =0 ;i<array.length();i++){
                        JSONObject o = array.getJSONObject(i);
                        ListItem item = new ListItem(o.getString("original_title"),o.getString("overview"),("https://image.tmdb.org/t/p/w400"+o.getString("poster_path")));
                        listItems.add(item);
                    }
                    adapter = new MyAdapter(listItems, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> System.out.println("fail"));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void JsonRequest(RequestQueue q) {
        String urlJSON = "https://api.themoviedb.org/3/movie/238?api_key=e7f77194498797fa45d447e0d6e76c90";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlJSON, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        String movie = "";
                        String description = "";
                        //Collect Info from JSON
                        try {
                            movie = response.getString("original_title");
                            description = response.getString("overview");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("hey this one:" + movie);
                        ListItem listItem = new ListItem(movie, description,"");
                        listItems.add(listItem);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ListItem listItem = new ListItem(
                                "That didn't work!", "description","");
                    }
                });
        q.add(jsonObjectRequest);
    }

}