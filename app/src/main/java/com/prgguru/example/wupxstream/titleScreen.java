package com.prgguru.example.wupxstream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class titleScreen extends AppCompatActivity {

    Button listen;
    Button login;
    TextView  WUPX;
    TextView stationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        //reference buttons
        listen = (Button) findViewById(R.id.listen);
        login = (Button) findViewById(R.id.login);

        //reference TextViews
        WUPX = (TextView)findViewById(R.id.WUPX);
        stationID = (TextView) findViewById(R.id.stationID);

        //reference imageView and set default penguin artwork
        final CircleImageView logo = (CircleImageView) findViewById(R.id.logo);
        logo.setImageResource(R.drawable.king);

    }

    //Intent. called when user clicks the "listen" button
    public void launchWebStream(View view) {
        Intent intent = new Intent(this, MainActivity.class); //change activity name later
        startActivity(intent);
    }

    ////Intent. called when user clicks the "login" button
    public void launchLoginPage(View view) {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }


}

