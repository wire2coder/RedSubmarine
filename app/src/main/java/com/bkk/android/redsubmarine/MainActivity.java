package com.bkk.android.redsubmarine;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.bkk.android.redsubmarine.adapter.MainActivityAdapter;
import com.bkk.android.redsubmarine.model.RedditPost;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.crash.component.FirebaseCrashRegistrar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String CLASS_TAG = MainActivity.class.getSimpleName();
    private static final String LOG_TAG = "ttt>>>: ";

    private static final String BASE_URL = "https://www.reddit.com/r/";
    private static final String REDDIT_URL1 = "https://www.reddit.com/r/subreddit/new.json?sort=new";
    private static final String REDDIT_URL2 = "https://www.reddit.com//.json";

    private TextView tv_data1;
    private List<RedditPost> redditPosts = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private MainActivityAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Menu sortMenu;

    // TODO: 9/11 add a Navigation Drawer
//    https://developer.android.com/training/implementing-navigation/nav-drawer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = findViewById(R.id.rv_redditPosts);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MainActivityAdapter(redditPosts, this);
        mRecyclerView.setAdapter(mAdapter); // >> mAdapter is EMPTY at this point

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, REDDIT_URL2, (String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(LOG_TAG, response.toString());

//                mAdapter.clearAdapter();

                // parse JSON data
                // data(JSON object) >> children(JSON array) >> data(JSON object)
                try {
                    JSONObject data = response.getJSONObject("data");
                    JSONArray children = data.getJSONArray("children");

                    for(int i=0; i<children.length(); i++) {

                        JSONObject post = children.getJSONObject(i).getJSONObject("data");

                        RedditPost redditPost = new RedditPost(
                                post.getString("title"),
                                post.getString("thumbnail"),
                                post.getString("url"),
                                post.getString("subreddit"),
                                post.getString("author"),
                                post.getString("permalink"),
                                post.getString("id"),
                                post.getString("subreddit_name_prefixed"),
                                post.getInt("score"),
                                post.getInt("num_comments"),
                                post.getLong("created_utc"),
                                post.getBoolean("over_18")

                        );

                        // now add the post to the ArrayList, but if check if the Array is empty
                        if (redditPosts == null) {
                            redditPosts = new ArrayList<>();
                        }

                        redditPosts.add(redditPost);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Data fetched from Reddit, now update the Adapter
//                mAdapter.notifyDataSetChanged();
                mAdapter.swapData(redditPosts);

            } // onResponse
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(LOG_TAG, "Error: " + error.getMessage());
            }

        });

        // Add the request to the RequestQueue, what is this "Queue" ?
        queue.add(request1);


    } // onCreate


    // Google Crashlytics for a crash
    public void forceACrash() {
        // [START crash_force_crash]
        Button crashButton = new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Crashlytics.getInstance().crash(); // Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        // [END crash_force_crash]
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);

        return true;
    }



} // class MainActivity
