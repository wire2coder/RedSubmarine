package com.bkk.android.redsubmarine;

import com.bkk.android.redsubmarine.model.Feed;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FeedAPI {

    String BASE_URL = "https://www.reddit.com/r/";

    @GET("earthporn/.rss")
    Call<Feed> getFeed();

}

