package com.prgguru.example.wupxstream;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.prgguru.example.wupxstream.R.string.Username;

public class login extends AppCompatActivity {

    //declare edit text fields
    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button loginButton;
    private Button registerButton;

    //url to the login page (on the server)
    private static final String Login_URL = "http://www.wupx.com/arcums/login/appLogin.php";
    //public static final URI Login_URL = "www.wupx.com/arcums/login/index.php";

    //reference the server key values
    public static final String Key_loginUsername = "username";
    public static final String Key_loginPassword = "encryptpass";
    private String loginUsername;
    private String loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //reference the edittext fields
        editTextLogin = (EditText) findViewById(R.id.login_username);
        editTextPassword = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.login_button);
        registerButton = (Button)findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    //if either field is empty
                    if(editTextLogin.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty()){
                        emptyFieldsAlert();
                    }

                    //if everything looks good
                    else
                    {
                        userLogin();//call the user login method.
                    }

            }


        });
    }


    //method to log user into the arcums DB
    private void userLogin(){

        //reference values passed in from the user (arcums login and password)
        loginUsername = editTextLogin.getText().toString();
        loginPassword = editTextPassword.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Login_URL,
                new Response.Listener<String>() { //on positive response from server
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("success")) { //response from the server
                            //notify user of successful login with a toast
                            Toast.makeText(login.this, R.string.login_Success, Toast.LENGTH_SHORT).show();

                            //open a new activity
                            //Intent intent = new Intent(login.this, playlist.class);
                            //startActivity(intent);

                            openProfile();

                        }

                        else if(response.trim().equals("not activated")){
                            //if the credentials are ok but user has not been activated, display an alert
                            //this prevents anyone from making an account and charting bogus/prank info through the app
                            notActivatedAlert();
                        }
                        else{

                            Log.d("log message", response.trim());
                            //login failed alert
                            loginFailedAlert();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error toast
                        Toast.makeText(login.this, R.string.error_response, Toast.LENGTH_SHORT).show();
                    }
                }){


            //returns a hashmap with key value pairs (input username and pw)
            @Override
            protected Map<String,String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put(Key_loginUsername, loginUsername);
                params.put(Key_loginPassword, loginPassword);
                return params;
            }

        };

        //create a requestQueue singleton object, add the request to the queue
        MySingleton.getInstance(login.this).addTorequestqueue(stringRequest);
    }



    //Intent. called when user clicks the "register" button
    public void register(View view) {
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }

    //opens the chartMusic activity upon a successful login
    private void openProfile(){
        Intent intent = new Intent(login.this, playlist.class);
        startActivity(intent);

    }


    //method to check for empty fields from user. If empty, display an alert. Make them fix it.
    public void emptyFieldsAlert(){

        //create a new alertDialogBuilder object
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#ed5853'>Error: Empty Fields</font>"));
        alertDialogBuilder.setMessage("You left a login field blank. Please try again.");
        alertDialogBuilder.setPositiveButton("ok",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if clicked, just close the dialog and wait for valid input.
            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    //method to check for empty fields from user. If empty, display an alert. Make them fix it.
    public void loginFailedAlert(){

        //create a new alertDialogBuilder object
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#ed5853'>Error: Login Failed</font>"));
        alertDialogBuilder.setMessage("You entered incorrect credentials or the user does not exist in ARCUMS yet.");
        alertDialogBuilder.setPositiveButton("ok",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if clicked, just close the dialog and wait for valid input.
                editTextLogin.setText("");
                editTextPassword.setText("");
            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }


    //displays if a user account has not yet been activated.
    public void notActivatedAlert(){

        //create a new alertDialogBuilder object
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#ed5853'>Error: Account Not Yet Activated</font>"));
        alertDialogBuilder.setMessage("Your account has not yet been activated. You cannot chart music until the current WUPX I.T. director has approved your account.");
        alertDialogBuilder.setPositiveButton("got it",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //clear the data
                editTextLogin.setText("");
                editTextPassword.setText("");
            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

}







