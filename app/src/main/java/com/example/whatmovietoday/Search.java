package com.example.whatmovietoday;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Search extends AppCompatActivity {
    private ImageButton btnSearch;
    private Button btnLoad;
    private TextView input;
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ListItem> listItems;
    private static final String URL_DATA = "https://api.themoviedb.org/3/search/movie?api_key=e7f77194498797fa45d447e0d6e76c90&language=en-US&query=";
    private String URL_DATA2 = "&page=1";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        input = findViewById(R.id.input);
        RequestQueue queue = Volley.newRequestQueue(this);
        btnSearch = (ImageButton) findViewById(R.id.search);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        listItems = new ArrayList<>();
        String searchFromMenu = MainActivity.search;
        btnLoad = findViewById(R.id.btnLoad);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search");
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        System.out.println(searchFromMenu);
        if (!searchFromMenu.equals("")){
            input.setText(searchFromMenu);
            listItems.clear();
            loadRecyclerViewData();
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!input.getText().toString().isEmpty()) {
                    listItems.clear();
                    loadRecyclerViewData();
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "Sorry, we don't know what you're looking for!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    listItems.clear();
                    loadRecyclerViewData();
                }
                return handled;
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadRecyclerViewData() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data");
        progressDialog.show();
        String x = URL_DATA + input.getText().toString().replace(' ', '+') + URL_DATA2;
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
        RequestQueue requestQueue = MainActivity.getSearch();
        requestQueue.add(stringRequest);
    }
}