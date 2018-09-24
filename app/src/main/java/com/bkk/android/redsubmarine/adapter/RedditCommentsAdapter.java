package com.bkk.android.redsubmarine.adapter;

import android.content.Context;
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
    private static final String CLASS_TAG = RedditCommentsAdapter.class.getSimpleName();
    private static final String LOG_TAG = "ttt>>>: ";

    private ArrayList<RedditComments> mRedditComments;
    private Context mContext;


    public RedditCommentsAdapter(Context mContext, ArrayList<RedditComments> mRedditComments) {
        this.mContext = mContext;
        this.mRedditComments = mRedditComments;
    }

    public void clearData() {

        mRedditComments.clear();
        notifyDataSetChanged();

    } // clearData


    // need 3 OVERRIDES
    @Override
    public AsdfViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        Context context = viewGroup.getContext();
//        int layout1 = R.layout.item_redditcomments_adapter;
//        LayoutInflater inflater = LayoutInflater.from(context);
//        boolean shouldAttachToParentImmediately = false;
//
//        View view = inflater.inflate(layout1, viewGroup, shouldAttachToParentImmediately);
//        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
//        return  recyclerViewHolder;

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_redditcomments_adapter, null);
        final AsdfViewHolder recyclerViewHolder = new AsdfViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(AsdfViewHolder recyclerViewHolder, int position) {

        // get 1 Reddit post
        RedditComments redditComments1 = mRedditComments.get(position);

        recyclerViewHolder.author.setText(redditComments1.getAuthor());
        // TODO: add 3 more things here

    } // onBindViewHolder()

    @Override
    public int getItemCount() {
//        return (mRedditComments != null) ? mRedditComments.size() : 0;
        return mRedditComments.size();
    }


    // put a class here
    public class AsdfViewHolder extends RecyclerView.ViewHolder {

        // class variables
        TextView author;
//        TextView commentText;
//        TextView postedDate;
//        TextView votes;

//        LinearLayout linearLayout;
//        RelativeLayout commentLevel;

        // constructor
        public AsdfViewHolder(View itemView) {
            super(itemView);

            this.author = itemView.findViewById(R.id.tv_author);
//            this.commentText = itemView.findViewById(R.id.tv_commentText);
//            this.postedDate = itemView.findViewById(R.id.tv_postedDate);
//            this.votes = itemView.findViewById(R.id.tv_votes_adapter_item);
//            this.linearLayout = itemView.findViewById(R.id.linearLayout2_comment_adapter);
//            this.commentLevel = itemView.findViewById(R.id.relativeLayout2_comment_adapter);
        }

    } // class AsdfViewHolder


} // class RedditCommentsAdapter
