package com.bkk.android.redsubmarine.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {

    public MainActivityAdapter() {
        super();
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityAdapter.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    // TODO: need to implement click listener
    public class ViewHolder extends RecyclerView.ViewHolder {

        //>> member variables
        private NetworkImageView thumbnail; // Volley ImageView

        private TextView title;
        private TextView url;
        private TextView author;
        private TextView comments;
        private TextView score;
        private TextView subreddit;


        //>> constructor
        public ViewHolder(View itemView) {
            super(itemView);


        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

} // class MainActivityAdapter extends
