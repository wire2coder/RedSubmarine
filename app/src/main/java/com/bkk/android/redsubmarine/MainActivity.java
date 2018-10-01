package com.bkk.android.redsubmarine;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.bkk.android.redsubmarine.model.RedditPost;
import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private static final String CLASS_TAG = MainActivity.class.getSimpleName();
    private static final String LOG_TAG = "ttt>>>: ";

    private static final String BASE_URL = "https://www.reddit.com/r/";
    private static String REDDIT_URL_NEW = "https://www.reddit.com/r/subreddit/new.json?sort=new";
    private static String REDDIT_URL2 = "https://www.reddit.com/.json";
    private String subRedditName = "";

    private List<RedditPost> redditPosts = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private MainActivityAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public SharedPreferences mSharedPreferences1;
    private AppDatabase mAppDatabase1;

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
        private Menu sortMenu;
        private Menu mDrawerMenu;
        private SearchView mSearchView;

        @BindView(R.id.drawer_view1) NavigationView drawer_view1;
        private Menu subRedditMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Stetho setup, for DEBUGGING
        Stetho.initializeWithDefaults(this);

        // Set up ButterKnife
        ButterKnife.bind(this);

        makeAToolBar();

        // getting "SharedPreferences"
        mSharedPreferences1 = getSharedPreferences("MainActivitySharePreferences", MODE_PRIVATE);

        // getting a copy of Room database
        mAppDatabase1 = AppDatabase.getsInstance( getApplicationContext() );



        // Navigation Drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);

        mDrawerMenu = drawer_view1.getMenu();

