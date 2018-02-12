package com.prgguru.example.wupxstream;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static com.prgguru.example.wupxstream.R.id.login;
import static com.prgguru.example.wupxstream.R.id.textView;

public class playlist extends AppCompatActivity {

    public static final String Key_Artist = "artist";
    public static final String Key_Song = "song";
    public static final String Key_Album = "album";
    public static final String Key_RecordLabel = "label";

    TextView title;
    EditText editTextArtist;
    EditText editTextSong;
    EditText editTextAlbum;
    EditText editTextRecordLabel;
    Button chartMusicButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);





        editTextArtist = (EditText) findViewById(R.id.editTextArtist);
        editTextSong = (EditText) findViewById(R.id.editTextSong);
        editTextAlbum = (EditText) findViewById(R.id.editTextAlbum);
        editTextRecordLabel = (EditText) findViewById(R.id.editTextRecordLabel);
        chartMusicButton = (Button) findViewById(R.id.chartMusicButton);


        //display username at the top
        title = (TextView) findViewById(R.id.title);
        Intent intent = getIntent();

        //call chartMusic method on click
        chartMusicButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                chartMusic();
            }

        });
    }



    public void chartMusic(){

        //print textview contents to logcat (debugging)
        Log.d("log message", "entering chartMusic method");

        //url to the registration page (on the server)
        String url = "http://www.wupx.com/arcums/playlist/appCharting .php";

        //http://www.wupx.com/arcums/playlist/appPlaylist.php

        //values passed in from the user
        final String artist = editTextArtist.getText().toString().trim();
        final String song = editTextSong.getText().toString().trim();
        final String album = editTextAlbum.getText().toString().trim();
        final String label = editTextRecordLabel.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response: ", response);
                        Toast.makeText(getApplicationContext(), R.string.playlist_toast, Toast.LENGTH_SHORT).show();
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
                params.put(Key_Artist, artist);
                params.put(Key_Song, song);
                params.put(Key_Album, album);
                params.put(Key_RecordLabel, label);
                return params;
            }

        };

        //create a single instance of the requestQueue and add the request
        MySingleton.getInstance(playlist.this).addTorequestqueue(stringRequest);
    }


}