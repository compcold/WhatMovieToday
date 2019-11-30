package com.example.whatmovietoday;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;


/**
 * COSC 3P97 Final Project
 * Brian Jenkins 6063481, Jasdeep Grewal
 *
 */
public class MainActivity extends AppCompatActivity {

    private static List<User> userList;
    private static UserDAO dao;
    private static User user;
    public static void setUser(User u){user = u;}
    public static User getUser() {return user;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Database
        UserDB db = UserDB.getDatabase(this);
        dao = db.getUserDAO();
        userList = dao.getAll();

        //Check if users exist
        dao.nukeTable();
        System.out.println(userList.size());

        if (userList.size() > 0){
            for (User u : userList){
                if (u.lastLogin = true){
                    user = u;
                }
            }
        }
        else {
            startActivity(new Intent(MainActivity.this,LoginSignup.class));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
