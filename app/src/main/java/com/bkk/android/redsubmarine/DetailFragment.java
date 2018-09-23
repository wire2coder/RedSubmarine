package com.bkk.android.redsubmarine;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.ArrayList;

public class DetailFragment extends Fragment {

    // class variables
    private static final String CLASS_TAG = DetailFragment.class.getSimpleName();
    private static final String LOG_TAG = "ttt>>>: ";

    ArrayList<RedditComments> mComments;


//    required empty constructor
    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // extracting data
        RedditPost redditPost1 =  getArguments().getParcelable("redditPost1");
//        Log.d( LOG_TAG, redditPost1.getThumbnail() );

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



        // TODO: 9/23 fetch and add comments to a RecyclerView, need to make a new Adapter
        // https://stackoverflow.com/questions/21579918/retrieving-comments-from-reddits-api
        // $.getJSON("http://www.reddit.com/r/" + sub + "/comments/" + id + ".json?", function (data)
        // https://www.reddit.com//r/funny/comments/9hwreb/a_shark_hanging_upside_down_looks_like_someone/.json
        // TODO: 9/23 make a network request to get comments

         // volleyRequest(); << this doesn't work
        // using regular AsyncTask instead

        class GetRedditComments extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... strings) {

                String url1 = "https://www.reddit.com//r/AskReddit/comments/9hzlya/women_who_have_given_birth_whats_something_no_one/.json";

                // mComments.addAll( processor.fetchComments() );
                    // processor = new CommentProcessor(url)
                // return "done"

                Log.i(">>>", url1);

                // Get the contents of the Reddit Post
                String dataStream = getComments1(url1);

                try {

                    JSONArray jsonArray = new JSONArray(dataStream)
                            .getJSONObject(1)
                            .getJSONObject("data")
                            .getJSONArray("children");

                    Log.i("comments>>>", jsonArray.toString() );

                    // Reddit comments with no reply, reply level zero
//                    process(comments, r, 0);
//                    Log.i("processor","COMMENTS:"+String.valueOf(comments.size()));

                    for(int i=0; i < jsonArray.length(); i++) {
                        if (jsonArray.getJSONObject(i).optString("kind") == null) {
                            continue;
                        }

                        if (jsonArray.getJSONObject(i).optString("kind").equals("t1") == false) {
                            continue;
                        }

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i).getJSONObject("data");

                        // Comment comment = loadComment(data,level);
                        // TODO: 9/23



                    } // inside a for loop

                } catch (Exception e) {
                    e.printStackTrace();
                }



                return null;
            } // doInBackground()

            @Override
            protected void onPostExecute(String result) {


                super.onPostExecute(result);
            } // onPostExecute()

        } // class GetRedditComments

        GetRedditComments getRedditComments = new GetRedditComments();
        getRedditComments.execute(); // TODO: put a URL here





        return rootView;
    } // onCreateView()


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


    }


    // helper
    public void volleyRequest() {

        RequestQueue queue1 = Volley.newRequestQueue( getActivity().getBaseContext() );
        String COMMENTS_URL1 = "https://www.reddit.com//r/funny/comments/9hwreb/a_shark_hanging_upside_down_looks_like_someone/.json";


        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET
                , COMMENTS_URL1
                , (String) null
                , new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("fragment_detail", response.toString());

//                mAdapter.clearAdapter();

                // parse JSON data
                // data(JSON object) >> children(JSON array) >> data(JSON object)


            } // onResponse
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(LOG_TAG, "fragment Error: " + error.getMessage());

                Toast.makeText(getActivity().getBaseContext()
                        , "VolleyError in Fragment",
                        Toast.LENGTH_SHORT).show();
            }

        });


        // Add the request to the RequestQueue, what is this "Queue" ?
        queue1.add(request1);

    } // volleyRequest

} // class DetailFragment
