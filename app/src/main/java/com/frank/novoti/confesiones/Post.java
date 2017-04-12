package com.frank.novoti.confesiones;

import java.util.Date;
import java.util.List;

/**
 * Created by Claudina on 11/04/17.
 */

public class Post  {
    private String text;
    private String category;
    private int numComent;
    private int likes;
    private int dislikes;
    private Date sentDate;
    private List<String> tags;


    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Post(String text, Date sentDate, List<String> tags) {
        this.text = text;
        this.category = "";
        this.numComent = 0;
        this.likes = 0;
        this.dislikes = 0;
        this.sentDate = sentDate;
        this.tags = tags;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getNumComent() {
        return numComent;
    }

    public void setNumComent(int numComent) {
        this.numComent = numComent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }
}
