package com.example.whatmovietoday;

import android.app.Activity;
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
import com.google.android.material.textfield.TextInputLayout;

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

public class Search extends Activity {
    private Button btnBack;
    private Button btnSearch;
    private Button btnLoad;
    private TextInputLayout input;
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
        input = (TextInputLayout) findViewById(R.id.input);
        btnBack = (Button) findViewById(R.id.back);
        btnSearch = (Button) findViewById(R.id.search);
        btnLoad = (Button) findViewById(R.id.btnLoad);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        listItems = new ArrayList<>();

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
        String x = URL_DATA + input.getEditText().getText().toString() + URL_DATA2;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, x, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray array = jsonObject.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        ListItem item = new ListItem(o.getString("original_title"), o.getString("overview"), ("https://image.tmdb.org/t/p/w400" + o.getString("poster_path")));
                        if (item.getHead().isEmpty()) {
                            item = new ListItem(o.getString("original_name"), o.getString("overview"), ("https://image.tmdb.org/t/p/w400" + o.getString("poster_path")));
                        }
                        System.out.println("this one:" + item);
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
}