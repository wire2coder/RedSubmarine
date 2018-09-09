package com.bkk.android.redsubmarine.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RedditPost implements Parcelable {

    // member variables
    private String title;
    private String thumbnail;
    private String url;
    private String subreddit;
    private String author;
    private String permalink;
    private String id;

    private int score, numberOfComments;
    private long postedDate;
    private Boolean over18;


    // Getters and Setters
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


    // Parcel code
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.thumbnail);
        dest.writeString(this.url);
        dest.writeString(this.subreddit);
        dest.writeString(this.author);
        dest.writeString(this.permalink);
        dest.writeString(this.id);
        dest.writeInt(this.score);
        dest.writeInt(this.numberOfComments);
        dest.writeLong(this.postedDate);
        dest.writeValue(this.over18);
    }

    // Parcel empty constructor
    public RedditPost() {
    }

    protected RedditPost(Parcel in) {
        this.title = in.readString();
        this.thumbnail = in.readString();
        this.url = in.readString();
        this.subreddit = in.readString();
        this.author = in.readString();
        this.permalink = in.readString();
        this.id = in.readString();
        this.score = in.readInt();
        this.numberOfComments = in.readInt();
        this.postedDate = in.readLong();
        this.over18 = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<RedditPost> CREATOR = new Parcelable.Creator<RedditPost>() {
        @Override
        public RedditPost createFromParcel(Parcel source) {
            return new RedditPost(source);
        }

        @Override
        public RedditPost[] newArray(int size) {
            return new RedditPost[size];
        }
    };

} // class RedditPost implements Parcelable





