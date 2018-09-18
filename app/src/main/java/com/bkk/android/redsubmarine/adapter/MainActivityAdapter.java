package com.bkk.android.redsubmarine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bkk.android.redsubmarine.MainActivity;
import com.bkk.android.redsubmarine.R;
import com.bkk.android.redsubmarine.model.RedditPost;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.RecyclerViewHolder> {

    private static final String CLASS_TAG = MainActivityAdapter.class.getSimpleName();
    private static final String LOG_TAG = "ttt>>>: ";

    private ImageLoader mImageLoader;
    private List<RedditPost> redditPosts;
    private Context mContext;

    // OnItemClickListener is an INTERFACE
    private OnItemClickListener mItemClickListener;


    //>> constructor
    public MainActivityAdapter(List<RedditPost> redditPosts, Context context) {
        this.redditPosts = redditPosts;
        this.mContext = context;
    }

    public void swapData(List<RedditPost> list1) {
        this.redditPosts = list1;

        notifyDataSetChanged();
    }

    public void clearAdapter() {
        if (redditPosts != null) {
            redditPosts.clear();
        }

        notifyDataSetChanged();
    }

    // Getter
    public List<RedditPost> getRedditPosts() {
        return this.redditPosts;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layout1 = R.layout.item_mainactivity_adapter;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layout1, viewGroup, shouldAttachToParentImmediately);
        RecyclerViewHolder rvh = new RecyclerViewHolder(view);
        return  rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        RedditPost redditPost = redditPosts.get(position);

        holder.tv_post_title.setText(redditPost.getTitle());

        // use Picasso here instead of Volley ImageLoader
        Picasso.get()
                .load(redditPost.getThumbnail())
                .into(holder.iv_thumbnail);

        holder.tv_vote_counter.setText( String.valueOf(redditPost.getScore()) );
        holder.tv_comments_counter.setText( String.valueOf(redditPost.getNumberOfComments()));
        holder.tv_subreddit_name.setText(redditPost.getSubreddit_name_prefixed());

    }

    @Override
    public int getItemCount() {
        Log.d(LOG_TAG, redditPosts != null ? String.valueOf( redditPosts.size() ) : "Size = 0" );
        return redditPosts != null ? redditPosts.size() : 0;
    }

    // TODO: need to implement click listener
    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //>> member variables
        protected ImageView iv_thumbnail; // Volley ImageView

        protected TextView url;
        protected TextView author;
        protected TextView tv_comments_counter;
        protected TextView tv_vote_counter;
        protected TextView tv_subreddit_name;
        protected TextView tv_post_title;


        //>> constructor
        public RecyclerViewHolder(View itemView) {
            super(itemView);

            this.tv_post_title = itemView.findViewById(R.id.tv_post_title);
            this.iv_thumbnail = itemView.findViewById(R.id.iv_thumbnail);
            this.tv_vote_counter = itemView.findViewById(R.id.tv_vote_counter);
            this.tv_comments_counter = itemView.findViewById(R.id.tv_comments_counter);
            this.tv_subreddit_name = itemView.findViewById(R.id.tv_subreddit_name);

//            itemView.setClickable(true); // TODO: remove this line
            itemView.setOnClickListener(this);

        }

        // implements View.OnClickListener
        @Override
        public void onClick(View view) {
             if (mItemClickListener != null) {
                 mItemClickListener.onItemClick( view, getAdapterPosition() );
             }
        }

        @Override
        public String toString() {
            return super.toString();
        }

    } // class RecyclerViewHolder


    // interface for clicking methods
    public interface OnItemClickListener {
        // method 1
        void onItemClick(View view, int postion);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

} // class MainActivityAdapter extends
