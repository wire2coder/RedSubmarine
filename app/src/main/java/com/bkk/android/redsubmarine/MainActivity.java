package com.bkk.android.redsubmarine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String BASE_URL = "https://www.reddit.com/r/";
    private static final String REDDIT_URL1 = "https://www.reddit.com/r/subreddit/new.json?sort=new";
    private static final String REDDIT_URL2 = "https://www.reddit.com//.json";

    private static final String LOG_TAG = "ttt>>>: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tv_data1 = findViewById(R.id.tv_data1);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);


        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, REDDIT_URL2, (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(LOG_TAG, response.toString());

                // parse JSON data
                // data(JSON object) >> children(JSON array) >> data(JSON object)
                try {
                    JSONObject data = response.getJSONObject("data");
                    JSONArray children = data.getJSONArray("children");

                    for(int i=0; i<children.length(); i++) {
                        JSONObject post = children.getJSONObject(i).getJSONObject("data");

                        // TODO 9/9: make a a RedditPost Object

                    }

                } catch (Exception e) {
                    // TODO: put something in here for handling exceptions
                }


            } // onResponse
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(LOG_TAG, "Error: " + error.getMessage());
            }

        });

        queue.add(request1);

// Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, REDDIT_URL1,
//                new Response.Listener<String>() {
//
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        mTextView.setText("Response is: "+ response.substring(0,500));
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
//            }
//        });

// Add the request to the RequestQueue.



    } // onCreate


} // class MainActivity
