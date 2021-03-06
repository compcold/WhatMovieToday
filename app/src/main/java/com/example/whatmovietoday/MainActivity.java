package com.example.whatmovietoday;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.List;


/**
 * COSC 3P97 Final Project
 *
 * This MainActivity houses the main menu and login/signup
 * functionality. It also holds all the setting button
 * properties.
 */
public class MainActivity extends AppCompatActivity {

    private static List<User> userList;
    public static UserDAO uDao;
    public static MovieDAO mDao;
    private static User activeUser;
    private static User tmpUser;
    private static Context cntx;
    public static UserDB db;
    public static Context getContext(){return cntx;}
    public static void setUser(User u){activeUser = u;}
    public static User getUser() {return activeUser;}
    public static ArrayList<MovieExploreObject> randomMovies;
    public static List<String> genres;
    Toolbar toolbar;

    //Volley Requests
    private static RequestQueue searchQueue;
    private static RequestQueue exploreQueue;
    private static RequestQueue favoriteQueue;

    public static RequestQueue getSearch(){return searchQueue;}
    public static RequestQueue getExplore(){return exploreQueue;}
    public static RequestQueue getFavorite(){return favoriteQueue;}

    //SignUp / Login
    private Button btnLogin;
    private Button btnSignUp;
    private TextView txtUsernameSign;
    private TextView txtPasswordSign;
    private TextView txtSignup;
    private TextView txtNick;

    //Main Menu
    private ImageButton btnMenuSearch;
    private ImageButton btnFavorite;
    private ImageButton btnExplore;
    private ImageButton btnRandom;
    private TextView txtSearchInput;
    public static String search;

    public static MovieDAO getMovieDao(){
        mDao = db.getMovieDAO();
        return mDao;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        //Instantiate Queues
        searchQueue = Volley.newRequestQueue(this);
        exploreQueue = Volley.newRequestQueue(this);
        favoriteQueue = Volley.newRequestQueue(this);

        //Database
        db = UserDB.getDatabase(this);
        uDao = db.getUserDAO();
        mDao = db.getMovieDAO();
        userList = uDao.getAll();


        //Check if users exist
        //uDao.nukeTable();
        //mDao.nukeTable();
        if (userList.size() > 0){

            List<User> testLst = uDao.getAll();
            for (int i=0; i<userList.size(); i++){
                System.out.println(testLst.get(i).username + " " + testLst.get(i).id);
            }

            //Check if user stayed logged in
            boolean userLoggedIn = false;
            for (User u : userList){
                if (u.lastLogin == true){
                    userLoggedIn = true;
                    activeUser = u;
                    break;
                }
            }

            //Go to login if user does not exist
            if (userLoggedIn == true){
                setContentView(R.layout.activity_main);
                toolbar = findViewById(R.id.toolbar);
                toolbar.setTitle("Hi " + activeUser.nickName + "!");
                setSupportActionBar(toolbar);
                setupMenu();
            }
            else {
                setContentView(R.layout.activity_login);
                setupLogin();
            }
        }
        else {
            setContentView(R.layout.activity_login);
            setupLogin();
        }
        cntx = this;

        //Generate Some FindAMovie Movies Real Quick
        newMovieList();
    }

