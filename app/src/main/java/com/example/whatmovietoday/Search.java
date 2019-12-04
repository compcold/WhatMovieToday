package com.example.whatmovietoday;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Search extends Activity {
    private Button btnBack;
    private Button btnSearch;
    private Button btnLoad;
    private TextInputEditText input;
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ListItem> listItems;
    private static final String URL_DATA = "https://api.themoviedb.org/3/search/movie?api_key=e7f77194498797fa45d447e0d6e76c90&language=en-US&query=";
    private String URL_DATA2 = "&page=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RequestQueue queue = Volley.newRequestQueue(this);
        input = findViewById(R.id.input);
        btnBack = (Button) findViewById(R.id.back);
        btnSearch = (Button) findViewById(R.id.search);
        btnLoad = (Button) findViewById(R.id.btnLoad);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        listItems = new ArrayList<>();
        String searchFromMenu = MainActivity.search;

        //  JsonRequest(queue);
//        for(int  i =0; i<4;i++){
//            ListItem listItem = new ListItem(
//                    "heading"+(i+1),"description"
//            );
//            listItems.add(listItem);
//        }

        //adapter = new SearchAdapter(listItems, this);
        //recyclerView.setAdapter(adapter);
        //System.out.println(Arrays.deepToString(listItems.toArray()));

        System.out.println(searchFromMenu);
        if (!searchFromMenu.equals("")){
            input.setText(searchFromMenu);
            listItems.clear();
            loadRecyclerViewData();
            btnLoad.setVisibility(View.VISIBLE);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItems.clear();
                loadRecyclerViewData();
                btnLoad.setVisibility(View.VISIBLE);
            }
        });
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //listItems.clear();
                String[] pageNum;
                pageNum = URL_DATA2.split("=");
                System.out.println("that one:" + Arrays.toString(pageNum));
                int x = Integer.valueOf(pageNum[1]);
                x++;
                URL_DATA2 = pageNum[0] + "=" + x;
                loadRecyclerViewData();
                btnLoad.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadRecyclerViewData() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();
        String x = URL_DATA + input.getText().toString() + URL_DATA2;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, x, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        ListItem item = new ListItem(o.getString("original_title"), o.getString("overview"), ("https://image.tmdb.org/t/p/w400" + o.getString("poster_path")), o.getString("id"));
                        if (item.getHead().isEmpty()) {
                            item = new ListItem(o.getString("original_name"), o.getString("overview"), ("https://image.tmdb.org/t/p/w400" + o.getString("poster_path")), o.getString("id"));
                        }
                        System.out.println("this one:" + item);
                        listItems.add(item);
                    }
                    adapter = new SearchAdapter(listItems, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> System.out.println("fail"));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}