package com.example.whatmovietoday;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

/**
 * Salting and Hashing the passwords would be ideal.
 * However it should never be done client side.
 * For the scope of this project, passwords will be
 * stored in plaintext.
 */
public class LoginSignup extends Activity {

    UserDB db = UserDB.getDatabase(this);
    final User u = new User();
    final UserDAO uDAO = db.getUserDAO();
    private Button btnLogin;
    private Button btnSignUp;
    private TextView txtUsername;
    private TextView txtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logsign);

        btnLogin = findViewById(R.id.btnLogIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                u.password = txtPassword.getText().toString();
                u.username = txtUsername.getText().toString();

                System.out.println("clicked");

                List<User> l = uDAO.getAll();
                Boolean exists = false;
                for (User i: l){
                    if (u.username.equals(i.username)){
                        exists = true;
                        break;
                    }
                }

                if (!exists){
                    uDAO.insert(u);
                    System.out.println("does not exist yet");
                }
                else {
                    System.out.println("exists");
                    Context context = getApplicationContext();
                    CharSequence text = "Username already exists! Please choose another";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u.password = txtPassword.getText().toString();
                u.username = txtUsername.getText().toString();
                List<User> l = uDAO.getAll();

                if (verifyUser(u,l)){
                    finish();
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

    }


    protected static boolean verifyUser(User user, List<User> l){
        for (User u : l){
            if (user.username.equals(u.username) && user.password.equals(u.password)){
                MainActivity.setUser(u);
                return true;
            }
        }
        return false;
    }
}
