package com.bkk.android.redsubmarine;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bkk.android.redsubmarine.adapter.RedditCommentsAdapter;
import com.bkk.android.redsubmarine.database.AppDatabase;
import com.bkk.android.redsubmarine.database.RedditPostEntry;
import com.bkk.android.redsubmarine.model.RedditComments;
import com.bkk.android.redsubmarine.model.RedditPost;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
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
        final RedditPost redditPost1 =  getArguments().getParcelable(getString(R.string.redditPost1) );
//        final String commentUrl = "https://www.reddit.com" + redditPost1.getPermalink() + ".json";
        final String commentUrl = Strings.REDDIT_URL + redditPost1.getPermalink() + Strings.JSON_EXTENSION;


        try {
                // use Picasso to get the "header image"
                Picasso.get()
                        .load(redditPost1.getThumbnail())
                        .into(imageView1);
        } catch (IllegalArgumentException e) {
            imageView1.setImageResource(R.drawable.ic_twotone_comment_24px);
        }


        tv_votes.setText( String.valueOf(redditPost1.getScore()) );
        tv_comments_count.setText( String.valueOf(redditPost1.getNumberOfComments()) );
        tv_post_title.setText(redditPost1.getTitle());

        // getting a copy of Room database
        mDb = AppDatabase.getsInstance( activity1.getApplicationContext() );

        // DONE: 10/2 read from database here and check do database reading here, get the ID of this post and load it to check if it is "Favorited"
        final RedditPostEntry redditPostEntry11 = mDb.redditPostDao().loadRedditPostEntryById( redditPost1.getId()  );

        if (redditPostEntry11 != null ) {
            Log.d(LOG_TAG, String.valueOf( redditPostEntry11.getId() ) );
            isFaved = true;
            save_pic_button.setChecked(true);
        } else {
//            Toast.makeText(getContext(), "redditPostEntry11 does not exist", Toast.LENGTH_SHORT).show();
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
                Log.d(LOG_TAG, "doInBackground " + inputUrl);

                // Get the contents of the Reddit Post
                String dataStream = getCommentsFromBufferReader(inputUrl);

                try {

                    JSONArray jsonArray = new JSONArray(dataStream)
                            .getJSONObject(1)
                            .getJSONObject( Strings.DATA )
                            .getJSONArray( Strings.CHILDREN );

                    Log.d("comments>>> ", jsonArray.toString() );

                    // Reddit comments with no reply, reply level zero
                    parseCommentsFromJson(mComments, jsonArray, 0);

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

                recyclerView1.setAdapter(redditCommentsAdapter);


            } // onPostExecute()

        } // GetRedditComments extends AsyncTask

        if (savedInstanceState == null) {

            GetRedditComments getRedditComments = new GetRedditComments();

            Log.d(LOG_TAG, "commentUrl>>> " + commentUrl);
            getRedditComments.execute(commentUrl);

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
//                                Log.d(LOG_TAG, redditPost1.getUrl() );
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

                                startActivity( Intent.createChooser(intentShare, Strings.ASDF_SHARE ) );
//                                startActivity( intentShare );
                                break;

                        } // switch

                        return true; // CONSUME THE "CLICK EVENT"

                    } // onMenuItemClick()

                }); // popupMenu1.setOnMenuItemClickListener()

                // show the "open with" and "share" menu
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

                    Toast.makeText(getContext(), Strings.REDDIT_POST_ENTRY_11_SAVED, Toast.LENGTH_SHORT).show();

                } else {
                    // delete the post from database
                    mDb.redditPostDao().deleteRedditPost(redditPostEntry11);

                    Toast.makeText(getContext(), Strings.REDDIT_POST_ENTRY_11_DELETED, Toast.LENGTH_SHORT).show();
                } // else


                // Code for updating the Widget when you remove or add stuff form the Database
                Intent dataUpdatedIntent = new Intent(getString(R.string.data_update_key))
                        .setPackage(getContext().getPackageName());

                getContext().sendBroadcast(dataUpdatedIntent);

            } // onClick()

        }); // save_pic_button.setOnClickListener()

        return rootView;
    } // onCreateView()


    // helper
    private void parseCommentsFromJson(ArrayList<RedditComments> redditComments_al, JSONArray jsonArray, int comment_indent_level ) throws Exception {

        for (int x=0; x < jsonArray.length(); x++) {

            JSONObject data1 = jsonArray.getJSONObject(x).getJSONObject( Strings.DATA );

            // extracting comments data from "data" and put the "data" inside the RedditComments object

            RedditComments redditComments1 = new RedditComments();

            int votes1 = data1.getInt( Strings.UPS ) - data1.getInt( Strings.DOWNS );
            redditComments1.setVotes( String.valueOf(votes1) );

            Calendar calendar = Calendar.getInstance( Locale.ENGLISH );
            calendar.setTimeInMillis( data1.getLong( Strings.CREATED_UTC ) * 1000 );
            String date1 = android.text.format.DateFormat.format("HH:mm  dd/MM/yy", calendar).toString();
            redditComments1.setPostedOn(date1);

            redditComments1.setText( data1.getString( Strings.BODY ) );
            redditComments1.setAuthor( data1.getString( Strings.AUTHOR ) );

            redditComments1.setLevel(comment_indent_level);

            if ( redditComments1.getAuthor() != null ) {
                redditComments_al.add(redditComments1);
                addCommentReplies(redditComments_al, data1, comment_indent_level + 1);
            } // if

        } // inside for loop

    } // parseCommentsFromJson()


    private void addCommentReplies( ArrayList<RedditComments> redditCommentsArrayList, JSONObject jsonObject, int comment_indent_level ) {

        try {

            if ( jsonObject.get( Strings.REPLIES ).equals("") ) {
                // no replies to the comment
                return; // exit the function
            }

            JSONArray jsonArray = jsonObject.getJSONObject( Strings.REPLIES )
                    .getJSONObject( Strings.DATA )
                    .getJSONArray( Strings.CHILDREN );

            parseCommentsFromJson(redditCommentsArrayList, jsonArray, comment_indent_level );

        } catch(Exception e) {
            Log.e(LOG_TAG, "error at addCommentReplies()" );
        }

    } // addCommentReplies()


    // helper
    public static String getCommentsFromBufferReader(String url) {

        String emptyString = "";

        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            StringBuffer stringBuffer1 = new StringBuffer();
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(httpURLConnection.getInputStream() ) );

            while ( (emptyString = bufferedReader.readLine()) != null) {
                stringBuffer1.append(emptyString).append("\n");
            }

            bufferedReader.close();
            return stringBuffer1.toString();

        } catch(Exception e) {
            Log.d(LOG_TAG, "error parseStream() ");
            return null;
        }

    } // getCommentsFromBufferReader()


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
        unBinder.unbind();
    } // onDestroyView()


} // class DetailFragment
