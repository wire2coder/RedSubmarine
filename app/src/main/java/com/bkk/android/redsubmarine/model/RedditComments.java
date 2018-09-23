package com.bkk.android.redsubmarine.model;

import android.os.Parcel;
import android.os.Parcelable;

// http://www.parcelabler.com/
public class RedditComments implements Parcelable {

    int level;
    String text;
    String author;
    String votes;
    String postedOn;


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(String postedOn) {
        this.postedOn = postedOn;
    }

    // constructor
    public RedditComments() {
    }

    protected RedditComments(Parcel in) {
        level = in.readInt();
        text = in.readString();
        author = in.readString();
        votes = in.readString();
        postedOn = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(level);
        dest.writeString(text);
        dest.writeString(author);
        dest.writeString(votes);
        dest.writeString(postedOn);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RedditComments> CREATOR = new Parcelable.Creator<RedditComments>() {
        @Override
        public RedditComments createFromParcel(Parcel in) {
            return new RedditComments(in);
        }

        @Override
        public RedditComments[] newArray(int size) {
            return new RedditComments[size];
        }
    };

} // class RedditComments
