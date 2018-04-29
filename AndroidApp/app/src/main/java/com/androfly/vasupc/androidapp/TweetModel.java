package com.androfly.vasupc.androidapp;

/**
 * Created by VasuPC on 25-03-2018.
 */

public class TweetModel {

    private String handle;
    private String date;
    private String tweet;
    private String link;
    private String inference;

    public TweetModel(){

    }

    public TweetModel(String handle, String date, String tweet, String link, String inference){
        this.handle = handle;
        this.date = date;
        this.tweet = tweet;
        this.link = link;
        this.inference = inference;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getInference() {
        return inference;
    }

    public void setInference(String inference) {
        this.inference = inference;
    }

}
