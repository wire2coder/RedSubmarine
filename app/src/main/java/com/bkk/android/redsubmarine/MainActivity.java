package com.bkk.android.redsubmarine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bkk.android.redsubmarine.adapter.MainActivityAdapter;
import com.bkk.android.redsubmarine.database.AppDatabase;
import com.bkk.android.redsubmarine.database.RedditPostEntry;
import com.bkk.android.redsubmarine.firebase.ReminderService;
import com.bkk.android.redsubmarine.model.RedditPost;
import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    // class variables
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    Context context1;

    // "https://www.reddit.com/r/";
    // "https://www.reddit.com/r/subreddit/new.json?sort=new";
    private String subRedditName = "";
    private String sortRedditBy = "";

    private List<RedditPost> redditPosts = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private MainActivityAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public SharedPreferences mSharedPreferences1;
    private AppDatabase mAppDatabase1;

    private ActionBarDrawerToggle drawerToggle1;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
        private Menu sortMenu;
        private Menu mDrawerMenu;
        private SearchView mSearchView;
        @BindView(R.id.drawer_view1) NavigationView drawer_view1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context1 = getApplicationContext();

        // Stetho setup, for DEBUGGING
        Stetho.initializeWithDefaults(this);

        // Set up ButterKnife
        ButterKnife.bind(this);

        // Top navigation toolbar
        makeAToolBar();

        // getting "SharedPreferences"
        mSharedPreferences1 = getSharedPreferences("MainActivitySharePreferences", MODE_PRIVATE);

        // getting a copy of Room database
        mAppDatabase1 = AppDatabase.getsInstance( getApplicationContext() );


        // Navigation Drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);

        mDrawerMenu = drawer_view1.getMenu();


        String string1 = getString(R.string.all_sub_reddits);
        List<String> subRedditList1 = Arrays.asList(string1.split(","));
        Log.d(LOG_TAG, "subRedditList1 " + subRedditList1.toString());

        for (int x = 0; x < subRedditList1.size(); x++) {
            MenuItem subRedditMenuItem = mDrawerMenu.add(subRedditList1.get(x));
            subRedditMenuItem.setIcon(R.drawable.ic_reddit_logo); // << used Reddit icon but it is brown.
        } // for

        drawer_view1.setItemIconTintList(null); // << make the Reddit item visible

        // Open the left-hand-side Drawer for "Favorites"
        drawer_view1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem1) {

                        // This is the action_favorites button
                        // R.id.action_favorites
                        // /menu/drawer_view.xml
                        if (menuItem1.getGroupId() == R.id.group_favorite) {

                            // clear the data in the ArrayList
                            redditPosts.clear();
                            Log.d(LOG_TAG, "redditPosts.clear() ");

                            // get data from database
                            mAppDatabase1.redditPostDao().loadAllSavedRedditPost();

                            // "initLoader"
                            ArrayList<RedditPostEntry> asdf1 =  (ArrayList) mAppDatabase1.redditPostDao().loadAllSavedRedditPost();

                            // need a for loop to loop through ArrayList
                            for (int x=0; x < asdf1.size(); x++) {
                                asdf1.get(x).getId();
                                Log.d(LOG_TAG, "asdf1.getId() " + String.valueOf(asdf1.get(x).getId()));

                                RedditPost redditPost = new RedditPost(
                                        asdf1.get(x).getTitle(),
                                        asdf1.get(x).getThumbnail(),
                                        asdf1.get(x).getUrl(),
                                        asdf1.get(x).getSubreddit(),
                                        asdf1.get(x).getAuthor(),
                                        asdf1.get(x).getPermalink(),
                                        asdf1.get(x).getPost_id(),
                                        asdf1.get(x).getSubreddit_name_prefixed(),
                                        asdf1.get(x).getScore(),
                                        asdf1.get(x).getNumberOfComments(),
                                        asdf1.get(x).getPostedDate(),
                                        asdf1.get(x).getOver18()
                                );

                                // now add to existing array list
                                redditPosts.add(redditPost);
                            } // for

                            // now put ArrayList into Adapter
                            mAdapter.swapData(redditPosts);

                            // set the title of the "top bar"
                            toolbar.setTitle( getString(R.string.favorited_posts) );

                            // set items as selected to persist high light
                            // menuItem.setCheckable(true);

//                            return true; // EAT the "event", if you use FALSE here, to execution won't flow down to mDrawerLayout.closeDrawers();
                        } else {

                            String subRedditName = menuItem1.toString();
                            Log.d(LOG_TAG,"subRedditName " + subRedditName);
                            updateMainActivity( subRedditName );

                        } // else

                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        return true; // EAT the "event"
                    } //onNavigationMenuSelected()
                }); // setNavigationItemSelectedListener()


        mRecyclerView = findViewById(R.id.rv_redditPosts);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        makeFirebaseJobDispatcher(); // << start a background service with Firebase Job Dispatcher

        makeNavigationDrawerMove(); // << navigation menu on the RIGHT of the screen

        // Program starting point!
        // making network request to Reddit
        updateMainActivity(Strings.HOME);

    } // onCreate


    private void makeAToolBar() {
        // Set the toolbar as the action bar
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getTitle()); // >> name of the current sub-reddit

        // Enable the app bar's "home" button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // << this line shows an arrow pointing left
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_twotone_menu_24px); // << this line replaced the "left pointing arrow" with the "3 lines icon"
    } // makeAToolBar()


    // helper
    public void updateMainActivity(String subRedditName1) {

        this.subRedditName = subRedditName1;
        toolbar.setTitle(subRedditName1);

        String url1;

        // .Sort By" urls
        // https://www.reddit.com/r/<subRedditName1>/new/.json

        if ( subRedditName.equals( getString(R.string.home) ) ) {

            Log.d(LOG_TAG, "subRedditName " + "home");
            url1 = Strings.REDDIT_URL + Strings.JSON_EXTENSION;

        } else {

            url1 =  Strings.REDDIT_URL
                    + Strings.REDDIT_URL_R
                    + subRedditName1
                    + "/"
                    + sortRedditBy
                    + Strings.JSON_EXTENSION;

        } // else

        Log.d(LOG_TAG, "url1 " + url1);
        volleyRequest(url1); // << Reddit homepage with JSON response

    } // updateMainActivity()


    // helper
    public void volleyRequest(String string_url) {

        mAdapter = new MainActivityAdapter(redditPosts, this);
        mRecyclerView.setAdapter(mAdapter); // >> mAdapter is EMPTY at this point
        mAdapter.SetOnItemClickListener(redditPostClick1); // >> do something when you click on a "View item"

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, string_url
                ,(String) null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
//                Log.d(LOG_TAG, response.toString());
                mAdapter.clearAdapter();

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
                mAdapter.swapData(redditPosts);

            } // onResponse
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError e1) {
                VolleyLog.d(LOG_TAG, "onErrorResponse " + e1.getMessage());
            } // onErrorResponse()

        });

        // Add the request to the RequestQueue, what is this "Queue" ?
        queue.add(request1);

    } // volleyRequest()


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_and_sort_menu, menu); // << this is an XML file
        this.sortMenu = menu;

        // for the Search button in reddit_post_search_menuarch_menu.xml
