package com.prgguru.example.wupxstream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.Logger;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class MainActivity extends ActionBarActivity {

    //variable declarations
    private Button playButton;
    private Button pauseButton;
    public TextView artistTextView;
    public TextView songTextView;

    //create mediaplayer object
    final MediaPlayer mediaPlayer = new MediaPlayer();

    public final String url = "http://www.wupx.com:8000/";
    private boolean isPlaying = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //reference buttons
        playButton = (Button) findViewById(R.id.playButton);
        pauseButton = (Button) findViewById(R.id.pauseButton);

        //reference textViews
        artistTextView = (TextView) findViewById(R.id.artist);
        //songTextView  = (TextView) findViewById(R.id.song);

        //set default text for textviews
        artistTextView.setText("Unknown Artist");

        //reference imageView and set default boring artwork
        final ImageView albumArt = (ImageView) findViewById(R.id.albumArt);
        albumArt.setImageResource(R.drawable.musicnote);


        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(final MediaPlayer mp) { //after async thread is prepared


                    //set an onclick listener for the play button
                    playButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!isPlaying) {
                                Toast.makeText(MainActivity.this, R.string.stream_start_toast, Toast.LENGTH_SHORT).show();

                                mp.start();

                                //use android timer class to call the metaDataArtist method every 60 sec.
                                int delay = 0; // delay for 0 sec.
                                int period = 60000; // repeat every 1 min.
                                Timer timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        //need to update the the textview on the UI thread (not async thread)
                                        MainActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                //attempts to grab metadata from shoutcast stream
                                                metaDataArtist(artistTextView, url);

                                                //fetch album art/metadata and display it in imageview
                                                getAlbumArt(url, albumArt);
                                            }
                                        });

                                    }
                                }, delay, period);

                                isPlaying = true;
                            }

                        }


                    });

                    //set an onclick listener for the pause button
                    pauseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isPlaying) {
                                Toast.makeText(MainActivity.this, R.string.stream_stop_toast, Toast.LENGTH_SHORT).show();

                                mp.pause();
                                isPlaying = false;
                                //mp.prepareAsync();

                            }

                        }
                    });

                }

            });



    }

    public void onDestroy() {

        super.onDestroy();
        mediaPlayer.release();

    }


    //sets a textview's contents to display Artist metadata.
    public void metaDataArtist(TextView artistTextView, String url){

        //create a metadataRetriever object
        FFmpegMediaMetadataRetriever metaDataArtist = new FFmpegMediaMetadataRetriever();//artist data

        //set the data source
        metaDataArtist.setDataSource(url);

        //extract metadata
        String artist = metaDataArtist.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ICY_METADATA);

        metaDataArtist.getFrameAtTime(2000000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST); //frame at 2 seconds

        //set the textview to that artist
        artistTextView.setText(artist);

        metaDataArtist.release();

        //print textview contents to logcat (debugging)
        Log.d("log message", "The artist data is: " + artist);

    }

    //attempts to fetch album artwork from metadata
    public void getAlbumArt(String url, ImageView albumArt){

        FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
        retriever.setDataSource(url);

        byte [] data = retriever.getEmbeddedPicture();

        if (data != null) {

            // convert the byte array to a bitmap
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            // do something with the image ...
            albumArt.setImageBitmap(bitmap);

            retriever.release();
        }

        else{
            albumArt.setImageResource(R.drawable.musicnote);
        }

    }

}
