package com.bkk.android.redsubmarine.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bkk.android.redsubmarine.DetailFragment;
import com.bkk.android.redsubmarine.R;
import com.bkk.android.redsubmarine.model.RedditComments;

import java.util.ArrayList;

public class RedditCommentsAdapter extends RecyclerView.Adapter<RedditCommentsAdapter.AsdfViewHolder> {

    // class variables
    private static final String LOG_TAG = RedditCommentsAdapter.class.getSimpleName();

    private ArrayList<RedditComments> mRedditComments;
    private Context mContext;


    public RedditCommentsAdapter(Context mContext, ArrayList<RedditComments> mRedditComments) {
        this.mContext = mContext;
        this.mRedditComments = mRedditComments;
    }


    @Override
    public AsdfViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context1 = viewGroup.getContext();

        View view = LayoutInflater
                .from(context1)
                .inflate(R.layout.item_redditcomments_adapter, null);

        AsdfViewHolder asdfViewHolder = new AsdfViewHolder(view);

        return asdfViewHolder;

    } // onCreateViewHolder()


    @Override
    public void onBindViewHolder(AsdfViewHolder asdfViewHolder, int position) {

        // get 1 Reddit post
        RedditComments redditComments1 = mRedditComments.get(position);

        asdfViewHolder.author.setText(redditComments1.getAuthor());
        asdfViewHolder.votes.setText(redditComments1.getVotes());
        asdfViewHolder.commentText.setText(redditComments1.getText());
        asdfViewHolder.postedDate.setText(redditComments1.getPostedOn());

        // adding "left padding" to the comments if it is a reply to a previous comment
        asdfViewHolder.linearLayout2.setPadding( redditComments1.getLevel() * 27, 0, 0, 0);

    } // onBindViewHolder()

    @Override
    public int getItemCount() {
        return (mRedditComments != null) ? mRedditComments.size() : 0;
    }


    public class AsdfViewHolder extends RecyclerView.ViewHolder {

        // class variables
        TextView author;
        TextView commentText;
        TextView postedDate;
        TextView votes;

        LinearLayout linearLayout2;
        RelativeLayout commentLevel2;

        // constructor
        public AsdfViewHolder(View itemView) {
            super(itemView);

            this.author = itemView.findViewById(R.id.tv_author);
            this.commentText = itemView.findViewById(R.id.tv_commentText);
            this.postedDate = itemView.findViewById(R.id.tv_postedDate);
            this.votes = itemView.findViewById(R.id.tv_votes_adapter_item);

            this.linearLayout2 = itemView.findViewById(R.id.linearLayout2_comment_adapter);
            this.commentLevel2 = itemView.findViewById(R.id.relativeLayout2_comment_adapter);
        }

    } // class AsdfViewHolder


} // class RedditCommentsAdapter
