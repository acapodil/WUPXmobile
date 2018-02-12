package com.prgguru.example.wupxstream;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
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

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.button;

public class register extends AppCompatActivity{

    //key values (these are the values in the DB)
    public static final String Key_Username = "username";
    public static final String Key_Realname = "name";
    public static final String Key_Password = "password";
    public static final String Key_Email = "email";

    //declare the editText field & Button
    private EditText editTextUsername;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPassword2;
    private Button buttonRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //reference editText fields and buttons
        editTextUsername = (EditText) findViewById(R.id.editTextUserName);
        editTextName = (EditText) findViewById(R.id.editTextRealName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword2 = (EditText) findViewById(R.id.editTextPasswordAgain);
        buttonRegister = (Button) findViewById(R.id.registerButton);



        buttonRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //if there are empty textFields
                if(editTextUsername.getText().toString().isEmpty() || editTextName.getText().toString().isEmpty() || editTextEmail.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty() || editTextPassword2.getText().toString().isEmpty()) {
                    emptyFieldsAlert();//display the proper alert Dialogue
                }

                //make sure the password fields match
                else if( !(editTextPassword.getText().toString().equals(editTextPassword2.getText().toString())) ){
                    mismatchedPasswordsAlert(editTextPassword.getText().toString(), editTextPassword2.getText().toString());//display the proper alert Dialogue
                }
                else{
                    //if everything looks good, call the registerUser() method.
                    registerUser();
                }

            }


        });
    }


    //method to add user to the arcums database
    private void registerUser() {

        //print textview contents to logcat (debugging)
        Log.d("log message", "entering register User method");

        //url to the registration page (on the server)
       String Register_URL = "http://www.wupx.com/arcums/login/register.php";

        //values passed in from the user
        final String Username = editTextUsername.getText().toString().trim();
        final String Name = editTextName.getText().toString().trim();
        final String Password = editTextPassword.getText().toString().trim();
        final String Password2 = editTextPassword2.getText().toString().trim();
        final String Email = editTextEmail.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Register_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response: ", response);
                        userAdded(); //display userAdded alert
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error toast - cant connect to the server
                        Toast.makeText(getApplicationContext(), R.string.registration_error, Toast.LENGTH_SHORT).show();
                    }
                }){

                //returns a hashmap with key value pairs
                @Override
                protected Map<String,String> getParams(){
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Key_Username, Username);
                    params.put(Key_Realname, Name);
                    params.put(Key_Password, Password2);
                    params.put(Key_Email, Email);
                    return params;
                }

        };

            //create a single instance of the requestQueue and add the request
            MySingleton.getInstance(register.this).addTorequestqueue(stringRequest);

    }




    //displays when user leaves one or more input field blank
    public void emptyFieldsAlert(){

            //create a new alertDialogBuilder object
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(Html.fromHtml("<font color='#ed5853'>Error: Empty Fields</font>"));
            alertDialogBuilder.setMessage("Please fill out all fields.");
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

    //displays when the provided passwords dont match
    public void mismatchedPasswordsAlert(String password1, String password2){

        //create a new alertDialogBuilder object
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#ed5853'>Error: mismatched passwords</font>"));
        alertDialogBuilder.setMessage("The passwords you provided didn't match. Please try again.");
        alertDialogBuilder.setPositiveButton("ok",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // if clicked, just close the dialog and wait for valid input.
                editTextPassword.setText("");
                editTextPassword2.setText("");
            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }


    //displays when user is Successfully added
    public void userAdded(){

        //create a new alertDialogBuilder object
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='#98ff68'>Success!</font>"));
        alertDialogBuilder.setMessage("You have successfully created an account in arcums. Once the IT director approves your profile, you will be able to log in and chart music. Congratulations!");
        alertDialogBuilder.setPositiveButton("ok",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(register.this, login.class);
                startActivity(intent);
                //clear all fields
                editTextUsername.setText("");
                editTextEmail.setText("");
                editTextName.setText("");
                editTextPassword.setText("");
                editTextPassword2.setText("");


            }
        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }



    //Intent. sends user to LOGIN activity after they successful registration
    public void login(View view) {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }


}