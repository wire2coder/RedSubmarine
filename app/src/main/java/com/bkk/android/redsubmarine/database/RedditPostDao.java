package com.bkk.android.redsubmarine.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bkk.android.redsubmarine.model.RedditPost;

import java.util.ArrayList;
import java.util.List;


@Dao
public interface RedditPostDao {

    // error: Not sure how to convert a Cursor to this method's return type
    @Query("SELECT * FROM redditpost ORDER BY id")
//    ArrayList<RedditPostEntry> loadAllSavedRedditPost(); << only List can you use in "DAO"
    List<RedditPostEntry> loadAllSavedRedditPost();

    @Insert()
    void insertRedditPost(RedditPostEntry redditPostEntry);

//    @Update()

    @Delete()
    void deleteRedditPost(RedditPostEntry redditPostEntry);

} // class RedditPostDao