//        int groupID = mDrawerMenu.getItem(0).getGroupId(); << might not need this
//        Log.i("groupID", String.valueOf(groupID)); << might not need this
//        mDrawerMenu.removeGroup(500); // << why is it 500? << might not need this

        mSharedPreferences1.edit().putString("SUBREDDITS_SHARE_PREF_KEY", "home").commit();
        mSharedPreferences1.edit().putBoolean("FIRST_RUN", false).commit();
        // DONE: 9/27, show a list of sub-reddits in the Navigation Drawer

        // TODO: 9/28, put the Strings into "SharePreferrences"
        String string1 = "home,all,announcements,Art,AskReddit,askscience,aww,blog,books,creepy,dataisbeautiful,DIY,Documentaries,EarthPorn,explainlikeimfive,Fitness,food,funny,Futurology,gadgets,gaming,GetMotivated,gifs,history,IAmA,InternetIsBeautiful,Jokes,LifeProTips,listentothis,mildlyinteresting,movies,Music,news,nosleep,nottheonion,OldSchoolCool,personalfinance,philosophy,photoshopbattles,pics,science,Showerthoughts,space,sports,television,tifu,todayilearned,TwoXChromosomes,UpliftingNews,videos,worldnews,WritingPrompts";
        List<String> subRedditList1 = Arrays.asList(string1.split(","));
        Log.i("subRedditList1", subRedditList1.toString());

        for (int x = 0; x < subRedditList1.size(); x++) {
            MenuItem subRedditMenuItem = mDrawerMenu.add(subRedditList1.get(x));
            subRedditMenuItem.setIcon(R.drawable.ic_reddit); // << used Reddit icon but it is brown.
        } // for

        drawer_view1.setItemIconTintList(null); // << make the Reddit item visible


        // Open the left-hand-side Drawer for "Favorites"
        drawer_view1.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem1) {

                        if (menuItem1.getGroupId() == R.id.groupFav) { // /menu/drawer_view.xml

                            // clear the data in the ArrayList
                            redditPosts.clear();
                            Log.i(LOG_TAG, "redditPosts.clear()");

                            // get data from database
                            mAppDatabase1.redditPostDao().loadAllSavedRedditPost();

                            // "initLoader"
                            ArrayList<RedditPostEntry> asdf1 =  (ArrayList) mAppDatabase1.redditPostDao().loadAllSavedRedditPost();
//                            Log.i("asdf1.size()", String.valueOf( asdf1.size() )  );

                            // need a for loop to loop through ArrayList
                            for (int x=0; x < asdf1.size(); x++) {
                                asdf1.get(x).getId();
                                Log.i("asdf1.getId(): ", String.valueOf(asdf1.get(x).getId()));

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
                            toolbar.setTitle("My Favorites Post");

                            // set items as selected to persist high light
                            // menuItem.setCheckable(true);

//                            return true; // EAT the "event", if you use FALSE here, to execution won't flow down to mDrawerLayout.closeDrawers();
                        } else {

                            String subRedditName = menuItem1.toString();
                            Log.i("subRedditName", subRedditName);
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

        // making network request to Reddit
//        volleyRequest(REDDIT_URL2);

        // Program starting point!
        updateMainActivity("home");

    } // onCreate


    private void makeAToolBar() {
        // Set the toolbar as the action bar
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("put anything here");

        // Enable the app bar's "home" button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // << this line shows an arrow pointing left
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu); // << this line replaced the "left pointing arrow" with the "3 lines icon"
    }


    // helper
    // ~ updateList()
    public void updateMainActivity(String subRedditName1) {

        this.subRedditName = subRedditName1;
        toolbar.setTitle(subRedditName1);

        // clearing the search bar
//        if ( mSearchView != null) {
//            mSearchView.setQuery("", false);
//            mSearchView.setIconified(true); // << show the search icon
//        }

        if ( subRedditName.equals("home") ) {
            Log.i("subRedditName", "subRedditName == home");
            volleyRequest(REDDIT_URL2); // << Reddit homepage with JSON response
        } else {

            volleyRequest("https://www.reddit.com/r/" + subRedditName + "/.json");

        } // else

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
                Log.d(LOG_TAG, response.toString());
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
//                mAdapter.notifyDataSetChanged();
                mAdapter.swapData(redditPosts);

            } // onResponse
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError e1) {
                VolleyLog.d(LOG_TAG, "onErrorResponse: " + e1.getMessage());
            } // onErrorResponse()

        });

        // Add the request to the RequestQueue, what is this "Queue" ?
        queue.add(request1);

    } // volleyRequest()


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_and_sort_menu, menu); // << this is an XML file

        // for the Search button in reddit_post_search_menuarch_menu.xml
        MenuItem searchItem = menu.findItem(R.id.item_search_menu);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.item_search_menu));

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
        });

        return true;
    } // onCreateOptionsMenu()

    public void searchForRedditPost(String currentSubRedditItem, String searchQuery) {

        subRedditName = currentSubRedditItem;
        toolbar.setTitle(subRedditName);

        String searchUrl;
        String redditJson = "https://www.reddit.com/";
        String searchString1 = "search.json?q=" + searchQuery;
        Log.i("searchForRedditPost()", subRedditName);


        if (currentSubRedditItem.equals("home") ) {
            searchUrl = redditJson + searchString1;
            Log.i("if1", subRedditName);

            // TODO: turn off "reddit post sorting" here

        } else {
            // https://www.reddit.com/r/ + subRedditName + /search.json?q + searchQuery
            searchUrl = redditJson + "r/" + subRedditName + "/search.json?q=" + searchQuery;
            Log.i("if1", subRedditName);
        } // else


        Log.i("if1", searchUrl);
        volleyRequest(searchUrl);

    } // searchForRedditPost()

    public MainActivityAdapter.OnItemClickListener redditPostClick1 = new MainActivityAdapter.OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position) {
            RedditPost redditPost1 = mAdapter.getRedditPosts().get(position);

//            intentDetailActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // TODO: I don't know the purpose of this line

            Bundle bundle1 = new Bundle();
            bundle1.putParcelable("redditPost1", redditPost1); // << using Parcelable

            final Intent intent1 = new Intent(getBaseContext(), DetailActivity.class);
            intent1.putExtras(bundle1);
            startActivity(intent1); // Start DetailActivity.java

//            bundle1.putString("title", redditPost1.getThumbnail());
//            bundle1.putString("thumbnail", redditPost1.getThumbnail());
//            bundle1.putString("url", redditPost1.getUrl());
//            bundle1.putString("subreddit", redditPost1.getSubreddit());
//            bundle1.putString("author", redditPost1.getAuthor());
//            bundle1.putString("permalink", redditPost1.getPermalink());
//            bundle1.putString("id", redditPost1.getId());
//            bundle1.putString("subreddit_name_prefixed", redditPost1.getSubreddit_name_prefixed());
//            bundle1.putInt("score", redditPost1.getScore());
//            bundle1.putInt("num_comments", redditPost1.getNumberOfComments());
//            bundle1.putBoolean("over_18", redditPost1.getOver18() );

        } // onItemClick()
    };


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


} // class MainActivity
