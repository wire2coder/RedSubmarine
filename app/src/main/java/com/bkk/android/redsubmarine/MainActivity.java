package com.bkk.android.redsubmarine;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private DrawerLayout mDrawerLayout;


    // TODO: Add a Click Listener when user click on a post and open Detail Activity 9/17

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the toolbar as the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the app bar's "home" button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // << this line shows an arrow pointing left
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu); // << this line replaced the "left pointing arrow" with the "3 lines icon"

        // Navigation Drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                        // set items as selected to persist high light
                        menuItem.setCheckable(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected. For example, swap UI fragments here
                        return false;
                    }
                }
        );



        mRecyclerView = findViewById(R.id.rv_redditPosts);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MainActivityAdapter(redditPosts, this);
        mAdapter.SetOnItemClickListener(redditPostClick1); // >> do something when you click on a "View item"
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


    // Open the drawer when the button is tapped
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);

                return true;
        }

        return super.onOptionsItemSelected(item);
    } // onOptionsItemSelected()


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

    // TODO:
    public MainActivityAdapter.OnItemClickListener redditPostClick1 = new MainActivityAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            RedditPost redditPost1 = mAdapter.getRedditPosts().get(position);

            Intent intentDetailActivity  = new Intent(getApplicationContext(), DetailActivity.class);

            intentDetailActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // TODO: I don't know the purpose of this line

            Bundle bundle1 = new Bundle();
            bundle1.putString("title", redditPost1.getThumbnail());
            bundle1.putString("thumbnail", redditPost1.getThumbnail());
            bundle1.putString("url", redditPost1.getUrl());
            bundle1.putString("subreddit", redditPost1.getSubreddit());
            bundle1.putString("author", redditPost1.getAuthor());
            bundle1.putString("permalink", redditPost1.getPermalink());
            bundle1.putString("id", redditPost1.getId());
            bundle1.putString("subreddit_name_prefixed", redditPost1.getSubreddit_name_prefixed());
            bundle1.putInt("score", redditPost1.getScore());
            bundle1.putInt("num_comments", redditPost1.getNumberOfComments());
            bundle1.putBoolean("over_18", redditPost1.getOver18() );

            Log.d("TTT>>>", redditPost1.getTitle() );
            startActivity(intentDetailActivity);




        }
    };

} // class MainActivity