//        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.item_search_menu)); // this search "icon" on the top right corner
        mSearchView = (SearchView) menu.findItem(R.id.item_search_menu).getActionView();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.clearFocus();

                searchForRedditPost(subRedditName, query);

                // false if the SearchView should perform the default action of showing any suggestions if available,
                // true if the action was handled by the listener.
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        }); // mSearchView.setOnQueryTextListener()

        return true;
    } // onCreateOptionsMenu()


    // helper
    public void searchForRedditPost(String currentSubRedditItem, String searchQuery) {

        subRedditName = currentSubRedditItem;
        toolbar.setTitle(subRedditName);

        String searchUrl;
        String searchString1 = Strings.SEARCH + Strings.JSON_EXTENSION +  Strings.QUERY  + searchQuery;
        Log.d(LOG_TAG,"searchString1 " + searchString1);

        // for "home" sub-reddit
        if (currentSubRedditItem.equals("home") ) {

            searchUrl = Strings.REDDIT_URL + searchString1;

        } else {
            // for "everything other sub-reddit" that is not "home

            // https://www.reddit.com/r/ + subRedditName + /search.json?q + searchQuery
            searchUrl = Strings.REDDIT_URL
                    + Strings.REDDIT_URL_R
                    + subRedditName
                    + "/" + Strings.SEARCH
                    + Strings.JSON_EXTENSION
                    + Strings.QUERY
                    + searchQuery;

        } // else


        Log.d(LOG_TAG,"searchUrl " + searchUrl);
        volleyRequest(searchUrl);

    } // searchForRedditPost()


    public MainActivityAdapter.OnItemClickListener redditPostClick1 = new MainActivityAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            RedditPost redditPost1 = mAdapter.getRedditPosts().get(position);

            Bundle bundle1 = new Bundle();
            bundle1.putParcelable( getString(R.string.redditPost1) , redditPost1); // << using Parcelable

            final Intent intent1 = new Intent(getBaseContext(), DetailActivity.class);
            intent1.putExtras(bundle1);
            startActivity(intent1); // Start DetailActivity.java

        } // onItemClick()
    };


    // Open the drawer when the button is tapped
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.item_sort_new:
                sortRedditBy = getString(R.string.new_reddit_post);
                break;

            case R.id.item_sort_hot:
                sortRedditBy = getString(R.string.hot_reddit_post);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        Log.d(LOG_TAG, "subRedditName " + subRedditName); // >> this works
        updateMainActivity(subRedditName);

        return true;
    } // onOptionsItemSelected()


    // helper
    private void makeNavigationDrawerMove() {
        drawerToggle1 = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar
                , R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        // note setDrawerListener() is deprecated
        mDrawerLayout.addDrawerListener(drawerToggle1);

    } // makeNavigationDrawerMove()


    // helper
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


    // helper
    public void makeFirebaseJobDispatcher() {

        FirebaseJobDispatcher dispatcher1 = new FirebaseJobDispatcher( new GooglePlayDriver(MainActivity.this) );


        Bundle myExtrasBundle = new Bundle();
        myExtrasBundle.putString("some_key", "some_value");

        Job myJob = dispatcher1.newJobBuilder()
                // the JobService that will be called
                .setService(ReminderService.class)
                // uniquely identifies the job
                .setTag("my-unique-tag")
                // one-off job
                .setRecurring(false)
                // don't persist past a device reboot
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(0, 60))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(false)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(
                        // only run on an unmetered network
                        Constraint.ON_UNMETERED_NETWORK,
                        // only run when the device is charging
                        Constraint.DEVICE_CHARGING
                )
                .setExtras(myExtrasBundle)
                .build();

        dispatcher1.mustSchedule(myJob);

    } // makeFirebaseJobDispatcher()


} // class MainActivity
