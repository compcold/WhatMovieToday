package com.example.whatmovietoday;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


/**
 * COSC 3P97 Final Project
 * Brian Jenkins 6063481, Jasdeep Grewal
 *
 * This MainActivity houses the main menu and login/signup
 * functionality. It also holds all the setting button
 * properties.
 *
 */
public class MainActivity extends AppCompatActivity {

    private static List<User> userList;
    private static UserDAO dao;
    private static User activeUser = new User();
    private static User tmpUser;
    public static void setUser(User u){activeUser = u;}
    public static User getUser() {return activeUser;}
    Toolbar toolbar;

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
    private ImageButton btnSearch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);

        //Database
        UserDB db = UserDB.getDatabase(this);
        dao = db.getUserDAO();
        userList = dao.getAll();

        //Check if users exist
        //dao.nukeTable();
        if (userList.size() > 0){

            //Check if user stayed logged in
            boolean userLoggedIn = false;
            User log = new User();

            for (User u : userList){
                if (u.lastLogin == true){
                    userLoggedIn = true;
                    log = u;
                    break;
                }
            }

            //Go to login if user does not exist
            if (userLoggedIn == true){
                activeUser = log;
                setContentView(R.layout.activity_main);
                toolbar = findViewById(R.id.toolbar);
                toolbar.setTitle("Hi " + activeUser.nickName + "!");
                setSupportActionBar(toolbar);
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

        //Menu Stuff
        btnMenuSearch = findViewById(R.id.btnMenuSearch);
        btnMenuSearch.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, Search.class)));
    }


    //Login view controls
    protected void setupLogin(){
        tmpUser = new User();
        btnLogin = findViewById(R.id.btnLogIn);
        txtUsernameSign = findViewById(R.id.txtUsername);
        txtPasswordSign = findViewById(R.id.txtPassword);
        txtSignup = findViewById(R.id.txtSignup);

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
                    activeUser.id = dao.getId(tmpUser.username);
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
                        dao.insert(tmpUser);
                        activeUser = tmpUser;
                        setContentView(R.layout.activity_main);
                        toolbar = findViewById(R.id.toolbar);
                        toolbar.setTitle("Hi " + activeUser.nickName + "!");
                        setSupportActionBar(toolbar);
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
                dao.setLogin(u.username);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            //Set last login to 0
            dao.removeLogin(activeUser.username);
            activeUser = null;
            userList = dao.getAll();
            setContentView(R.layout.activity_login);
            setupLogin();
        }
        return super.onOptionsItemSelected(item);
    }
}
