package com.bkk.android.redsubmarine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bkk.android.redsubmarine.adapter.RedditCommentsAdapter;
import com.bkk.android.redsubmarine.database.AppDatabase;
import com.bkk.android.redsubmarine.database.RedditPostEntry;
import com.bkk.android.redsubmarine.model.RedditComments;
import com.bkk.android.redsubmarine.model.RedditPost;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DetailFragment extends Fragment {

    // class variables
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    Boolean isFaved;

    ArrayList<RedditComments> mComments = new ArrayList<>();
    RedditCommentsAdapter redditCommentsAdapter;
    LinearLayoutManager linearLayoutManager1;
    RecyclerView recyclerView1;

    private AppDatabase mDb;

    // ButterKnife "binding"
    // do not use the same "BindView name" and the name of the "UI variable", you will NULL POINTER EXCEPTION
    private Unbinder unBinder;
    @BindView(R.id.fragment_share_button) ImageView share_pic_button;
    @BindView(R.id.fragment_save_button) CheckBox save_pic_button;
    @BindView(R.id.fragment_header_image) ImageView imageView1;
    @BindView(R.id.fragment_votes) TextView tv_votes;
    @BindView(R.id.fragment_comments_count) TextView tv_comments_count;
    @BindView(R.id.fragment_post_title) TextView tv_post_title;


//    required empty constructor for Fragment
    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // ButterKnife
        unBinder = ButterKnife.bind(this, rootView);

        final Activity activity1 = this.getActivity();

        // extracting data from DetailActivity
        final RedditPost redditPost1 =  getArguments().getParcelable("redditPost1");
        final String commentUrl = "https://www.reddit.com" + redditPost1.getPermalink() + ".json";

//        Log.d( LOG_TAG, redditPost1.getThumbnail() );
//        Log.d( LOG_TAG, "www.reddit.com" + redditPost1.getPermalink() + ".json" );

        try {
                // use Picasso to get the "header image"
                Picasso.get()
                        .load(redditPost1.getThumbnail())
                        .into(imageView1);
        } catch (IllegalArgumentException e) {
            imageView1.setImageResource(R.drawable.ic_comment_black_24dp);
        }


        tv_votes.setText( String.valueOf(redditPost1.getScore()) );
        tv_comments_count.setText( String.valueOf(redditPost1.getNumberOfComments()) );
        tv_post_title.setText(redditPost1.getTitle());

        // getting a copy of Room database
        mDb = AppDatabase.getsInstance( activity1.getApplicationContext() );

        // DONE: 10/2 read from database here and check do database reading here, get the ID of this post and load it to check if it is "Favorited"
        final RedditPostEntry redditPostEntry11 = mDb.redditPostDao().loadRedditPostEntryById( redditPost1.getId()  );

        if (redditPostEntry11 != null ) {
            Log.i(LOG_TAG, String.valueOf( redditPostEntry11.getId() ) );
            isFaved = true;
            save_pic_button.setChecked(true);
        } else {
            Toast.makeText(getContext(), "redditPostEntry11 does not exist", Toast.LENGTH_SHORT).show();
            isFaved = false;
            save_pic_button.setChecked(false);
        }


        // https://stackoverflow.com/questions/21579918/retrieving-comments-from-reddits-api
        // $.getJSON("http://www.reddit.com/r/" + sub + "/comments/" + id + ".json?", function (data)
        // https://www.reddit.com//r/funny/comments/9hwreb/a_shark_hanging_upside_down_looks_like_someone/.json


        // using regular AsyncTask instead, volleyRequest(); << this doesn't work
        class GetRedditComments extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... inputs) {

                String inputUrl = inputs[0];
                Log.i(LOG_TAG, "doInBackground " + inputUrl);

                // Get the contents of the Reddit Post
                String dataStream = getComments1(inputUrl);

                try {

                    JSONArray jsonArray = new JSONArray(dataStream)
                            .getJSONObject(1)
                            .getJSONObject("data")
                            .getJSONArray("children");

                    Log.i("comments>>>", jsonArray.toString() );

                    // Reddit comments with no reply, reply level zero
                    process1(mComments, jsonArray, 0);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                // now go to onPostExecute()
                return "this goes to onPostExecute() as 'result' ";
            } // doInBackground()


            @Override
            protected void onPostExecute(String result) {

                recyclerView1 = rootView.findViewById(R.id.rv_post_comments);

                redditCommentsAdapter = new RedditCommentsAdapter( activity1, mComments);
                linearLayoutManager1 = new LinearLayoutManager(getContext());

                recyclerView1.setLayoutManager(linearLayoutManager1);
                recyclerView1.setNestedScrollingEnabled(false); // can't scroll inside the Adapter's Layoutfile

                // TODO: 9/25, maybe do the adapterSwapData() here instead
                recyclerView1.setAdapter(redditCommentsAdapter);


            } // onPostExecute()

        } // GetRedditComments extends AsyncTask

        if (savedInstanceState == null) {

            GetRedditComments getRedditComments = new GetRedditComments();

            Log.i(LOG_TAG, "commentUrl>>>" + commentUrl);
            getRedditComments.execute(commentUrl);

        } else {

            // TODO: get data out of Share Preferences
            // get data out of ParcelableArrrayList

            // make a new Adapter

            // RecyclerView.setAdapter()
            // RecyclerView.setLayout()
            // RecyclerView.setNestedScrolling(false)
        }


        // Completed: add setOnClickListener() for the "share button, the 3 dots"
        share_pic_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // go make a XML layout file in /menu
                PopupMenu popupMenu1 = new PopupMenu(getContext(), share_pic_button);
                popupMenu1.getMenuInflater().inflate(R.menu.share_menu_button_layout, popupMenu1.getMenu());

                popupMenu1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {

                            case R.id.three_dot_menu_open_with:
                                Intent intentOpen = new Intent((Intent.ACTION_VIEW));
//                                Log.i(LOG_TAG, redditPost1.getUrl() );
                                Uri uri1 = Uri.parse( redditPost1.getUrl() );

                                intentOpen.setData(uri1); // << wow, this works
                                startActivity(intentOpen);
                                break;

                            case R.id.three_dot_menu_share_with:
                                Intent intentShare = new Intent(Intent.ACTION_SEND);
                                Uri uri2 = Uri.parse( redditPost1.getUrl() );

                                intentShare.putExtra(Intent.EXTRA_TEXT, uri2);
                                intentShare.setType("text/plain");
                                startActivity(intentShare);

                                startActivity( Intent.createChooser(intentShare, "asdf Share") );
//                                startActivity( intentShare );
                                break;

                        } // switch

                        return true; // CONSUME THE "CLICK EVENT"

                    } // onMenuItemClick()

                }); // popupMenu1.setOnMenuItemClickListener()

                // show the three_dot_menu
                popupMenu1.show();

            } // onClick()

        }); // fragment_share_button.setOnClickListener()


        save_pic_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if ( isFaved == false ) { // << the post is not saved
                    // first, get data to saved into the database
                    RedditPostEntry redditPostEntry1
                            = new RedditPostEntry( redditPost1.getTitle()
                            , redditPost1.getThumbnail()
                            , redditPost1.getUrl()
                            , redditPost1.getSubreddit()
                            , redditPost1.getAuthor()
                            , redditPost1.getPermalink()
                            , redditPost1.getId()
                            , redditPost1.getSubreddit_name_prefixed()
                            , redditPost1.getScore()
                            , redditPost1.getNumberOfComments()
                            , redditPost1.getPostedDate()
                            , redditPost1.getOver18()
                            , 1
                    );

                    // get database object
                    mDb.redditPostDao().insertRedditPost(redditPostEntry1);

                    Toast.makeText(getContext(), "redditPostEntry11 saved", Toast.LENGTH_SHORT).show();

                } else {
                    // delete the post from database
                    mDb.redditPostDao().deleteRedditPost(redditPostEntry11);

                    Toast.makeText(getContext(), "redditPostEntry11 deleted", Toast.LENGTH_SHORT).show();
                } // else


                // Code for updating the Widget when you remove or add stuff form the Database
                Intent dataUpdatedIntent = new Intent(getString(R.string.data_update_key))
                        .setPackage(getContext().getPackageName());

                getContext().sendBroadcast(dataUpdatedIntent);

            } // onClick()

        }); // save_pic_button.setOnClickListener()


        // TODO: add Google Ads


        return rootView;
    } // onCreateView()


    // TODO: rename this, what does this do again? parsing comments from JSON
    // helper
    private void process1( ArrayList<RedditComments> redditComments_al, JSONArray jsonArray, int comment_level ) throws Exception {

        for (int x=0; x < jsonArray.length(); x++) {

            // TODO: might not need this
//            if (jsonArray.getJSONObject(x).optString("kind") == null) {
//                continue;
//            }

            // TODO: might not need this
//            if (jsonArray.getJSONObject(x).optString("kind").equals("t1") == false) {
//                continue;
//            }

            JSONObject data1 = jsonArray.getJSONObject(x).getJSONObject("data");

            // extracting comments data from "data" and put the "data" inside the RedditComments object

            RedditComments redditComments1 = new RedditComments();

            redditComments1.setText( data1.getString("body") );
            redditComments1.setAuthor( data1.getString("author") );

            int votes1 = data1.getInt("ups") - data1.getInt("downs");
            redditComments1.setVotes( String.valueOf(votes1) );

            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
            calendar.setTimeInMillis( data1.getLong("created_utc") * 1000 );
            String date1 = android.text.format.DateFormat.format("HH:mm  dd/MM/yy", calendar).toString();
            redditComments1.setPostedOn(date1);

            redditComments1.setLevel(comment_level);

            if ( redditComments1.getAuthor() != null ) {
                redditComments_al.add(redditComments1);
                addCommentReplies(redditComments_al, data1, comment_level + 1);
            } // if

        } // inside for loop

    } // process1()


    private void addCommentReplies( ArrayList<RedditComments> redditCommentsArrayList, JSONObject jsonObject, int comment_level ) {

        try {

            if ( jsonObject.get("replies").equals("") ) {
                // no replies to the comment
                return; // exit the function
            }

            JSONArray jsonArray = jsonObject.getJSONObject("replies")
                    .getJSONObject("data")
                    .getJSONArray("children");

            process1(redditCommentsArrayList, jsonArray, comment_level );

        } catch(Exception e) {
            Log.e(LOG_TAG, "error at addCommentReplies()" );
        }

    } // addCommentReplies()


    // helper
    public static String getComments1(String url) {

        String empty = "";

        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            StringBuffer stringBuffer = new StringBuffer();
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(httpURLConnection.getInputStream() ) );

            while ( (empty = bufferedReader.readLine()) != null) {
                stringBuffer.append(empty).append("\n");
            }

            bufferedReader.close();
            return stringBuffer.toString();

        } catch(Exception e) {
            Log.d(LOG_TAG, "error: parseStream()");
            return null;
        }

    } // getComments1()


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
        unBinder.unbind();
    } // onDestroyView()


} // class DetailFragment
