package com.bkk.android.redsubmarine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bkk.android.redsubmarine.model.RedditPost;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailFragment extends Fragment {

    // class variables
    private static final String CLASS_TAG = DetailFragment.class.getSimpleName();
    private static final String LOG_TAG = "ttt>>>: ";

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


        // TODO: 9/22 fetch and add comments to a RecyclerView, need to make a new Adapter


        return rootView;
    } // onCreateView()


} // class DetailFragment
