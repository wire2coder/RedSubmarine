package com.bkk.android.redsubmarine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorSpace;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class DetailFragment extends Fragment {

    // class variables
    private static final String CLASS_TAG = DetailFragment.class.getSimpleName();
    private static final String LOG_TAG = "ttt>>>: ";

    ArrayList<RedditComments> mComments = new ArrayList<>();
    RedditCommentsAdapter redditCommentsAdapter;
    LinearLayoutManager linearLayoutManager1;
    RecyclerView recyclerView1;

//    required empty constructor
    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // ButterKnife
        ButterKnife.bind(this, rootView);

        final Activity activity1 = this.getActivity();

        // extracting data from DetailActivity
        RedditPost redditPost1 =  getArguments().getParcelable("redditPost1");
        final String commentUrl = "https://www.reddit.com" + redditPost1.getPermalink() + ".json";

//        Log.d( LOG_TAG, redditPost1.getThumbnail() );
//        Log.d( LOG_TAG, "www.reddit.com" + redditPost1.getPermalink() + ".json" );




        ImageView imageView1 = rootView.findViewById(R.id.fragment_header_image);

        // use Picasso to get the "header image"
        Picasso.get()
                .load(redditPost1.getThumbnail())
                .into(imageView1);


        TextView tv_votes = rootView.findViewById(R.id.fragment_votes);
        TextView tv_comments_count = rootView.findViewById(R.id.fragment_comments_count);
        TextView tv_post_title = rootView.findViewById(R.id.fragment_post_title);

        tv_votes.setText( String.valueOf(redditPost1.getScore()) );
        tv_comments_count.setText( String.valueOf(redditPost1.getNumberOfComments()) );

        tv_post_title.setText(redditPost1.getTitle());


        // https://stackoverflow.com/questions/21579918/retrieving-comments-from-reddits-api
        // $.getJSON("http://www.reddit.com/r/" + sub + "/comments/" + id + ".json?", function (data)
        // https://www.reddit.com//r/funny/comments/9hwreb/a_shark_hanging_upside_down_looks_like_someone/.json

            // volleyRequest(); << this doesn't work
            // using regular AsyncTask instead

        class GetRedditComments extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... inputs) {

//                String url1 = "https://www.reddit.com//r/AskReddit/comments/9hzlya/women_who_have_given_birth_whats_something_no_one/.json";

                // mComments.addAll( processor.fetchComments() );
                // processor = new CommentProcessor(url)

                String inputUrl = inputs[0];
                Log.i("doInBackground", inputUrl);

                // Get the contents of the Reddit Post
//                String dataStream = getComments1("https://www.reddit.com/r/sports/comments/9inlgv/vance_macdonald_with_one_of_my_favorite_stiff/.json");
                String dataStream = getComments1(inputUrl);

                try {

                    JSONArray jsonArray = new JSONArray(dataStream)
                            .getJSONObject(1)
                            .getJSONObject("data")
                            .getJSONArray("children");

                    Log.i("comments>>>", jsonArray.toString() );

                    // Reddit comments with no reply, reply level zero
                    process1(mComments, jsonArray, 0);
//                    Log.i("ggg"," Comments AL: " + String.valueOf(mComments.size()) );

                } catch (Exception e) {
                    e.printStackTrace();
                }


                // now go to onPostExecute()
                return "this goes to onPostExecute() as 'result' ";
            } // doInBackground()

            @Override
            protected void onPostExecute(String result) {

//                Log.i("ggg"," Comments AL: " + String.valueOf(mComments.size()) );
//                Log.i("ggg"," Comments AL: " + mComments );
                Log.i("ggg>>>", result);

                recyclerView1 = rootView.findViewById(R.id.rv_post_comments);

                redditCommentsAdapter = new RedditCommentsAdapter( activity1, mComments);
                linearLayoutManager1 = new LinearLayoutManager(getContext());

                recyclerView1.setLayoutManager(linearLayoutManager1);
                recyclerView1.setNestedScrollingEnabled(false); // can't scroll inside the Adapter's Layoutfile

                recyclerView1.setAdapter(redditCommentsAdapter);


            } // onPostExecute()

        } // GetRedditComments extends AsyncTask

        if (savedInstanceState == null) {

            GetRedditComments getRedditComments = new GetRedditComments();

            Log.i("commentUrl>>>", commentUrl);
            getRedditComments.execute(commentUrl); // TODO: put a URL here

        } else {
            // get data out of ParcelableArrrayList

            // make a new Adapter

            // RecyclerView.setAdapter()
            // RecyclerView.setLayout()
            // RecyclerView.setNestedScrolling(false)
        }



        // TODO: add setOnClickListener()


        // TODO: add Google Ads

        return rootView;
    } // onCreateView()


    // helper
    private void process1( ArrayList<RedditComments> redditComments_al, JSONArray jsonArray, int comment_level ) throws Exception {

        for (int x=0; x < jsonArray.length(); x++) {

            if (jsonArray.getJSONObject(x).optString("kind") == null) {
                continue;
            }

            if (jsonArray.getJSONObject(x).optString("kind").equals("t1") == false) {
                continue;
            }

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
                // Log.i("ttt>>>", redditComments1.getAuthor());
                redditComments_al.add(redditComments1);

                addCommentReplies(redditComments_al, data1, comment_level + 1);
//                Log.i("ttt>>>", String.valueOf(redditComments_al.size())  );
            }

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
    public static HttpURLConnection createConnection(String url){

        try {

            HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(url).openConnection();
            httpURLConnection.setReadTimeout(30000);
            httpURLConnection.setRequestProperty("User-Agent", "Firefox 50");
            return httpURLConnection;

        } catch(Exception e) {

            Log.d("CONNECTION FAILED", e.toString());
            return null;

        }

    }


    // helper
    public static String getComments1(String url) {

        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();

            StringBuffer stringBuffer = new StringBuffer();
            String empty = "";

            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(httpURLConnection.getInputStream() ) );

            while ( (empty = bufferedReader.readLine()) != null) {
                stringBuffer.append(empty).append("\n");
            }

            bufferedReader.close();

            return stringBuffer.toString();

        } catch(Exception e) {

            Log.d(CLASS_TAG, "error: parseStream()");
            return null;

        }

    } // getComments1()


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

} // class DetailFragment
