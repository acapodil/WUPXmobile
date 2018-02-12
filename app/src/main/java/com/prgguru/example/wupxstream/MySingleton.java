package com.prgguru.example.wupxstream;

import android.app.DownloadManager;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by acapodil on 2/28/2017. Singleton class for the request queue
 */

public class MySingleton {

    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    //constructor for the singleton class
    private MySingleton(Context context){
        mCtx = context;
        requestQueue = getRequestQueue();

    }

    //returns an instance of the requestqueue singleton class
    public static synchronized MySingleton getInstance(Context context)
    {
        if(mInstance==null){
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    //method that returns a requestQueue
    public RequestQueue getRequestQueue(){
        //if requestqueue is null, initialize the requestqueue
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    //adds a request to the singleton requestqueue
    public <T>void addTorequestqueue(Request<T> request){
        requestQueue.add(request);
    }

}