    protected void setupMenu() {
        cntx = this;
        btnMenuSearch = findViewById(R.id.search);
        txtSearchInput = findViewById(R.id.txtSearchInput);

        btnMenuSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = txtSearchInput.getText().toString();
                startActivity(new Intent(MainActivity.this, Search.class));
            }
        });

        txtSearchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    search = txtSearchInput.getText().toString();
                    startActivity(new Intent(MainActivity.this, Search.class));
                }
                return handled;
            }
        });


        btnExplore = findViewById(R.id.btnExplore);
        btnExplore.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, Explore.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle()));

        btnFavorite = findViewById(R.id.btnFavorite);
        btnFavorite.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, Favorite.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle()));

        btnRandom = findViewById(R.id.btnRandom);
        btnRandom.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                if (mDao.getUserSaves(activeUser.id).size() < 1) {
                    Context context = getApplicationContext();
                    CharSequence text = "Use the 'Search' or 'Explore' tools to add Favorites!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                 else {
                    startActivity(new Intent(MainActivity.this, FindAMovie.class));
                 }
             }
         });
    }


    //Login view controls
    protected void setupLogin(){
        tmpUser = new User();
        btnLogin = findViewById(R.id.btnLogIn);
        txtUsernameSign = findViewById(R.id.txtUsername);
        txtPasswordSign = findViewById(R.id.txtPassword);
        txtSignup = findViewById(R.id.txtSignup);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Welcome!");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmpUser.password = txtPasswordSign.getText().toString();
                tmpUser.username = txtUsernameSign.getText().toString();
                List<User> l = userList;

                if (verifyUser(tmpUser,l)){
                    setContentView(R.layout.activity_main);
                    toolbar = findViewById(R.id.toolbar);
                    toolbar.setTitle("Hi " + activeUser.nickName + "!");
                    setSupportActionBar(toolbar);
                    activeUser.id = uDao.getId(tmpUser.username);
                    setupMenu();
                }
                else {
                    Context context = getApplicationContext();
                    CharSequence text = "Username or password incorrect! Please try again";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });

        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_signup);
                signUpSetup();
            }
        });
    }


    protected void signUpSetup(){
        tmpUser = new User();
        btnSignUp = findViewById(R.id.btnSignUp);
        txtNick = findViewById(R.id.txtNick);
        txtUsernameSign = findViewById(R.id.txtUsername);
        txtPasswordSign = findViewById(R.id.txtPassword);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_login);
                setupLogin();
            }
        });

        getSupportActionBar().setTitle("You're making the right choice!");
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tmpUser.password = txtPasswordSign.getText().toString();
                tmpUser.username = txtUsernameSign.getText().toString();
                tmpUser.nickName = txtNick.getText().toString();

                List<User> l = userList;
                Boolean exists = false;
                for (User i : l) {
                    if (tmpUser.username.equals(i.username)) {
                        exists = true;
                        break;
                    }
                }

                //Check if it exists
                if (!exists) {

                    //Check for all fields
                    if (txtPasswordSign.getText().toString().isEmpty()
                            || txtUsernameSign.getText().toString().isEmpty()
                            || txtNick.getText().toString().isEmpty())
                    {
                        setToast("Please fill in all fields");
                    }

                    //Check for spaces characters
                    else if (txtPasswordSign.getText().toString().contains(" ")
                            || txtUsernameSign.getText().toString().contains(" ")
                            || txtNick.getText().toString().contains(" "))
                    {
                        setToast("Spaces aren't allowed here! Duh!");
                    }

                    else {
                        uDao.insert(tmpUser);
                        activeUser = tmpUser;
                        activeUser.id = uDao.getId(tmpUser.username);
                        setContentView(R.layout.activity_main);
                        toolbar = findViewById(R.id.toolbar);
                        toolbar.setTitle("Hi " + activeUser.nickName + "!");
                        setSupportActionBar(toolbar);
                        setupMenu();
                    }
                }
                else {
                    setToast("Username already exists! Please choose another");
                }
            }
        });
    }


    //Verify if a user already exists
    protected static boolean verifyUser(User user, List<User> l){
        for (User u : l){
            if (user.username.equals(u.username) && user.password.equals(u.password)){
                MainActivity.setUser(u);
                uDao.setLogin(u.username);
                return true;
            }
        }
        return false;
    }


    protected void setToast(String s){
        Context context = getApplicationContext();
        CharSequence text = s;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public static void newMovieList(){
        MovieExploreHelper.newRandomMovies();
        randomMovies = MovieExploreHelper.mList;
    }

    private void openURL(String url){
        if (url != null) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            if(activeUser != null ) {
                //Set last login to 0
                uDao.removeLogin(activeUser.username);
                activeUser = null;
                userList = uDao.getAll();
                setContentView(R.layout.activity_login);
                setupLogin();
            }
            else {
                finishAffinity();
                System.exit(0);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}

