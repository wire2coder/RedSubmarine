package com.bkk.android.redsubmarine.database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


// this is a TABLE name, TABLE for the Database
@Entity(tableName = "redditpost")
public class RedditPostEntry {

    // member variables
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String thumbnail;
    private String url;
    private String subreddit;
    private String author;
    private String permalink;
    private String post_id; // << just id inside RedditPost.java
    private String subreddit_name_prefixed;
    private int score;
    private int numberOfComments;
    private long postedDate;
    private Boolean over18;
    private int isFavorited; // yes is 1, no is 0


    // Constructor
    public RedditPostEntry(String title, String thumbnail, String url, String subreddit, String author, String permalink, String post_id, String subreddit_name_prefixed, int score, int numberOfComments, long postedDate, Boolean over18, int isFavorited) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.url = url;
        this.subreddit = subreddit;
        this.author = author;
        this.permalink = permalink;
        this.post_id = post_id;
        this.subreddit_name_prefixed = subreddit_name_prefixed;
        this.score = score;
        this.numberOfComments = numberOfComments;
        this.postedDate = postedDate;
        this.over18 = over18;
        this.isFavorited = isFavorited;
    } // Constructor


    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getSubreddit_name_prefixed() {
        return subreddit_name_prefixed;
    }

    public void setSubreddit_name_prefixed(String subreddit_name_prefixed) {
        this.subreddit_name_prefixed = subreddit_name_prefixed;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public long getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(long postedDate) {
        this.postedDate = postedDate;
    }

    public Boolean getOver18() {
        return over18;
    }

    public void setOver18(Boolean over18) {
        this.over18 = over18;
    }

    public int getIsFavorited() {
        return isFavorited;
    }

    public void setIsFavorited(int isFavorited) {
        this.isFavorited = isFavorited;
    }

} // class RedditPostEntry
