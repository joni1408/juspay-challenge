package com.prempal.juspay_challenge;

/**
 * Created by prempal on 10/3/16.
 */
public class Article {
    String title;
    String link;

    Article(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }
}
